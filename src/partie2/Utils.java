package partie2;

import error.ObjectTooFat;

import java.io.*;
import java.util.*;

public class Utils {

    public int fractionnalPacking(List<Node> objList, int boxHeight){
        int totalHeight = 0;
        for(Node o :objList){
            totalHeight += o.getHeight();
        }
        return (int)Math.ceil((double)totalHeight / (double)boxHeight);
    }


    public List<Box> firstFitDecreasingPacking(List<Node> objListSort, int boxHeight)throws ObjectTooFat {
        List<Box> boxList = new ArrayList<>();

        try{
            //Si il n'y a pas d'objet on retourne une liste vide
            if(objListSort.size()==0){
                return new ArrayList<>();
            }else {//On crée une boite si il y a des objets
                boxList.add(new Box(boxHeight));
            }
            int currentObject = 1;

            //Parcours des objets
            for (Node o : objListSort) {
                if(o.getHeight() > boxHeight)
                    throw new ObjectTooFat();

                boolean boxFound = false;

                for(Box b : boxList){
                    if(!b.isInConflict(o)) {                                                    //On test si l'objet est en conflit
                        if (o.getHeight() <= b.getHeightLeft()) {                               //Si la taille de l'objet est inférieur au restant de place de la boite
                            b.addObjetToBox(currentObject, o);                                  //On ajoute
                            boxFound = true;
                            break;
                        }
                    }
                }

                if(!boxFound){
                    Box newBox = new Box(boxHeight);                 //On crée une nouvelle boite
                    newBox.addObjetToBox(currentObject, o);                             //On ajoute l'objet à la nouvelle boite
                    boxList.add(newBox);                                                //On ajoute une nouvelle boite
                }
                currentObject++;

            }
        }catch(ObjectTooFat e){
            System.out.println("An object is too fat for the basic height of a box");
        }
        return boxList;
    }

    public List<Box> bestFitDecreasingPacking(List<Node> objListSort, int boxHeight)throws ObjectTooFat{
        List<Box> boxList = new ArrayList<>();

        try{
            //Si il n'y a pas d'objet on retourne une liste vide
            if(objListSort.size()==0){
                return new ArrayList<>();
            }else {//On crée une boite si il y a des objets
                boxList.add(new Box(boxHeight));
            }
            int currentObject = 1;

            //Parcours des objets
            for (Node o : objListSort) {
                if(o.getHeight() > boxHeight)
                    throw new ObjectTooFat();

                Box bestBox = boxList.get(0);
                boolean isInsert = false;
                for(Box b : boxList){
                    if(!b.isInConflict(o)) {                                                    //On test si l'objet est en conflit
                        if (o.getHeight() <= b.getHeightLeft()){                                //On test si l'objet peut rentrer dans la boite
                            if(o.getHeight() + b.getHeightLeft()                                //On test si la taille restante est meilleure que la dernière meilleure boite
                                    > bestBox.getHeightLeft())  {
                                bestBox = b;                                                       //On définit la meilleure boite comme étant celle ci (Si meilleure)
                                isInsert = true;                                                   //L'objet sera inséré dans une boite
                            }                                                                       //Sinon la meilleur boite restera celle de base
                        }                                                                           //Le booleen isInsert servant à vérifier si l'objet sera inséré dans une boite existante
                    }                                                                               //Dans le cas où aucune des condition n'est respecté, il sera restera à false
                }
                if(isInsert)                                                                    //On test si l'objet a une boite de destination
                    bestBox.addObjetToBox(currentObject, o);                                        //On ajoute l'objet à la meilleure boite
                else{                                                                           //Sinon
                    Box newBox = new Box(boxHeight);                             //On crée une nouvelle boite
                    boxList.add(newBox);                                                            //On ajoute une nouvelle boite
                    bestBox = newBox;                                                               //On ajoute l'objet à la nouvlle boite
                    bestBox.addObjetToBox(currentObject, o);                                        //On ajoute l'objet à la meilleure boite
                }
                currentObject++;
            }
        }catch(ObjectTooFat e){
            System.out.println("An object is too fat for the basic height of a box");
        }
        return boxList;
    }

    List<Node> readFile(String path) throws IOException {
        List<Node> nodeList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line;

        Random r = new Random();

        while ((line = br.readLine()) != null) {
            if(line.startsWith("p")) {
                line = line.substring(7);
                Scanner scanner = new Scanner(line);
                int max = scanner.nextInt();
                for(int i = 0; i < max; i++){
                    nodeList.add(new Node(10 + r.nextInt(41), new ArrayList<>()));
                }
            } else {
                if(line.startsWith("e")) {
                    line = line.substring(2);
                    Scanner scanner = new Scanner(line);
                    int edge1 = scanner.nextInt();
                    int edge2 = scanner.nextInt();
                    nodeList.get(edge1 - 1).addNeighbour(nodeList.get(edge2 - 1));
                    nodeList.get(edge2 - 1).addNeighbour(nodeList.get(edge1 - 1));
                }
            }
        }

        return nodeList;
    }



    public List<Node> dSatur(List<Node> nodeList){
        List<Node> nodeColored = new ArrayList<>();

        nodeList.sort(Comparator.comparingInt(Node::getDegree));
        Collections.reverse(nodeList);
        Node maxDegree = nodeList.get(0);
        maxDegree.setColor(1);

        nodeColored.add(maxDegree);
        nodeList.remove(maxDegree);

        while(!nodeList.isEmpty()) {
            Node maxSaturateNode = null;
            int maxSaturation = Integer.MIN_VALUE;

            for (Node obj : nodeList) {
                if (obj.getNumberOfColoredNeighbour() > maxSaturation || (obj.getNumberOfColoredNeighbour() == maxSaturation && maxSaturateNode != null && obj.getDegree() > maxSaturateNode.getDegree())) {
                    maxSaturation = obj.getNumberOfColoredNeighbour();
                    maxSaturateNode = obj;
                }
            }

            assert maxSaturateNode != null;

            List<Integer> colorAlreadyAssign = new ArrayList<>();                                           //List des couleur déjà associé aux voisin de l'objet en cours

            for (Node obj : maxSaturateNode.getNeighbourList()) {
                colorAlreadyAssign.add(obj.getColor());
            }
            colorAlreadyAssign.sort(Comparator.comparingInt(o -> o));

            int before = -1;
            boolean assigned = false;
            for (Integer i : colorAlreadyAssign) {
                if (before != i) {
                    if (before + 1 != i) {
                        assigned = true;
                        maxSaturateNode.setColor(i);
                        nodeColored.add(maxSaturateNode);
                        nodeList.remove(maxSaturateNode);
                        break;
                    } else {
                        before = i;
                    }
                }
            }
            if (!assigned) {
                maxSaturateNode.setColor(maxSaturation + 1);
                nodeColored.add(maxSaturateNode);
                nodeList.remove(maxSaturateNode);
            }
        }

        List<Integer> colorUsed = new ArrayList<>();
        for(Node n : nodeColored){
            if(!colorUsed.contains(n.getColor()))
                colorUsed.add(n.getColor());
        }
        return nodeColored;
    }

    public List<Box> dSaturWithFFDPacking(List<Node> objListSort, int boxHeight)throws ObjectTooFat {

        List<Node> coloredList = dSatur(objListSort);

        List<Box> boxList = new ArrayList<>();

        try{
            //Si il n'y a pas d'objet on retourne une liste vide
            if(coloredList.size()==0){
                return new ArrayList<>();
            }else {//On crée une boite si il y a des objets
                boxList.add(new Box(boxHeight));
            }


            int currentObject = 1;
            for(Node n : coloredList){
                boolean bool = false;
                if(n.getHeight() > boxHeight)
                        throw new ObjectTooFat();

                for(Box b : boxList){
                    if(n.getHeight() <= b.getHeightLeft()) {
                        b.addObjetToBox(currentObject, n);
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    Box newBox = new Box(boxHeight);
                    newBox.addObjetToBox(currentObject,n);
                    boxList.add(newBox);
                }
                currentObject++;
            }

        }catch(ObjectTooFat e){
            System.out.println("An object is too fat for the basic height of a box");
        }
        return boxList;
    }

    public List<Box> dSaturWithBFDPacking(List<Node> objListSort, int boxHeight)throws ObjectTooFat {

        List<Node> coloredList = dSatur(objListSort);

        List<Box> boxList = new ArrayList<>();

        try{
            //Si il n'y a pas d'objet on retourne une liste vide
            if(coloredList.size()==0){
                return new ArrayList<>();
            }else {//On crée une boite si il y a des objets
                boxList.add(new Box(boxHeight));
            }
            int currentObject = 1;

            //Parcours des objets
            for (Node o : coloredList) {
                if(o.getHeight() > boxHeight)
                    throw new ObjectTooFat();

                Box bestBox = boxList.get(0);
                boolean isInsert = false;
                for(Box b : boxList){
                    if (o.getHeight() <= b.getHeightLeft()){                                //On test si l'objet peut rentrer dans la boite
                        if(o.getHeight() + b.getHeightLeft()                                //On test si la taille restante est meilleure que la dernière meilleure boite
                                > bestBox.getHeightLeft())  {
                            bestBox = b;                                                       //On définit la meilleure boite comme étant celle ci (Si meilleure)
                            isInsert = true;                                                   //L'objet sera inséré dans une boite
                        }                                                                       //Sinon la meilleur boite restera celle de base
                    }                                                                           //Le booleen isInsert servant à vérifier si l'objet sera inséré dans une boite existante
                                                                                                   //Dans le cas où aucune des condition n'est respecté, il sera restera à false
                }
                if(isInsert)                                                                    //On test si l'objet a une boite de destination
                    bestBox.addObjetToBox(currentObject, o);                                        //On ajoute l'objet à la meilleure boite
                else{                                                                           //Sinon
                    Box newBox = new Box(boxHeight);                                         //On crée une nouvelle boite
                    boxList.add(newBox);                                                            //On ajoute une nouvelle boite
                    bestBox = newBox;                                                               //On ajoute l'objet à la nouvlle boite
                    bestBox.addObjetToBox(currentObject, o);                                        //On ajoute l'objet à la meilleure boite
                }
                currentObject++;
            }


        }catch(ObjectTooFat e){
            System.out.println("An object is too fat for the basic height of a box");
        }
        return boxList;
    }




}

package partie2;

import partie2.error.ObjectTooFat;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {

    public int firstPacking(List<Node> objList, int boxHeight){
        int res = 0;
        int box = 1;
        for (Node obj: objList) {
            if(res + obj.getHeight()<= boxHeight)
                res += obj.getHeight();
            else
                box++;
            res = obj.getHeight();
        }
        return box;
    }

    public int firstPackingOtherImplementation(List<Node> objList, int boxHeight){
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
            int currentObject = 0;

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

    public List<Node> readFile(String path) throws IOException {

        List<Node> nodeList = new ArrayList<>();

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        int nbEdge = 0;
        while ((st = br.readLine()) != null) {
            String[] lineSplit = st.split(" ");

            if(lineSplit[0].equals("p")) {
                for(int i = 0; i < Integer.parseInt(lineSplit[2]); i++){
                    Random r = new Random();
                    int random =  r.nextInt((50 - 10) + 1) + 10;
                    nodeList.add(new Node(random, new ArrayList<>()));
                }
            }


            if(lineSplit[0].equals("e")) {
                int edge1 = Integer.parseInt(lineSplit[1]);
                int edge2 = Integer.parseInt(lineSplit[2]);
                nodeList.get(edge1-1).addNeighbour(nodeList.get(edge2-1));
                nodeList.get(edge2-1).addNeighbour(nodeList.get(edge1-1));
                nbEdge++;
            }
        }
        System.out.println("NOMBRE EDGE : "+nbEdge);
        System.out.println("------------------- INITIALISTAION DU GRAPHE TERMINE ----------------");

        for(Node obj: nodeList){
            System.out.println("NODE NEIGHBOUR : " + obj.getNeighbourList().size());
        }

        return nodeList;
    }



    public List<Node> dSatur(List<Node> nodeList){

        List<Node> nodeColored = new ArrayList<>();
        System.out.println("SIZE OF ORIGINAL LIST : "+ nodeList);
        System.out.println("SIZE OF COLORED LIST : "+ nodeColored);

        Node maxDegreeNode =null;
//        System.out.println("SIZE OF Neighbour node 2: "+  nodeList.get(2).getNeighbourList().size());

        //Identification du sommet avec le degré max
        for (Node obj : nodeList) {
            if (maxDegreeNode == null || maxDegreeNode.getNeighbourList().size() < obj.getNeighbourList().size()) {
//                System.out.println("SATURATION OF "+ obj + " : "+ obj.getNeighbourList().size());
                maxDegreeNode = obj;
            }
        }
        if (maxDegreeNode != null) {
            maxDegreeNode.setColor(1);
            nodeColored.add(maxDegreeNode);
            nodeList.remove(maxDegreeNode);
        }else
            System.out.println("Aucun objet trouvé");

        while(nodeList.size()>0) {
            Node maxSaturateNode = nodeList.get(0);
            //Choix d'un sommet avec le degrée de saturation max

            for (Node obj : nodeList) {
                System.out.println("NUMBER OF COLORED NEIGHBOUR : "+ obj.getNumberOfColoredNeighbour());
                if(maxSaturateNode != null)
                    System.out.println("NUMBER OF COLORED NEIGHBOUR : "+ maxSaturateNode.getNumberOfColoredNeighbour());

                if (maxSaturateNode.getNumberOfColoredNeighbour() < obj.getNumberOfColoredNeighbour()) {
                    maxSaturateNode = obj;
                }

                if (maxSaturateNode.getNumberOfColoredNeighbour() == obj.getNumberOfColoredNeighbour()) {
                    if (obj.getNeighbourList().size() >= maxSaturateNode.getNeighbourList().size()) {
                        maxSaturateNode = obj;
                    }
                }
            }

            List<Integer> colorAlreadyAssign = new ArrayList<>();                                           //List des couleur déjà associé aux voisin de l'objet en cours

            for (Node obj : maxSaturateNode.getNeighbourList()) {
                    colorAlreadyAssign.add(obj.getColor());
            }

            System.out.println("COLOR OF NEIGHBOUR FOR NODE "+ maxSaturateNode + "    : "+ colorAlreadyAssign);

//            Collections.sort(colorAlreadyAssign);                                                           //Tris des entier dans l'ordre croissant

            boolean colorFound = false;                                                                     //Boolean permettantde vérifier si une couleur a été trouvé (qui n'est donc pas dans les voisins)
            int i = 1;
            while(!colorFound) {                                                                            //On indent un entier qui défini la couleur tant qu'on a pas trouvé une couleur qui n'est pas dans un voisin
                System.out.println("i tested : "+ i);
                if (!colorAlreadyAssign.contains(i)) {                                                      //Si la couleur n'est pas dans un voisin
                    colorFound = true;                                                                      //On marque que la couleur est trouvé
                    maxSaturateNode.setColor(i);                                                          //On met cette couleur à l'objet courant
                    nodeColored.add(maxSaturateNode);
                    nodeList.remove(maxSaturateNode);
                }else {
                    i++;
                }
            }
            System.out.println("\n\n----------------CHANGE OF NODE----------------\n\n");

//
//            System.out.println("         OBJ : " + maxSaturateNode);
//            System.out.println("         OBJ NUMBER OF COLORED NEIGHBOUR : " + maxSaturateNode.getNeighbourList());
//            System.out.println("         OBJ COLORED : " + maxSaturateNode.getColor());
//
//            System.out.println("         OBJ TO COMPARE : " + maxSaturateNode.getNeighbourList().get(3));
//            System.out.println("         OBJ COLOR TO COMPARE : " + maxSaturateNode.getNeighbourList().get(3).getColor());
//            System.out.println("         OBJ NUMBER OF COLORED NEIGHBOUR : " + maxSaturateNode.getNeighbourList().get(3).getNeighbourList().contains(maxDegreeNode));
//            System.out.println();
        }

        boolean faux = false;
        for(Node o: nodeColored){
            int color = o.getColor();
            System.out.println("Color to test : "+ color);
            for(Node o2: o.getNeighbourList()){
                if(o2.getColor() == color){
                    faux = true;
                }
            }
        }
        System.out.println("La vérif trouve un conflit dans les voisins ? " + faux);
        return nodeColored;
    }

    public int findNumberOfColor(List<Node> nodeList){
        List<Integer> color = new ArrayList<>();
        for (Node obj : nodeList){
            if(!color.contains(obj.getColor())){
                color.add(obj.getColor());
            }
        }
        System.out.println("COLOR LIST : "+color);
        return color.size();
    }


    public List<Box> dSaturWithFFDPacking(List<Node> objListSort, int boxHeight)throws ObjectTooFat {
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
                    if(!b.isInConflictWithColor(o)) {                                                    //On test si l'objet est en conflit
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


}

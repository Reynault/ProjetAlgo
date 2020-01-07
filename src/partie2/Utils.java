package partie2;

import partie2.error.ObjectTooFat;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {

    public int firstPacking(List<Object> objList, int boxHeight){
        int res = 0;
        int box = 1;
        for (Object obj: objList) {
            if(res + obj.getHeight()<= boxHeight)
                res += obj.getHeight();
            else
                box++;
            res = obj.getHeight();
        }
        return box;
    }

    public int firstPackingOtherImplementation(List<Object> objList, int boxHeight){
        int totalHeight = 0;
        for(Object o :objList){
            totalHeight += o.getHeight();
        }
        return (int)Math.ceil((double)totalHeight / (double)boxHeight);
    }


    public List<Box> firstFitDecreasingPacking(List<Object> objListSort, int boxHeight)throws ObjectTooFat {
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
            for (Object o : objListSort) {
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

    public List<Box> bestFitDecreasingPacking(List<Object> objListSort, int boxHeight)throws ObjectTooFat{
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
            for (Object o : objListSort) {
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

    public List<Object> readFile(String path) throws IOException {

        List<Object> objectList = new ArrayList<>();

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
                    objectList.add(new Object(random, new ArrayList<>()));
                }
            }


            if(lineSplit[0].equals("e")) {
                int edge1 = Integer.parseInt(lineSplit[1]);
                int edge2 = Integer.parseInt(lineSplit[2]);
                objectList.get(edge1-1).addNeighbour(objectList.get(edge2-1));
                objectList.get(edge2-1).addNeighbour(objectList.get(edge1-1));
                nbEdge++;
            }
        }
        System.out.println("NOMBRE EDGE : "+nbEdge);
        System.out.println("------------------- INITIALISTAION DU GRAPHE TERMINE ----------------");
        return objectList;
    }



    public List<Object> dSatur(List<Object> objectList){
        List<Object> objectColored = new ArrayList<>();
        List<Object> otherObjecList = objectList;

        System.out.println("SIZE OF ORIGINAL LIST : "+ objectList);
        System.out.println("SIZE OF OTHER LIST : "+ otherObjecList);
        System.out.println("SIZE OF COLORED LIST : "+ objectColored);

        Object maxDegreeObject =null;
        System.out.println("SIZE OF Neighbour node 2: "+  objectList.get(2).getNeighbourList().size());

        //Identification du sommet avec le degré max
        for (Object obj : otherObjecList) {
            if (maxDegreeObject == null || maxDegreeObject.getNeighbourList().size() < obj.getNeighbourList().size()) {
                System.out.println("SATURATION OF "+ obj + " : "+ obj.getNeighbourList().size());
                maxDegreeObject = obj;
            }
        }
        if (maxDegreeObject != null) {
            maxDegreeObject.setColor(1);
            objectColored.add(maxDegreeObject);
            otherObjecList.remove(maxDegreeObject);
        }else
            System.out.println("Aucun objet trouvé");



        while(objectList.size()!=0) {

            System.out.println("ORIGINAL LIST : "+ objectList);
            System.out.println("SIZE OF COLORED LIST : "+ objectColored);

            Object maxSaturateObject = null;
            //Choix d'un sommet avec le degrée de saturation max

            for (Object obj : otherObjecList) {
                System.out.println("NUMBER OF NEIGHBOUR OF "+ obj + " : "+ obj.getNeighbourList().size());

                System.out.println("SATURATION OF "+ obj + " : "+ obj.getNumberOfColoredNeighbour());
                if (maxSaturateObject == null || maxSaturateObject.getNumberOfColoredNeighbour() < obj.getNumberOfColoredNeighbour()) {
                    maxSaturateObject = obj;
                } else if (maxSaturateObject.getNumberOfColoredNeighbour() == obj.getNumberOfColoredNeighbour()) {
                    if (obj.getNeighbourList().size() > maxSaturateObject.getNeighbourList().size()) {
                        maxSaturateObject = obj;
                    }
                }
            }

            System.out.println("         OBJ : " + maxSaturateObject);
//            System.out.println("         OBJ NUMBER OF COLORED NEIGHBOUR : " + maxSaturateObject.getNumberOfColoredNeighbour());
//
//            System.out.println("         LEFT IN ORIGINAL LIST : " + otherObjecList.size());

            List<Integer> colorAlreadyAssign = new ArrayList<>();                                           //List des couleur déjà associé aux voisin de l'objet en cours

            if (maxSaturateObject != null) {

                for (Object obj : maxSaturateObject.getNeighbourList()) {
                    if(obj.getColor()!=0)
                        colorAlreadyAssign.add(obj.getColor());
                }

                Collections.sort(colorAlreadyAssign);                                                           //Tris des entier dans l'ordre croissant
                System.out.println("                COLOR LIST SIZE :"+colorAlreadyAssign.size());
                System.out.println("                COLOR LIST :"+colorAlreadyAssign);

                boolean colorFound = false;                                                                     //Boolean permettantde vérifier si une couleur a été trouvé (qui n'est donc pas dans les voisins)
                int i = 1;
                while(!colorFound) {                                                             //On indent un entier qui défini la couleur tant qu'on a pas trouvé une couleur qui n'est pas dans un voisin
                    System.out.println(i);
                    if (!colorAlreadyAssign.contains(i)) {                                                      //Si la couleur n'est pas dans un voisin
                        colorFound = true;                                                                      //On marque que la couleur est trouvé
                        maxSaturateObject.setColor(i);                                                          //On met cette couleur à l'objet courant
                        otherObjecList.remove(maxSaturateObject);
                        objectColored.add(maxSaturateObject);
                    }
                    i++;
                }
            }else{
                System.out.println("MaxSaturationObject null!-----------------------------------------------------");
            }

        }
        return objectColored;
    }

    public int findNumberOfColor(List<Object> objectList){
        List<Integer> color = new ArrayList<>();
        for (Object obj : objectList){
            if(!color.contains(obj.getColor())){
                color.add(obj.getColor());
            }
        }
        return color.size();
    }

}

package partie2;

import java.util.ArrayList;
import java.util.List;

public class Box {

    int boxHeight;
    List<Tuple> objectList;
    List<Object> objectConflict;
    int heightLeft;

    public Box(int boxHeight){
        this.boxHeight = boxHeight;
        this.objectList = new ArrayList<>();
        this.heightLeft = boxHeight;
        objectConflict = new ArrayList<>();
    }

    public int getHeightLeft() {
        return heightLeft;
    }

    /**
     * Methode qui ajoute un Objet à la boite
     * On ajoute le Tuple
     * On ajoute les objets qui sotnt en conflit avec le dernier objet inséré
     * On met à jour la place restante
     * @param index
     * @param object
     */
    public void addObjetToBox(int index, Object object){
        this.objectList.add(new Tuple(index, object.getHeight()));
        this.heightLeft = this.heightLeft - object.getHeight();
        this.objectConflict.addAll(object.getNeighbourList());
    }

    public boolean isInConflict(Object o) {
//        System.out.println("ObjectConflict : " + objectConflict);
        return objectConflict.contains(o);
    }

    public String display() {
        int i = 0;
        String res = "(" + (boxHeight - heightLeft) + "[";

        for(Tuple t: objectList){
            res += t.display();
            if(i != objectList.size()-1){
                res += ",";
            }
            i++;
        }
        res += "])";
        return res;
    }
}

package partie2;

import java.util.ArrayList;
import java.util.List;

public class Box {

    int boxHeight;
    List<Node> nodeList;
    List<Tuple> objectListTuple;
    List<Node> nodeConflict;
    int heightLeft;

    public Box(int boxHeight){
        this.boxHeight = boxHeight;
        this.nodeList = new ArrayList<>();
        this.objectListTuple = new ArrayList<>();
        this.heightLeft = boxHeight;
        nodeConflict = new ArrayList<>();
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
     * @param node
     */
    public void addObjetToBox(int index, Node node){
        this.objectListTuple.add(new Tuple(index, node.getHeight()));
        this.nodeList.add(node);
        this.heightLeft = this.heightLeft - node.getHeight();
        for(Node obj : node.getNeighbourList()){
            if(!this.nodeConflict.contains(obj))
                this.nodeConflict.add(obj);
        }
    }

    public boolean isInConflict(Node o) {
//        System.out.println("ObjectConflict : " + nodeConflict);
        return nodeConflict.contains(o);
    }

    public boolean isInConflictWithColor(Node o) {
        boolean res =  nodeConflict.contains(o);
        for (Node obj: nodeList) {
            if (obj.getColor() == o.getColor()) {
                res = true;
                break;
            }
        }
        return res;
    }

    public String display() {
        int i = 0;
        String res = "(" + (boxHeight - heightLeft) + "[";

        for(Tuple t: objectListTuple){
            res += t.display();
            if(i != objectListTuple.size()-1){
                res += ",";
            }
            i++;
        }
        res += "])";
        return res;
    }


}

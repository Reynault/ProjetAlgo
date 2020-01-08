package partie2;

import java.util.List;

public class Node implements Comparable<Node>{

    private List<Node> neighbourList;
    private int height;

    private int color = 0;

    public Node(int height, List<Node> neighbourList){
        this.height = height;
        this.neighbourList = neighbourList;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Methode qui test si l'objet o est présent dans les voisins de cet objet
     * Si c'est le cas (il est présent dans neighbourList) alors la fonction
     * retourne vrai, sinon false
     * @param o Objet à tester
     * @return true si o est dans neighbourList, false sinon
     */
    public boolean isANeighbour(Node o){
        return  neighbourList.contains(o);
    }


    public List<Node> getNeighbourList(){
        return neighbourList;
    }

    public void addNeighbour(Node neighbour){
        neighbourList.add(neighbour);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Methode qui retourne le nombre de voisins colorées
     * @return
     */
    public int getNumberOfColoredNeighbour() {
        int res=0;
        for(Node obj : neighbourList){
            if(obj.getColor()!= 0){
                res ++;
            }
        }
        return res;
    }


    /*
     **  Implement the natural order for this class
     */
    public int compareTo(Node p)
    {
        return Integer.compare(getHeight(), p.getHeight());
    }


    public int getDegree() {
        return neighbourList.size();
    }
}

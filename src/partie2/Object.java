package partie2;

import java.util.List;

public class Object implements Comparable{

    private int height;

    private List<Object> neighbourList;

    private int color = 0;

    public Object(int height, List<Object> neighbourList){
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
    public boolean isANeighbour(Object o){
        return  neighbourList.contains(o);
    }


    public List<Object> getNeighbourList(){
        return neighbourList;
    }

    public void addNeighbour(Object neighbour){
        neighbourList.add(neighbour);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int compareTo(java.lang.Object o) {
        Object object = (Object)o;
        int compareHeight = object.getHeight();
        return compareHeight-this.height;
    }

    /**
     * Methode qui retourne le nombre de voisins colorées
     * @return
     */
    public int getNumberOfColoredNeighbour() {
        int res=0;
        for(Object obj : neighbourList){
            if(obj.getColor()!= 0){
                res ++;
            }
        }
        return res;
    }



}

package partie2;

public class Tuple {

    int index;
    int objectHeight;

    public Tuple(int index, int objectHeight){
        this.index = index;
        this.objectHeight = objectHeight;
    }

    public String display() {
        return index + "-" + objectHeight;
    }
}

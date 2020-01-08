package partie2;

import partie2.error.ObjectTooFat;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String args[]) throws IOException, ObjectTooFat {

        Utils utils = new Utils();

        List<Node> obj = new ArrayList<>();

        Random r = new Random();
        int random = r.nextInt((50 - 10) + 1) + 10;

        obj.add(new Node( r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Node(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Node(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Node(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Node(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Node(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));


        int res = utils.firstPacking(obj, 10);
        System.out.println("Le nombre de boite est de : "+res);

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        List<Node> nodeList = utils.readFile("DSJC125.5.txt");

        //FIRST FIT DECREASING PACKING
       Collections.sort(nodeList, new Comparator<Node>() {
           @Override public int compare(Node p1, Node p2) {
               return p2.getHeight() - p1.getHeight(); // Ascending
           }
       });
        List<Box> listBox = utils.firstFitDecreasingPacking(nodeList, 150);
        int i = 0;
        for (Box b: listBox) {
            System.out.println("BOX "+ i + " : ");
            System.out.print(b.display());
            System.out.println("\n");
            i++;
        }
        System.out.println("TOTAL OF BOX : " + listBox.size());

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        //BEST FIT DECREASING PACKING
        List<Box> listBoxBest = utils.bestFitDecreasingPacking(nodeList, 150);

        System.out.println("   NODE NUMBER : "+ nodeList.get(0).getNeighbourList().size());


        int j = 0;
        for (Box b: listBoxBest) {
            System.out.println("BOX "+ j + " : ");
            System.out.print(b.display());
            System.out.println("\n");
            j++;
        }
        System.out.println("TOTAL OF BOX : " + listBoxBest.size());

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        List<Node> nodeList2 = utils.readFile("DSJC125.5.txt");

//        //FIRST FIT DECREASING PACKING
        Collections.sort(nodeList2);

        System.out.println("   NODE NUMBER : "+ nodeList2.size());
        System.out.println("   NODE NEIGHBOUR of NODE 1 : "+ nodeList2.get(0).getNeighbourList().size());


        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        List<Node> coloredList = utils.dSatur(nodeList2);
//        for(Node objColored : coloredList){
//            System.out.println("   COLOR : "+objColored.getColor());
//        }
        System.out.println("TOTAL OF COLORED NODE : " + coloredList.size());
        System.out.println("NUMBER OF COLOR USE : "+ utils.findNumberOfColor(coloredList));

    }



}



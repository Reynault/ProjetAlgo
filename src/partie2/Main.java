package partie2;

import partie2.error.ObjectTooFat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String args[]) throws IOException, ObjectTooFat {

        Utils utils = new Utils();

        List<Object> obj = new ArrayList<>();

        Random r = new Random();
        int random = r.nextInt((50 - 10) + 1) + 10;

        obj.add(new Object( r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Object(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Object(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Object(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Object(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));
        obj.add(new Object(r.nextInt((50 - 10) + 1) + 10, new ArrayList<>()));


        int res = utils.firstPacking(obj, 10);
        System.out.println("Le nombre de boite est de : "+res);

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        List<Object> objectList = utils.readFile("DSJC125.5.txt");

        //FIRST FIT DECREASING PACKING
       Collections.sort(objectList);
        List<Box> listBox = utils.firstFitDecreasingPacking(objectList, 150);
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
        List<Box> listBoxBest = utils.bestFitDecreasingPacking(objectList, 150);
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

        List<Object> objectList2 = utils.readFile("DSJC125.5.txt");
//
//
//        //FIRST FIT DECREASING PACKING
        Collections.sort(objectList2);

        System.out.println("   NODE NUMBER : "+objectList2.size());


        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println();

        List<Object> coloredList = utils.dSatur(objectList2);
        for(Object objColored : coloredList){
            System.out.println("   COLOR : "+objColored.getColor());
        }
        System.out.println("TOTAL OF COLORED NODE : " + coloredList.size());
        System.out.println("NUMBER OF COLOR USE : "+ utils.findNumberOfColor(coloredList));

    }



}



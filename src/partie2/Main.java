package partie2;

import error.ObjectTooFat;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String args[]) throws IOException, ObjectTooFat {
        Utils utils = new Utils();

        System.out.println();
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("------------------------ DSJC125.5.txt------------------------------");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println();

        List<Node> nodeList = utils.readFile("DSJC125.5.txt");

        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));

        //FIRST FIT DECREASING PACKING
        nodeList.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());


        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());

        //Car DSatur supprime de nodeList, on doit relire le fichier
        List<Node> nodeList2 = utils.readFile("DSJC125.5.txt");
        nodeList2.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());

        System.out.println();
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("------------------------ DSJC250.5.txt ------------------------------");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println();

        nodeList = utils.readFile("DSJC250.5.txt");

        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));

        //FIRST FIT DECREASING PACKING
        nodeList.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());


        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());

        //Car DSatur supprime de nodeList, on doit relire le fichier
        nodeList2 = utils.readFile("DSJC250.5.txt");
        nodeList2.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());


        System.out.println();
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("------------------------ DSJC500.5.txt ------------------------------");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println();

        nodeList = utils.readFile("DSJC500.5.txt");

        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));

        //FIRST FIT DECREASING PACKING
        nodeList.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());


        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());

        //Car DSatur supprime de nodeList, on doit relire le fichier
        nodeList2 = utils.readFile("DSJC500.5.txt");
        nodeList2.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());



        System.out.println();
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("------------------------ DSJC1000.5.txt -----------------------------");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println();
        nodeList = utils.readFile("DSJC1000.5.txt");

        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));

        //FIRST FIT DECREASING PACKING
        nodeList.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());


        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());

        //Car DSatur supprime de nodeList, on doit relire le fichier
        nodeList2 = utils.readFile("DSJC1000.5.txt");
        nodeList2.sort(Comparator.comparingInt(Node::getHeight));

        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());

    }
}



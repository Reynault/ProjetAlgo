package partie2;

import error.ObjectTooFat;
import partie1.Implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String args[]) throws IOException, ObjectTooFat {
        Utils utils = new Utils();

//        System.out.println();
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println("------------------------ DSJC125.5.txt------------------------------");
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println();
//
//        List<Node> nodeList = utils.readFile("/ressources/DSJC125.5.txt");
//
//        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));
//
//        //FIRST FIT DECREASING PACKING
//        nodeList.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
//        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());
//
//
//        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());
//
//        //Car DSatur supprime de nodeList, on doit relire le fichier
//        List<Node> nodeList2 = utils.readFile("ressources/DSJC125.5.txt");
//        nodeList2.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());
//
//        System.out.println();
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println("------------------------ DSJC250.5.txt ------------------------------");
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println();
//
//        nodeList = utils.readFile("ressources/DSJC250.5.txt");
//
//        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));
//
//        //FIRST FIT DECREASING PACKING
//        nodeList.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
//        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());
//
//
//        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());
//
//        //Car DSatur supprime de nodeList, on doit relire le fichier
//        nodeList2 = utils.readFile("ressources/DSJC250.5.txt");
//        nodeList2.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());
//
//
//        System.out.println();
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println("------------------------ DSJC500.5.txt ------------------------------");
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println();
//
//        nodeList = utils.readFile("ressources/DSJC500.5.txt");
//
//        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));
//
//        //FIRST FIT DECREASING PACKING
//        nodeList.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
//        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());
//
//
//        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());
//
//        //Car DSatur supprime de nodeList, on doit relire le fichier
//        nodeList2 = utils.readFile("ressources/DSJC500.5.txt");
//        nodeList2.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());
//
//
//
//        System.out.println();
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println("------------------------ DSJC1000.5.txt -----------------------------");
//        System.out.println("/////////////////////////////////////////////////////////////////////");
//        System.out.println();
//        nodeList = utils.readFile("ressources/DSJC1000.5.txt");
//
//        System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));
//
//        //FIRST FIT DECREASING PACKING
//        nodeList.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
//        System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());
//
//
//        System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());
//
//        //Car DSatur supprime de nodeList, on doit relire le fichier
//        nodeList2 = utils.readFile("/ressources/DSJC1000.5.txt");
//        nodeList2.sort(Comparator.comparingInt(Node::getHeight));
//
//        System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());


        if (args.length != 1) {
            System.out.println("Usage: java <chemin du fichier>");
            System.out.println("exemple: java ../partie1/DSJC1000.5.txt");
        }else{
            try {
                // Récupération du fichier
                File fichier = new File(args[0]);
                // Et lecture
                BufferedReader lecteur = new BufferedReader(new FileReader(fichier));

                // Transformation du graphe vers l'ensemble des contraintes, puis application de l'algorithme
                try {
                    System.out.println("Lecture du fichier");

                    System.out.println();
                    System.out.println("/////////////////////////////////////////////////////////////////////");
                    System.out.println("------------------------ "+ args[0] + " ------------------------------");
                    System.out.println("/////////////////////////////////////////////////////////////////////");
                    System.out.println();
                    List<Node> nodeList = utils.readFile(args[0]);

                    System.out.println("Fractionnal Packing : " + utils.fractionnalPacking(nodeList, 150));

                    //FIRST FIT DECREASING PACKING
                    nodeList.sort(Comparator.comparingInt(Node::getHeight));

                    System.out.println("FFD-Packing : " + utils.firstFitDecreasingPacking(nodeList, 150).size());
                    System.out.println("BFD-Packing : " + utils.bestFitDecreasingPacking(nodeList, 150).size());


                    System.out.println("DSatur-FFD-Packing : " + utils.dSaturWithFFDPacking(nodeList, 150).size());

                    //Car DSatur supprime de nodeList, on doit relire le fichier
                    List<Node> nodeList2 = utils.readFile(args[0]);
                    nodeList2.sort(Comparator.comparingInt(Node::getHeight));

                    System.out.println("DSatur-BFD-Packing : " + utils.dSaturWithBFDPacking(nodeList2, 150).size());


                }catch (Exception e){
                    System.out.println("Erreur lors de la lecture du fichier: "+e.getMessage());
                    e.printStackTrace();
                }

                lecteur.close();
            }catch (Exception e){
                System.out.println("Erreur lors de la récupération du fichier: "+e.getMessage());
            }
        }
    }
}



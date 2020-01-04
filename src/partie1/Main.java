package partie1;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Classe principale dans laquelle se trouve l'algorithme probabiliste
 * permettant de résoudre le problème (3, 2) - SSS
 */
public class Main {

    /**
     * Main qui exécute le programme.
     *
     * Il demande un chemin vers un fichier (qui se trouve dans le dossier contenant les graphes (doc/partie1/), donc le chemin
     * est partiellement indiqué, il suffit juste de donner le nom du graphe à tester
     *
     * S'il n'y a pas de paramètres, le main exécute l'algorithme sur tous les graphes
     * à tester.
     *
     * @param arguments Le nom du fichier à indiquer
     */
    public static void main(String[] arguments) {
        // Si aucuns arguments
        if (arguments.length != 1) {
            // Rappel sur l'utilisation du programme
            System.out.println("Usage : java Main <<Nom du graphe>>");
            System.out.println("Exemple : java Main graph1");
        }else{
            try {
                // Sinon, récupération du fichier
                File file = new File("doc/partie1/"+arguments[0]+".txt");
                // Et lecture
                FileInputStream fis = new FileInputStream(file);


            }catch (IOException e){
                System.out.println("Erreur lors de la récupération du fichier: "+e.getMessage());
            }
        }
    }

    /**
     * Méthode permettant de passer d'un fichier contenant un graphe représenté
     * par matrice d'adjacence, vers un tableau de contraintes binaires
     * @param fichier stream vers le fichier ouvert
     * @return tableau contenant les contraintes binaires
     */
    public static boolean[][][][] graphVersSSS(FileInputStream fichier) {
        int largeur = fichier.

        int[][][][] res;



        return null;
    }
}

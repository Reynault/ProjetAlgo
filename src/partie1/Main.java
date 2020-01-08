package partie1;


import java.io.*;

/**
 * Classe principale dans laquelle se trouve l'algorithme probabiliste
 * permettant de résoudre le problème (3, 2) - SSS
 */
public class Main {

    /**
     * Main qui exécute le programme.
     *
     * Il demande un chemin vers un fichier qui contient un graphe présentée par matrice d'adjacences.
     *
     * @param arguments Le nom du fichier à indiquer
     */
    public static void main(String[] arguments) {
        // Si aucuns arguments
        if (arguments.length != 1) {
            System.out.println("Usage: java <Nom du programme> <nom du fichier>");
            System.out.println("exemple: java main ../partie1/graph1.txt");
        }else{
            try {
                // Récupération du fichier
                File fichier = new File(arguments[0]);
                // Et lecture
                BufferedReader lecteur = new BufferedReader(new FileReader(fichier));

                // Transformation du graphe vers l'ensemble des contraintes, puis application de l'algorithme
                try {
                    System.out.println("Lecture du fichier");
                    boolean[][][][] binaire = graphVersSSS(lecteur);

                    boolean[][] unaire = new boolean[binaire.length][Implementation.NB_COULEUR];
                    Implementation impl = new Implementation(binaire, unaire);
                    System.out.println("Resolution en cours");
                    boolean res = impl.resoudre();

                    System.out.println("---");
                    if(res){
                        System.out.println("Graphe coloriable");
                    }else{
                        System.out.println("Graphe non coloriable");
                    }
                }catch (Exception e){
                    System.out.println("Erreur lors de la lecture du fichier: "+e.getMessage());
                    e.printStackTrace();
                }

                lecteur.close();
            }catch (Exception e){
                System.out.println("Erreur lors de la recuperation du fichier: "+e.getMessage());
            }
        }
    }

    /**
     * Méthode permettant de passer d'un fichier contenant un graphe représenté
     * par matrice d'adjacence, vers un tableau de contraintes binaires (Comme présenté dans le rapport)
     * @param lecteur stream vers le fichier ouvert
     * @return tableau contenant les contraintes binaires
     */
    public static boolean[][][][] graphVersSSS(BufferedReader lecteur) throws Exception{
        int compteur = 0, taille = 0;
        boolean[][][][] res = null;
        boolean premiereLigne = true;
        String ligne;
        char caractereCourant;

        // Lecture des lignes du fichier
        while((ligne = lecteur.readLine()) != null){

            // Si c'est la première ligne, récupération de la taille
            if(premiereLigne) {
                taille = ligne.length();
                res = new boolean[taille][taille][Implementation.NB_COULEUR][Implementation.NB_COULEUR];
                premiereLigne = false;
            }

            // Vérification si le compteur (numéro de la ligne courante) est supérieur à la taille d'une ligne
            if(compteur >= taille){
                throw new Exception("Fichier non conforme, nombre de colonnes différent du nombre de lignes ");
            }

            // Pour tous les caractères d'une ligne (en partant du compteur puisqu'on ne prend que la moitié de la matrice)
            for(int i = compteur; i < taille; i++){
                caractereCourant = ligne.charAt(i);

                // S'il y a un 1, alors il y a une arête entre les deux sommets
                // Il faut donc ajouter dans le tableau les trois contraintes suivantes
                // [(S1, R), (S2, R)], [(S1, B), (S2, B)], [(S1, V), (S2, V)]
                // avec S1 correspondant au sommet de la ligne
                // et S2 correspondant au sommet de la colonne
                //
                // Pour plus d'explications, voir le rapport question 5.
                if(caractereCourant == '1'){
                    res[compteur][i][Implementation.ROUGE][Implementation.ROUGE] = true;
                    res[compteur][i][Implementation.VERT][Implementation.VERT] = true;
                    res[compteur][i][Implementation.BLEU][Implementation.BLEU] = true;
                }
            }

            compteur ++;
        }

        // Vérifications de la taille, et si le fichier n'est pas vide
        if(compteur != taille){
            throw new Exception("Fichier non conforme, nombre de colonnes différent du nombre de lignes ");
        }
        if(res == null){
            throw new Exception("Fichier vide.");
        }

        return res;
    }
}

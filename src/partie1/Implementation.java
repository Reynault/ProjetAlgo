package partie1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation est une classe qui contient l'implémentation de l'algorithme proposé pour résoudre
 * le problème (3-2)-SSS
 */
public class Implementation {
    private boolean[][][][] binaires;
    private boolean[][] unaires;

    private int nbUnaire;
    private int nbBinaire;
    private List<Integer> variables;
    private int taille;

    /**
     * Constructeur prenant en paramètre les deux tableaux contenant les contraintes
     *
     * @param binaires le tableau de booléen indiquant les contraintes binaires
     * @param unaires  le tableau de booléen indiquant les contraintes unaires
     */
    public Implementation(boolean[][][][] binaires, boolean[][] unaires) {
        this.binaires = binaires;
        this.unaires = unaires;

        // Récupération des infos (Taille, nombre Unaire, nombre Binaire)
        this.taille = binaires.length;
        this.variables = new ArrayList<>();
        this.nbUnaire = calculNbUnaire(unaires, variables);
        this.nbBinaire = calculNbBinaire(binaires, variables);
    }

    /**
     * Méthode de récupération du nombre de contraintes binaires
     *
     * @param binaires tableau contenant les contraintes binaires
     * @param variables Dictionnaire contenant la liste des variables, ainsi que le nombre d'occurence
     * @return nombre de contraintes binaires
     */
    private int calculNbBinaire(boolean[][][][] binaires, List<Integer> variables) {
        int nbBinaire = 0;

        // Parcours des quatre dimensions, puis si l'élément est Vrai, incrémentation de 1
        for (int i = 0; i < binaires.length; i++) {
            for (int j = 0; j < binaires[i].length; j++) {
                for (int k = 0; k < binaires[i][j].length; k++) {
                    for (int l = 0; l < binaires[i][j][k].length; l++) {
                        if (binaires[i][j][k][l]) {
                            nbBinaire++;

                            // Mise à jour de la liste des variables
                            if(!variables.contains(i)){
                                variables.add(i);
                            }

                            if(!variables.contains(j)){
                                variables.add(j);
                            }
                        }
                    }
                }
            }
        }

        return nbBinaire;
    }

    /**
     * Méthode de récupération du nombre de contraintes unaires
     *
     * @param unaires tableau contenant les contraintes unaires
     * @param variables Dictionnaire contenant la liste des variables, ainsi que le nombre d'occurence
     * @return nombre de contraintes unaires
     */
    private int calculNbUnaire(boolean[][] unaires, List<Integer> variables) {
        int nbUnaire = 0;

        // Parcours des deux dimensions, si un élément est vrai, incrémentation de 1
        for (int i = 0; i < unaires.length; i++) {
            for (int j = 0; j < unaires[i].length; j++) {
                if (unaires[i][j]) {
                    nbUnaire++;

                    // Mise à jour de la liste des variables
                    if(!variables.contains(i)){
                        variables.add(i);
                    }
                }
            }
        }

        return nbUnaire;
    }

    /**
     * Implementation de l'algorithme présenté dans le sujet, utilisation des quatre règles, avec comme
     * condition d'arrêt, soit de trouver une contradiction, soit d'avoir un ensemble de contraintes vide.
     *
     * @param binaires  le tableau de booléen indiquant les contraintes binaires
     * @param unaires   le tableau de booléen indiquant les contraintes unaires
     * @param nbBinaire nombre de contraintes binaires
     * @param nbUnaire  nombre de contraintes unaires
     * @return un booléen indiquant si l'ensemble est satisfiable ou non
     */
    private boolean tentative(boolean[][][][] binaires, boolean[][] unaires, int nbBinaire, int nbUnaire, List<Integer> variables) {
        boolean satisfait = true;

        // L'algorithme boucle tant qu'il n'y a pas de contradiction, et encore des contraintes
        while (satisfait && variables.size() != 0){

            // Vérification de la première contrainte (arrêt en cas de succès)
            if(verifPremiereRegle(unaires)){
                satisfait = false;
            }

            // Si pas de contradiction
            if(satisfait){
                // On commence par récupérer une variable qui possède des contraintes unaires (null s'il n'y en a pas)

            }
        }



            // Si il existe une variable avec des contraintes unaires, on compte

            // S'il y en a deux application de la règle 2

            // Sinon application de la règle trois

            // S'il n'y a pas de contraintes unaires, application de la règle 4 sur la première contrainte binaire

            return false;
    }

    /**
     * Méthode qui permet de vérifier s'il y a une contradiction dans l'ensemble des contraintes unaires
     *
     * @param unaires ensemble des contraintes unaires
     * @return un booléen qui est vrai si il y a une contradiction
     */
    private boolean verifPremiereRegle(boolean[][] unaires) {
        boolean contradiction = false;

        // Parcours de l'ensemble des sommets pour vérifier si les trois contraintes existent
        for (int i = 0; i < unaires.length && !contradiction; i++) {
            if (unaires[i][0] && unaires[i][1] && unaires[i][2]) {
                contradiction = true;
            }
        }

        return contradiction;
    }

    /**
     * L'algorithme proposé correspond à un algorithme Monte-Carlo qui lorsqu'il répond Vrai (Ici, cela signifie
     * que l'ensemble est satisfiable), s'arrête, mais recommence un certain nombre de fois lorsque la réponse est Faux.
     * <p>
     * En effet, étant un algorithme probabiliste qui a une chance sur deux, qu'un ensemble C satisfiable devient
     * insatisfiable dans certains cas. (D'après la question n°9)  Nous ne sommes pas sûr si la réponse est correcte
     * dans le cas de la réponse Faux.
     * <p>
     * Cette méthode répète un certain nombre de fois l'algorithme sur l'ensemble des contraintes, puis retourne
     * Vrai s'il y a eu une réponse Vrai, et Faux sinon.
     *
     * @return un booléen indiquant si oui ou non l'ensemble de contraintes est satisfiable
     */
    public boolean resoudre() {
        // Initialisation du nombre de tentatives
        long nbTentative = 0, nbMax = Math.round(10 * Math.pow(2, ((double) taille) / 2)) + 1;
        boolean satisfait = false;

        // Boucle dans laquelle on réalise les tentative, et qui s'arrête, si on atteint le max, ou si
        // on trouve que c'est satisfiable
        while (!satisfait && nbTentative < nbMax) {
            satisfait = tentative(binaires, unaires, nbBinaire, nbUnaire, variables);
            nbTentative++;
        }

        return satisfait;
    }
}

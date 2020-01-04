package partie1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation est une classe qui contient l'implémentation de l'algorithme proposé pour résoudre
 * le problème (3-2)-SSS
 */
public class Implementation {
    public static int NB_COULEUR = 3;

    public static int ROUGE = 0;
    public static int VERT = 1;
    public static int BLEU = 2;

    private boolean[][][][] binaires;
    private boolean[][] unaires;

    private int nbUnaire;
    private int nbBinaire;
    private List<Integer> variables;
    private int taille;
    private int nbCouleur;

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
        this.nbCouleur = unaires[0].length;
    }

    /**
     * Méthode de récupération du nombre de contraintes binaires
     *
     * @param binaires  tableau contenant les contraintes binaires
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
                            if (!variables.contains(i)) {
                                variables.add(i);
                            }

                            if (!variables.contains(j)) {
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
     * @param unaires   tableau contenant les contraintes unaires
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
                    if (!variables.contains(i)) {
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
     * @param nbCouleur  nombre de couleurs
     * @return un booléen indiquant si l'ensemble est satisfiable ou non
     */
    private boolean tentative(boolean[][][][] binaires, boolean[][] unaires, int nbBinaire, int nbUnaire,
                              List<Integer> variables, int nbCouleur) {
        boolean satisfait = true;

        // L'algorithme boucle tant qu'il n'y a pas de contradiction, et encore des contraintes
        while (satisfait && variables.size() != 0) {

            // Vérification de la première contrainte (arrêt en cas de succès)
            if (verifPremiereRegle(unaires)) {
                satisfait = false;
            }

            // S'il n'y a pas de contradiction
            if (satisfait) {
                boolean[] couleurs;
                int nbContraintes;
                int variableCourante, indice;
                boolean existeUnaire = false;

                // On commence par récupérer une variable qui possède des contraintes unaires

                variableCourante = 0;
                couleurs = new boolean[nbCouleur];
                nbContraintes = 0;
                indice = 0;
                // Parcours des variables, tant qu'on ne trouve pas de contraintes unaires
                while (!existeUnaire && indice < variables.size()) {
                    variableCourante = variables.get(indice);

                    // Pour toutes les contraintes possibles pour une variable, on vérifie l'existence
                    for (int j = 0; j < unaires[variableCourante].length; indice++) {
                        // Si l'une d'entre elles existe, incrémentation du nombre et indication de l'existence
                        if (unaires[variableCourante][j]) {
                            couleurs[j] = true;
                            nbContraintes ++;
                            existeUnaire = true;
                        }
                    }
                }

                if (existeUnaire) {

                    // Suppression des contraintes unaires
                    for (int i = 0; i < unaires[variableCourante].length; i++) {
                        unaires[variableCourante][i] = false;
                        nbUnaire --;
                    }

                    if (nbContraintes == 2) {
                        // S'il y en a deux, application de la règle deux du sujet

                        // Suppression des contraintes binaires
                        for(int i = 0; i < binaires.length; i++){
                            for(int j = 0; j < nbCouleur; j++){
                                for(int k = 0; k < nbCouleur; k++) {
                                    if(!couleurs[j] && binaires[variableCourante][i][j][k]) {
                                        unaires[i][k] = true;
                                        nbUnaire ++;
                                    }

                                    if(!couleurs[j] && binaires[i][variableCourante][k][j]) {
                                        unaires[i][k] = true;
                                        nbUnaire ++;
                                    }

                                    if (binaires[variableCourante][i][j][k]) {
                                        binaires[variableCourante][i][j][k] = false;
                                        nbBinaire--;
                                    }

                                    if (binaires[i][variableCourante][k][j]) {
                                        binaires[i][variableCourante][k][j] = false;
                                        nbBinaire--;
                                    }

                                    if(!variableExiste(i, binaires, unaires, nbCouleur)){
                                        variables.remove(i);
                                    }
                                }
                            }
                        }

                        // Suppression de la variable
                        variables.remove(variableCourante);

                    } else {
                        // Sinon, application de la règle trois

                        // Récupération des couleurs
                        int c = 0;
                        boolean trouve = false;
                        while (c < couleurs.length && !trouve){
                            if(couleurs[c]){
                                trouve = true;
                            }else {
                                c++;
                            }
                        }

                        // Parcours des contraintes pour enlever les contraintes binaires
                        for(int i = 0 ; i < binaires.length; i++){
                            for(int j = 0; j < nbCouleur; j++){
                                if(binaires[variableCourante][i][c][j]){
                                    binaires[variableCourante][i][j][c] = false;
                                    nbBinaire--;
                                }
                                if(binaires[i][variableCourante][j][c]){
                                    binaires[i][variableCourante][c][j] = false;
                                    nbBinaire--;
                                }
                                if(!variableExiste(i, binaires, unaires, nbCouleur)){
                                    variables.remove(i);
                                }
                            }
                        }

                        // Parcours des contraintes pour les couples de contraintes de la forme [(x, V ),(y, b)] et [(x, B),(z, c)]
                        int c1, c2;
                        trouve = true;

                        while (trouve){
                            trouve = false;

                            for(int i = 0; i < binaires.length; i ++){
                                for(int j = 0; j < binaires.length; j++){

                                }
                            }
                        }
                    }
                } else {
                    // S'il n'y a pas de contraintes unaires, application de la règle 4 sur la première contrainte binaire
                    boolean trouve = false;

                    int v1, v2, c1, c2;

                    // Récupération d'une première contrainte
                    for(int i = 0; i < binaires.length && !trouve ; i++){
                        for(int j = 0; j < binaires[i].length && !trouve ; j++){
                            for(int k = 0; k < binaires[i][j].length && !trouve ; k++){
                                for(int l = 0; l < binaires[i][j][k].length && !trouve ; l++){
                                    if(binaires[i][j][k][l]){
                                        v1 = i;
                                        v2 = j;
                                        c1 = k;
                                        c2 = l;
                                        trouve = true;
                                    }
                                }
                            }
                        }
                    }

                    // Application de la modification
                    int choix = new Random().nextInt(4);
                    switch (choix){
                        case 0:
                            // TODO : Utilisation du modulo (couleur + 1) % 3
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
            }
        }

        return satisfait;
    }

    /**
     * Méthode qui permet de vérifier si une variable existe
     * @param variable numéro de la variable à tester
     * @param binaires tableau contenant l'ensemble des contraintes binaires
     * @param unaires tableau contenant l'ensemble des contraintes unaires
     * @param nbCouleur nombre de couleurs
     * @return vrai si la variable a encore des contraintes, faux sinon
     */
    private boolean variableExiste(int variable, boolean[][][][] binaires, boolean[][] unaires, int nbCouleur){
        boolean res = false;

        // Parcours dans le tableau des contraintes unaires
        for(int i = 0; i < nbCouleur && !res; i++){
            if(unaires[variable][i]){
                res = true;
            }
        }

        // Parcours dans le tableau des contraintes binaires
        for(int i = 0; i < binaires.length && !res; i++){
            for(int j = 0; j < nbCouleur; j++){
                for(int k = 0; k < nbCouleur; k++){
                    if(binaires[variable][i][j][k]){
                        res = true;
                    }

                    if(binaires[i][variable][k][j]){
                        res = true;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Méthode qui permet de vérifier s'il y a une contradiction dans l'ensemble des contraintes unaires
     *
     * @param unaires ensemble des contraintes unaires
     * @return un booléen qui est vrai si il y a une contradiction
     */
    private boolean verifPremiereRegle(boolean[][] unaires) {
        int j;
        boolean contradiction = false;

        // Parcours de l'ensemble des sommets pour vérifier si les trois contraintes existent
        for (int i = 0; i < unaires.length && !contradiction; i++) {
            j = 0;
            // Parcours des contraintes possibles, si l'une d'entre elles est fausse, alors on stop
            while (j < unaires[i].length && unaires[i][j]) {
                j++;
            }

            // Si j a atteint la valeur de la taille du tableau, alors toutes les contraintes unaires pour
            // une seule variable existent, donc il y a une contradiction
            if (j == unaires[i].length) {
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
        long nbTentative = 0, nbMax = Math.round(10 * Math.pow(2, ((double) taille) / 10)) + 1;
        boolean satisfait = false;

        // Boucle dans laquelle on réalise les tentative, et qui s'arrête, si on atteint le max, ou si
        // on trouve que c'est satisfiable
        while (!satisfait && nbTentative < nbMax) {
            satisfait = tentative(binaires, unaires, nbBinaire, nbUnaire, variables, nbCouleur);
            nbTentative++;
        }

        return satisfait;
    }
}

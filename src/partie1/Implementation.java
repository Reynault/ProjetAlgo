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

    // Variables non modifiées
    private boolean[][][][] binaires;
    private boolean[][] unaires;
    private int nbUnaire;
    private int nbBinaire;
    private List<Integer> variables;

    // Variables temporaires modifiées
    private boolean[][][][] tmpBinaires;
    private boolean[][] tmpUnaires;
    private List<Integer> tmpVariables;

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
        this.variables = new ArrayList<>();
        this.nbUnaire = calculNbUnaire(unaires, variables);
        this.nbBinaire = calculNbBinaire(binaires, variables);
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
     * @param nbBinaire nombre de contraintes binaires
     * @param nbUnaire  nombre de contraintes unaires
     * @return un booléen indiquant si l'ensemble est satisfiable ou non
     */
    private boolean tentative(int nbBinaire, int nbUnaire) {
        boolean satisfait = true;

        // L'algorithme boucle tant qu'il n'y a pas de contradiction, et encore des contraintes
        while (satisfait && tmpVariables.size() != 0) {
            System.out.println("Nouvelle étape de l'algorithme");
            // Vérification de la première contrainte (arrêt en cas de succès)
            if (verifPremiereRegle()) {
                System.out.println("Contradiction detectée");
                satisfait = false;
            }

            // S'il n'y a pas de contradiction
            if (satisfait) {
                System.out.println("Pas de contradiction");
                boolean[] couleurs;
                int nbContraintes;
                int variableCourante, indice;
                boolean existeUnaire = false;

                // On commence par récupérer une variable qui possède des contraintes unaires

                variableCourante = 0;
                couleurs = new boolean[NB_COULEUR];
                nbContraintes = 0;
                indice = 0;
                System.out.println("Récupération des contraintes unaires");
                // Parcours des variables, tant qu'on ne trouve pas de contraintes unaires
                while (!existeUnaire && indice < tmpVariables.size()) {
                    variableCourante = tmpVariables.get(indice);

                    // Pour toutes les contraintes possibles pour une variable, on vérifie l'existence
                    for (int j = 0; j < tmpUnaires[variableCourante].length; j++) {
                        // Si l'une d'entre elles existe, incrémentation du nombre et indication de l'existence
                        if (tmpUnaires[variableCourante][j]) {
                            couleurs[j] = true;
                            nbContraintes ++;
                            existeUnaire = true;
                        }
                    }

                    indice++;
                }

                if (existeUnaire) {

                    System.out.println("Contraintes unaires trouvées et suppression");
                    // Suppression des contraintes unaires
                    for (int i = 0; i < tmpUnaires[variableCourante].length; i++) {
                        tmpUnaires[variableCourante][i] = false;
                        nbUnaire --;
                    }

                    if (nbContraintes == 2) {
                        System.out.println("Nombre de contraintes = 2");
                        // S'il y en a deux, application de la règle deux du sujet

                        System.out.println("Suppression des contraintes binaires");
                        // Suppression des contraintes binaires
                        for(int i = 0; i < tmpBinaires.length; i++){
                            for(int j = 0; j < NB_COULEUR; j++){
                                for(int k = 0; k < NB_COULEUR; k++) {
                                    if(!couleurs[j] && tmpBinaires[variableCourante][i][j][k]) {
                                        tmpUnaires[i][k] = true;
                                        nbUnaire ++;
                                    }

                                    if(!couleurs[j] && tmpBinaires[i][variableCourante][k][j]) {
                                        tmpUnaires[i][k] = true;
                                        nbUnaire ++;
                                    }

                                    if (tmpBinaires[variableCourante][i][j][k]) {
                                        tmpBinaires[variableCourante][i][j][k] = false;
                                        nbBinaire--;
                                    }

                                    if (tmpBinaires[i][variableCourante][k][j]) {
                                        tmpBinaires[i][variableCourante][k][j] = false;
                                        nbBinaire--;
                                    }

                                    if(!variableExiste(i)){
                                        tmpVariables.remove(i);
                                    }
                                }
                            }
                        }

                        // Suppression de la variable
                        tmpVariables.remove(variableCourante);

                    } else {
                        System.out.println("Nombre de contrainte = 1");
                        // Sinon, application de la règle trois

                        System.out.println("Récupération de la couleur");
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

                        System.out.println("Suppression des contraintes binaires");
                        // Parcours des contraintes pour enlever les contraintes binaires
                        for(int i = 0 ; i < tmpBinaires.length; i++){
                            for(int j = 0; j < NB_COULEUR; j++){
                                if(tmpBinaires[variableCourante][i][c][j]){
                                    tmpBinaires[variableCourante][i][j][c] = false;
                                    nbBinaire--;
                                }
                                if(tmpBinaires[i][variableCourante][j][c]){
                                    tmpBinaires[i][variableCourante][c][j] = false;
                                    nbBinaire--;
                                }
                                if(!variableExiste(i)){
                                    tmpVariables.remove(i);
                                }
                            }
                        }

                        // Parcours des contraintes pour les couples de contraintes de la forme [(x, V ),(y, b)] et [(x, B),(z, c)]
                        int c1 = (c+1) % NB_COULEUR;
                        int c2 = (c+2) % NB_COULEUR;

                        System.out.println("Gestion du cas [(x, V ),(y, b)] et [(x, B),(y, b)");
                        // Gestion du cas où on a [(x, V ),(y, b)] et [(x, B),(y, b)]
                        for(int i = 0; i < tmpBinaires.length; i++){
                            for(int j = 0; j < NB_COULEUR; j++){
                                if( (tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1]) &&
                                        (tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2])){
                                    // Dans ce cas on ajoute une contrainte unaire
                                    // Puisque si on applique la modification directement, on obtient une nouvelle
                                    // contrainte du type [(y, b), (y, b)], on peut simplifier avec (y, b)
                                    tmpUnaires[i][j] = true;
                                    nbUnaire ++;

                                    tmpBinaires[variableCourante][i][c1][j] = false;
                                    tmpBinaires[i][variableCourante][j][c1] = false;
                                    tmpBinaires[variableCourante][i][c2][j] = false;
                                    tmpBinaires[i][variableCourante][j][c2] = false;
                                }
                            }
                        }

                        boolean trouve1 = true;
                        boolean trouve2 = true;
                        int x = 0, y = 0, couleurX = 0, couleurY = 0;

                        System.out.println("Gestion du cas global");
                        // Gestion des couples [(x, V ),(y, b)] et [(x, B),(z, c)]
                        while (trouve1 && trouve2){
                            trouve1 = false;
                            trouve2 = false;

                            // Recherche d'un couple
                            for(int i = 0; i < tmpBinaires.length && (!trouve1 || !trouve2); i++){
                                for(int j = 0; j < NB_COULEUR; j++){
                                    if(tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1]){
                                        x = i;
                                        couleurX = j;

                                        trouve1 = true;
                                    }

                                    if(tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2]){
                                        y = i;
                                        couleurY = j;

                                        trouve2 = true;
                                    }
                                }
                            }

                            // Suppression du couple
                            if(trouve1 && trouve2){
                                tmpBinaires[variableCourante][x][c1][couleurX] = false;
                                tmpBinaires[x][variableCourante][couleurX][c1] = false;

                                tmpBinaires[variableCourante][y][c2][couleurY] = false;
                                tmpBinaires[y][variableCourante][couleurY][c2] = false;

                                tmpBinaires[x][y][couleurX][couleurY] = true;
                            }
                        }

                        // S'il n'y a pas de contraintes restantes, suppression de la variable
                        if(!trouve1 && !trouve2){
                            tmpVariables.remove(variableCourante);
                        }
                    }
                } else {
                    System.out.println("Il n'y a pas de contraintes unaires");
                    // S'il n'y a pas de contraintes unaires, application de la règle 4 sur la première contrainte binaire
                    boolean trouve = false;

                    int v1 = 0, v2 = 0, c1 = 0, c2 = 0;

                    System.out.println("Récupération d'une première contrainte binaire");
                    // Récupération d'une première contrainte
                    for(int i = 0; i < tmpBinaires.length && !trouve ; i++){
                        for(int j = 0; j < tmpBinaires[i].length && !trouve ; j++){
                            for(int k = 0; k < tmpBinaires[i][j].length && !trouve ; k++){
                                for(int l = 0; l < tmpBinaires[i][j][k].length && !trouve ; l++){
                                    if(tmpBinaires[i][j][k][l]){
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

                    System.out.println("Application du choix aléatoire");
                    // Application de la modification
                    int choix = new Random().nextInt(4);
                    switch (choix){
                        case 0:
                            tmpUnaires[v1][c1] = true;
                            tmpUnaires[v2][(c2+1) % NB_COULEUR] = true;
                            break;
                        case 1:
                            tmpUnaires[v1][c1] = true;
                            tmpUnaires[v2][(c2+2) % NB_COULEUR] = true;
                            break;
                        case 2:
                            tmpUnaires[v1][(c1+1) % NB_COULEUR ] = true;
                            tmpUnaires[v2][(c2) % NB_COULEUR] = true;
                            break;
                        case 3:
                            tmpUnaires[v1][(c1+2) % NB_COULEUR] = true;
                            tmpUnaires[v2][(c2) % NB_COULEUR] = true;
                            break;
                    }
                    nbUnaire += 2;
                }
            }
        }

        return satisfait;
    }

    /**
     * Méthode qui permet de vérifier si une variable existe
     * @param variable numéro de la variable à tester
     * @return vrai si la variable a encore des contraintes, faux sinon
     */
    private boolean variableExiste(int variable){
        boolean res = false;

        // Parcours dans le tableau des contraintes unaires
        for(int i = 0; i < NB_COULEUR && !res; i++){
            if(tmpUnaires[variable][i]){
                res = true;
            }
        }

        // Parcours dans le tableau des contraintes binaires
        for(int i = 0; i < tmpBinaires.length && !res; i++){
            for(int j = 0; j < NB_COULEUR; j++){
                for(int k = 0; k < NB_COULEUR; k++){
                    if(tmpBinaires[variable][i][j][k]){
                        res = true;
                    }

                    if(tmpBinaires[i][variable][k][j]){
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
     * @return un booléen qui est vrai si il y a une contradiction
     */
    private boolean verifPremiereRegle() {
        int j;
        boolean contradiction = false;

        // Parcours de l'ensemble des sommets pour vérifier si les trois contraintes existent
        for (int i = 0; i < tmpUnaires.length && !contradiction; i++) {
            j = 0;
            // Parcours des contraintes possibles, si l'une d'entre elles est fausse, alors on stop
            while (j < tmpUnaires[i].length && tmpUnaires[i][j]) {
                System.out.println("(i, j) - valeur : ("+i+", "+j+") - "+tmpUnaires[i][j]);
                j++;
            }

            // Si j a atteint la valeur de la taille du tableau, alors toutes les contraintes unaires pour
            // une seule variable existent, donc il y a une contradiction
            if (j == tmpUnaires[i].length) {
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
        long nbTentative = 0;
        long nbMax = Math.round(10 * Math.pow(2, ((double) binaires.length) / 10)) + 1;
        boolean satisfait = false;

        // Boucle dans laquelle on réalise les tentative, et qui s'arrête, si on atteint le max, ou si
        // on trouve que c'est satisfiable
        while (!satisfait && nbTentative < nbMax) {
            System.out.println("-------------- Nouvelle tentative --------------");

            // Copie des deux tableaux
            tmpBinaires = new boolean[binaires.length][binaires.length][NB_COULEUR][NB_COULEUR];
            for(int i = 0; i < binaires.length; i++){
                for(int j = 0; j < binaires[i].length; j++){
                    for(int k = 0; k < NB_COULEUR; k++){
                        for(int l = 0; l < NB_COULEUR; l++){
                            tmpBinaires[i][j][k][l] = binaires[i][j][k][l];
                        }
                    }
                }
            }

            tmpUnaires = new boolean[unaires.length][NB_COULEUR];
            for(int i = 0 ; i < unaires.length; i++){
                for(int j = 0; j < NB_COULEUR; j++){
                    tmpUnaires[i][j] = unaires[i][j];
                }
            }

            tmpVariables = new ArrayList<>(variables);

            satisfait = tentative(nbBinaire, nbUnaire);
            System.out.println("-------------- Resultat de la tentative: "+satisfait+" --------------");
            nbTentative++;
        }

        return satisfait;
    }

    private void afficherUnaires(){
        System.out.println("Affichage unaires");
        for(int i = 0; i < unaires.length; i++){
            for(int j = 0; j < unaires[i].length; j++){
                System.out.print(unaires[i][j]);
            }
            System.out.println();
        }
    }
}

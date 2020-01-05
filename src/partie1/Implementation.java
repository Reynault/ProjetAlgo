package partie1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation est une classe qui contient l'implémentation de l'algorithme proposé pour résoudre
 * le problème (3-2)-SSS
 */
public class Implementation {

    public final static int NB_COULEUR = 3;

    public final static int ROUGE = 0;
    public final static int VERT = 1;
    public final static int BLEU = 2;

    // Variables non modifiées
    private final boolean[][][][] binaires;
    private final boolean[][] unaires;
    private final List<Integer> variables;
    private final int taille;

    // Variables temporaires modifiées lors de l'execution de l'algorithme
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
        this.taille = unaires.length;

        // Récupération des variables qui possèdent des contraintes binaires
        this.variables = new ArrayList<>();
        for(int i = 0; i < binaires.length; i++){
            for(int j = 0; j < binaires.length; j++){
                for(int k = 0; k < NB_COULEUR; k++){
                    for(int l = 0; l < NB_COULEUR; l++){
                        if(binaires[i][j][k][l]){
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
    }

    /**
     * Implementation de l'algorithme présenté dans le sujet, utilisation des quatre règles, avec comme
     * condition d'arrêt, soit de trouver une contradiction, soit d'avoir un ensemble de contraintes vide.
     *
     * Il faut alors appliquer sur chaque variable de sorte à toutes les supprimer
     *
     * @return un booléen indiquant si l'ensemble est satisfiable ou non
     */
    private boolean tentative() {
        boolean satisfait = true;
        int nbUnaire, variableCourante;

        // Tant que le système n'a pas de contradiction et qu'il reste des variables à tester
        while(satisfait && tmpVariables.size() > 0){
            // Récupération de la première variable
            variableCourante = tmpVariables.get(0);
            System.out.println("Variable courante: "+variableCourante);
            // Récupération du nombre de contraintes unaires
            nbUnaire = recupNbUnaire(variableCourante);
            System.out.println("Nombre de contraintes: "+nbUnaire);

            // Il faut ensuite appliquer les règles en fonction du nombre de contraintes unaires
            switch (nbUnaire){
                case 0:
                    // Si la variable ne possède pas de contraintes unaires, application du quatrième cas
                    quatriemeCas(variableCourante);
                    break;
                case 1:
                    // Lorsqu'il y a une contrainte, application du troisième cas
                    troisiemeCas(variableCourante);
                    //System.exit(0);
                    break;
                case 2:
                    // Lorsqu'il y a deux contraintes unaires, application du deuxième cas
                    deuxiemeCas(variableCourante);
                    break;
                case 3:
                    // S'il y a trois contraintes unaires, cela signifie qu'il y a une contradiction
                    satisfait = false;
                    break;
                default:
                    // Si plus de 3, non satisfiable (on est pas censé en avoir plus de trois)
                    satisfait = false;
                    break;
            }
        }
        return satisfait;
    }

    /**
     * Méthode qui permet de récupérer le nombre de contraintes unaires d'une
     * variable
     * @param variableCourante variable dont on veut connaître le nombre de contraintes unaires
     * @return le nombre de contraintes unaires
     */
    private int recupNbUnaire(int variableCourante) {
        int nbUnaire = 0;
        for(int i = 0; i < NB_COULEUR; i++){
            if(tmpUnaires[variableCourante][i]){
                nbUnaire ++;
            }
        }
        return nbUnaire;
    }

    /**
     * Méthode qui applique le deuxième cas de l'algorithme, celui dans lequel une variable possède
     * deux contraintes unaires.
     * @param variableCourante la variable courante
     */
    private void deuxiemeCas(int variableCourante){
        boolean[] couleurs = new boolean[NB_COULEUR];

        System.out.println("Regle deux");
        // S'il y en a deux, application de la règle deux du sujet

        System.out.println("Suppression des contraintes unaires");
        // Suppression des contraintes unaires
        for (int i = 0; i < NB_COULEUR; i++) {
            tmpUnaires[variableCourante][i] = false;
            couleurs[i] = true;
        }
        affichageContraintes();

        System.out.println("Suppression des contraintes binaires");

        // Suppression des contraintes binaires - Cas dans lequel la couleur fait partie de celles des
        // contraintes unaires
        boolean trouve = false;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                for (int k = 0; k < NB_COULEUR; k++) {
                    if (couleurs[j] && tmpBinaires[variableCourante][i][j][k]) {
                        tmpBinaires[variableCourante][i][j][k] = false;
                        trouve = true;
                    }

                    if (couleurs[j] && tmpBinaires[i][variableCourante][k][j]) {
                        tmpBinaires[i][variableCourante][k][j] = false;
                        trouve = true;
                    }
                }
            }
        }

        // Suppression des contraintes binaires - Cas dans lequel la couleur n'est pas celle des
        // contraintes unaires
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                for (int k = 0; k < NB_COULEUR; k++) {

                    if (!couleurs[j] && tmpBinaires[variableCourante][i][j][k]) {
                        tmpUnaires[i][k] = true;
                        tmpBinaires[variableCourante][i][j][k] = false;
                    }

                    if (!couleurs[j] && tmpBinaires[i][variableCourante][k][j]) {
                        tmpUnaires[i][k] = true;
                        tmpBinaires[i][variableCourante][k][j] = false;
                    }
                }
            }
        }


        affichageContraintes();

        System.out.println("Suppression de la variable");

        // Suppression de la variable
        tmpVariables.remove((Integer) variableCourante);

        affichageVariables();
    }

    /**
     * Méthode qui permet d'appliquer le troisième cas de l'algorithme, cas dans lequel
     * la variable possède une seul contrainte unaire
     * @param variableCourante variable à tester
     */
    private void troisiemeCas(int variableCourante){
        // Sinon, application de la règle trois
        System.out.println("Règle trois");

        // Récupération de la couleur
        int c = -1;
        for(int i = 0; i < NB_COULEUR && c == -1 ; i++){
            if(tmpUnaires[variableCourante][i]){
                c = i;
            }
        }

        System.out.println("Couleur: "+c);
        System.out.println("Variable courante: "+variableCourante);

        System.out.println("Suppression des contraintes binaires");
        // Parcours des contraintes pour enlever les contraintes binaires
        for (int i = 0; i < tmpBinaires.length; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                if (tmpBinaires[variableCourante][i][c][j]) {
                    tmpBinaires[variableCourante][i][c][j] = false;
                }
                if (tmpBinaires[i][variableCourante][j][c]) {
                    tmpBinaires[i][variableCourante][j][c] = false;
                }
            }
        }
        affichageContraintes();

        System.out.println("Gestion du cas : [(x, V ),(y, b)] et [(x, B),(y, b)]");
        // Parcours des contraintes pour les couples de contraintes de la forme [(x, V ),(y, b)] et [(x, B),(z, c)]
        int c1 = (c + 1) % NB_COULEUR;
        int c2 = (c + 2) % NB_COULEUR;

        // Gestion du cas où on a [(x, V ),(y, b)] et [(x, B),(y, b)]
        for (int i = 0; i < tmpBinaires.length; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                if ((tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1]) &&
                        (tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2])) {
                    // Dans ce cas on ajoute une contrainte unaire
                    // Puisque si on applique la modification directement, on obtient une nouvelle
                    // contrainte du type [(y, b), (y, b)], on peut simplifier avec (y, b)
                    tmpUnaires[i][j] = true;
                }
            }
        }
        affichageContraintes();

        boolean trouve1 = true;
        boolean trouve2 = true;
        int x = 0, y = 0, couleurX = 0, couleurY = 0;

        System.out.println("Gestion du cas : [(x, V ),(y, b)] et [(x, B),(z, c)]");
        boolean[][][][] decouvert = new boolean[tmpBinaires.length][tmpBinaires.length][NB_COULEUR][NB_COULEUR];
        // Gestion des couples [(x, V ),(y, b)] et [(x, B),(z, c)]
        while (trouve1 && trouve2) {
            trouve1 = false;
            trouve2 = false;

            // Recherche d'un couple
            for (int i = 0; i < tmpBinaires.length && (!trouve1 || !trouve2); i++) {
                for (int j = 0; j < NB_COULEUR; j++) {
                    if (variableCourante != i) {
                        if (!decouvert[variableCourante][i][c1][j] && !decouvert[i][variableCourante][j][c1] &&
                                (tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1])) {
                            x = i;
                            couleurX = j;
                            decouvert[variableCourante][i][c1][j] = true;
                            decouvert[i][variableCourante][j][c1] = true;

                            trouve1 = true;
                        }

                        if (!decouvert[variableCourante][i][c2][j] && !decouvert[i][variableCourante][j][c2] &&
                                (tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2])) {
                            y = i;
                            couleurY = j;
                            decouvert[variableCourante][i][c2][j] = true;
                            decouvert[i][variableCourante][j][c2] = true;

                            trouve2 = true;
                        }
                    }
                }
            }

            System.out.println("var courante " + variableCourante);
            System.out.println("couleur " + c);

            // Ajout du nouveau couple
            if (trouve1 && trouve2) {
                System.out.println("Couple trouvé: (x, y, cx, cy) - (" + x + ", " + y + ", " + couleurX + ", " + couleurY + ")");

                tmpBinaires[x][y][couleurX][couleurY] = true;
                affichageContraintes();
            }
        }

        // S'il n'y a pas de contraintes restantes, suppression de la variable
        if(!trouve1 && !trouve2){
            System.out.println("Pas de contraintes restantes, donc suppression de la variable");
            tmpVariables.remove((Integer) variableCourante);
            affichageVariables();
        }
    }

    /**
     * Méthode qui applique le quatrième cas sur l'ensemble des contraintes binaires et unaires
     */
    private void quatriemeCas(int variableCourante){
        boolean trouve = false;
        int v1 = 0, v2 = 0, c1 = 0, c2 = 0;

        System.out.println("Application du quatrième cas");

        // Récupération d'une première contrainte qui contient la variable courante
        for(int i = 0; i < taille && !trouve; i++){
            for(int j = 0; j < NB_COULEUR && !trouve; j++){
                for(int k = 0; k < NB_COULEUR && !trouve; k++){
                    if(tmpBinaires[variableCourante][i][j][k]){
                        v1 = variableCourante;
                        v2 = i;
                        c1 = j;
                        c2 = k;
                        trouve = true;
                    }

                    if(tmpBinaires[i][variableCourante][k][j]){
                        v2 = variableCourante;
                        v1 = i;
                        c1 = k;
                        c2 = j;
                        trouve = true;
                    }
                }
            }
        }

        // Si on ne trouve pas de contrainte binaire avec la variable courante, alors cela signifie qu'elle
        // ne se trouve pas dans l'ensemble des contraintes
        if(!trouve){
            tmpVariables.remove((Integer) variableCourante);

        }else {
            System.out.println("Contrainte trouvée: (v1, v2, c1, c2) - (" + v1 + ", " + v2 + ", " + c1 + ", " + c2 + ")");

            // Application de la modification
            int choix = new Random().nextInt(4);

            System.out.println("Choix réalisé: " + choix);

            switch (choix) {
                case 0:
                    tmpUnaires[v1][c1] = true;
                    tmpUnaires[v2][(c2 + 1) % NB_COULEUR] = true;
                    break;
                case 1:
                    tmpUnaires[v1][c1] = true;
                    tmpUnaires[v2][(c2 + 2) % NB_COULEUR] = true;
                    break;
                case 2:
                    tmpUnaires[v1][(c1 + 1) % NB_COULEUR] = true;
                    tmpUnaires[v2][c2] = true;
                    break;
                case 3:
                    tmpUnaires[v1][(c1 + 2) % NB_COULEUR] = true;
                    tmpUnaires[v2][c2] = true;
                    break;
            }

            affichageContraintes();
        }
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

            // Copie de la liste des variables
            tmpVariables = new ArrayList<>(variables);

            satisfait = tentative();

            System.out.println("-------------- Resultat de la tentative: "+satisfait+" --------------");
            nbTentative++;
        }

        return satisfait;
    }

    /**
     * Méthode de debogage, affichage de l'ensemble des contraintes
     */
    private void affichageContraintes(){
        System.out.println("AffichageContraintes");
        ArrayList<String> ensContrainte = new ArrayList<>();
        // Binaires
        for(int i = 0; i < tmpBinaires.length; i++){
            for(int j = 0; j < tmpBinaires[i].length; j++){
                for(int k = 0; k < NB_COULEUR; k++){
                    for(int l = 0; l < NB_COULEUR; l++){
                        if(tmpBinaires[i][j][k][l]){
                            ensContrainte.add("[( "+i+", "+k+") ,( "+j+", "+l+")]");
                        }
                    }
                }
            }
        }
        // Affichage binaires
        System.out.print("{ ");
        for(String s : ensContrainte){
            System.out.print(s+", ");
        }
        System.out.print(" }");

        ensContrainte = new ArrayList<>();
        // Unaires
        for(int i = 0; i < tmpUnaires.length; i++){
            for(int j = 0; j < NB_COULEUR; j++){
                if(tmpUnaires[i][j]){
                    ensContrainte.add("[( "+i+", "+j+")]");
                }
            }
        }

        System.out.println();

        // Affichage unaires
        System.out.print("{ ");
        for(String s : ensContrainte){
            System.out.print(s+", ");
        }
        System.out.print(" }");
        System.out.println();

    }


    private void affichageVariables() {
        System.out.println();
        System.out.println("Affichage des variables");
        for(Integer i: tmpVariables){
            System.out.print(i+" - ");
        }
        System.out.println();
    }
}

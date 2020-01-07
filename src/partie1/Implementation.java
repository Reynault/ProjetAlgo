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
    private int variableCourante;

    /**
     * Constructeur prenant en paramètre les deux tableaux contenant les contraintes
     *
     * Ce constructeur va alors remplir automatiquement le tableau des variables
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
     * @return un booléen indiquant si l'ensemble est satisfiable ou non
     */
    private boolean tentative() {
        boolean satisfait = true;
        int nbUnaire;
        // Tant que le système n'a pas de contradiction et qu'il reste des variables à tester
        while(satisfait && tmpVariables.size() > 0){
            // Récupération du nombre de contraintes unaires d'une première variable
            nbUnaire = recupNbUnaire();

            // Il faut ensuite appliquer les règles en fonction du nombre de contraintes unaires
            switch (nbUnaire){
                case 0:
                    // Si aucunes variables ne possèdent de contraintes unaires, application du quatrième cas
                    quatriemeCas();
                    break;
                case 1:
                    // Lorsqu'il y a une contrainte, application du troisième cas
                    troisiemeCas();
                    break;
                case 2:
                    // Lorsqu'il y a deux contraintes unaires, application du deuxième cas
                    deuxiemeCas();
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
     * variable, s'il n'y a pas de contraintes unaires, alors elle renvoie 0
     * @return le nombre de contraintes unaires
     */
    private int recupNbUnaire() {
        int nbUnaire = 0;
        boolean trouve = false;

        for(int i = 0; i < tmpVariables.size() && !trouve; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                if (tmpUnaires[tmpVariables.get(i)][j]) {
                    nbUnaire++;
                    trouve = true;
                    variableCourante = tmpVariables.get(i);
                }
            }
        }

        return nbUnaire;
    }

    /**
     * Méthode qui applique le deuxième cas de l'algorithme, celui dans lequel une variable possède
     * deux contraintes unaires.
     */
    private void deuxiemeCas(){
        // Suppression des contraintes unaires et remplissage d'un tableau qui va nous servir
        // lors de la suppression des contraintes binaires, celui-ci va contenir les couleurs
        boolean[] couleurs = new boolean[NB_COULEUR];

        for (int i = 0; i < NB_COULEUR; i++) {
            if(tmpUnaires[variableCourante][i]){
                couleurs[i] = true;
            }
            tmpUnaires[variableCourante][i] = false;
        }

        // Suppression des contraintes binaires
        
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                for (int k = 0; k < NB_COULEUR; k++) {
                    // Vérification du cas dans lequel la couleur est celle d'une des contraintes
                    // unaires
                    if (couleurs[j] && tmpBinaires[variableCourante][i][j][k]) {
                        tmpBinaires[variableCourante][i][j][k] = false;

                        // Lors de la suppression d'une contrainte binaire, il faut tout de même
                        // vérifier la présence de l'autre variable contenue dans l'ensemble, si elle
                        // ne l'est plus, cela signifie qu'il faut la supprimer dans la liste des variables
                        if (!variableExiste(i)) {
                            tmpVariables.remove((Integer) i);
                        }
                    }

                    if (couleurs[j] && tmpBinaires[i][variableCourante][k][j]) {
                        tmpBinaires[i][variableCourante][k][j] = false;
                        if (!variableExiste(i)) {
                            tmpVariables.remove((Integer) i);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                for (int k = 0; k < NB_COULEUR; k++) {

                    // Vérification du cas dans lequel la contrainte binaire possède la couleur
                    // qui ne fait pas partie des contraintes unaires
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

        // Suppression de la variable
        tmpVariables.remove((Integer) variableCourante);
    }

    /**
     * Méthode qui permet d'appliquer le troisième cas de l'algorithme, cas dans lequel
     * la variable possède une seul contrainte unaire
     */
    private void troisiemeCas() {
        // Récupération de la couleur de la contrainte unaire
        int couleur = 0;
        for (int i = 0; i < NB_COULEUR; i++) {
            if (tmpUnaires[variableCourante][i]) {
                couleur = i;
                tmpUnaires[variableCourante][i] = false;
            }
        }

        // Liste des contraintes de la forme [(x, c1 ),(y, b)]
        ArrayList<Couple> listeC1 = new ArrayList<>();
        // Liste des contraintes de la forme [(x, c2),(z, c)]
        ArrayList<Couple> listeC2 = new ArrayList<>();

        // Calcul des autres couleurs
        int c1 = (couleur + 1) % NB_COULEUR;
        int c2 = (couleur + 2) % NB_COULEUR;

        // Parcours du tableau des contraintes binaires
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < NB_COULEUR; j++) {
                // Si on voit une contrainte binaire qui contient la couleur de la contrainte unaire
                // on la supprime
                if (tmpBinaires[variableCourante][i][couleur][j]) {
                    tmpBinaires[variableCourante][i][couleur][j] = false;
                    if(!variableExiste(i)){
                        tmpVariables.remove((Integer) i);
                    }
                }
                if (tmpBinaires[i][variableCourante][j][couleur]) {
                    tmpBinaires[i][variableCourante][j][couleur] = false;
                    if(!variableExiste(i)){
                        tmpVariables.remove((Integer) i);
                    }
                }

                // Gestion du premier cas dans lequel on teste l'égalité [(x, c1),(y, b)] [(x, c2),(z, c)]
                // avec y = z et b = c, cela créer une contrainte unaire
                if ((tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1]) &&
                        (tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2])) {
                    tmpUnaires[i][j] = true;
                    tmpBinaires[variableCourante][i][c1][j] = false;
                    tmpBinaires[i][variableCourante][j][c1] = false;
                    tmpBinaires[variableCourante][i][c2][j] = false;
                    tmpBinaires[i][variableCourante][j][c2] = false;
                } else {
                    // Si la contrainte est de type [(x, c1),(y, b)], ajout dans la liste correspondante
                    if (tmpBinaires[variableCourante][i][c1][j] || tmpBinaires[i][variableCourante][j][c1] && i != variableCourante) {
                        listeC1.add(new Couple(i, j));
                    }

                    // Si la contrainte est de type [(x, c2),(z, c)], ajout dans la liste correspondante
                    if (tmpBinaires[variableCourante][i][c2][j] || tmpBinaires[i][variableCourante][j][c2] && i != variableCourante) {
                        listeC2.add(new Couple(i, j));
                    }
                }
            }
        }

        // Parcours des deux listes et ajout des contraintes binaires correspondantes
        int indice = 0;
        while (indice < listeC1.size() && indice < listeC2.size()) {
            Couple couple1 = listeC1.get(indice);
            Couple couple2 = listeC2.get(indice);

            // Suppression des contraintes binaires qui ne sont plus utiles
            tmpBinaires[variableCourante][couple1.getV()][c1][couple1.getC()] = false;
            tmpBinaires[couple1.getV()][variableCourante][couple1.getC()][c1] = false;
            tmpBinaires[variableCourante][couple2.getV()][c2][couple2.getC()] = false;
            tmpBinaires[couple2.getV()][variableCourante][couple2.getC()][c2] = false;

            tmpBinaires[couple1.getV()][couple2.getV()][couple1.getC()][couple2.getC()] = true;
            indice++;
        }

        // Enfin, si il ne reste pas de contrainte binaire seule, il faut supprimer la variable courante qui n'a alors
        // plus de contraintes associées
        if( (indice == listeC1.size()) && (indice == listeC2.size())){
            tmpVariables.remove((Integer) variableCourante);
        }
    }

    /**
     * Méthode qui applique le quatrième cas sur l'ensemble des contraintes binaires et unaires
     */
    private void quatriemeCas(){
        boolean trouve = false;
        int v1 = 0, v2 = 0, c1 = 0, c2 = 0;

        // Récupération d'une première contrainte binaire
        for(int i = 0; i < taille && !trouve; i++){
            for(int j = 0; j < taille && !trouve; j++){
                for(int k = 0; k < NB_COULEUR && !trouve; k++){
                    for(int l = 0; l < NB_COULEUR  && !trouve; l++)
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

        // Application de la modification
        int choix = new Random().nextInt(4);
        // En fonction du choix aléatoire, ajout de deux nouvelles contraintes unaires
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
    }

    /**
     * Méthode qui permet de vérifier l'existence d'une variable
     * @param variable la variable à vérifier
     * @return
     */
    private boolean variableExiste(int variable){
        boolean trouve = false;
        for(int i = 0; i < taille && !trouve; i++){
            for(int j = 0; j < NB_COULEUR && !trouve; j++){
                for(int k = 0; k <  NB_COULEUR && !trouve; k++){
                    if(tmpBinaires[variable][i][j][k]){
                        trouve = true;
                    }
                    if(tmpBinaires[i][variable][k][j]){
                        trouve = true;
                    }
                }
            }
        }
        return trouve;
    }

    /**
     * L'algorithme proposé correspond à un algorithme Monte-Carlo qui lorsqu'il répond Vrai (Ici, cela signifie
     * que l'ensemble est satisfiable), s'arrête, mais recommence un certain nombre de fois lorsque la réponse est Faux.
     * <p>
     * En effet, étant un algorithme probabiliste qui a une chance sur deux, qu'un ensemble C satisfiable devienne
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

        // Boucle dans laquelle on réalise les tentative qui s'arrête, si on atteint le max, ou si
        // on trouve que c'est satisfiable
        while (!satisfait && nbTentative < nbMax) {
            // Copie des deux tableaux

            // Binaire
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

            // Unaire
            tmpUnaires = new boolean[unaires.length][NB_COULEUR];
            for(int i = 0 ; i < unaires.length; i++){
                for(int j = 0; j < NB_COULEUR; j++){
                    tmpUnaires[i][j] = unaires[i][j];
                }
            }

            // Copie de la liste des variables
            tmpVariables = new ArrayList<>(variables);

            // On lance l'algorithme
            satisfait = tentative();
            nbTentative++;
        }

        return satisfait;
    }

    // Méthodes de débogage

    /**
     * Méthode de debogage, affichage de l'ensemble des contraintes (binaires puis unaires)
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

    /**
     * Méthode de débogage qui affiche les variables encore disponibles
     */
    private void affichageVariables() {
        System.out.println();
        System.out.println("Affichage des variables");
        for(Integer i: tmpVariables){
            System.out.print(i+" - ");
        }
        System.out.println();
    }
}

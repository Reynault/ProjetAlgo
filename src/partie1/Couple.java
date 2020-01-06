package partie1;

import java.util.Objects;

/**
 * Classe représentant un Couple (v, c) avec v le numéro du sommet et c sa couleur
 */
public final class Couple {
    private final int v;
    private final int c;

    public Couple(int v, int c) {
        this.v = v;
        this.c = c;
    }

    public int getV() {
        return v;
    }

    public int getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple couple = (Couple) o;
        return v == couple.v &&
                c == couple.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, c);
    }
}

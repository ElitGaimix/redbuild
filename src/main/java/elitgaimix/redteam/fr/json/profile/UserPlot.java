package elitgaimix.redteam.fr.json.profile;

import java.util.ArrayList;
import java.util.List;

public class UserPlot {
    private int nombre;
    private List<Integer> coX;
    private List<Integer> coZ;
    private List<Integer> taille;
    private List<UserPlotFusion> fusions;

    public int getNombre() {
        return this.nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public List<Integer> getCoX() {
        return this.coX;
    }

    public void setCoX(List<Integer> coX) {
        this.coX = coX;
    }

    public List<Integer> getCoZ() {
        return this.coZ;
    }

    public void setCoZ(List<Integer> coZ) {
        this.coZ = coZ;
    }

    public List<Integer> getTaille() {
        return this.taille;
    }

    public void setTaille(List<Integer> taille) {
        this.taille = taille;
    }

    public List<UserPlotFusion> getFusions() {
        return this.fusions;
    }

    public void setFusions(List<UserPlotFusion> fusions) {
        this.fusions = fusions;
    }

    public UserPlot(int nombre, List<Integer> coX, List<Integer> coZ, List<Integer> taille,
            List<UserPlotFusion> fusions) {
        this.nombre = nombre;
        this.coX = coX;
        this.coZ = coZ;
        this.taille = taille;
        this.fusions = fusions;
    }

    public static UserPlot createPlotJson() {
        return new UserPlot(0, new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(),
                new ArrayList<UserPlotFusion>());
    }
}

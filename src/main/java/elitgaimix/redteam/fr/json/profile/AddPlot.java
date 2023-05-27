package elitgaimix.redteam.fr.json.profile;

import java.util.ArrayList;
import java.util.List;

public class AddPlot {
    private List<Integer> coX;
    private List<Integer> coZ;
    private List<Integer> taille;
    private List<Boolean> trust;
    private List<String> player;

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

    public List<Boolean> getTrust() {
        return this.trust;
    }

    public void setTrust(List<Boolean> trust) {
        this.trust = trust;
    }

    public List<String> getPlayer() {
        return this.player;
    }

    public void setPlayer(List<String> player) {
        this.player = player;
    }

    public AddPlot(List<Integer> coX, List<Integer> coZ, List<Integer> taille, List<Boolean> trust,
            List<String> player) {
        this.coX = coX;
        this.coZ = coZ;
        this.taille = taille;
        this.trust = trust;
        this.player = player;
    }

    public static AddPlot createPlotJson() {
        return new AddPlot(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>());
    }
}

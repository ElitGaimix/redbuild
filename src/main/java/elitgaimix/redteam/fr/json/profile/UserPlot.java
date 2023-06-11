package elitgaimix.redteam.fr.json.profile;

import java.util.List;

public class UserPlot {
    private Integer coX;
    private Integer coZ;
    private Integer taille;
    private List<UserPlotFusion> fusions;


    public Integer getCoX() {
        return this.coX;
    }

    public void setCoX(Integer coX) {
        this.coX = coX;
    }

    public Integer getCoZ() {
        return this.coZ;
    }

    public void setCoZ(Integer coZ) {
        this.coZ = coZ;
    }

    public Integer getTaille() {
        return this.taille;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public List<UserPlotFusion> getFusions() {
        return this.fusions;
    }

    public void setFusions(List<UserPlotFusion> fusions) {
        this.fusions = fusions;
    }

    public UserPlot(Integer coX, Integer coZ, Integer taille, List<UserPlotFusion> fusions) {
        this.coX = coX;
        this.coZ = coZ;
        this.taille = taille;
        this.fusions = fusions;
    }
    


}

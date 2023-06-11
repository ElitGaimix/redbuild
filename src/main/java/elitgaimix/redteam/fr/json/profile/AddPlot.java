package elitgaimix.redteam.fr.json.profile;

public class AddPlot {
    private Integer coX;
    private Integer coZ;
    private Integer taille;
    private Boolean trust;
    private String player;

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

    public Boolean isTrust() {
        return this.trust;
    }

    public Boolean getTrust() {
        return this.trust;
    }

    public void setTrust(Boolean trust) {
        this.trust = trust;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public AddPlot(Integer coX, Integer coZ, Integer taille, Boolean trust, String player) {
        this.coX = coX;
        this.coZ = coZ;
        this.taille = taille;
        this.trust = trust;
        this.player = player;
    }
}

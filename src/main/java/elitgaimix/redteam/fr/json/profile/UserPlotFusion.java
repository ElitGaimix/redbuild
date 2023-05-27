package elitgaimix.redteam.fr.json.profile;

public class UserPlotFusion {
    private Integer coX;
    private Integer coZ;
    private Integer tail;

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

    public Integer getTail() {
        return this.tail;
    }

    public void setTail(Integer tail) {
        this.tail = tail;
    }

    public UserPlotFusion(Integer coX, Integer coZ, Integer tail) {
        this.coX = coX;
        this.coZ = coZ;
        this.tail = tail;
    }
}
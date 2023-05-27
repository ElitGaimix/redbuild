package elitgaimix.redteam.fr.json.Plot;

import java.util.List;

public class Plot {
    private Integer coX;
    private Integer coZ;
    private Boolean take;
    private Integer tail;
    private String player;
    private List<PlotFusion> plotFusion;

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

    public Boolean isTake() {
        return this.take;
    }

    public Boolean getTake() {
        return this.take;
    }

    public void setTake(Boolean take) {
        this.take = take;
    }

    public Integer getTail() {
        return this.tail;
    }

    public void setTail(Integer tail) {
        this.tail = tail;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<PlotFusion> getPlotFusion() {
        return this.plotFusion;
    }

    public void setPlotFusion(List<PlotFusion> plotFusion) {
        this.plotFusion = plotFusion;
    }

    public Plot(Integer coX, Integer coZ, Boolean take, Integer tail, String player, List<PlotFusion> plotFusion) {
        this.coX = coX;
        this.coZ = coZ;
        this.take = take;
        this.tail = tail;
        this.player = player;
        this.plotFusion = plotFusion;
    }

}

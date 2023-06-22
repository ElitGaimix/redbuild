package elitgaimix.redteam.fr.json.Plot;

import java.util.List;

public class Plot {
    private Integer X;
    private Integer Z;
    private Boolean take;
    private Integer tail;
    private String player;
    private List<PlotFusion> plotFusion;

    public int getChunkX(){
        return X * 4;
    }

    public int getChunkZ(){
        return Z * 4;
    }

    public Integer getX() {
        return this.X;
    }

    public void setX(Integer X) {
        this.X = X;
    }

    public Integer getZ() {
        return this.Z;
    }

    public void setZ(Integer Z) {
        this.Z = Z;
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

    public Plot(Integer X, Integer Z, Boolean take, Integer tail, String player, List<PlotFusion> plotFusion) {
        this.X = X;
        this.Z = Z;
        this.take = take;
        this.tail = tail;
        this.player = player;
        this.plotFusion = plotFusion;
    }
    

}

package elitgaimix.redteam.fr.json.Plot;

import java.util.List;

public class PlotFile {
    private List<Plot> plot;

    public List<Plot> getPlot() {
        return this.plot;
    }

    public void setPlot(List<Plot> plot) {
        this.plot = plot;
    }

    public PlotFile(List<Plot> plot) {
        this.plot = plot;
    }
}

package elitgaimix.redteam.fr.json.profile;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {
    private UUID uuid;
    private int plotnombre;
    private List<UserPlot> plot;
    private List<AddPlot> addplot;
    private Boolean menu;
    private Boolean banmap1;
    private Boolean banmap2;
    private Boolean banmap3;
    private Boolean banmap4;

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getPlotnombre() {
        return this.plotnombre;
    }

    public void setPlotnombre(int plotnombre) {
        this.plotnombre = plotnombre;
    }

    public List<UserPlot> getPlot() {
        return this.plot;
    }

    public void setPlot(List<UserPlot> plot) {
        this.plot = plot;
    }

    public List<AddPlot> getAddplot() {
        return this.addplot;
    }

    public void setAddplot(List<AddPlot> addplot) {
        this.addplot = addplot;
    }

    public Boolean isMenu() {
        return this.menu;
    }

    public Boolean getMenu() {
        return this.menu;
    }

    public void setMenu(Boolean menu) {
        this.menu = menu;
    }

    public Boolean isBanmap1() {
        return this.banmap1;
    }

    public Boolean getBanmap1() {
        return this.banmap1;
    }

    public void setBanmap1(Boolean banmap1) {
        this.banmap1 = banmap1;
    }

    public Boolean isBanmap2() {
        return this.banmap2;
    }

    public Boolean getBanmap2() {
        return this.banmap2;
    }

    public void setBanmap2(Boolean banmap2) {
        this.banmap2 = banmap2;
    }

    public Boolean isBanmap3() {
        return this.banmap3;
    }

    public Boolean getBanmap3() {
        return this.banmap3;
    }

    public void setBanmap3(Boolean banmap3) {
        this.banmap3 = banmap3;
    }

    public Boolean isBanmap4() {
        return this.banmap4;
    }

    public Boolean getBanmap4() {
        return this.banmap4;
    }

    public void setBanmap4(Boolean banmap4) {
        this.banmap4 = banmap4;
    }

    public Profile(UUID uuid, int plotnombre, List<UserPlot> plot, List<AddPlot> addplot, Boolean menu, Boolean banmap1, Boolean banmap2, Boolean banmap3, Boolean banmap4) {
        this.uuid = uuid;
        this.plotnombre = plotnombre;
        this.plot = plot;
        this.addplot = addplot;
        this.menu = menu;
        this.banmap1 = banmap1;
        this.banmap2 = banmap2;
        this.banmap3 = banmap3;
        this.banmap4 = banmap4;
    }

    
    

    public static Profile createProfile(Player p) {
        return new Profile(p.getUniqueId(),0,
                new ArrayList<UserPlot>(), new ArrayList<AddPlot>(), true, false, false, false, false);
    }
}

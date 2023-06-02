package elitgaimix.redteam.fr.json.load;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class NPCDeplace {
    private int entityid;
    private Player p;
    private BossBar bar;

    public int getEntityid() {
        return this.entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public Player getPlayer() {
        return this.p;
    }

    public void setPlayer(Player p) {
        this.p = p;
    }

    public BossBar getBar() {
        return this.bar;
    }

    public void setBar(BossBar bar) {
        this.bar = bar;
    }

    public NPCDeplace(int entityid, Player p, BossBar bar) {
        this.entityid = entityid;
        this.p = p;
        this.bar = bar;
    }


   
}

package elitgaimix.redteam.fr.json.load;

import org.bukkit.entity.Player;

public class NPCDeplace {
    private int entityid;
    private Player p;

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

    public NPCDeplace(int entityid, Player p) {
        this.entityid = entityid;
        this.p = p;
    }
}

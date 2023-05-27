package elitgaimix.redteam.fr.json.npc;

import java.util.List;


public class NPCJson {
    private List<MAINPC> npc;

    public List<MAINPC> getNpc() {
        return this.npc;
    }

    public void setNpc(List<MAINPC> npc) {
        this.npc = npc;
    }

    public NPCJson(List<MAINPC> npc) {
        this.npc = npc;
    }

}

package elitgaimix.redteam.fr.json.npc;

public class MAINPC {
    private String name;
    private String world;
    private String textureValue;
    private String textureSignature;
    private int entityID;
    private CONPC conpc;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getTextureValue() {
        return this.textureValue;
    }

    public void setTextureValue(String textureValue) {
        this.textureValue = textureValue;
    }

    public String getTextureSignature() {
        return this.textureSignature;
    }

    public void setTextureSignature(String textureSignature) {
        this.textureSignature = textureSignature;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public CONPC getConpc() {
        return this.conpc;
    }

    public void setConpc(CONPC conpc) {
        this.conpc = conpc;
    }

    public MAINPC(String name, String world, String textureValue, String textureSignature, int entityID, CONPC conpc) {
        this.name = name;
        this.world = world;
        this.textureValue = textureValue;
        this.textureSignature = textureSignature;
        this.entityID = entityID;
        this.conpc = conpc;
    }

    

}

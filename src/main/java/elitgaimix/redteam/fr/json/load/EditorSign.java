package elitgaimix.redteam.fr.json.load;

import javax.annotation.Nullable;

public class EditorSign {
    private int X;
    private int Y;
    private int Z;
    private EditorSignEnum type;
    private @Nullable int EntityID;

    public int getX() {
        return this.X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return this.Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getZ() {
        return this.Z;
    }

    public void setZ(int Z) {
        this.Z = Z;
    }

    public EditorSignEnum getType() {
        return this.type;
    }

    public void setType(EditorSignEnum type) {
        this.type = type;
    }

    public int getEntityID() {
        return this.EntityID;
    }

    public void setEntityID(int EntityID) {
        this.EntityID = EntityID;
    }

    public EditorSign(int X, int Y, int Z, EditorSignEnum type, int EntityID) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.type = type;
        this.EntityID = EntityID;
    }
}

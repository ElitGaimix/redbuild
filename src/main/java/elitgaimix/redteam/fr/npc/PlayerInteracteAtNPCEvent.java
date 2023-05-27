package elitgaimix.redteam.fr.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;





public class PlayerInteracteAtNPCEvent extends Event implements Cancellable {
    private final Player player;
    private final int EntityID;
    private boolean sneak;
    private boolean Cancellable = false;
    private ServerboundInteractPacket.ActionType actionType;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerInteracteAtNPCEvent(Player player, int EntityID,boolean sneak,ServerboundInteractPacket.ActionType actionType){
        this.actionType = actionType;
        this.sneak = sneak;
        this.player = player;
        this.EntityID = EntityID;
    }
    public ServerboundInteractPacket.ActionType getActionType(){
        return actionType;
    }
    public Boolean getSneak(){
        return sneak;
    }
    public Player getPlayer(){
        return player;
    }
    public int getEntityID(){
        return EntityID;
    }

    @Override
    public boolean isCancelled() {
        return Cancellable;
    }

    @Override
    public void setCancelled(boolean cancel) {
     Cancellable = cancel;   
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
    
}

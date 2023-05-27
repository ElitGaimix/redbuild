package elitgaimix.redteam.fr.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;

public class SignUpdateEvent extends Event implements Cancellable{
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean Cancellable = false;
    private String[] lines;
    private Player player;
    private BlockPos pos;

    public SignUpdateEvent(String[] lines, Player player, BlockPos pos) {
        this.lines = lines;
        this.player = player;
        this.pos = pos;
    }

    public String[] getLines(){
        return lines;
    }
    public BlockPos getPos(){
        return pos;
    }
    public Player getPlayer(){
        return player;
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

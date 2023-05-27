package elitgaimix.redteam.fr.fonction;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import elitgaimix.redteam.fr.Plugin;
import io.netty.channel.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class NPCUtils {

    public static byte toRawYaw(float yaw) {
		return (byte) (yaw * 256F / 360F);
	}
	public static ServerGamePacketListenerImpl playerConnection(Player player) {
		return ((CraftPlayer) player).getHandle().connection;
		
	}
	public static void sendSignData(Player player, String[] lines) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("String line must be of length 4");
        }

        final BlockPos blockPosition = new BlockPos(
                player.getLocation().getBlockX(), 1,
                player.getLocation().getBlockZ());

        ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(
                blockPosition,
                Blocks.OAK_WALL_SIGN.defaultBlockState());
		networkManager(player).send(packet);

        SignBlockEntity sign = new SignBlockEntity(blockPosition,
                Blocks.OAK_WALL_SIGN.defaultBlockState());
        CraftSign craftSign = new CraftSign(player.getWorld(), sign);
        craftSign.setLine(0, lines[0]);
        craftSign.setLine(1, lines[1]);
        craftSign.setLine(2, lines[2]);
        craftSign.setLine(3, lines[3]);

        craftSign.applyTo(sign);

       networkManager(player).send(sign.getUpdatePacket());

        ClientboundOpenSignEditorPacket openSignEditor = new ClientboundOpenSignEditorPacket(
                blockPosition);
				networkManager(player).send(openSignEditor);
    }
	/**
	 * 
	 * @param player
	 * @return
	 */
	public static Connection networkManager(Player player){
		return playerConnection(player).getConnection();
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	public static Channel channel(Player player){
		return networkManager(player).channel;
	}

}

package elitgaimix.redteam.fr.npc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import elitgaimix.redteam.fr.Plugin;
import elitgaimix.redteam.fr.fonction.NPCUtils;
import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.load.NPCDeplace;
import elitgaimix.redteam.fr.json.npc.CONPC;
import elitgaimix.redteam.fr.json.npc.MAINPC;
import elitgaimix.redteam.fr.json.npc.NPCJson;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

public class npc implements CommandExecutor, Listener {
    private Plugin plugin;

    public npc(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
        if(cmd.getName().equalsIgnoreCase("npc")){
        
            String npcname = "";
            Player p = (Player) sender;
            if (args.length > 0) {
                npcname = args[0];
            }
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcname);
            ServerLevel nmsWorld = ((CraftWorld) Bukkit.getWorld("plotworld")).getHandle();
            ServerPlayer entityPlayer = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), nmsWorld,
                    gameProfile, null);
            Location ploc = p.getLocation();
            if (args.length == 0)
                entityPlayer.setCustomNameVisible(false);
            entityPlayer.setPos(ploc.getX(), ploc.getY(), ploc.getZ());
            entityPlayer.setXRot(ploc.getPitch());
            entityPlayer.setYRot(ploc.getYaw());
            entityPlayer.setYBodyRot(ploc.getYaw());
            entityPlayer.setYHeadRot(ploc.getYaw());
            entityPlayer.setId((int) (5000 + (Math.random() * (10000 + 5000))));

            Property prop = new Property(null, null, null);
            if (args.length > 1) {
                try {
                    ServerPlayer pla = ((CraftPlayer) Bukkit.getServer().getPlayer(args[1])).getHandle();
                    prop = pla.getGameProfile().getProperties().get("textures").iterator().next();

                    entityPlayer.getGameProfile().getProperties().removeAll("textures");
                    entityPlayer.getGameProfile().getProperties().put("textures",
                            new Property("textures", prop.getValue(), prop.getSignature()));
                } catch (Exception e) {
                    p.sendMessage("§4Le joueur " + args[1] + " n'existe pas ou n'est pas connecter sur le serveur.");
                    plugin.getLogger().warning(e.getMessage());

                }
            }

            Object[] allp = Bukkit.getServer().getOnlinePlayers().toArray();
            int n = 0;
            while (allp.length > n) {
                Player player = (Player) allp[n];

                ServerPlayerConnection playerco = ((CraftPlayer) player).getHandle().connection;
                playerco.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                        entityPlayer));
                playerco.send(new ClientboundAddPlayerPacket(entityPlayer));
                playerco.send(new ClientboundTeleportEntityPacket(entityPlayer));
                playerco.send(new ClientboundMoveEntityPacket.Rot(entityPlayer.getId(),
                        NPCUtils.toRawYaw(ploc.getYaw()), NPCUtils.toRawYaw(ploc.getPitch()), true));
                playerco.send(new ClientboundRotateHeadPacket(entityPlayer, NPCUtils.toRawYaw(ploc.getYaw())));
                playerco.send(new ClientboundAnimatePacket(entityPlayer, ClientboundAnimatePacket.SWING_MAIN_HAND));
                n++;
            }
            NPCJson tamere = new NPCJson(null);
            List<MAINPC> npcen = new ArrayList<MAINPC>();
            if (plugin.npc != null && plugin.npc.getNpc() != null) {
                tamere = plugin.npc;
                npcen = plugin.npc.getNpc();
            }
            npcen.add(new MAINPC(npcname, "plotworld", prop.getValue(), prop.getSignature(),entityPlayer.getId(),entityPlayer.getUUID(),
                    new CONPC(ploc.getX(), ploc.getY(), ploc.getZ(), ploc.getYaw(), ploc.getPitch())));
            tamere.setNpc(npcen);
            plugin.npc = tamere;

            FileUtils.saveFile(
                    new File(new File(plugin.getDataFolder(), plugin.getConfig().getString("repertory.main")),
                            "NPC.json"),
                    tamere);
        
        return false;
    }else if(cmd.getName().equalsIgnoreCase("setnpc")){
        Player p = (Player) sender;
        for(NPCDeplace npcDeplace : plugin.NPCDeplace){
            if(npcDeplace.getPlayer() == p){
                for(MAINPC npc : plugin.npc.getNpc()){
                    if(npc.getEntityID() == npcDeplace.getEntityid()){
                                ServerLevel nmsworld = ((CraftWorld) Bukkit.getServer().getWorld(npc.getWorld())).getHandle();
                                ServerPlayer ServerNPC = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), nmsworld,
                                new GameProfile(npc.getNPCUUID(), npc.getName()), null);
                                ServerNPC.setPos(npc.getConpc().getX(), npc.getConpc().getY(),
                                npc.getConpc().getZ());
                                ServerNPC.setXRot(npc.getConpc().getYaw());
                                ServerNPC.setYRot(npc.getConpc().getPitch());
                                ServerNPC.setYBodyRot(npc.getConpc().getYaw());
                                ServerNPC.setYHeadRot(npc.getConpc().getYaw());
                                ServerNPC.setId(npc.getEntityID());
                                ServerNPC.getGameProfile().getProperties().removeAll("textures");
                                ServerNPC.getGameProfile().getProperties().put("textures",
                                new Property("textures", npc.getTextureValue(), npc.getTextureSignature()));
                    }
                }
                break;
            }
        }
    }}
    return false;}
}

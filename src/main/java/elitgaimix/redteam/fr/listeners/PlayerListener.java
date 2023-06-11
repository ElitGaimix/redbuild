package elitgaimix.redteam.fr.listeners;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import elitgaimix.redteam.fr.Plugin;
import elitgaimix.redteam.fr.fonction.NPCUtils;
import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.Plot.Plot;
import elitgaimix.redteam.fr.json.Plot.PlotFusion;
import elitgaimix.redteam.fr.json.load.EditorSign;
import elitgaimix.redteam.fr.json.load.EditorSignEnum;
import elitgaimix.redteam.fr.json.npc.MAINPC;
import elitgaimix.redteam.fr.json.profile.AddPlot;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.json.profile.UserPlot;
import elitgaimix.redteam.fr.npc.PlayerInteracteAtNPCEvent;
import elitgaimix.redteam.fr.npc.SignUpdateEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PlayerListener implements Listener {

    private File saveDir;
    private Plugin plugin;

    public PlayerListener(Plugin plugin) {
        this.plugin = plugin;
        this.saveDir = new File(plugin.getDataFolder(), plugin.getConfig().getString("repertory.player"));
        if (!saveDir.exists()) {
            this.saveDir.mkdir();
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();
        injectPlayer(p);
        Player player = event.getPlayer();
        if(player.hasPermission("redteam.fondateur")){
            player.setPlayerListName("§4[Fondateur] " + player.getName());
        }
        Component header = Component.Serializer.fromJson("[\"\",{\"text\":\"caca\",\"obfuscated\":true,\"color\":\"dark_purple\"},{\"text\":\" \\u2583\\u2585\\u2587\\u2589 RedBuild \\u2589\\u2587\\u2585\\u2583 \",\"color\":\"gold\"},{\"text\":\"caca\",\"obfuscated\":true,\"color\":\"dark_purple\"}]");
        Component footer = Component.Serializer.fromJson("{\"text\":\"Bienvenue sur RedBuild !\",\"color\":\"gray\"}");

        NPCUtils.networkManager(player).send(new ClientboundTabListPacket(header, footer));
        p.teleport(new Location(Bukkit.getWorld(plugin.getConfig().getString("spawn.lobby.map")),
                plugin.getConfig().getInt("spawn.lobby.x"), plugin.getConfig().getInt("spawn.lobby.y"),
                plugin.getConfig().getInt("spawn.lobby.z"),plugin.getConfig().getInt("spawn.lobby.yaw"),plugin.getConfig().getInt("spawn.lobby.pitch")));
        player.addAttachment(plugin, "redteam.build", false);
        int n = 0;
        List<MAINPC> allnpc = Plugin.npc.getNpc();
        while (n < allnpc.size()) {
            ServerLevel nmsworld = ((CraftWorld) Bukkit.getServer().getWorld(allnpc.get(n).getWorld())).getHandle();
            ServerPlayer npc = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), nmsworld,
                    new GameProfile(UUID.randomUUID(), allnpc.get(n).getName()), null);
            npc.setPos(allnpc.get(n).getConpc().getX(), allnpc.get(n).getConpc().getY(),
                    allnpc.get(n).getConpc().getZ());
            npc.setXRot(allnpc.get(n).getConpc().getYaw());
            npc.setYRot(allnpc.get(n).getConpc().getPitch());
            npc.setYBodyRot(allnpc.get(n).getConpc().getYaw());
            npc.setYHeadRot(allnpc.get(n).getConpc().getYaw());
            npc.setId(allnpc.get(n).getEntityID());

            npc.getGameProfile().getProperties().removeAll("textures");
            npc.getGameProfile().getProperties().put("textures",
                    new Property("textures", allnpc.get(n).getTextureValue(), allnpc.get(n).getTextureSignature()));
            ((CraftPlayer) p).getHandle().connection
                  .send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                        npc));
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundAddPlayerPacket(npc));
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundTeleportEntityPacket(npc));
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundMoveEntityPacket.Rot(npc.getId(),
                    NPCUtils.toRawYaw(allnpc.get(n).getConpc().getYaw()),
                    NPCUtils.toRawYaw(allnpc.get(n).getConpc().getPitch()), true));
            ((CraftPlayer) p).getHandle().connection
                    .send(new ClientboundRotateHeadPacket(npc, NPCUtils.toRawYaw(allnpc.get(n).getConpc().getYaw())));
            ((CraftPlayer) p).getHandle().connection
                    .send(new ClientboundAnimatePacket(npc, ClientboundAnimatePacket.SWING_MAIN_HAND));
            n++;
        }

        File file = new File(saveDir, player.getName() + ".json");
        Profile profile = FileUtils.openProfileFile(file, player);
        if (Boolean.TRUE
                .equals(profile.getBanmap1() && profile.getBanmap2() && profile.getBanmap3() && profile.getBanmap4())) {
            player.kickPlayer("Vous êtes banni de toute les map");
        }
        if (Boolean.TRUE.equals(profile.getBanmap1()) && player.getWorld() == Bukkit.getWorld("flatroom")) {
            player.sendTitle("§4Vous êtes banni de cette map", null, 1, 100, 20);
            player.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
        }

        if (Boolean.TRUE.equals(profile.getBanmap2()) && (player.getWorld() == Bukkit.getWorld("world"))) {
            player.sendTitle("§4Vous êtes banni de cette map", null, 1, 100, 20);
            player.teleport(new Location(Bukkit.getWorld("plotworld"), 0, 0, 0));

        }

        if (Boolean.TRUE.equals(profile.getBanmap3()) && (player.getWorld() == Bukkit.getWorld("bedwars"))) {
            player.sendTitle("§4Vous êtes banni de cette map", null, 1, 100, 20);
            player.teleport(new Location(Bukkit.getWorld("flatroom"), 0, 0, 0));

        }

        if (Boolean.TRUE.equals(profile.getBanmap4()) && player.getWorld() == Bukkit.getWorld("plotworld")) {
            player.sendTitle("§4Vous êtes banni de cette map", null, 1, 100, 20);
            player.teleport(new Location(Bukkit.getWorld("bedwars"), 0, 0, 0));

        }
        if (Boolean.TRUE.equals(profile.getMenu())) {
            Material menu = Material.CLOCK;
            if (plugin.getConfig().getBoolean("resource.force")) {
                menu = Material.SUGAR_CANE;
            }
            ItemStack item = new ItemStack(menu, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6Menu");
            meta.setCustomModelData(1);
            item.setItemMeta(meta);
            p.getInventory().setItem(8, item);

            if (p.isOp()) {
                ItemStack ely = new ItemStack(Material.ELYTRA);
                ItemMeta mely = ely.getItemMeta();
                mely.setDisplayName("§5Elytra de base");
                ely.setItemMeta(mely);
                p.getInventory().setChestplate(ely);

                ItemStack fus = new ItemStack(Material.FIREWORK_ROCKET);
                ItemMeta mfus = fus.getItemMeta();
                mfus.setDisplayName("§5Fusée de base");
                fus.setItemMeta(mfus);
                p.getInventory().setItemInOffHand(fus);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        removePlayer(e.getPlayer());
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerInteractNPCEvent(PlayerInteracteAtNPCEvent e){
        Player p = e.getPlayer();
        int entityID = e.getEntityID();
        if(p.hasPermission("redteam.npc")){
            for(MAINPC npc : plugin.npc.getNpc()){
                if(npc.getEntityID() == entityID){

                    Inventory inv = Bukkit.createInventory(null, 9, "" + npc.getEntityID());

                    ItemStack item = new ItemStack(Material.BARRIER, 1);
                    ItemMeta itemm = item
                            .getItemMeta();
                    itemm.setDisplayName("§4Delete NPC");
                    item.setItemMeta(itemm);
                    inv.setItem(4, item);
                    

                    ItemStack item1 = new ItemStack(Material.WRITABLE_BOOK, 1);
                    ItemMeta itemm1 = item1
                            .getItemMeta();
                    itemm1.setDisplayName("§dRename NPC");
                    item1.setItemMeta(itemm1);
                    inv.setItem(3, item1);  
                                    
                    ItemStack item2 = new ItemStack(Material.ENDER_PEARL, 1);
                    ItemMeta itemm2 = item2
                            .getItemMeta();
                    itemm2.setDisplayName("§aMove NPC");
                    item2.setItemMeta(itemm2);
                    inv.setItem(5, item2);

                    ItemStack item3 = new ItemStack(Material.ENDER_PEARL, 1);
                    ItemMeta itemm3 = item3
                            .getItemMeta();
                    itemm3.setDisplayName("§aMove NPC");
                    item3.setItemMeta(itemm3);
                    inv.setItem(5, item3);
                    p.openInventory(inv);
                    break;
                }
            }
        }
    }
    
    private void removePlayer(Player player){
        Channel channel = NPCUtils.channel(player);
        channel.eventLoop().submit(()->{
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

   private void injectPlayer(Player player){
    ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

        @Override
        @SuppressWarnings("unchecked")
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
            if(msg instanceof Packet){
                Packet<ServerGamePacketListener> p =  (Packet<ServerGamePacketListener>) msg;
                if(p instanceof ServerboundInteractPacket packet){
                    Bukkit.getScheduler()
                    .runTask(plugin, () -> {
                        // Executer dans le thread principal
                        var event = new PlayerInteracteAtNPCEvent(player, packet.getEntityId(), false,packet.getActionType());
                        event.callEvent();
                    });
                }else if(p instanceof ServerboundSignUpdatePacket packet){
                    Bukkit.getScheduler()
                    .runTask(plugin, () -> {
                    var event = new SignUpdateEvent(packet.getLines(), player, packet.getPos());
                    event.callEvent();
                });
                }
                
            }
            super.channelRead(channelHandlerContext, msg);
        }
    };
    
    ChannelPipeline pipeline = NPCUtils.channel(player).pipeline();
    pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    
    
   }
    @EventHandler
    public void SignUpdateEvent(SignUpdateEvent e){
        Player player = e.getPlayer();
        for(EditorSign eSign : plugin.OpenEditorSign){
            if(e.getPos().getX() == eSign.getX() && e.getPos().getY() == 1 && e.getPos().getZ() == eSign.getZ() && eSign.getType() == EditorSignEnum.NPCRENAME){      
                    for(MAINPC npc : plugin.npc.getNpc()){                       
                        if(npc.getEntityID() == eSign.getEntityID()){                          
                            for(Player p : Bukkit.getServer().getOnlinePlayers()){                               
                                NPCUtils.networkManager(player).send(new ClientboundRemoveEntitiesPacket(npc.getEntityID()));
                                
                                ServerLevel nmsworld = ((CraftWorld) Bukkit.getServer().getWorld(npc.getWorld())).getHandle();
                                String[] lines = e.getLines();
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
                                NPCUtils.networkManager(player).send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,ServerNPC));
                                ServerNPC = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), nmsworld,
                                new GameProfile(npc.getNPCUUID(), lines[1]), null);
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
                                NPCUtils.networkManager(p)
                                .send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                                ServerNPC));
                                NPCUtils.networkManager(p).send(new ClientboundAddPlayerPacket(ServerNPC));
                                NPCUtils.networkManager(p).send(new ClientboundTeleportEntityPacket(ServerNPC));
                                NPCUtils.networkManager(p).send(new ClientboundMoveEntityPacket.Rot(npc.getEntityID(),
                                NPCUtils.toRawYaw(npc.getConpc().getYaw()),
                                NPCUtils.toRawYaw(npc.getConpc().getPitch()), true));
                                NPCUtils.networkManager(p)
                                .send(new ClientboundRotateHeadPacket(ServerNPC, NPCUtils.toRawYaw(npc.getConpc().getYaw())));
                                NPCUtils.networkManager(p)
                                .send(new ClientboundAnimatePacket(ServerNPC, ClientboundAnimatePacket.SWING_MAIN_HAND));
                                plugin.npc.getNpc().remove(npc);
                                npc.setName(lines[1]);
                                plugin.npc.getNpc().add(npc);
                                FileUtils.saveFile(
                                new File(new File(plugin.getDataFolder(), plugin.getConfig().getString("repertory.main")),
                                "NPC.json"),
                                plugin.npc);
                                plugin.OpenEditorSign.remove(eSign);
                                
                            }
                        }
                    } 
                }
            }
        }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (!p.getInventory().contains(Material.SUGAR_CANE)) {
            Player player = e.getPlayer();
            File file = new File(saveDir, player.getName() + ".json");
            Profile profile = FileUtils.openProfileFile(file, player);
            Material menu = Material.CLOCK;
            if (plugin.getConfig().getBoolean("resource.force")) {
                menu = Material.SUGAR_CANE;
            }
            if (profile.getMenu() == true) {

                ItemStack item = new ItemStack(menu, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§6Menu");
                meta.setCustomModelData(1);
                item.setItemMeta(meta);
                p.getInventory().setItem(8, item);

                if (p.isOp()) {
                    ItemStack ely = new ItemStack(Material.ELYTRA);
                    ItemMeta mely = ely.getItemMeta();
                    mely.setDisplayName("§5Elytra de base");
                    ely.setItemMeta(mely);
                    p.getInventory().setChestplate(ely);

                    ItemStack fus = new ItemStack(Material.FIREWORK_ROCKET);
                    ItemMeta mfus = fus.getItemMeta();
                    mfus.setDisplayName("§5Fusée de base");
                    fus.setItemMeta(mfus);
                    p.getInventory().setItemInOffHand(fus);
                }
            }
        }
    }

    @EventHandler
    public void OnPlayerDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Menu") && item.getType() == Material.SUGAR_CANE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void RessourcePack(PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED
                && this.plugin.getConfig().getBoolean("resource.force") == true) {
            e.getPlayer().kickPlayer("§4Veuillez accepter le resource pack !");
            plugin.getLogger()
                    .warning("[RedTeam]Le joueur " + e.getPlayer().getName() + " n'a pas accepter le resource pack.");
        }
    }

    @EventHandler
    public void EntityExplose(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void OnPlayerUse(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
                && !p.hasPermission("redteam.build")) {
                    e.setCancelled(true);
                if(p.getWorld() == Bukkit.getWorld(plugin.getConfig().getString("plot.world"))){
            Chunk PlayerChunk = e.getClickedBlock().getChunk();
            Profile profile = FileUtils.openProfileFile(new File(saveDir, p.getName() + ".json"), p);

            int coX = 0;
            int coZ = 0;
            int n = 0;
            boolean Players = false;
            boolean fin = false;
            //a refaire !
            while (fin != true) {
                UserPlot plot = null;
                AddPlot addplot = null;
                if (profile.getPlot().size() > n) {
                 plot = profile.getPlot().get(n);
             } else if (profile.getAddplot().size() > n) {
                 addplot = profile.getAddplot().get(n);
                }else{
                    fin= true;
                }
                try {
                    if(plot != null){
                        coX = plot.getCoX();
                        coZ = plot.getCoZ();
                        if ((coX == PlayerChunk.getX() + 1 || coX == PlayerChunk.getX()
                                || coX == PlayerChunk.getX() - 1)
                                && (coZ == PlayerChunk.getZ() + 1 || coZ == PlayerChunk.getZ()
                                        || coZ == PlayerChunk.getZ() - 1)) {
                            Players = true;
                            fin = true;
                        }
                    }else if(addplot != null){
                        coX = addplot.getCoX();
                        coZ = addplot.getCoX();
                        if ((coX == PlayerChunk.getX() + 1 || coX == PlayerChunk.getX()
                                || coX == PlayerChunk.getX() - 1)
                                && (coZ == PlayerChunk.getZ() + 1 || coZ == PlayerChunk.getZ()
                                        || coZ == PlayerChunk.getZ() - 1)) {
                            if (Bukkit.getPlayer(addplot.getPlayer()) != null
                                    || Boolean.TRUE.equals(addplot.getTrust())) {
                                Players = true;
                            }
                            fin = true;
                        }
                    }
                    n++;

                } catch (Exception er) {
                    Players = false;
                    fin = true;

                }

            }
            Plot plot = FileUtils.getPlot(p);
            for(PlotFusion pFusion : plot.getPlotFusion()){
                if(pFusion){

                }
            }
            if (Players != true) {
                e.setCancelled(true);
                if (this.plugin.getConfig().getBoolean("message.break")) {
                    p.sendMessage("§4Vous n'avez pas la permission: §credteam.build");
                }

            }
        }
    }
    }
}

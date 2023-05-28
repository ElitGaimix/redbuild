package elitgaimix.redteam.fr.listeners;

import elitgaimix.redteam.fr.Plugin;
import elitgaimix.redteam.fr.fonction.NPCUtils;
import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.load.EditorSign;
import elitgaimix.redteam.fr.json.load.EditorSignEnum;
import elitgaimix.redteam.fr.json.npc.MAINPC;
import elitgaimix.redteam.fr.json.profile.Profile;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;

import java.io.File;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;




public class MenuGui implements Listener {
    private File saveDir;
    private Plugin plugin;

    public MenuGui(Plugin plugin) {
        this.plugin = plugin;
        this.saveDir = new File(plugin.getDataFolder(), "/profiles/");
        if (!saveDir.exists()) {
            this.saveDir.mkdir();
        }
    }

    @EventHandler
    public void onClickAir(PlayerInteractEvent e) {

        ItemStack item = e.getItem();
        Player p = e.getPlayer();
        if(!p.hasPermission("redteam.build")){
            if(e.getAction() == Action.PHYSICAL){
                Block block = e.getClickedBlock();
                if (block == null) return;
                if (block.getType() == Material.FARMLAND){
                    e.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                    e.setCancelled(true);
                }
            }
        }
        if (item != null) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Menu")) {
                Bukkit.dispatchCommand(p, "menu");
            }
            if (item.getType() == Material.FIREWORK_ROCKET && p.getGameMode() == GameMode.SURVIVAL && p.isOp()) {
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
    public void onMoove(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
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

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getInventory();
        String invname = e.getView().getTitle();
        if (item != null) {
            if(item.getType() == Material.BARRIER && item.getItemMeta().getDisplayName().equalsIgnoreCase("§4Delete NPC") && p.hasPermission("redteam.npc")){
                int EntityID = Integer.parseInt(invname);
                e.setCancelled(true);
                for(MAINPC npc : plugin.npc.getNpc()){
                    if(npc.getEntityID() == EntityID){
                        for(Player player : Bukkit.getServer().getOnlinePlayers()){
                            NPCUtils.networkManager(player).send( new ClientboundRemoveEntitiesPacket(npc.getEntityID()));
                        }
                    plugin.npc.getNpc().remove(npc);
                    FileUtils.saveFile(
                    new File(new File(plugin.getDataFolder(), plugin.getConfig().getString("repertory.main")),
                            "NPC.json"),
                    plugin.npc);
                    p.closeInventory();
                    p.sendMessage("§2Le npc " + npc.getName() + " a été suprimé");
                    break;
                    }
                }
            }
            if(item.getType() == Material.WRITABLE_BOOK && item.getItemMeta().getDisplayName().equalsIgnoreCase("§dRename NPC") && p.hasPermission("redteam.npc")){
                e.setCancelled(true);
                p.closeInventory();
                for(MAINPC npc : plugin.npc.getNpc()){
                    if(npc.getEntityID() == Integer.parseInt(invname)){
                        plugin.OpenEditorSign.add(new EditorSign((int) p.getLocation().getX(), 1, (int) p.getLocation().getZ(), EditorSignEnum.NPCRENAME, npc.getEntityID()));
                        NPCUtils.sendSignData(p,new String[] {"",npc.getName(),"",""});
                        break;
                    }

            }
            }
            if (item.getType() == Material.FIREWORK_ROCKET && p.getGameMode() == GameMode.SURVIVAL && p.isOp()) {
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

            if (item.getType() == Material.RED_TERRACOTTA
                    && item.getItemMeta().getDisplayName().equalsIgnoreCase("§4Fermer")) {
                e.setCancelled(true);
                p.closeInventory();

            }
            if ((item.getType() == Material.GREEN_CONCRETE || item.getType() == Material.RED_CONCRETE)
                    && (item.getItemMeta().getDisplayName().equalsIgnoreCase("§4Ban: Activer")
                            || item.getItemMeta().getDisplayName().equalsIgnoreCase("§aBan: Désactiver"))) {
                e.setCancelled(true);

                String pname = invname;

                pname = ChatColor.stripColor(pname);

                Player player = Bukkit.getServer().getPlayer(pname);

                File file = new File(saveDir, pname + ".json");
                Profile profile = FileUtils.openProfileFile(file, player);
                ItemStack gc = new ItemStack(Material.RED_CONCRETE);
                ItemMeta mgc = gc.getItemMeta();
                mgc.setDisplayName("§4Ban: Activer");
                gc.setItemMeta(mgc);

                ItemStack rc = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta mrc = rc.getItemMeta();
                mrc.setDisplayName("§aBan: Désactiver");
                rc.setItemMeta(mrc);

                if (e.getSlot() == 20) {

                    Boolean banmap1 = profile.getBanmap1();
                    profile.setBanmap1(!banmap1);
                    FileUtils.saveFile(file, profile);

                    if (profile.getBanmap1() == true) {
                        inv.setItem(20, gc);
                    } else {
                        inv.setItem(20, rc);
                    }

                }
                if (e.getSlot() == 21) {

                    Boolean banmap2 = profile.getBanmap2();
                    profile.setBanmap2(!banmap2);
                    FileUtils.saveFile(file, profile);

                    if (profile.getBanmap2() == true) {
                        inv.setItem(21, gc);
                    } else {
                        inv.setItem(21, rc);
                    }

                }
                if (e.getSlot() == 23) {

                    Boolean banmap3 = profile.getBanmap3();
                    profile.setBanmap3(!banmap3);
                    FileUtils.saveFile(file, profile);

                    if (profile.getBanmap3() == true) {
                        inv.setItem(23, gc);
                    } else {
                        inv.setItem(23, rc);
                    }

                }
                if (e.getSlot() == 24) {

                    Boolean banmap4 = profile.getBanmap4();
                    profile.setBanmap4(!banmap4);
                    FileUtils.saveFile(file, profile);

                    if (profile.getBanmap4() == true) {
                        inv.setItem(24, gc);
                    } else {
                        inv.setItem(24, rc);
                    }

                }

            }
        }
        if (invname.equalsIgnoreCase("§4Menu Admin")) {
            e.setCancelled(true);
            if (item != null) {
                if (item.getType() == Material.RED_TERRACOTTA) {
                    p.closeInventory();
                }

                if (item.getType() == Material.BARRIER) {
                    inv.remove(Material.BARRIER);
                    Object[] pc = Bukkit.getServer().getOnlinePlayers().toArray();
                    Player pl = Bukkit
                            .getPlayer(pc[0].toString().replace("CraftPlayer{name=", "").replace("}", ""));
                    int nb = 0;
                    while (pl != null) {
                        pl = Bukkit
                                .getPlayer(pc[nb].toString().replace("CraftPlayer{name=", "").replace("}", ""));
                        ItemStack skull2 = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta sm2 = (SkullMeta) skull2.getItemMeta();
                        sm2.setOwningPlayer(pl);
                        sm2.setDisplayName(pl.getDisplayName());
                        skull2.setItemMeta(sm2);
                        inv.setItem(nb + 10, skull2);

                        nb++;
                    }

                    p.updateInventory();
                }

                if (item.getType() == Material.PLAYER_HEAD) {
                    Player player = (Player) Bukkit
                            .getOfflinePlayer(item.getItemMeta().getDisplayName().replace("§4", ""));
                    File file = new File(saveDir, player.getName() + ".json");
                    Profile profile = FileUtils.openProfileFile(file,
                            player);
                    inv = Bukkit.createInventory(null, 27, player.getDisplayName());

                    ItemStack vitre = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                    ItemMeta mvitre = vitre
                            .getItemMeta();
                    mvitre.setDisplayName("§r§kcaca");
                    vitre.setItemMeta(mvitre);
                    inv.setItem(0, vitre);
                    inv.setItem(1, vitre);
                    inv.setItem(2, vitre);
                    inv.setItem(3, vitre);
                    inv.setItem(4, vitre);
                    inv.setItem(5, vitre);
                    inv.setItem(6, vitre);
                    inv.setItem(8, vitre);
                    inv.setItem(7, vitre);
                    inv.setItem(9, vitre);
                    inv.setItem(17, vitre);
                    inv.setItem(18, vitre);
                    inv.setItem(19, vitre);
                    inv.setItem(25, vitre);
                    inv.setItem(26, vitre);

                    ItemStack gc = new ItemStack(Material.RED_CONCRETE);
                    ItemMeta mgc = gc.getItemMeta();
                    mgc.setDisplayName("§4Ban: Activer");
                    gc.setItemMeta(mgc);

                    ItemStack rc = new ItemStack(Material.GREEN_CONCRETE);
                    ItemMeta mrc = rc
                            .getItemMeta();
                    mrc.setDisplayName("§aBan: Désactiver");
                    rc.setItemMeta(mrc);
                    if (profile.getBanmap1() == true) {
                        inv.setItem(20, gc);
                    } else {
                        inv.setItem(20, rc);
                    }

                    if (profile.getBanmap2() == true) {
                        inv.setItem(21, gc);
                    } else {
                        inv.setItem(21, rc);
                    }

                    if (profile.getBanmap3() == true) {
                        inv.setItem(23, gc);
                    } else {
                        inv.setItem(23, rc);
                    }

                    if (profile.getBanmap4() == true) {
                        inv.setItem(24, gc);
                    } else {
                        inv.setItem(24, rc);
                    }

                    ItemStack rl = new ItemStack(Material.REDSTONE_LAMP, 1);
                    ItemMeta mrl = rl.getItemMeta();
                    mrl.setDisplayName("§4Redstone");
                    rl.setItemMeta(mrl);
                    inv.setItem(11, rl);

                    ItemStack mpp = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta mmpp = mpp.getItemMeta();
                    mmpp.setDisplayName("§aMap principale");
                    mpp.setItemMeta(mmpp);
                    inv.setItem(12, mpp);

                    ItemStack bed = new ItemStack(Material.RED_BED, 1);
                    ItemMeta mbed = bed.getItemMeta();
                    mbed.setDisplayName("§cBed Wars");
                    bed.setItemMeta(mbed);
                    inv.setItem(14, bed);

                    ItemStack plw = new ItemStack(Material.SMOOTH_SANDSTONE, 1);
                    ItemMeta mplw = plw.getItemMeta();
                    mplw.setDisplayName("§ePlot World");
                    plw.setItemMeta(mplw);
                    inv.setItem(15, plw);

                    Player pl = (Player) Bukkit
                            .getOfflinePlayer(item.getItemMeta().getDisplayName().replace("§4", ""));
                    ItemStack skull2 = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta sm2 = (SkullMeta) skull2
                            .getItemMeta();
                    sm2.setOwningPlayer(pl);
                    sm2.setDisplayName(item.getItemMeta().getDisplayName());
                    skull2.setItemMeta(sm2);
                    inv.setItem(13, skull2);

                    ItemStack fermer = new ItemStack(Material.RED_TERRACOTTA);
                    ItemMeta fm = fermer.getItemMeta();
                    fm.setDisplayName("§4Fermer");
                    fermer.setItemMeta(fm);
                    inv.setItem(22, fermer);

                    p.openInventory(inv);
                }
            }
        }
        
        if (invname.equalsIgnoreCase("§4Menu RedTeam")) {

            if (item.getType() == Material.GRASS_BLOCK) {
                p.sendMessage("§6Teleportation....");
                try {
                    p.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
                    p.sendMessage("§aTeleportation réussi!");
                } catch (Exception exc) {
                    p.sendMessage("§4Echec de la teleportation: " + e);

                }

            }
            if (item.getType() == Material.RED_BED) {
                p.sendMessage("§6Teleportation....");
                try {
                    p.teleport(new Location(Bukkit.getWorld("bedwars"), 0, 0, 0));
                    p.sendMessage("§aTeleportation réussi!");
                } catch (Exception exc) {
                    p.sendMessage("§4Echec de la teleportation: " + e);

                }

            }
            if (item.getType() == Material.REDSTONE_LAMP) {
                p.sendMessage("§6Teleportation....");
                try {
                    p.teleport(new Location(Bukkit.getWorld("flatroom"), 0, 0, 0));
                    p.sendMessage("§aTeleportation réussi!");
                } catch (Exception exc) {
                    p.sendMessage("§4Echec de la teleportation: " + e);

                }

            }
            if (item.getType() == Material.SMOOTH_SANDSTONE) {
                p.sendMessage("§6Teleportation....");
                try {
                    p.teleport(new Location(Bukkit.getWorld("plotworld"), 0, 0, 0));
                    p.sendMessage("§aTeleportation réussi!");
                } catch (Exception exc) {
                    p.sendMessage("§4Echec de la teleportation: " + e);

                }

            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§5Debug Stick")
                    || item.getType() == Material.POTION || item.getType() == Material.BARRIER
                    || item.getType() == Material.STRUCTURE_VOID) {
                e.setCancelled(false);
            } else {
                e.setCancelled(true);
            }

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§2Warp")) {
                ItemStack vitre = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                ItemMeta mvitre = vitre
                        .getItemMeta();
                mvitre.setDisplayName("§r§kcaca");
                vitre.setItemMeta(mvitre);
                inv.setItem(33, vitre);
                if (p.isOp()) {
                    ItemStack rl = new ItemStack(Material.REDSTONE_LAMP, 1);
                    ItemMeta mrl = rl.getItemMeta();
                    mrl.setDisplayName("§4Redstone");
                    rl.setItemMeta(mrl);
                    inv.setItem(16, rl);

                    ItemStack mpp = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta mmpp = mpp.getItemMeta();
                    mmpp.setDisplayName("§aMap principale");
                    mpp.setItemMeta(mmpp);
                    inv.setItem(43, mpp);
                }
                ItemStack bed = new ItemStack(Material.RED_BED, 1);
                ItemMeta mbed = bed.getItemMeta();
                mbed.setDisplayName("§cBed Wars");
                bed.setItemMeta(mbed);
                inv.setItem(34, bed);

                ItemStack plw = new ItemStack(Material.SMOOTH_SANDSTONE, 1);
                ItemMeta mplw = plw
                        .getItemMeta();
                mplw.setDisplayName("§ePlot World");
                plw.setItemMeta(mplw);
                inv.setItem(25, plw);
            }
            if (item.getType() == Material.RED_TERRACOTTA) {
                p.closeInventory();
            }

            if (item.getType() == Material.GREEN_CONCRETE) {
                Player player = p;
                File file = new File(saveDir, player.getName() + ".json");
                Profile profile = FileUtils.openProfileFile(file,
                        player);
                profile.setMenu(false);
                FileUtils.saveFile(file, profile);
                Material menu = Material.CLOCK;
                if (plugin.getConfig().getBoolean("resource.force")) {
                    menu = Material.SUGAR_CANE;
                }
                player.getInventory().remove(menu);
                p.closeInventory();
                Bukkit.dispatchCommand(p, "menu");
            }

            if (item.getType() == Material.COMMAND_BLOCK) {
                inv = Bukkit.createInventory(null, 27, "§4Menu Admin");

                ItemStack vitre = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                ItemMeta mvitre = vitre
                        .getItemMeta();
                mvitre.setDisplayName("§r§kcaca");
                vitre.setItemMeta(mvitre);
                inv.setItem(0, vitre);
                inv.setItem(1, vitre);
                inv.setItem(2, vitre);
                inv.setItem(3, vitre);
                inv.setItem(4, vitre);
                inv.setItem(5, vitre);
                inv.setItem(6, vitre);
                inv.setItem(8, vitre);
                inv.setItem(7, vitre);
                inv.setItem(9, vitre);
                inv.setItem(17, vitre);
                inv.setItem(18, vitre);
                inv.setItem(19, vitre);
                inv.setItem(20, vitre);
                inv.setItem(21, vitre);
                inv.setItem(23, vitre);
                inv.setItem(24, vitre);
                inv.setItem(25, vitre);
                inv.setItem(26, vitre);

                ItemStack biv = new ItemStack(Material.BARRIER, 1);
                ItemMeta mbiv = biv.getItemMeta();
                mbiv.setDisplayName("§4Ban map");
                biv.setItemMeta(mbiv);
                inv.setItem(13, biv);

                ItemStack fermer = new ItemStack(Material.RED_TERRACOTTA);
                ItemMeta fm = fermer.getItemMeta();
                fm.setDisplayName("§4Fermer");
                fermer.setItemMeta(fm);
                inv.setItem(22, fermer);

                p.openInventory(inv);

            }

            if (item.getType() == Material.RED_CONCRETE) {
                Player player = p;
                File file = new File(saveDir, player.getName() + ".json");
                Profile profile = FileUtils.openProfileFile(file, player);
                profile.setMenu(true);
                FileUtils.saveFile(file, profile);
                Material menu = Material.CLOCK;
                if (plugin.getConfig().getBoolean("resource.force")) {
                    menu = Material.SUGAR_CANE;
                }
                ItemStack item1 = new ItemStack(menu, 1);
                ItemMeta meta = item1
                        .getItemMeta();
                meta.setDisplayName("§6Menu");
                meta.setCustomModelData(1);
                item1.setItemMeta(meta);
                p.getInventory().setItem(8, item1);
                p.closeInventory();
                Bukkit.dispatchCommand(p, "menu");
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§5Item custome")) {
                ItemStack vitre = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                ItemMeta mvitre = vitre
                        .getItemMeta();
                mvitre.setDisplayName("§r§kcaca");
                vitre.setItemMeta(mvitre);
                inv.setItem(29, vitre);

                ItemStack dbs = new ItemStack(Material.DEBUG_STICK, 1);
                ItemMeta mdbs = dbs.getItemMeta();
                mdbs.setDisplayName("§5Debug Stick");
                dbs.setItemMeta(mdbs);
                inv.setItem(10, dbs);

                ItemStack biv = new ItemStack(Material.BARRIER, 1);
                ItemMeta mbiv = biv
                        .getItemMeta();
                mbiv.setDisplayName("§4Block Invisible");
                biv.setItemMeta(mbiv);
                inv.setItem(28, biv);

                ItemStack eiv = new ItemStack(Material.STRUCTURE_VOID, 1);
                ItemMeta meiv = eiv.getItemMeta();
                meiv.setDisplayName("§9Block de vide");
                eiv.setItemMeta(meiv);
                inv.setItem(37, eiv);

                ItemStack item3 = new ItemStack(Material.POTION, 1);
                PotionMeta meta3 = (PotionMeta) item3
                        .getItemMeta();
                meta3.setColor(Color.RED);
                meta3.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1), true);
                meta3.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1), true);
                meta3.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1), true);
                meta3.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 127), true);
                meta3.setDisplayName("§4RedTeam");
                item3.setItemMeta(meta3);
                inv.setItem(19, item3);

                p.updateInventory();
            }
        }

    }

}

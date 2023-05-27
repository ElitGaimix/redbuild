package elitgaimix.redteam.fr.commands;

import org.bukkit.command.*;
import org.bukkit.command.Command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.Plugin;

public class PluginManagerCommand implements CommandExecutor {

    private File saveDir;

    public PluginManagerCommand(Plugin plugin) {
        this.saveDir = new File(plugin.getDataFolder(), plugin.getConfig().getString("repertory.player"));
        if (!saveDir.exists()) {
            this.saveDir.mkdir();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            File file = new File(saveDir, player.getName() + ".json");
            Profile profile = FileUtils.openProfileFile(file, player);

            Inventory inv = Bukkit.createInventory(null, 54, "§4Menu RedTeam");

            ItemStack warp = new ItemStack(Material.COMPASS, 1);
            ItemMeta mwarp = warp.getItemMeta();
            mwarp.setDisplayName("§2Warp");
            warp.setItemMeta(mwarp);
            inv.setItem(32, warp);

            ItemStack plot = new ItemStack(Material.NAME_TAG);
            ItemMeta mplot = plot.getItemMeta();
            mplot.setDisplayName("§4Plot");
            plot.setItemMeta(mplot);
            inv.setItem(13, plot);

            ItemStack parametre = new ItemStack(Material.TRIPWIRE_HOOK, 1);
            ItemMeta mparametre = parametre.getItemMeta();
            mparametre.setDisplayName("§8Parametre");
            parametre.setItemMeta(mparametre);
            inv.setItem(45, parametre);

            // RedTeam
            ItemStack item4 = new ItemStack(Material.REDSTONE, 1);
            ItemMeta meta4 = item4.getItemMeta();
            meta4.setDisplayName("§4RedTeam");
            item4.setItemMeta(meta4);
            inv.setItem(22, item4);

            // Item custom
            ItemStack item5 = new ItemStack(Material.DEBUG_STICK, 1);
            ItemMeta meta5 = item5.getItemMeta();
            meta5.setDisplayName("§5Item custome");
            item5.setItemMeta(meta5);
            inv.setItem(30, item5);
            // /give @p
            // minecraft:potion{CustomPotionEffects:[{Id:5,Amplifier:126,Duration:2147483647,ShowParticles:0b},{Id:14,Duration:2147483647,ShowParticles:0b},{Id:16,Duration:2147483647,ShowParticles:0b},{Id:24,Duration:2147483647,ShowParticles:0b}],CustomPotionColor:14024706,display:{Name:"\"RedTeam\"",Lore:["Potion
            // de la RedTeam"]}}
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta sm = (SkullMeta) skull.getItemMeta();
            sm.setOwningPlayer(Bukkit.getOfflinePlayer("La_perle"));
            sm.setDisplayName("§4La_perle");
            skull.setItemMeta(sm);
            inv.setItem(23, skull);

            ItemStack skull1 = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta sm1 = (SkullMeta) skull1.getItemMeta();
            sm1.setOwningPlayer(player);
            sm1.setDisplayName("§6Statistiques");
            skull1.setItemMeta(sm1);
            inv.setItem(31, skull1);

            ItemStack skull2 = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta sm2 = (SkullMeta) skull2.getItemMeta();
            sm2.setOwningPlayer(Bukkit.getOfflinePlayer("ElitGaimix"));
            sm2.setDisplayName("§4ElitGaimix");
            skull2.setItemMeta(sm2);
            inv.setItem(21, skull2);

            ItemStack fermer = new ItemStack(Material.RED_TERRACOTTA);
            ItemMeta fm = fermer.getItemMeta();
            fm.setDisplayName("§4Fermer");
            fermer.setItemMeta(fm);
            inv.setItem(49, fermer);

            if (profile.getMenu() == true) {
                ItemStack menu = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta mm = menu.getItemMeta();
                mm.setDisplayName("§2Item Menu: Activer");
                menu.setItemMeta(mm);
                inv.setItem(53, menu);
            } else {

                ItemStack menu = new ItemStack(Material.RED_CONCRETE);
                ItemMeta mm = menu.getItemMeta();
                mm.setDisplayName("§4Item Menu: Desactiver");
                menu.setItemMeta(mm);
                inv.setItem(53, menu);
            }

            // Vitre noir
            ItemStack item1 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
            ItemMeta meta1 = item1.getItemMeta();
            meta1.setDisplayName("§r§kcaca");
            item1.setItemMeta(meta1);
            if (!player.isOp()) {
                ItemStack aide = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta maide = aide.getItemMeta();
                maide.setDisplayName("§6Aide");
                aide.setItemMeta(maide);
                inv.setItem(8, aide);
                inv.setItem(0, item1);
            } else {
                ItemStack admin = new ItemStack(Material.COMMAND_BLOCK, 1);
                ItemMeta madmin = admin.getItemMeta();
                madmin.setDisplayName("§4Menu admin");
                admin.setItemMeta(madmin);
                inv.setItem(0, admin);
                inv.setItem(8, item1);
            }
            inv.setItem(1, item1);
            inv.setItem(2, item1);
            inv.setItem(3, item1);
            inv.setItem(4, item1);
            inv.setItem(5, item1);
            inv.setItem(6, item1);
            inv.setItem(7, item1);
            inv.setItem(9, item1);
            inv.setItem(17, item1);
            inv.setItem(18, item1);
            inv.setItem(26, item1);
            inv.setItem(27, item1);
            inv.setItem(35, item1);
            inv.setItem(36, item1);
            inv.setItem(44, item1);
            inv.setItem(46, item1);
            inv.setItem(47, item1);
            inv.setItem(48, item1);
            inv.setItem(50, item1);
            inv.setItem(51, item1);
            inv.setItem(52, item1);

            player.openInventory(inv);
            return true;

            /*
             * ItemStack item3 = new ItemStack(Material.POTION, 1);
             * PotionMeta meta3 = (PotionMeta) item3.getItemMeta();
             * meta3.setColor(Color.RED);
             * meta3.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
             * 1000000, 1), true);
             * meta3.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1),
             * true);
             * meta3.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
             * 1000000, 1), true);
             * meta3.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
             * 1000000, 127), true);
             * meta3.setDisplayName("§8§4RedTeam");
             * item3.setItemMeta(meta3);
             * inv.setItem(3, item3);
             */

        }
        return true;
    }
}

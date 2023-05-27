package elitgaimix.redteam.fr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class invsee implements CommandExecutor, Listener {

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                try {
                    Player cible = Bukkit.getServer().getPlayer(args[0]);
                    Inventory inv = cible.getInventory();
                    Inventory ninv = Bukkit.createInventory(null, 45, cible.getName());

                    ninv.setContents(inv.getContents());
                    p.openInventory(ninv);

                } catch (Exception e) {
                    p.sendMessage("§4Le joueur " + args[0] + " n'existe pas ou n'est pas connecter sur le serveur.");
                    p.sendMessage(e.getMessage());
                }
            } else {
                p.sendMessage("§4Command: /invsee [joueur]");
            }

        }
        return false;
    }
}

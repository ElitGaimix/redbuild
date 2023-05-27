package elitgaimix.redteam.fr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import elitgaimix.redteam.fr.Plugin;

public class tp implements CommandExecutor {
    private Plugin plugin;

    public tp(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("tphere")) {
                try {
                    Player cible = Bukkit.getServer().getPlayer(args[0]);
                    if (cible != null) {
                        p.sendMessage("§6Le joueur " + cible.getName() + " a été téléporter sur vous.");
                        cible.teleport(p.getLocation());
                    }
                } catch (Exception e) {
                    p.sendMessage(
                            "§4Le joueur " + args[0] + " n'existe pas ou n'est pas connecter sur le serveur.");
                    plugin.getLogger().warning(e.getMessage());
                }
            } else if (cmd.getName().equalsIgnoreCase("tp")) {
                if (args.length == 1) {
                    try {
                        Player cible = Bukkit.getServer().getPlayer(args[0]);
                        if (cible != null) {
                            p.sendMessage("§6Vous vous tétéportez vers " + cible.getName() + ".");
                            p.teleport(cible);
                        }
                    } catch (Exception e) {
                        p.sendMessage(
                                "§4Le joueur " + args[0] + " n'existe pas ou n'est pas connecter sur le serveur.");
                        plugin.getLogger().warning(e.getMessage());
                    }
                } else if (args.length >= 3) {
                    try {
                        p.teleport(new Location(p.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                                Integer.parseInt(args[2])));
                        p.sendMessage("§6Téléportation réussite.");
                    } catch (Exception e) {
                        p.sendMessage("§4Les coordonner son invalide");
                    }
                } else {
                    p.sendMessage("§4command: /tp [joueur/x] [y] [z]");
                }

            }
        }
        return false;
    }
}

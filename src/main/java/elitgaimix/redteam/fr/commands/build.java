package elitgaimix.redteam.fr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import elitgaimix.redteam.fr.Plugin;

public class build implements CommandExecutor {
    private Plugin plugin;

    public build(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                try {
                    Player s = p;
                    p = Bukkit.getServer().getPlayer(args[0]);
                    if (p.hasPermission("redteam.build")) {
                        s.sendMessage("§aLe joueur " + args[0] + " n'a plus la permission de build");
                    } else {
                        s.sendMessage("§aLe joueur " + args[0] + " a maintenant la permission de build");
                    }

                } catch (Exception e) {
                    p.sendMessage("§4Ce joueur n'existe pas");
                    return true;
                }
            }
            if (p.hasPermission("redteam.build")) {
                p.addAttachment(plugin, "redteam.build", false);
                p.sendMessage("§cVous ne pouvez plus build");
            } else {
                p.addAttachment(plugin, "redteam.build", true);
                p.sendMessage("§aVous pouvez maintenant build");
            }
        }
        return false;
    }
}

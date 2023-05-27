package elitgaimix.redteam.fr.commands;

import java.nio.charset.StandardCharsets;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class unicode implements CommandExecutor {

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String uni = args[0];
            byte[] charset = uni.getBytes(StandardCharsets.UTF_8);
            String result = new String(charset, StandardCharsets.UTF_8);
            Inventory inv = Bukkit.createInventory(null, 54, result);
            p.openInventory(inv);
        }
        return true;
    }
}

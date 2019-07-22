package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;
import ws.maze.paidprotection.objects.PaidRegion;

public class CancelCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        Player player = (Player) sender;
        if(args.length > 1) return false;

        if(plugin.regions.get(player.getUniqueId()) != null) {
            PaidRegion reg = plugin.regions.get(player.getUniqueId());
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "Canceled creation of region  " + ChatColor.BLUE + reg.getName() + ChatColor.GREEN + ".");
            plugin.regions.remove(player.getUniqueId());
        }else{
            player.sendMessage(plugin.prefix + ChatColor.RED + "You must create a region first using" +
                    ChatColor.GOLD + ChatColor.ITALIC + " /paidprotection create <name>" + ChatColor.RED + ".");
        }

        return true;
    }
}

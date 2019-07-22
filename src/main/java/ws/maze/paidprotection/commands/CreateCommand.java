package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;
import ws.maze.paidprotection.objects.PaidRegion;

public class CreateCommand implements CommandInterface{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        Player player = (Player) sender;
        if(args.length > 2) return false;
        if(args.length == 1) {
            player.sendMessage(plugin.prefix + ChatColor.RED + "You must specify a name.");
            return false;
        }

        if(plugin.regions.get(player.getUniqueId()) == null) plugin.regions.put(player.getUniqueId(), new PaidRegion(args[1]));
        else plugin.regions.replace(player.getUniqueId(), new PaidRegion(args[1]));

        player.sendMessage(plugin.prefix + ChatColor.GREEN + "Created region " + ChatColor.BLUE + args[1] + ChatColor.GREEN + ".");
        player.sendMessage(plugin.prefix + ChatColor.GREEN + "Add corners using" + ChatColor.GOLD + ChatColor.ITALIC + " /paidprotection addcorner");

        return true;
    }
}

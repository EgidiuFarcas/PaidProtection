package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;
import ws.maze.paidprotection.objects.PaidRegion;

public class ViewCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        Player player = (Player) sender;
        if(args.length > 1) return false;

        if(plugin.regions.get(player.getUniqueId()) != null) {
            PaidRegion reg = plugin.regions.get(player.getUniqueId());
            if(reg.getCornerCount() != 0){
                reg.viewRegion(player, plugin);
            }else{
                player.sendMessage(plugin.prefix + ChatColor.RED + "You have to add at least one corner using" +
                        ChatColor.GOLD + ChatColor.ITALIC + " /paidprotection addcorner" + ChatColor.RED + ".");
            }
        }else{
            player.sendMessage(plugin.prefix + ChatColor.RED + "You must create a region first using" +
                    ChatColor.GOLD + ChatColor.ITALIC + " /paidprotection create <name>" + ChatColor.RED + ".");
        }
        return true;
    }
}

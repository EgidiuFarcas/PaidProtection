package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;

public class HelpCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {

        Player player = (Player) sender;

        //We don't have to check if the args length is equal to one, but you will have to check if it is greater than 1.
        if(args.length > 1) return false;

        player.sendMessage(ChatColor.GRAY + "===============" + plugin.prefix + ChatColor.WHITE + "HELP" + ChatColor.GRAY + "===============");
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "Aliases" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /pp" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection c1 " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Sets the first corner of the region" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection c2 " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Sets the second corner of the region" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection buy " + ChatColor.AQUA + "<region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Shows region value and asks for confirmation." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection confirm " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Confirms region protection purchase." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection cancel " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Cancels region protection purchase." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection delete" + ChatColor.AQUA + " <region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Removes protection and gives 85% of value back to you." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection value" + ChatColor.AQUA + " <region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Shows how much you would get back if you delete the region." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection view" + ChatColor.AQUA + " <region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Shows a border around specified region." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection hide " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Hides the border." );
        if(player.hasPermission("paidprotection.reload")) {
            player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + ChatColor.ITALIC + "/paidprotection reload " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Reloads the plugin.");
        }
        return true;
    }
}

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

        player.sendMessage(ChatColor.GRAY + "=============== " + plugin.prefix + ChatColor.WHITE + "HELP" + ChatColor.GRAY + " ===============");
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "Aliases" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /pp" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection create " + ChatColor.AQUA + "<region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Creates an empty region" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection select " + ChatColor.AQUA + "<region_name> " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Selects an existing region" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection addcorner " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Adds a corner to a selected region" );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection cancel " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Cancels region creation process." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection view " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Shows region." );
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + "/paidprotection hide " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Hides region." );
//        if(player.hasPermission("paidprotection.reload")) {
//            player.sendMessage(ChatColor.GRAY + "> " + ChatColor.GOLD + ChatColor.ITALIC + "/paidprotection reload " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Reloads the plugin.");
//        }
        return true;
    }
}

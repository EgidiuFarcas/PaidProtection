package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;

public class BaseCommand implements CommandInterface {

    //The command should be automatically created.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        //Because in our CommandHandler we are already checking if the sender's instance is a player, we don't have to do it here.
        Player p = (Player) sender;
        //For the purpose of this tutorial I am just sending the player a message.
        p.sendMessage(plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
        return false;
    }
}

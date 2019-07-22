package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;

public class HideCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        Player player = (Player) sender;
        if(args.length > 1) return false;

        if(plugin.regions.get(player.getUniqueId()) != null) {
            plugin.regions.get(player.getUniqueId()).hideRegion();
        }else{
            player.sendMessage(plugin.prefix + ChatColor.RED + "You are not viewing a region. Nothing to hide.");
        }
        return true;
    }
}

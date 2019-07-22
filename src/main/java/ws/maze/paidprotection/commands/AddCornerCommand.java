package ws.maze.paidprotection.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;
import ws.maze.paidprotection.objects.PaidRegion;

import java.util.HashMap;
import java.util.Map;

public class AddCornerCommand implements CommandInterface{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin) {
        Player player = (Player) sender;

        //We don't have to check if the args length is equal to one, but you will have to check if it is greater than 1.
        if(args.length > 1) return false;

        PaidRegion reg = plugin.regions.get(player.getUniqueId());
        if(reg == null){
            player.sendMessage(plugin.prefix + ChatColor.RED + "You must create a region first using" + ChatColor.GOLD +
                    ChatColor.ITALIC + " /paidprotection create <name>" + ChatColor.RED + ".");
            return true;
        }

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        if (!set.getRegions().isEmpty()) {
            player.sendMessage(plugin.prefix + ChatColor.RED + "This location is already part of a protection, try another one.");
            return true;
        }

        int x = ((int) player.getLocation().getX()) + Integer.signum((int) player.getLocation().getX());
        int z = ((int) player.getLocation().getZ()) + Integer.signum((int) player.getLocation().getZ());
        Location new_loc = new Location(player.getWorld(), x, 75, z);

        reg.addCorner(new_loc);
        plugin.regions.replace(player.getUniqueId(), reg);

        player.sendMessage(plugin.prefix + ChatColor.GREEN + "Added corner at: " + ChatColor.GOLD +
                new_loc.getX() + ChatColor.GREEN + ", " + ChatColor.GOLD + new_loc.getZ());

        if (reg.getCornerCount() == 2){
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "You added the minimum amount of corners you can use "
                    + ChatColor.GOLD + ChatColor.ITALIC + "/paidprotection buy" + ChatColor.GREEN +
                    " to finish creation and buy the region or you can continue adding corners.");
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "You can also cancel creation and start over using "
                    + ChatColor.GOLD + ChatColor.ITALIC + "/paidprotection cancel");
        }

        return true;
    }

}

package ws.maze.paidprotection.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ws.maze.paidprotection.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ConstructCommand implements CommandExecutor {

    private Main plugin;


    private Map<UUID, Map<Integer, Location>> corners = new HashMap<>();
    private Map<UUID, String> region_name = new HashMap<>();
    private Map<UUID, Integer> region_price = new HashMap<>();
    private Map<UUID, BukkitTask> region_display = new HashMap<>();

    public ConstructCommand(Main plugin){
        this.plugin = plugin;
        this.plugin.getCommand("paidprotection").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(plugin.prefix + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("paidprotection")) {
            if (args.length == 0) { //Sender only typed '/hello' and nothing else
                player.sendMessage(plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                        ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
                return true;
            }else if(args.length == 1 ) {
                if (args[0].equalsIgnoreCase("c1")) {

                    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                    ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
                    if (!set.getRegions().isEmpty()) {
                        player.sendMessage(plugin.prefix + ChatColor.RED + "This location is already part of a protection, try another one.");
                        return true;
                    }

                    if (corners.get(player.getUniqueId()) != null) corners.get(player.getUniqueId()).clear();

                    if(region_name.get(player.getUniqueId()) != null) region_name.remove(player.getUniqueId());

                    Map<Integer, Location> corner = new HashMap<>();
                    int x = ((int) player.getLocation().getX()) + Integer.signum((int) player.getLocation().getX());
                    int z = ((int) player.getLocation().getZ()) + Integer.signum((int) player.getLocation().getZ());
                    Location new_loc = new Location(player.getWorld(), x, 0, z);
                    corner.put(1, new_loc);
                    corners.put(player.getUniqueId(), corner);

                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "Set first corner at: " + ChatColor.GOLD +
                            new_loc.getX() + ChatColor.GREEN + ", " + ChatColor.GOLD + new_loc.getZ());

                    return true;
                }

                else if (args[0].equalsIgnoreCase("c2")) {

                    if (corners.get(player.getUniqueId()) == null || corners.get(player.getUniqueId()).isEmpty()) {
                        player.sendMessage(plugin.prefix + ChatColor.RED + "You must set the corner1 first." + ChatColor.RED + ChatColor.ITALIC + " Use /paidprotection c1 .");
                        return true;
                    }

                    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                    ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
                    if (!set.getRegions().isEmpty()) {
                        player.sendMessage(plugin.prefix + ChatColor.RED + "This location is already part of a protection, try another one.");
                        return true;
                    }

                    if(region_name.get(player.getUniqueId()) != null) region_name.remove(player.getUniqueId());

                    int x = ((int) player.getLocation().getX()) + Integer.signum((int) player.getLocation().getX());
                    int z = ((int) player.getLocation().getZ()) + Integer.signum((int) player.getLocation().getZ());

                    BlockVector3 min = BlockVector3.at(corners.get(player.getUniqueId()).get(1).getX(), 0, corners.get(player.getUniqueId()).get(1).getZ());
                    BlockVector3 max = BlockVector3.at(x, 256, z);
                    ProtectedRegion test = new ProtectedCuboidRegion("dummy", min, max);
                    RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                    ApplicableRegionSet overlaps = regions.getApplicableRegions(test);

                    if (!overlaps.getRegions().isEmpty()) {
                        player.sendMessage(plugin.prefix + ChatColor.RED + "The selected region overlaps one or more regions.");
                        return true;
                    }

                    Location new_loc = new Location(player.getWorld(), x, 0, z);

                    if (corners.get(player.getUniqueId()).get(2) != null)
                        corners.get(player.getUniqueId()).replace(2, new_loc);
                    else corners.get(player.getUniqueId()).put(2, new_loc);

                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "Set second corner at: " + ChatColor.GOLD +
                            new_loc.getX() + ChatColor.GREEN + ", " + ChatColor.GOLD + new_loc.getZ());

                    Logger log = Bukkit.getLogger();
                    log.info(corners.toString());

                    return true;
                }

                else if (args[0].equalsIgnoreCase("confirm")) {

                    if(region_name.get(player.getUniqueId()) == null || region_name.get(player.getUniqueId()).isEmpty()){
                        player.sendMessage(plugin.prefix + ChatColor.BLUE + "There is nothing to confirm");
                        return true;
                    }

                    String reg_name = region_name.get(player.getUniqueId());

                    if(createProtectedRegion(corners.get(player.getUniqueId()), region_name.get(player.getUniqueId()), region_price.get(player.getUniqueId()), player)){
                        corners.get(player.getUniqueId()).clear();
                        player.sendMessage(plugin.prefix + ChatColor.GREEN + "You are now the owner of this land. View your land with "
                                + ChatColor.GOLD + "/paidprotection view " + reg_name + ChatColor.GREEN + " while you are sitting in it.");
                    }
                    return true;
                }

                else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
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

                else if (args[0].equalsIgnoreCase("cancel")) {
                    if(region_name.get(player.getUniqueId()) == null || region_name.get(player.getUniqueId()).isEmpty()){
                        player.sendMessage(plugin.prefix + ChatColor.BLUE + "There is nothing to cancel");
                        return true;
                    }

                    corners.get(player.getUniqueId()).clear();
                    player.sendMessage(plugin.prefix + ChatColor.BLUE + "You have cancelled the protection purchase.");
                    return true;
                }

                else if (args[0].equalsIgnoreCase("reload")) {
                    if(player.hasPermission("paidprotection.reload")){
                        plugin.reloadConfig();
                        player.sendMessage(plugin.prefix + ChatColor.GREEN + "Configuration reloaded.");
                    }else{
                        player.sendMessage(plugin.prefix + ChatColor.RED + "You don't have permission to use this command.");
                    }
                    return true;
                }

                else if(args[0].equalsIgnoreCase("hide")){
                    stopViewing(player);
                    return true;
                }

                else if(args[0].equalsIgnoreCase("buy")) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "You must specify a name.");
                    return true;
                }

                else {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                            ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
                    return true;
                }
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("buy")) {
                    UUID player_uuid = player.getUniqueId();

                    if (corners.get(player_uuid) == null || corners.get(player_uuid).get(2) == null || corners.get(player_uuid).isEmpty()){
                        player.sendMessage(plugin.prefix + ChatColor.RED + "You must select a region first.");
                        return true;
                    }
                    RegionManager container = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                    if(container.hasRegion(player.getName()+"_"+args[1])){
                        player.sendMessage(plugin.prefix + ChatColor.RED + "Region name is already use.");
                        return true;
                    }

                    region_name.put(player_uuid, args[1]);

                    int value = compute_value(player);
                    int area = compute_area(player);

                    if(region_price.get(player_uuid) != null) region_price.replace(player_uuid, value);
                    else region_price.put(player_uuid, value);

                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "The selected region " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " with an area of " + ChatColor.GOLD +
                            area + ChatColor.GREEN + " blocks has the price of " + ChatColor.GOLD + value + "$ \n" +
                            plugin.prefix + ChatColor.GREEN + "Use " + ChatColor.GOLD + "/paidprotection confirm" +
                            ChatColor.GREEN + " to buy the land or " + ChatColor.GOLD + "/paidprotection cancel" + ChatColor.GREEN + " to cancel.");

                    return true;
                }

                else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")){
                    RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                    if(regions.hasRegion(player.getName()+"_"+args[1])){
                        ProtectedRegion region = regions.getRegion(player.getName()+"_"+args[1]);
                        if(region.isOwner(localPlayer)){
                            int value = (compute_value(region) * 85)/100;
                            regions.removeRegion(player.getName()+"_"+args[1], RemovalStrategy.REMOVE_CHILDREN);
                            plugin.getEconomy().depositPlayer(player, value);
                            player.sendMessage(plugin.prefix + ChatColor.GREEN + "Region successfully removed.");
                            player.sendMessage(plugin.prefix + ChatColor.GREEN + "You got " + ChatColor.GOLD + value + "$" + ChatColor.GREEN + " back.");
                            return true;
                        }else{
                            player.sendMessage(plugin.prefix + ChatColor.RED + "You are not the owner of that region.");
                            return true;
                        }
                    }else{
                        player.sendMessage(plugin.prefix + ChatColor.RED + "The region does not exist.");
                        return true;
                    }
                }

                else if(args[0].equalsIgnoreCase("value")){
                    RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                    if(regions.hasRegion(player.getName()+"_"+args[1])){
                        ProtectedRegion region = regions.getRegion(player.getName()+"_"+args[1]);
                        if(region.isOwner(localPlayer)){
                            int value = (compute_value(region) * 85)/100;
                            player.sendMessage(plugin.prefix + ChatColor.GREEN + "If you delete the region you will get  " + ChatColor.GOLD + value + "$" + ChatColor.GREEN + " back.");
                            return true;
                        }else{
                            player.sendMessage(plugin.prefix + ChatColor.RED + "You are not the owner of that region.");
                            return true;
                        }
                    }else{
                        player.sendMessage(plugin.prefix + ChatColor.RED + "The region does not exist.");
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("view")) {
                    RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                    if(regions.hasRegion(player.getName()+"_"+args[1])){
                        ProtectedRegion region = regions.getRegion(player.getName()+"_"+args[1]);
                        if(region.isOwner(localPlayer)){
                            showArea(player, region.getMinimumPoint(), region.getMaximumPoint());
                            return true;
                        }else{
                            player.sendMessage(plugin.prefix + ChatColor.RED + "You are not the owner of that region.");
                            return true;
                        }
                    }else{
                        player.sendMessage(plugin.prefix + ChatColor.RED + "The region does not exist.");
                        return true;
                    }
                }

                else {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                            ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
                    return true;
                }
            }else{
                player.sendMessage(plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                        ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
                return true;
            }
        }

        return false;
    }

    private void stopViewing(Player player){
        if(region_display.get(player.getUniqueId()) != null){
            region_display.get(player.getUniqueId()).cancel();
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "Stopped area visualisation.");
        }else{
            player.sendMessage(plugin.prefix + ChatColor.YELLOW + "There is no visualisation running.");
        }
    }

    private void showArea( final Player p, BlockVector3 min, BlockVector3 max) {
        BukkitTask task = new BukkitRunnable() {
            double alpha = 0;
            double beta = 0;
            double y = 0;
            boolean found_y = false;

            public void run() {
                // Each cycle alpha gets increase by pi / 16 which divides the whole circle into 32 sections
                for(double i = ((!found_y) ? 0 : y); i < ((!found_y) ? 1 : y + 4); i+=1){
                    for(alpha = 0; min.getBlockX() + alpha <= max.getBlockX()+1; alpha++){
                        beta = 0;
                        double x1 = (double) min.getBlockX() + alpha;
                        double z1 = (double) min.getBlockZ() + beta;
                        double y1 = p.getWorld().getHighestBlockYAt((int) x1, (int) z1)+1;
                        Location firstLocation = new Location(p.getWorld(), x1, (!found_y) ? y1 : i, z1);

                        p.spawnParticle( Particle.FLAME, firstLocation, 0, 0, 0, 0, 0 );

                        if(!found_y){
                            y = Math.max(y1, y);
                        }

                        beta = max.getBlockZ() - min.getBlockZ() + 1;
                        x1 = (double) min.getBlockX() + alpha;
                        z1 = (double) min.getBlockZ() + beta;
                        y1 = p.getWorld().getHighestBlockYAt((int) x1, (int) z1)+1;
                        firstLocation = new Location(p.getWorld(), x1, (!found_y) ? y1 : i, z1);

                        p.spawnParticle( Particle.FLAME, firstLocation, 0, 0, 0, 0, 0 );

                        if(!found_y){
                            y = Math.max(y1, y);
                        }
                    }
                    for(beta = 0; min.getBlockZ() + beta <= max.getBlockZ()+1; beta++){
                        alpha = 0;
                        double x1 = (double) min.getBlockX() + alpha;
                        double z1 = (double) min.getBlockZ() + beta;
                        double y1 = p.getWorld().getHighestBlockYAt((int) x1, (int) z1)+1;
                        Location firstLocation = new Location(p.getWorld(), x1, (!found_y) ? y1 : i, z1);

                        p.spawnParticle( Particle.FLAME, firstLocation, 0, 0, 0, 0, 0 );

                        if(!found_y){
                            y = Math.max(y1, y);
                        }

                        alpha = max.getBlockX() - min.getBlockX() + 1;
                        x1 = (double) min.getBlockX() + alpha;
                        z1 = (double) min.getBlockZ() + beta;
                        y1 = p.getWorld().getHighestBlockYAt((int) x1, (int) z1)+1;
                        firstLocation = new Location(p.getWorld(), x1, (!found_y) ? y1 : i, z1);

                        p.spawnParticle( Particle.FLAME, firstLocation, 0, 0, 0, 0, 0 );

                        if(!found_y){
                            y = Math.max(y1, y);
                        }
                    }
                }
                found_y = true;
            }
        }.runTaskTimer( this.plugin, 0, 2);

        if(region_display.get(p.getUniqueId()) != null){
            region_display.get(p.getUniqueId()).cancel();
            region_display.replace(p.getUniqueId(), task);
        }else region_display.put(p.getUniqueId(), task);
    }

    private int getSign(double coord1, double coord2){
        if(coord1 > coord2) return -1;
        else return 1;
    }

    private boolean createProtectedRegion(Map<Integer, Location> corner, String name, Integer price, Player owner){
        if(!plugin.getEconomy().has(owner, price)){
            corners.get(owner.getUniqueId()).clear();
            owner.sendMessage(plugin.prefix + ChatColor.RED + "You can't afford this region.");
            owner.sendMessage(plugin.prefix + ChatColor.RED + "Purchase cancelled.");
            return false;
        }
        World world = BukkitAdapter.adapt(owner.getWorld());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);

        BlockVector3 min = BlockVector3.at(corner.get(1).getX(), 0, corner.get(1).getZ());
        BlockVector3 max = BlockVector3.at(corner.get(2).getX(), 256, corner.get(2).getZ());

        ProtectedCuboidRegion reg = new ProtectedCuboidRegion(owner.getName() + "_" + name, min, max);

        DefaultDomain owners = reg.getOwners();
        owners.addPlayer(owner.getUniqueId());
        reg.setOwners(owners);

        reg.setFlag(Flags.GREET_MESSAGE, ChatColor.AQUA + "You entered " + ChatColor.YELLOW + owner.getDisplayName() + ChatColor.AQUA +"'s region.");
        reg.setFlag(Flags.FAREWELL_MESSAGE, ChatColor.GREEN + "You are now in wilderness");
        reg.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);
        reg.setFlag(Flags.MOB_DAMAGE, StateFlag.State.DENY);
        reg.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        reg.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        reg.setFlag(Flags.PVP, StateFlag.State.DENY);
        reg.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
        reg.setFlag(Flags.USE, StateFlag.State.ALLOW);
        reg.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);

        regions.addRegion(reg);

        plugin.getEconomy().withdrawPlayer(owner, price);
        return true;
    }
    private int compute_value(Player player){
        int value = compute_area(player) * ((int) plugin.getConfig().get("block_value"));
        return (value == 0) ? ((int) plugin.getConfig().get("block_value")) : value;
    }

    private int compute_value(ProtectedRegion region){
        int l1 = Math.abs(Math.abs(region.getMaximumPoint().getX()) - Math.abs(region.getMinimumPoint().getX()));
        int l2 = Math.abs(Math.abs(region.getMaximumPoint().getZ()) - Math.abs(region.getMinimumPoint().getZ()));
        int value = l1 * l2 * ((int) plugin.getConfig().get("block_value"));
        return (value == 0) ? ((int) plugin.getConfig().get("block_value")) : value;
    }

    private int compute_area(Player player){
        Map<Integer, Location> cs = corners.get(player.getUniqueId());
        int l1 = (int) Math.abs(Math.abs(cs.get(1).getX()) - Math.abs(cs.get(2).getX()));
        int l2 = (int) Math.abs(Math.abs(cs.get(1).getZ()) - Math.abs(cs.get(2).getZ()));
        return l1 * l2;
    }
}

package ws.maze.paidprotection.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ws.maze.paidprotection.Main;

import java.util.ArrayList;

public class PaidRegion {
    public static int TYPE_CUBOID = 0;
    public static int TYPE_POLYGON = 0;

    private ProtectedRegion wg_reg;

    private ArrayList<Location> corners = new ArrayList<>();
    private int value;
    private int area;
    private int type = 0; //0 - Cuboid | 1 - Polygon
    private boolean is_shown = false;
    private BukkitTask view_task;
    private String name;

    public PaidRegion(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void createRegion(Player player){
        //TODO Create Region
    }

    public void hideRegion(){
        if(this.view_task != null) this.view_task.cancel();
        this.is_shown = false;
    }

    public void viewRegion(Player player, Main plugin){
        //Temp Corner View
        this.view_task = new BukkitRunnable() {

            public void run() {
                for (Location loc : corners) {
                    player.spawnParticle(Particle.EXPLOSION_LARGE, loc, 0, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer( plugin, 0, 2);
        this.is_shown = true;
        //TODO View Region Walls
    }

    public boolean isRegionShown(){
        return this.is_shown;
    }

    public void addCorner(Location location){
        this.corners.add(location);
        if(this.corners.size() > 2) this.type = 1;
    }

    public int getCornerCount(){
        return this.corners.size();
    }

    public int getValue(Main plugin){
        this.value = this.getArea() * ((int) plugin.getConfig().get("block_value"));
        return (this.value == 0) ? ((int) plugin.getConfig().get("block_value")) : this.value;
    }

    public int getArea(){
        return (this.type == PaidRegion.TYPE_CUBOID) ? getCuboidArea() : getPolygonArea();
    }

    private int getCuboidArea(){
        //TODO Compute cuboid area
        return this.area;
    }

    private int getPolygonArea(){
        //TODO Compute Polygon area
        return this.area;
    }

}

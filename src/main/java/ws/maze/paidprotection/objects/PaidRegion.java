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

    private ProtectedRegion wg_reg;

    private ArrayList<Location> corners = new ArrayList<>();
    private int value;
    private int area;
    private int type = 0; //0 - Cuboid | 1 - Polygon
    private boolean editing = true;
    private BukkitTask view_task;

    public void createRegion(Player player){
        //TODO Create Region
    }

    public void hideRegion(){
        if(this.view_task != null) this.view_task.cancel();
    }

    public void viewRegion(Player player, Main plugin){
        this.view_task = new BukkitRunnable() {

            public void run() {
                for (Location loc : corners) {
                    player.spawnParticle(Particle.EXPLOSION_LARGE, loc, 0, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer( plugin, 0, 2);
        //TODO View Region
    }

    public void addCorner(Location location){
        if(!this.editing) return;
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
        //TODO Compute Area
        return this.area;
    }

}

package ws.maze.paidprotection;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ws.maze.paidprotection.commands.ConstructCommand;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "Paid" + ChatColor.GOLD + "Protection" + ChatColor.GRAY + "] > ";
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;


    public void onEnable(){
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        new ConstructCommand(this);

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

}

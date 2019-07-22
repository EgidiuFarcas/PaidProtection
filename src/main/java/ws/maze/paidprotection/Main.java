package ws.maze.paidprotection;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ws.maze.paidprotection.commands.*;
import ws.maze.paidprotection.objects.PaidRegion;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "Paid" + ChatColor.GOLD + "Protection" + ChatColor.GRAY + "] > ";
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    public HashMap<UUID, PaidRegion> regions = new HashMap<>();

    public void registerCommands() {
        CommandHandler handler = new CommandHandler(this);

        //Registers the command /paidprotection which has no arguments.
        handler.register("paidprotection", new BaseCommand());

        //Registers the command /paidprotection args based on args[0] (args)
        handler.register("create", new CreateCommand());
        handler.register("addcorner", new AddCornerCommand());
        handler.register("view", new ViewCommand());
        handler.register("hide", new HideCommand());
        handler.register("help", new HelpCommand());
        handler.register("?", new HelpCommand());

        getCommand("paidprotection").setExecutor(handler);
    }

    public void onEnable(){
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        new ConstructCommand(this);
//        this.registerCommands();
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

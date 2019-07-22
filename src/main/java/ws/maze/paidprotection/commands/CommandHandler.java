package ws.maze.paidprotection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ws.maze.paidprotection.Main;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    //This is where we will store the commands
    private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();
    private Main plugin;

    public CommandHandler(Main plugin){
        this.plugin = plugin;
    }

    //Register method.
    public void register(String name, CommandInterface cmd) {
        commands.put(name, cmd);
    }

    //This will be used to check if a string exists or not.
    public boolean exists(String name) {
        return commands.containsKey(name);
    }

    //Getter method for the Executor.
    public CommandInterface getExecutor(String name) {
        return commands.get(name);
    }

    //Template. All commands will have this in common.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {
                getExecutor("paidprotection").onCommand(sender, cmd, commandLabel, args, this.plugin);
                return true;
            }
            if(args.length >= 1) {

                //If that argument exists in our registration in the onEnable();
                if(exists(args[0])){
                    //Get The executor with the name of args[0].
                    getExecutor(args[0]).onCommand(sender, cmd, commandLabel, args, this.plugin);
                    return true;
                } else {

                    //Send a message to the sender if the command doesn't exist.
                    sender.sendMessage(this.plugin.prefix + ChatColor.RED + "Invalid syntax, use " + ChatColor.RED +
                            ChatColor.ITALIC + "/paidprotection help" + ChatColor.RED + " for a list of commands.");
                    return true;
                }
            }
        } else {
            sender.sendMessage(this.plugin.prefix + ChatColor.RED + "Only players can use this command.");
            return true;
        }
        return false;
    }

}

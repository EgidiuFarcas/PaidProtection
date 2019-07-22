package ws.maze.paidprotection.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ws.maze.paidprotection.Main;

public interface CommandInterface {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, Main plugin);

}

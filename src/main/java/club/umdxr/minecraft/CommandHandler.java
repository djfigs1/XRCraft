package club.umdxr.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * A command handler processes commands.
 */
public interface CommandHandler {

    /**
     * Gets the command base name and aliases.
     *
     * return The set of the command labels that this handler processes.
     */
    String[] getCmdNames();

    /**
     * Handles the command.
     *
     * @param sender
     * @param cmd
     * @param cmdLbl
     * @param args
     * @return
     */
    boolean handleCommand(CommandSender sender, Command cmd, String cmdLbl, String[] args);

}

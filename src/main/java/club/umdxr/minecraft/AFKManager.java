package club.umdxr.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

/**
 * Manages AFK players and makes
 */
public class AFKManager implements Listener, CommandHandler {

    private HashSet<UUID> afkPlayers = new HashSet<UUID>();

    public void addPlayer(Player player) {
        afkPlayers.add(player.getUniqueId());
        player.setInvulnerable(true);
        player.setPlayerListName(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + player.getName());
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.DARK_GRAY + " is now AFK.");
    }

    public void removePlayer(Player player) {
        afkPlayers.remove(player.getUniqueId());
        player.setInvulnerable(false);
        player.setPlayerListName(player.getName());
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GRAY + " is no longer AFK.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        // If the player moves, they are no longer AFK.
        if (afkPlayers.contains(p.getUniqueId())) {
            removePlayer(p);
        }
    }

    /**
     * Removes an AFK player if they disconnected.
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        if (afkPlayers.contains(e.getPlayer().getUniqueId()))
            removePlayer(e.getPlayer());
    }

    @Override
    public String[] getCmdNames() {
        return new String[] {
            "afk"
        };
    }

    @Override
    public boolean handleCommand(CommandSender sender, Command cmd, String cmdLbl, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            // Add/remove player from AFK.
            if (afkPlayers.contains(p))
                removePlayer(p);
            else
                addPlayer(p);
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
        }

        return true;
    }
}

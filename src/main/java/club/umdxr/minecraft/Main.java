package club.umdxr.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

	static ArrayList<UUID> pvpPlayers = new ArrayList<UUID>();
	private static Main instance;
	private TimeAnnouncer announcer;
	private AFKManager afkManager;
	private WaypointManager waypointManager;

	private HashMap<String, CommandHandler> commandHandlers = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(PlayerListener.shared, this);
		announcer = new TimeAnnouncer();

		afkManager = new AFKManager();
		addCommandHandler(afkManager);
		addEventListener(afkManager);

		waypointManager = new WaypointManager();
		addCommandHandler(waypointManager);
		addEventListener(waypointManager);
	}

	@Override
	public void onDisable() {
		announcer.stop();
		commandHandlers.clear();
	}

	public static Main getInstance() {
		return instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdLbl, String[] args) {
		String lowerCaseCmdLbl = cmdLbl.toLowerCase();

		if (cmdLbl.equalsIgnoreCase("pvp")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID playerUUID = player.getUniqueId();
				if (pvpPlayers.contains(playerUUID)) {
					pvpPlayers.remove(playerUUID);
					player.setDisplayName(player.getName());
					player.sendMessage(ChatColor.GREEN + "You are no longer participating in PvP.");
					this.getLogger().log(Level.INFO, player.getName() + " disabled PvP");
				} else {
					player.setDisplayName(ChatColor.RED + player.getName() + ChatColor.RESET);
					player.sendMessage(ChatColor.RED + "You are now participating in PvP.");
					pvpPlayers.add(playerUUID);
					this.getLogger().log(Level.INFO, player.getName() + " enabled PvP");
				}
			}
		} else if (cmdLbl.equalsIgnoreCase("saypos")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				int x = player.getLocation().getBlockX();
				int y = player.getLocation().getBlockY();
				int z = player.getLocation().getBlockZ();
				Bukkit.broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.AQUA + String.format(" is at X: %d Y: %d Z: %d", x, y, z));
			}
		}

		if (commandHandlers.containsKey(lowerCaseCmdLbl))
			return commandHandlers.get(lowerCaseCmdLbl).handleCommand(sender, cmd, cmdLbl, args);

		return true;
	}

	private void addCommandHandler(CommandHandler handler) {
		for (String cmdLbl : handler.getCmdNames()) {
			commandHandlers.put(cmdLbl, handler);
		}
	}

	private void addEventListener(Listener listener) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(listener, this);
	}
}

package club.umdxr.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerListener implements Listener {

	public static PlayerListener shared = new PlayerListener();
	public static ArrayList<UUID> monsters = new ArrayList<>();

	/**
	 * Messages the player where they died.
	 *
	 * @param e The player death event.
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Location deathLocation = e.getEntity().getLocation();
		e.getEntity().sendMessage(ChatColor.RED + String.format("You died at X: %d Y: %d Z: %d", deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ()));

		if (monsters.contains(e.getEntity().getUniqueId())) {
			e.setDeathMessage(e.getEntity().getDisplayName() + " is an absolute monster for trying to attack the most innocent creature in the world");
			e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
			e.setKeepInventory(true);

			monsters.remove(e.getEntity().getUniqueId());
		}
	}

	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player victim = (Player) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player attacker = (Player) e.getDamager();
				e.setCancelled(!checkPvP(attacker, victim));
			} else if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player) {
					Player attacker = (Player) arrow.getShooter();
					e.setCancelled(!checkPvP(attacker, victim));
				}
			}
		} else if (e.getEntityType() == EntityType.FOX && e.getDamager() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			e.setCancelled(true);
			monsters.add(attacker.getUniqueId());
			attacker.setHealth(0);
		}
	}


	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		if (e.getBedEnterResult() == BedEnterResult.OK) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + e.getPlayer().getDisplayName() + ChatColor.AQUA + " has gone to bed, rise and shine!");
			e.getPlayer().getWorld().setTime(0);
			e.getPlayer().getWorld().setStorm(false);
		}
	}

	@EventHandler
	public void onPlayerSendMessage(AsyncPlayerChatEvent e) {
		e.setFormat("%s: %s");
		PluginUtil.playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_HAT, 1f);
	}

	@EventHandler
	public void onPlayerBedExit(PlayerBedLeaveEvent e) {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PluginUtil.playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f);
		e.setJoinMessage(ChatColor.GREEN + "-> " + ChatColor.YELLOW + e.getPlayer().getDisplayName() + ChatColor.GRAY + " joined the game");
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		PluginUtil.playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f);
		e.setQuitMessage(ChatColor.RED + "<- " + ChatColor.YELLOW + e.getPlayer().getDisplayName() + ChatColor.GRAY + " left the game");
	}

	private boolean checkPvP(Player attacker, Player victim) {
		if (!(Main.pvpPlayers.contains(attacker.getUniqueId()))) {
			attacker.sendMessage(ChatColor.RED + "You can't attack that player because you are not participating in PvP. To do so, use /pvp");
			return false;
		} else if (!(Main.pvpPlayers.contains(victim.getUniqueId()))) {
			attacker.sendMessage(ChatColor.RED + "You can't attack that player because they have PvP disabled.");
			return false;
		}
		return true;
	}
}

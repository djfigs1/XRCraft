package club.umdxr.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PluginUtil {

    /**
     * Plays a sound for all online players.
     *
     * @param sound The type of sound to play
     * @param pitch The pitch of the sound
     */
    public static void playSoundForAllPlayers(Sound sound, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1f, pitch);
        }
    }

}

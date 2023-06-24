package club.umdxr.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Announces the time on the hour.
 */
public class TimeAnnouncer extends TimerTask {

    private Timer timer;

    public TimeAnnouncer() {
        timer = new Timer();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR, startDate.get(Calendar.HOUR) + 1);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        // Run clock very hour.
        timer.schedule(this, startDate.getTime(), 1000 * 60 * 60 );
        Main.getInstance().getLogger().info("Scheduled time announcer!");
    }

    public void stop() {
        timer.cancel();
    }

    @Override
    public void run() {
        // Only announce the time when there are players to see it.
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Date now = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            Bukkit.broadcastMessage(ChatColor.GRAY + "The time is now " + ChatColor.YELLOW + timeFormat.format(now));
            PluginUtil.playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_PLING, 1f);
        }
    }
}

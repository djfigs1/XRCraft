package club.umdxr.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.*;

public class WaypointManager implements CommandHandler, Listener {

    private HashMap<UUID, ArrayList<Waypoint>> waypoints = new HashMap<>();
    private static String[] cmdNames = new String [] {
        "waypoint", "wp"
    };

    private static HashSet<String> subCmdNames = new HashSet<>();

    @Override
    public String[] getCmdNames() {
        return cmdNames;
    }

    @Override
    public boolean handleCommand(CommandSender sender, Command cmd, String cmdLbl, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length >= 1) {
               String subCmd = args[0];

               if (subCmd.equalsIgnoreCase("add")) {
                   addSubCmd(p, args);
               } else if (subCmd.equalsIgnoreCase("list")) {
                   listSubCmd(p, args);
               }
            } else {
                listSubCmd(p, args);
            }
        }

        return true;
    }

    private void addSubCmd(Player p, String[] args) {
        if (args.length >= 2) {
            Waypoint w = Waypoint.fromPlayerCurrentLocation(args[1], p);
            addWaypoint(p, w);
            p.sendMessage(ChatColor.GREEN + "Added the waypoint: " + w);
        }
    }

    private void removeSubCmd(Player p, String name) {

    }

    private void listSubCmd(Player p, String[] args) {
        ArrayList<Waypoint> playerWaypoints = getPlayerWaypoints(p);
        if (playerWaypoints != null) {
            for (Waypoint w : playerWaypoints) {
                p.sendMessage(ChatColor.YELLOW + w.toString());
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have any current waypoints.");
        }
    }

    private ArrayList<Waypoint> getPlayerWaypoints(Player p) {
        return waypoints.get(p.getUniqueId());
    }

    private void addWaypoint(Player p, Waypoint w) {
        UUID u = p.getUniqueId();

        // If the player is not within the waypoints dictionary, create their waypoint list.
        if (!waypoints.containsKey(u))
        {
            waypoints.put(u, new ArrayList<>());
        }

        // Add waypoint to list.
        waypoints.get(u).add(w);
    }

    @EventHandler
    private void onTabComplete(TabCompleteEvent e) {
        CommandSender s = e.getSender();
        String buffer = e.getBuffer();
        String[] args = buffer.split(" ");
//
//        if (args.length >= 1 && subCmdNames) {
//            List<String> completions = new ArrayList<String>();
//            switch (args.length) {
//                case 1:
//                    completions.add("add");
//                    completions.add("list");
//                    completions.add("remove");
//                    break;
//            }
//
//            e.setCompletions(completions);
//        }


    }

    static class Waypoint {
        String name;
        Location location;

        public static Waypoint fromPlayerCurrentLocation(String name, Player p) {
            Waypoint w = new Waypoint();
            w.name = name;
            w.location = p.getLocation();
            return w;
        }

        public String toString() {
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            return String.format("%s - X: %d Y: %d Z: %d", name, x, y, z);
        };
    }
}

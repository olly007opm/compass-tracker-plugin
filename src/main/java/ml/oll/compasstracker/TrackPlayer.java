package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class TrackPlayer implements Listener {
    public final CompassTracker plugin;
    private static final HashMap<OfflinePlayer, ArrayList<OfflinePlayer>> tracking = new HashMap<>();

    public TrackPlayer(CompassTracker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    public static void addTracking(OfflinePlayer target, OfflinePlayer player) {
        ArrayList<OfflinePlayer> trackingPlayers;
        if (tracking.get(target) != null) {
            trackingPlayers = tracking.get(target);
        } else {
            trackingPlayers = new ArrayList<>();
        }
        trackingPlayers.add(player);
        tracking.put(target, trackingPlayers);
    }

    public static void removeTracking(OfflinePlayer target, OfflinePlayer player) {
        ArrayList<OfflinePlayer> trackingPlayers = tracking.get(target);
        trackingPlayers.remove(player);
        tracking.put(target, trackingPlayers);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player target = event.getPlayer();
        if (tracking.containsKey(target) && !tracking.get(target).isEmpty()) {
            for (OfflinePlayer player : tracking.get(target)) {
                if (player.isOnline()) {
                    Player onlinePlayer = (Player) player;
                    int trackingCompassSlot = -1;
                    int taskID = -1;
                    for (ItemStack item : onlinePlayer.getInventory()) {
                        if (item != null && item.getType() != Material.AIR) {
                            NBTItem itemNbt = new NBTItem(item);
                            if (itemNbt.hasKey("tracking") && itemNbt.getString("tracking").equals(String.valueOf(target.getUniqueId()))) {
                                trackingCompassSlot = onlinePlayer.getInventory().first(item);
                            }
                            if (itemNbt.hasKey("taskid")) {
                                taskID = itemNbt.getInteger("taskid");
                            }
                        }
                    }
                    ItemStack compass = GenerateCompassItem.getTrackingCompass(target, taskID);
                    if (trackingCompassSlot != -1) {
                        onlinePlayer.getInventory().setItem(trackingCompassSlot, compass);
                    } else {
                        onlinePlayer.sendMessage(Component.text("띒 §cCompass not found"));
                    }
                }
            }
        }
    }

    public static void stopTracking(OfflinePlayer target, OfflinePlayer player) {
        TrackPlayer.removeTracking(target, player);
        if (player.isOnline()) {
            Player onlinePlayer = (Player) player;
            int trackingCompassSlot = -1;
            int taskID = -1;
            for (ItemStack item : onlinePlayer.getInventory()) {
                if (item != null && item.getType() != Material.AIR) {
                    NBTItem itemNbt = new NBTItem(item);
                    if (itemNbt.hasKey("tracking") && itemNbt.getString("tracking").equals(String.valueOf(target.getUniqueId()))) {
                        trackingCompassSlot = onlinePlayer.getInventory().first(item);
                    }
                    if (itemNbt.hasKey("taskid")) {
                        taskID = itemNbt.getInteger("taskid");
                    }
                }
            }
            ItemStack item = onlinePlayer.getInventory().getItem(trackingCompassSlot);
            if (trackingCompassSlot != -1 && item != null) {
                onlinePlayer.getInventory().remove(item);
            } else {
                onlinePlayer.sendMessage(Component.text("띒 §cCompass not found"));
            }

            if (taskID != -1) {
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }
    }
}

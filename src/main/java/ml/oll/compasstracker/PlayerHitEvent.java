package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerHitEvent implements Listener {
    public final CompassTracker plugin;

    public PlayerHitEvent(CompassTracker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player player = (Player) event.getDamager();

            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (handItem != null && handItem.getType() != null && handItem.getType() != Material.AIR) {
                NBTItem handItemNbt = new NBTItem(handItem);
                if (handItemNbt.hasKey("identifier") && handItemNbt.getString("identifier").equals("s6:tracking_compass")) {
                    if (handItemNbt.hasKey("tracking")) {
                        if (handItemNbt.getString("tracking").equals(String.valueOf(target.getUniqueId()))) {
                            player.sendMessage(Component.text("띑 §eCompass is already tracking this player"));
                        } else {
                            Entity tracked = Bukkit.getEntity(UUID.fromString(handItemNbt.getString("tracking")));
                            player.sendMessage(Component.text("띑 §eCompass is already tracking a different player (" + tracked.getName() + ")"));
                            player.sendMessage(Component.text("띑 §7Right click to stop tracking"));
                        }
                    } else {
                        player.sendMessage(Component.text("띐 §aNow tracking §2" + target.getName() + " §afor 60 seconds."));
                        player.sendMessage(Component.text("띐 §7Right click to stop tracking"));

                        int slot = player.getInventory().getHeldItemSlot();
                        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            TrackPlayer.stopTracking(target, player);
                            player.sendMessage(Component.text("띑 §eTracking compass finished"));
                        }, 1200);
                        ItemStack trackingCompass = GenerateCompassItem.getTrackingCompass(target, taskID);
                        player.getInventory().setItem(slot, trackingCompass);

                        TrackPlayer.addTracking(target, player);
                    }
                }
            }
        }
    }
}

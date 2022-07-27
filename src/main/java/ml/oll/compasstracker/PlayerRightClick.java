package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerRightClick implements Listener {
    public final CompassTracker plugin;

    public PlayerRightClick(CompassTracker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().isRightClick()) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (handItem.getType() != Material.AIR) {
                NBTItem handItemNbt = new NBTItem(handItem);
                if (handItemNbt.hasKey("identifier") && handItemNbt.getString("identifier").equals("s6:tracking_compass")) {
                    if (handItemNbt.hasKey("tracking")) {
                        OfflinePlayer target = Bukkit.getPlayer(UUID.fromString(handItemNbt.getString("tracking")));
                        if (target != null) {
                            TrackPlayer.stopTracking(target, player);
                            player.sendMessage(Component.text("띐 §aStopped tracking " + target.getName()));
                        } else {
                            player.sendMessage(Component.text("띒 §cPlayer not found"));
                        }
                    }
                }
            }
        }
    }
}

package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilHandler implements Listener {

    public final CompassTracker plugin;

    public AnvilHandler(CompassTracker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onUseAnvil(PrepareAnvilEvent event) {
        ItemStack item = event.getInventory().getFirstItem();
        if (item != null && item.getType() != Material.AIR) {
            NBTItem itemNbt = new NBTItem(item);
            if (itemNbt.hasKey("identifier") && itemNbt.getString("identifier").equals("s6:tracking_compass")) {
                event.getInventory().setResult(null);
                for (HumanEntity player : event.getViewers()) {
                    player.sendMessage("띒 §cThis item cannot be used in an anvil");
                }
            }
        }
    }
}

package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingHandler implements Listener {
    public final CompassTracker plugin;

    public CraftingHandler(CompassTracker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(PrepareItemCraftEvent event) {
        ItemStack[] items = event.getInventory().getMatrix();
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                NBTItem itemNbt = new NBTItem(item);
                if (itemNbt.hasKey("identifier") && itemNbt.getString("identifier").equals("s6:tracking_compass")) {
                    event.getInventory().setResult(null);
                    for (HumanEntity player : event.getViewers()) {
                        player.sendMessage("띒 §cThis item cannot be used in a crafting table");
                    }
                    break;
                }
            }
        }
    }
}

package ml.oll.compasstracker;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@SuppressWarnings("DuplicatedCode")
public class GenerateCompassItem {
    public static ItemStack getCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.displayName(Component.text(ChatColor.RED + "Tracking Compass"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Hit a player with this to start tracking"));
        lore.add(Component.text("§7Lasts for 60 seconds"));
        compassMeta.lore(lore);
        compass.setItemMeta(compassMeta);

        NBTItem compassNbt = new NBTItem(compass);
        compassNbt.setString("identifier", "s6:tracking_compass");
        compass = compassNbt.getItem();

        return compass;
    }

    public static ItemStack getTrackingCompass(Player target, int taskID) {
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.displayName(Component.text(ChatColor.RED + "Tracking Compass"));
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(target.getLocation());

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Right click to stop tracking"));
        lore.add(Component.text("§7Lasts for 60 seconds"));
        lore.add(Component.text("§eTracking: " + target.getName()));
        compassMeta.lore(lore);
        compass.setItemMeta(compassMeta);

        NBTItem compassNbt = new NBTItem(compass);
        compassNbt.setString("identifier", "s6:tracking_compass");
        compassNbt.setString("tracking", String.valueOf(target.getUniqueId()));
        compassNbt.setInteger("taskid", taskID);
        compass = compassNbt.getItem();

        return compass;
    }
}

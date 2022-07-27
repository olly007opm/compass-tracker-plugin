package ml.oll.compasstracker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompassTracker extends JavaPlugin {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[CompassTracker] Plugin enabled");
        getCommand("givecompass").setExecutor(new GiveCompassCommand(this));

        new PlayerHitEvent(this);
        new TrackPlayer(this);
        new PlayerRightClick(this);
        new CraftingHandler(this);
        new AnvilHandler(this);

        NamespacedKey key = new NamespacedKey(this, "tracking_compass");
        ShapedRecipe recipe = new ShapedRecipe(key, GenerateCompassItem.getCompass());
        recipe.shape("TRT", "ICI", "III");

        recipe.setIngredient('T', Material.REDSTONE_TORCH);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('C', Material.COMPASS);

        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[CompassTracker] Plugin disabled");
    }
}

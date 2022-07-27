package ml.oll.compasstracker;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveCompassCommand implements CommandExecutor {

    public final CompassTracker plugin;
    public GiveCompassCommand(CompassTracker plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("s6.tracker.give")) {
            sender.sendMessage("띒 §cYou do not have permission to run this command");
            return true;
        }

        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage("띒 §cNot enough arguements supplied");
            sender.sendMessage("띒 §7Usage: /givecompass <player>");
        }

        ItemStack compass = GenerateCompassItem.getCompass();
        Player target;

        if (args.length < 1 && sender instanceof Player) {
            target = (Player) sender;
        } else if (Bukkit.getPlayer(args[0]) != null) {
            target = Bukkit.getPlayer(args[0]);
        } else {
            sender.sendMessage("띒 §cPlayer not found");
            return true;
        }

        assert target != null;
        target.getInventory().addItem(compass);

        sender.sendMessage("띐 §aGave tracking compass");

        return true;
    }
}

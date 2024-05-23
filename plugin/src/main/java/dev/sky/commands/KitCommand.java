package dev.sky.commands;

import dev.sky.data.DataManager;
import dev.sky.main.SkyKits;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KitCommand implements CommandExecutor {

    private final SkyKits plugin;
    private final DataManager dataManager;

    public KitCommand(SkyKits plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Only players can use this command."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /sk <create|give> <args>"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return handleCreateCommand(player, args);
            case "give":
                return handleGiveCommand(player, args);
            default:
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Unknown subcommand."));
                return false;
        }
    }

    private boolean handleCreateCommand(Player player, String[] args) {
        if (args.length != 4) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /sk create <name> <cooldown>"));
            return false;
        }

        String kitName = args[1];
        int cooldown;

        try {
            cooldown = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Cooldown must be a number."));
            return false;
        }

        ItemStack[] inventoryContents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        dataManager.createKit(player, kitName, cooldown, inventoryContents, armorContents);

        return true;
    }

    private boolean handleGiveCommand(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /sk give <player> <kit_name>"));
            return false;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player not found or not online."));
            return true;
        }

        String kitName = args[2];
        dataManager.giveKit(target, kitName);
        return true;
    }
}
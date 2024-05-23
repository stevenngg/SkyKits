package dev.sky.data;

import com.google.gson.Gson;
import dev.sky.main.SkyKits;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final SkyKits plugin;
    private final Gson gson;

    public DataManager(SkyKits plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    public void createKit(Player player, String kitName, int cooldown, ItemStack[] inventoryContents, ItemStack[] armorContents) {
        Map<String, Object> kitData = new HashMap<>();
        kitData.put("name", kitName);
        kitData.put("cooldown", cooldown);
        kitData.put("inventoryContents", inventoryContents);
        kitData.put("armorContents", armorContents);

        saveKitData(kitName, kitData);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Kit " + kitName + " created with a cooldown of " + cooldown + " seconds."));
    }

    public void giveKit(Player player, String kitName) {
        File kitFile = new File(plugin.getDataFolder(), "data/" + kitName + ".json");

        if (!kitFile.exists()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Kit not found."));
            return;
        }

        try (FileReader reader = new FileReader(kitFile)) {
            // Parse the JSON file to get the kit data
            Map<String, Object> kitData = gson.fromJson(reader, Map.class);
            if (kitData == null) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Error loading kit data."));
                return;
            }

            // Apply the kit data to the player
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear();

            ItemStack[] inventoryContents = (ItemStack[]) kitData.get("inventoryContents");
            if (inventoryContents != null) {
                playerInventory.setContents(inventoryContents);
            }

            ItemStack[] armorContents = (ItemStack[]) kitData.get("armorContents");
            if (armorContents != null) {
                playerInventory.setArmorContents(armorContents);
            }

            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You received the kit " + kitName + "."));
        } catch (IOException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Error loading kit data."));
            e.printStackTrace();
        }
    }

    private void saveKitData(String kitName, Map<String, Object> kitData) {
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File kitFile = new File(dataFolder, kitName + ".json");

        try (FileWriter writer = new FileWriter(kitFile)) {
            gson.toJson(kitData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
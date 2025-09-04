package SlotMachine.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuración del sistema de Slot Machine
 * Basado en la configuración original pero mejorado
 */
public class SlotMachineConfig {
    
    private final JavaPlugin plugin;
    private final File configFile;
    private FileConfiguration config;
    
    public SlotMachineConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "SlotMachine.yml");
        loadConfig();
    }
    
    private void loadConfig() {
        if (!configFile.exists()) {
            createDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    private void createDefaultConfig() {
        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(configFile);
            
            // Configuración básica - REQUIERE RESOURCE PACK con:
            // - Modelo 3D: casinod3
            // - Sonidos: dtools3:tools.casino.*
            // - Animaciones: idle, eon, dedita, nutria, etc.
            defaultConfig.set("slot_machine.model_id", "casinod3");
            defaultConfig.set("slot_machine.idle_animation", "idle");
            defaultConfig.set("slot_machine.item_required", "vithiums_fichas");
            defaultConfig.set("slot_machine.force", 0.3);
            defaultConfig.set("slot_machine.sound.bet", "dtools3:tools.casino.bet");
            defaultConfig.set("slot_machine.sound.win", "dtools3:tools.casino.win");
            
            // Configuración de slots y recompensas (manteniendo tu sistema)
            defaultConfig.set("SlotMachine.minerales.two_out_of_three", Arrays.asList(
                "coal 12", "iron_ingot 10", "gold_ingot 8"
            ));
            defaultConfig.set("SlotMachine.minerales.three_out_of_three", Arrays.asList(
                "diamond 5", "netherite_scrap 3", "pepita_infernal 3", "corrupted_netherite_scrap 2"
            ));
            
            defaultConfig.set("SlotMachine.itemsvarios.two_out_of_three", Arrays.asList(
                "bread 16", "cooked_beef 8"
            ));
            defaultConfig.set("SlotMachine.itemsvarios.three_out_of_three", Arrays.asList(
                "totem_of_undying 1", "enchanted_golden_apple 2"
            ));
            
            defaultConfig.set("SlotMachine.pociones.two_out_of_three", Arrays.asList(
                "potion 3", "splash_potion 2"
            ));
            defaultConfig.set("SlotMachine.pociones.three_out_of_three", Arrays.asList(
                "ultra_pocion_resistencia_fuego 1"
            ));
            
            defaultConfig.set("SlotMachine.vithiums_fichas.two_out_of_three", Arrays.asList(
                "vithiums_fichas 5", "vithiums_fichas 8"
            ));
            defaultConfig.set("SlotMachine.vithiums_fichas.three_out_of_three", Arrays.asList(
                "vithiums_fichas 15", "vithiums_fichas 20"
            ));
            
            defaultConfig.set("SlotMachine.totems.two_out_of_three", Arrays.asList(
                "totem_of_undying 1"
            ));
            defaultConfig.set("SlotMachine.totems.three_out_of_three", Arrays.asList(
                "doubletotem 1", "lifetotem 1"
            ));
            
            // Configuración de probabilidades
            defaultConfig.set("SlotMachine.probabilities.two_out_of_three", 0.15);
            defaultConfig.set("SlotMachine.probabilities.three_out_of_three", 0.05);
            
            // Configuración de slots (basado en DedsafioUtils)
            defaultConfig.set("slots.eon.name", "Totem Eon");
            defaultConfig.set("slots.eon.item_reward.id", "totem_eon");
            defaultConfig.set("slots.eon.item_reward.amount", 1);
            defaultConfig.set("slots.eon.probability", "10%");
            defaultConfig.set("slots.eon.animation", "eon");
            defaultConfig.set("slots.eon.wait_time", 8.5);
            defaultConfig.set("slots.eon.sound", "dtools3:tools.casino.win");
            
            defaultConfig.set("slots.dedita.name", "Vithium Fichas");
            defaultConfig.set("slots.dedita.item_reward.id", "vithiums_fichas");
            defaultConfig.set("slots.dedita.item_reward.amount", 20);
            defaultConfig.set("slots.dedita.probability", "30%");
            defaultConfig.set("slots.dedita.animation", "dedita");
            defaultConfig.set("slots.dedita.wait_time", 9.0);
            defaultConfig.set("slots.dedita.sound", "dtools3:tools.casino.win");
            
            defaultConfig.set("slots.nutria.name", "Nutritótem");
            defaultConfig.set("slots.nutria.item_reward.id", "nutritotem");
            defaultConfig.set("slots.nutria.item_reward.amount", 1);
            defaultConfig.set("slots.nutria.probability", "10%");
            defaultConfig.set("slots.nutria.animation", "nutria");
            defaultConfig.set("slots.nutria.wait_time", 9.6);
            defaultConfig.set("slots.nutria.sound", "dtools3:tools.casino.win");
            
            defaultConfig.set("slots.corazon.name", "Corazón Extra");
            defaultConfig.set("slots.corazon.item_reward.id", "corazon_extra");
            defaultConfig.set("slots.corazon.item_reward.amount", 1);
            defaultConfig.set("slots.corazon.probability", "10%");
            defaultConfig.set("slots.corazon.animation", "corazon");
            defaultConfig.set("slots.corazon.wait_time", 10.5);
            defaultConfig.set("slots.corazon.sound", "dtools3:tools.casino.win");
            
            // Slots sin premio
            for (int i = 1; i <= 7; i++) {
                defaultConfig.set("slots.no_reward_" + i + ".name", "Sin premio " + i);
                defaultConfig.set("slots.no_reward_" + i + ".probability", (50 + i * 5) + "%");
                defaultConfig.set("slots.no_reward_" + i + ".animation", String.valueOf(i));
                defaultConfig.set("slots.no_reward_" + i + ".wait_time", 8.0 + (i * 0.5));
            }
            
            defaultConfig.save(configFile);
            
        } catch (IOException e) {
            plugin.getLogger().severe("Error creando configuración de Slot Machine: " + e.getMessage());
        }
    }
    
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    // Getters para configuración
    public String getModelId() {
        return config.getString("slot_machine.model_id", "casinod3");
    }
    
    public String getIdleAnimation() {
        return config.getString("slot_machine.idle_animation", "idle");
    }
    
    public String getItemRequired() {
        return config.getString("slot_machine.item_required", "vithiums_fichas");
    }
    
    public double getForce() {
        return config.getDouble("slot_machine.force", 0.3);
    }
    
    public String getBetSound() {
        return config.getString("slot_machine.sound.bet", "dtools3:tools.casino.bet");
    }
    
    public String getWinSound() {
        return config.getString("slot_machine.sound.win", "dtools3:tools.casino.win");
    }
    
    public List<String> getRewards(String category, String type) {
        return config.getStringList("SlotMachine." + category + "." + type);
    }
    
    public double getProbability(String type) {
        return config.getDouble("SlotMachine.probabilities." + type, 0.05);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
}
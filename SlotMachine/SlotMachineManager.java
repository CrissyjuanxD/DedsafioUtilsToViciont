package SlotMachine;

import SlotMachine.api.SlotMachineModel;
import SlotMachine.cache.smachine.SlotMachine;
import SlotMachine.cache.tools.smachine.SlotMachineTool;
import SlotMachine.listeners.SlotMachineListener;
import SlotMachine.providers.modelengine.ModelEngine3;
import SlotMachine.providers.modelengine.ModelEngine4;
import SlotMachine.commands.SlotMachineCommand;
import SlotMachine.utils.ItemCreator;
import vct.hardcore3.ViciontHardcore3;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

/**
 * Gestor principal del sistema de Slot Machine - Basado en DTools3
 */
public class SlotMachineManager {
    
    private final ViciontHardcore3 plugin;
    private SlotMachineTool slotMachineTool;
    private SlotMachineListener listener;
    private ItemCreator itemCreator;
    private ModelEngine4 modelEngine4;
    private ModelEngine3 modelEngine3;
    private boolean useModelEngine4;
    private SlotMachineCommand slotMachineCommand;
    private final Map<Location, SlotMachineModel> activeMachines = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    public SlotMachineManager(ViciontHardcore3 plugin) {
        this.plugin = plugin;
        initialize();
    }
    
    private void initialize() {
        // Cargar configuración
        loadConfiguration();
        
        // Inicializar utilidades
        this.itemCreator = new ItemCreator(plugin);
        
        // Detectar ModelEngine
        detectModelEngine();
        
        // Inicializar listener
        this.listener = new SlotMachineListener(plugin, this);
        
        // Registrar eventos
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        
        // Inicializar y registrar comandos
        initializeCommands();
        
        plugin.getLogger().info("SlotMachine system initialized successfully!");
    }
    
    private void loadConfiguration() {
        File configFile = new File(plugin.getDataFolder(), "tools/slot_machines.yml");
        if (!configFile.exists()) {
            plugin.saveResource("tools/slot_machines.yml", false);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.slotMachineTool = new SlotMachineTool(config);
    }
    
    private void detectModelEngine() {
        try {
            // Intentar ModelEngine 4 primero
            Class.forName("com.ticxo.modelengine.api.ModelEngineAPI");
            Class.forName("com.ticxo.modelengine.api.model.ModeledEntity");
            this.modelEngine4 = new ModelEngine4(plugin);
            this.useModelEngine4 = true;
            plugin.getLogger().info("Detected ModelEngine 4");
        } catch (ClassNotFoundException e) {
            try {
                // Intentar ModelEngine 3
                Class.forName("com.ticxo.modelengine.ModelEngine");
                this.modelEngine3 = new ModelEngine3(plugin);
                this.useModelEngine4 = false;
                plugin.getLogger().info("Detected ModelEngine 3");
            } catch (ClassNotFoundException e2) {
                plugin.getLogger().warning("ModelEngine not found, using fallback");
                this.useModelEngine4 = false;
            }
        }
    }
    
    private void initializeCommands() {
        this.slotMachineCommand = new SlotMachineCommand(plugin, this);
        
        PluginCommand command = plugin.getCommand("slotmachine");
        if (command != null) {
            command.setExecutor(slotMachineCommand);
            command.setTabCompleter(slotMachineCommand);
        }
    }
    
    public boolean createSlotMachine(Location location, Player creator, String machineId) {
        SlotMachine slotMachine = slotMachineTool.getSlotMachine(machineId);
        if (slotMachine == null) {
            slotMachine = slotMachineTool.getDefaultSlotMachine();
            if (slotMachine == null) {
                creator.sendMessage(ChatColor.of("#FF6B6B") + "۞ Error: SlotMachine no configurada.");
                return false;
            }
        }
        
        // Verificar si ya existe una máquina en esa ubicación
        if (activeMachines.containsKey(location)) {
            creator.sendMessage(ChatColor.of("#FF6B6B") + "۞ Ya existe una Slot Machine en esa ubicación.");
            return false;
        }
        
        // Crear entidad ArmorStand
        ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        entity.setVisible(false);
        entity.setGravity(false);
        entity.setInvulnerable(true);
        entity.setCanPickupItems(false);
        entity.setCustomName("SlotMachine");
        entity.setCustomNameVisible(false);
        entity.setSmall(false);
        entity.setArms(false);
        entity.setBasePlate(false);
        entity.setMarker(true);
        
        // Metadata para identificar como slot machine
        entity.setMetadata("slot_machine", new FixedMetadataValue(plugin, slotMachine.getModelId()));
        
        // Crear modelo 3D
        boolean modelCreated = false;
        if (useModelEngine4 && modelEngine4 != null) {
            modelCreated = modelEngine4.createModel(entity, slotMachine.getModelId());
        } else if (modelEngine3 != null) {
            modelCreated = modelEngine3.createModel(entity, slotMachine.getModelId());
        }
        
        if (!modelCreated) {
            plugin.getLogger().warning("Failed to create 3D model for slot machine");
        }
        
        // Crear modelo de datos
        SlotMachineModel model = new SlotMachineModel(slotMachine.getId(), location);
        model.setEntity(entity);
        activeMachines.put(location, model);
        
        creator.sendMessage(ChatColor.of("#B5EAD7") + "۞ Slot Machine creada exitosamente!");
        return true;
    }
    
    public boolean removeSlotMachine(Location location) {
        SlotMachineModel model = activeMachines.remove(location);
        if (model == null) {
            return false;
        }
        
        if (model.getEntity() != null) {
            if (useModelEngine4 && modelEngine4 != null) {
                modelEngine4.removeModel(model.getEntity());
            } else if (modelEngine3 != null) {
                modelEngine3.removeModel(model.getEntity());
            }
            model.getEntity().remove();
        }
        
        return true;
    }
    
    public void reloadConfiguration() {
        loadConfiguration();
    }
    
    public int getActiveMachinesCount() {
        return activeMachines.size();
    }
    
    public int getLoadedConfigsCount() {
        return slotMachineTool.getSlotMachines().size();
    }
    
    public boolean isModelEngineAvailable() {
        return modelEngine4 != null || modelEngine3 != null;
    }
    
    public List<String> getAvailableMachineIds() {
        return new ArrayList<>(slotMachineTool.getSlotMachines().keySet());
    }
    
    public SlotMachineModel getSlotMachine(Location location) {
        return activeMachines.get(location);
    }
    
    public void startUsing(SlotMachine slotMachine, SlotMachineModel model, Player player) {
        // Implementar lógica de uso
        model.setActive(true);
        model.setLastUsed(System.currentTimeMillis());
        
        // Ejecutar spin
        executeSpin(slotMachine, model, player);
    }
    
    private void executeSpin(SlotMachine slotMachine, SlotMachineModel model, Player player) {
        // Calcular resultado basado en probabilidades
        // Implementar lógica de spin similar a DTools3
        
        player.sendMessage(ChatColor.of("#B5EAD7") + "۞ ¡Girando la máquina!");
        
        // Finalizar después de un tiempo
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            model.setActive(false);
        }, 100L);
    }
    
    public void cleanupPlayerMachines(Player player) {
        // Limpiar máquinas del jugador
        for (SlotMachineModel model : activeMachines.values()) {
            if (model.isActive()) {
                model.setActive(false);
            }
        }
    }
    
    public void shutdown() {
        // Limpiar todas las máquinas activas
        for (SlotMachineModel model : activeMachines.values()) {
            if (model.getEntity() != null) {
                if (useModelEngine4 && modelEngine4 != null) {
                    modelEngine4.removeModel(model.getEntity());
                } else if (modelEngine3 != null) {
                    modelEngine3.removeModel(model.getEntity());
                }
                model.getEntity().remove();
            }
        }
        activeMachines.clear();
        
        plugin.getLogger().info("SlotMachine system shutdown completed.");
    }
    
    // Getters
    public SlotMachineTool getSlotMachineTool() { return slotMachineTool; }
    public SlotMachineListener getListener() { return listener; }
    public ItemCreator getItemCreator() { return itemCreator; }
}
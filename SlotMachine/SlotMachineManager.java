package SlotMachine;

import SlotMachine.commands.SlotMachineCommands;
import SlotMachine.config.SlotMachineConfig;
import SlotMachine.handlers.SlotMachineHandler;
import SlotMachine.listeners.SlotMachineListener;
import SlotMachine.models.SlotMachineModel;
import vct.hardcore3.ViciontHardcore3;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Gestor principal del sistema de Slot Machine
 * Combina la lógica de DedsafioUtils con las recompensas de ViciontHardcore3
 */
public class SlotMachineManager {
    
    private final ViciontHardcore3 plugin;
    private SlotMachineConfig config;
    private SlotMachineHandler handler;
    private SlotMachineListener listener;
    private SlotMachineCommands commands;
    
    public SlotMachineManager(ViciontHardcore3 plugin) {
        this.plugin = plugin;
        initialize();
    }
    
    private void initialize() {
        // Inicializar configuración
        this.config = new SlotMachineConfig(plugin);
        
        // Inicializar handler principal
        this.handler = new SlotMachineHandler(plugin, config);
        
        // Inicializar listener
        this.listener = new SlotMachineListener(plugin, handler);
        
        // Inicializar comandos
        this.commands = new SlotMachineCommands(plugin, handler);
        
        // Registrar eventos
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        
        // Registrar comandos
        plugin.getCommand("spawnslotmachine").setExecutor(commands);
        plugin.getCommand("spawnslotmachine").setTabCompleter(commands);
        plugin.getCommand("giveslotmachine").setExecutor(commands);
        plugin.getCommand("giveslotmachine").setTabCompleter(commands);
        
        plugin.getLogger().info("SlotMachine system initialized successfully!");
    }
    
    public void shutdown() {
        if (handler != null) {
            handler.shutdown();
        }
        plugin.getLogger().info("SlotMachine system shutdown completed.");
    }
    
    // Getters
    public SlotMachineConfig getConfig() { return config; }
    public SlotMachineHandler getHandler() { return handler; }
    public SlotMachineListener getListener() { return listener; }
    public SlotMachineCommands getCommands() { return commands; }
}
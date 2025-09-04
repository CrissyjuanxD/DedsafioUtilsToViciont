package SlotMachine.providers;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor para detectar y manejar diferentes versiones de ModelEngine
 */
public class ModelEngineProvider {
    
    private final JavaPlugin plugin;
    private ModelEngineVersion version;
    
    public enum ModelEngineVersion {
        NONE,
        VERSION_3,
        VERSION_4
    }
    
    public ModelEngineProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        detectVersion();
    }
    
    private void detectVersion() {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("ModelEngine")) {
            version = ModelEngineVersion.NONE;
            plugin.getLogger().warning("ModelEngine no detectado. Usando efectos básicos.");
            return;
        }
        
        try {
            // Intentar detectar ModelEngine 4
            Class.forName("com.ticxo.modelengine.api.ModelEngineAPI");
            version = ModelEngineVersion.VERSION_4;
            plugin.getLogger().info("ModelEngine 4 detectado.");
        } catch (ClassNotFoundException e) {
            try {
                // Intentar detectar ModelEngine 3
                Class.forName("com.ticxo.modelengine.api.ModelEngineAPI");
                version = ModelEngineVersion.VERSION_3;
                plugin.getLogger().info("ModelEngine 3 detectado.");
            } catch (ClassNotFoundException ex) {
                version = ModelEngineVersion.NONE;
                plugin.getLogger().warning("Versión de ModelEngine no compatible.");
            }
        }
    }
    
    public ModelEngineVersion getVersion() {
        return version;
    }
    
    public boolean isAvailable() {
        return version != ModelEngineVersion.NONE;
    }
    
    public boolean isVersion4() {
        return version == ModelEngineVersion.VERSION_4;
    }
    
    public boolean isVersion3() {
        return version == ModelEngineVersion.VERSION_3;
    }
}
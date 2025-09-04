package SlotMachine.providers;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor fallback cuando ModelEngine no está disponible
 * Permite que el plugin funcione sin ModelEngine
 */
public class FallbackModelEngineProvider extends ModelEngineProvider {
    
    public FallbackModelEngineProvider(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean createModel(Entity entity, String modelId) {
        plugin.getLogger().info("ModelEngine not available - using fallback for model: " + modelId);
        // El plugin funcionará sin modelos 3D, solo con efectos básicos
        return true;
    }
    
    @Override
    public boolean playAnimation(Entity entity, String animation) {
        plugin.getLogger().info("ModelEngine not available - skipping animation: " + animation);
        return true;
    }
    
    @Override
    public boolean stopAnimation(Entity entity) {
        return true;
    }
    
    @Override
    public boolean removeModel(Entity entity) {
        return true;
    }
    
    @Override
    public boolean hasModel(Entity entity) {
        return false;
    }
    
    @Override
    public String getVersion() {
        return "Fallback (No ModelEngine)";
    }
}
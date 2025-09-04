package SlotMachine.providers;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor base para ModelEngine
 * Basado en el sistema de DedsafioUtils
 */
public abstract class ModelEngineProvider {
    
    protected final JavaPlugin plugin;
    
    public ModelEngineProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Crea un modelo en una entidad
     */
    public abstract boolean createModel(Entity entity, String modelId);
    
    /**
     * Reproduce una animación
     */
    public abstract boolean playAnimation(Entity entity, String animation);
    
    /**
     * Detiene una animación
     */
    public abstract boolean stopAnimation(Entity entity);
    
    /**
     * Remueve el modelo de una entidad
     */
    public abstract boolean removeModel(Entity entity);
    
    /**
     * Verifica si la entidad tiene un modelo
     */
    public abstract boolean hasModel(Entity entity);
    
    /**
     * Obtiene la versión de ModelEngine
     */
    public abstract String getVersion();
    
    /**
     * Detecta automáticamente la versión de ModelEngine
     */
    public static ModelEngineProvider createProvider(JavaPlugin plugin) {
        try {
            // Intentar ModelEngine 4 primero
            Class.forName("com.ticxo.modelengine.api.ModelEngineAPI");
            plugin.getLogger().info("Detected ModelEngine 4");
            return new ModelEngine4Provider(plugin);
        } catch (ClassNotFoundException e) {
            try {
                // Intentar ModelEngine 3
                Class.forName("com.ticxo.modelengine.api.ModelEngine");
                plugin.getLogger().info("Detected ModelEngine 3");
                return new ModelEngine3Provider(plugin);
            } catch (ClassNotFoundException e2) {
                plugin.getLogger().warning("ModelEngine not found, using fallback provider");
                return new FallbackModelEngineProvider(plugin);
            }
        }
    }
}
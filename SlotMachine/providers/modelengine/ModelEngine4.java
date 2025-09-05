package SlotMachine.providers.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor para ModelEngine 4 - Basado en DTools3
 */
public class ModelEngine4 {
    
    private final JavaPlugin plugin;
    
    public ModelEngine4(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public boolean createModel(Entity entity, String modelId) {
        try {
            // Crear ModeledEntity usando la API correcta de ME4
            ModeledEntity modeledEntity = ModelEngineAPI.api().getModelManager().createModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            // Crear ActiveModel
            ActiveModel activeModel = ModelEngineAPI.api().getModelManager().createActiveModel(modelId);
            if (activeModel == null) {
                plugin.getLogger().warning("Model not found: " + modelId);
                return false;
            }
            
            // Añadir modelo a la entidad
            modeledEntity.addActiveModel(activeModel);
            modeledEntity.setBaseEntityVisible(false);
            
            plugin.getLogger().info("Created ModelEngine 4 model: " + modelId + " on entity: " + entity.getUniqueId());
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating ModelEngine 4 model: " + e.getMessage());
            return false;
        }
    }
    
    public boolean playAnimation(Entity entity, String animation) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.api().getModelManager().getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            // Reproducir animación en todos los modelos activos
            modeledEntity.getActiveModels().values().forEach(model -> {
                if (model.getAnimationHandler().hasAnimation(animation)) {
                    model.getAnimationHandler().playAnimation(animation, 1.0, 1.0, 1.0, true);
                    plugin.getLogger().info("Playing animation: " + animation + " on entity: " + entity.getUniqueId());
                }
            });
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error playing animation: " + e.getMessage());
            return false;
        }
    }
    
    public boolean removeModel(Entity entity) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.api().getModelManager().getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            modeledEntity.destroy();
            plugin.getLogger().info("Removed ModelEngine 4 model from entity: " + entity.getUniqueId());
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error removing model: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasModel(Entity entity) {
        try {
            return ModelEngineAPI.api().getModelManager().getModeledEntity(entity) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
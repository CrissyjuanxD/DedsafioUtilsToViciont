package SlotMachine.providers;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor para ModelEngine 4
 * Basado en el código de DedsafioUtils
 */
public class ModelEngine4Provider extends ModelEngineProvider {
    
    public ModelEngine4Provider(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean createModel(Entity entity, String modelId) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
            if (activeModel == null) {
                plugin.getLogger().warning("Model not found: " + modelId);
                return false;
            }
            
            modeledEntity.addModel(activeModel, true);
            plugin.getLogger().info("Created ModelEngine 4 model: " + modelId + " on entity: " + entity.getUniqueId());
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating ModelEngine 4 model: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean playAnimation(Entity entity, String animation) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            // Reproducir animación en todos los modelos activos
            modeledEntity.getModels().values().forEach(model -> {
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
    
    @Override
    public boolean stopAnimation(Entity entity) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }
            
            // Detener todas las animaciones
            modeledEntity.getModels().values().forEach(model -> {
                model.getAnimationHandler().stopAllAnimations();
            });
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error stopping animations: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeModel(Entity entity) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
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
    
    @Override
    public boolean hasModel(Entity entity) {
        try {
            return ModelEngineAPI.getModeledEntity(entity) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getVersion() {
        return "ModelEngine 4";
    }
}
package SlotMachine.providers.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Proveedor para ModelEngine 4 - API actualizada
 */
public class ModelEngine4 {

    private final JavaPlugin plugin;

    public ModelEngine4(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean createModel(Entity entity, String modelId) {
        try {
            // Obtener o crear ModeledEntity (ME4 usa getOrCreate)
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }

            // Crear ActiveModel
            ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
            if (activeModel == null) {
                plugin.getLogger().warning("Model not found: " + modelId);
                return false;
            }

            // Añadir modelo a la entidad
            modeledEntity.addModel(activeModel, true);
            modeledEntity.setBaseEntityVisible(false); // Ocultar entidad base

            plugin.getLogger().info("Created ModelEngine 4 model: " + modelId + " on entity: " + entity.getUniqueId());
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Error creating ModelEngine 4 model: " + e.getMessage());
            return false;
        }
    }

    public boolean removeModel(Entity entity) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            if (modeledEntity == null) {
                return false;
            }

            // Remover todos los modelos
            modeledEntity.getModels().clear();
            modeledEntity.setBaseEntityVisible(true); // Hacer visible la entidad base nuevamente

            plugin.getLogger().info("Removed ModelEngine 4 model from entity: " + entity.getUniqueId());
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Error removing model: " + e.getMessage());
            return false;
        }
    }

    public boolean hasModel(Entity entity) {
        try {
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            return modeledEntity != null && !modeledEntity.getModels().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
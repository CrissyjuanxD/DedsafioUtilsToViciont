package SlotMachine.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Modelo de API para SlotMachine - Basado en DTools3
 */
public class SlotMachineModel {
    
    private final String id;
    private final Location location;
    private Entity entity;
    private boolean isActive;
    private String currentAnimation;
    private long lastUsed;
    
    public SlotMachineModel(String id, Location location) {
        this.id = id;
        this.location = location;
        this.isActive = false;
        this.lastUsed = 0;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public Location getLocation() { return location; }
    
    public Entity getEntity() { return entity; }
    public void setEntity(Entity entity) { this.entity = entity; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getCurrentAnimation() { return currentAnimation; }
    public void setCurrentAnimation(String currentAnimation) { this.currentAnimation = currentAnimation; }
    
    public long getLastUsed() { return lastUsed; }
    public void setLastUsed(long lastUsed) { this.lastUsed = lastUsed; }
}
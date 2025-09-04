package SlotMachine.models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Modelo de datos para una Slot Machine
 * Basado en la lógica de DedsafioUtils pero adaptado
 */
public class SlotMachineModel {
    
    private final Location location;
    private final String modelId;
    private UUID currentUser;
    private boolean isSpinning;
    private String currentAnimation;
    private long lastUsed;
    private SlotResult lastResult;
    
    public SlotMachineModel(Location location, String modelId) {
        this.location = location;
        this.modelId = modelId;
        this.isSpinning = false;
        this.lastUsed = 0;
    }
    
    // Getters y Setters
    public Location getLocation() { return location; }
    public String getModelId() { return modelId; }
    
    public UUID getCurrentUser() { return currentUser; }
    public void setCurrentUser(UUID currentUser) { this.currentUser = currentUser; }
    
    public boolean isSpinning() { return isSpinning; }
    public void setSpinning(boolean spinning) { isSpinning = spinning; }
    
    public String getCurrentAnimation() { return currentAnimation; }
    public void setCurrentAnimation(String currentAnimation) { this.currentAnimation = currentAnimation; }
    
    public long getLastUsed() { return lastUsed; }
    public void setLastUsed(long lastUsed) { this.lastUsed = lastUsed; }
    
    public SlotResult getLastResult() { return lastResult; }
    public void setLastResult(SlotResult lastResult) { this.lastResult = lastResult; }
    
    public boolean isAvailable() {
        return !isSpinning && currentUser == null;
    }
    
    public boolean canUse(Player player) {
        return isAvailable() || (currentUser != null && currentUser.equals(player.getUniqueId()));
    }
    
    /**
     * Clase interna para representar el resultado de un slot
     */
    public static class SlotResult {
        private final String slotId;
        private final String itemId;
        private final int amount;
        private final String animation;
        private final double waitTime;
        private final String sound;
        private final boolean hasReward;
        
        public SlotResult(String slotId, String itemId, int amount, String animation, double waitTime, String sound, boolean hasReward) {
            this.slotId = slotId;
            this.itemId = itemId;
            this.amount = amount;
            this.animation = animation;
            this.waitTime = waitTime;
            this.sound = sound;
            this.hasReward = hasReward;
        }
        
        // Getters
        public String getSlotId() { return slotId; }
        public String getItemId() { return itemId; }
        public int getAmount() { return amount; }
        public String getAnimation() { return animation; }
        public double getWaitTime() { return waitTime; }
        public String getSound() { return sound; }
        public boolean hasReward() { return hasReward; }
    }
}
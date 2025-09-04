package SlotMachine.utils;

import SlotMachine.config.SlotMachineConfig;
import SlotMachine.models.SlotMachineModel;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Calculadora de probabilidades para las Slot Machines
 * Basado en la lógica de DedsafioUtils
 */
public class ProbabilityCalculator {
    
    private final SlotMachineConfig config;
    private final List<SlotEntry> slotEntries;
    private final Random random;
    
    public ProbabilityCalculator(SlotMachineConfig config) {
        this.config = config;
        this.slotEntries = new ArrayList<>();
        this.random = new Random();
        loadSlotEntries();
    }
    
    private void loadSlotEntries() {
        ConfigurationSection slotsSection = config.getConfig().getConfigurationSection("slots");
        if (slotsSection == null) return;
        
        for (String slotId : slotsSection.getKeys(false)) {
            ConfigurationSection slotSection = slotsSection.getConfigurationSection(slotId);
            if (slotSection == null) continue;
            
            String name = slotSection.getString("name", slotId);
            String itemId = slotSection.getString("item_reward.id", null);
            int amount = slotSection.getInt("item_reward.amount", 1);
            String probabilityStr = slotSection.getString("probability", "0%");
            double waitTime = slotSection.getDouble("wait_time", 5.0);
            String animation = slotSection.getString("animation", "default");
            String sound = slotSection.getString("sound", "dtools3:tools.casino.win");
            
            // Parsear probabilidad
            double probability = parseProbability(probabilityStr);
            boolean hasReward = itemId != null && !itemId.isEmpty();
            
            SlotEntry entry = new SlotEntry(slotId, name, itemId, amount, probability, waitTime, animation, sound, hasReward);
            slotEntries.add(entry);
        }
        
        // Ordenar por probabilidad (menor probabilidad = más raro)
        slotEntries.sort(Comparator.comparingDouble(SlotEntry::getProbability));
    }
    
    private double parseProbability(String probabilityStr) {
        try {
            if (probabilityStr.endsWith("%")) {
                return Double.parseDouble(probabilityStr.substring(0, probabilityStr.length() - 1)) / 100.0;
            } else {
                return Double.parseDouble(probabilityStr);
            }
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Calcula el resultado de un spin
     */
    public SlotMachineModel.SlotResult calculateResult() {
        double roll = random.nextDouble();
        double cumulativeProbability = 0.0;
        
        // Recorrer slots desde el más raro al más común
        for (SlotEntry entry : slotEntries) {
            cumulativeProbability += entry.getProbability();
            
            if (roll <= cumulativeProbability) {
                return new SlotMachineModel.SlotResult(
                    entry.getSlotId(),
                    entry.getItemId(),
                    entry.getAmount(),
                    entry.getAnimation(),
                    entry.getWaitTime(),
                    entry.getSound(),
                    entry.hasReward()
                );
            }
        }
        
        // Si no se encontró resultado, devolver el último (más común)
        if (!slotEntries.isEmpty()) {
            SlotEntry lastEntry = slotEntries.get(slotEntries.size() - 1);
            return new SlotMachineModel.SlotResult(
                lastEntry.getSlotId(),
                lastEntry.getItemId(),
                lastEntry.getAmount(),
                lastEntry.getAnimation(),
                lastEntry.getWaitTime(),
                lastEntry.getSound(),
                lastEntry.hasReward()
            );
        }
        
        // Resultado por defecto si no hay slots configurados
        return new SlotMachineModel.SlotResult("no_reward", null, 0, "default", 5.0, "dtools3:tools.casino.lose", false);
    }
    
    /**
     * Clase interna para representar una entrada de slot
     */
    private static class SlotEntry {
        private final String slotId;
        private final String name;
        private final String itemId;
        private final int amount;
        private final double probability;
        private final double waitTime;
        private final String animation;
        private final String sound;
        private final boolean hasReward;
        
        public SlotEntry(String slotId, String name, String itemId, int amount, double probability, double waitTime, String animation, String sound, boolean hasReward) {
            this.slotId = slotId;
            this.name = name;
            this.itemId = itemId;
            this.amount = amount;
            this.probability = probability;
            this.waitTime = waitTime;
            this.animation = animation;
            this.sound = sound;
            this.hasReward = hasReward;
        }
        
        // Getters
        public String getSlotId() { return slotId; }
        public String getName() { return name; }
        public String getItemId() { return itemId; }
        public int getAmount() { return amount; }
        public double getProbability() { return probability; }
        public double getWaitTime() { return waitTime; }
        public String getAnimation() { return animation; }
        public String getSound() { return sound; }
        public boolean hasReward() { return hasReward; }
    }
}
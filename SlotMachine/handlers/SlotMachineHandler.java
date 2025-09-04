package SlotMachine.handlers;

import SlotMachine.config.SlotMachineConfig;
import SlotMachine.models.SlotMachineModel;
import SlotMachine.utils.ItemCreator;
import SlotMachine.utils.ProbabilityCalculator;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import items.EconomyItems;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler principal para el sistema de Slot Machine
 * Combina la lógica de DedsafioUtils con las recompensas de ViciontHardcore3
 */
public class SlotMachineHandler {
    
    private final JavaPlugin plugin;
    private final SlotMachineConfig config;
    private final ItemCreator itemCreator;
    private final ProbabilityCalculator probabilityCalculator;
    
    // Mapas para gestionar las máquinas activas
    private final Map<Location, SlotMachineModel> activeMachines = new ConcurrentHashMap<>();
    private final Map<UUID, Location> playerMachines = new ConcurrentHashMap<>();
    private final Map<Location, ActiveModel> activeModels = new ConcurrentHashMap<>();
    private final Map<Location, BukkitRunnable> activeAnimations = new ConcurrentHashMap<>();
    
    public SlotMachineHandler(JavaPlugin plugin, SlotMachineConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.itemCreator = new ItemCreator(plugin);
        this.probabilityCalculator = new ProbabilityCalculator(config);
    }
    
    /**
     * Crea una nueva slot machine en la ubicación especificada
     */
    public boolean createSlotMachine(Location location, Player creator) {
        if (activeMachines.containsKey(location)) {
            creator.sendMessage(ChatColor.of("#FF6B6B") + "۞ Ya existe una máquina en esta ubicación.");
            return false;
        }
        
        // Colocar el bloque de la máquina
        location.getBlock().setType(Material.ORANGE_GLAZED_TERRACOTTA);
        
        // Crear el modelo 3D con ModelEngine
        SlotMachineModel machine = new SlotMachineModel(location, config.getModelId());
        activeMachines.put(location, machine);
        
        // Spawnar modelo 3D con ModelEngine
        spawnModelEngineModel(location);
        
        // Efectos visuales y sonoros
        spawnCreationEffects(location);
        
        creator.sendMessage(ChatColor.of("#B5EAD7") + "۞ Slot Machine creada exitosamente!");
        plugin.getLogger().info("Slot Machine created at " + locationToString(location) + " by " + creator.getName());
        
        return true;
    }
    
    /**
     * Crea el modelo 3D usando ModelEngine
     */
    private void spawnModelEngineModel(Location location) {
        try {
            // Verificar que ModelEngine esté disponible
            if (!plugin.getServer().getPluginManager().isPluginEnabled("ModelEngine")) {
                plugin.getLogger().warning("ModelEngine no está disponible. Usando efectos básicos.");
                return;
            }
            
            // Crear modelo usando ModelEngine API
            ActiveModel model = ModelEngineAPI.createActiveModel(config.getModelId());
            if (model != null) {
                // Posicionar el modelo
                Location modelLoc = location.clone().add(0.5, 0, 0.5);
                ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(modelLoc);
                
                if (modeledEntity != null) {
                    modeledEntity.addModel(model, true);
                    activeModels.put(location, model);
                    
                    // Iniciar animación idle
                    model.getAnimationHandler().playAnimation(config.getIdleAnimation(), true);
                    
                    plugin.getLogger().info("Modelo 3D creado: " + config.getModelId() + " en " + locationToString(location));
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error creando modelo ModelEngine: " + e.getMessage());
            // Fallback a efectos básicos
            spawnBasicEffects(location);
        }
    }
    
    /**
     * Efectos básicos si ModelEngine no está disponible
     */
    private void spawnBasicEffects(Location location) {
        World world = location.getWorld();
        Location effectLoc = location.clone().add(0.5, 1, 0.5);
        
        // Partículas básicas para simular la máquina
        world.spawnParticle(Particle.ELECTRIC_SPARK, effectLoc, 5, 0.3, 0.3, 0.3, 0.05);
        world.spawnParticle(Particle.ENCHANT, effectLoc, 3, 0.2, 0.2, 0.2, 0.1);
    }
    
    /**
     * Inicia el uso de una slot machine
     */
    public boolean startUsing(Location location, Player player) {
        SlotMachineModel machine = activeMachines.get(location);
        if (machine == null) {
            return false;
        }
        
        if (!machine.canUse(player)) {
            Player currentUser = Bukkit.getPlayer(machine.getCurrentUser());
            if (currentUser != null && currentUser.isOnline()) {
                player.sendMessage(ChatColor.of("#FF6B6B") + "۞ Esta máquina está siendo usada por " + currentUser.getName());
            } else {
                // Limpiar usuario desconectado
                machine.setCurrentUser(null);
                return startUsing(location, player);
            }
            return false;
        }
        
        // Verificar fichas
        if (!hasRequiredItem(player)) {
            player.sendMessage(ChatColor.of("#FFB3BA") + "۞ Necesitas Vithium Fichas para usar la máquina tragamonedas.");
            return false;
        }
        
        // Asignar máquina al jugador
        machine.setCurrentUser(player.getUniqueId());
        playerMachines.put(player.getUniqueId(), location);
        machine.setLastUsed(System.currentTimeMillis());
        
        // Sonido de inicio
        location.getWorld().playSound(location, Sound.BLOCK_CHEST_OPEN, 1.0f, 1.2f);
        
        return true;
    }
    
    /**
     * Ejecuta el spin de la slot machine
     */
    public void executeSpin(Location location, Player player) {
        SlotMachineModel machine = activeMachines.get(location);
        if (machine == null || machine.isSpinning()) {
            return;
        }
        
        if (!machine.getCurrentUser().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ No tienes permiso para usar esta máquina.");
            return;
        }
        
        // Consumir ficha
        if (!consumeRequiredItem(player)) {
            player.sendMessage(ChatColor.of("#FFB3BA") + "۞ No tienes suficientes fichas.");
            return;
        }
        
        machine.setSpinning(true);
        
        // Calcular resultado
        SlotMachineModel.SlotResult result = probabilityCalculator.calculateResult();
        machine.setLastResult(result);
        
        // Iniciar animación
        startSpinAnimation(location, player, result);
        
        // Reproducir animación en el modelo 3D
        playModelAnimation(location, "spin");
        
        // Sonidos de inicio
        playSound(location, config.getBetSound());
    }
    
    /**
     * Reproduce una animación en el modelo 3D
     */
    private void playModelAnimation(Location location, String animationName) {
        ActiveModel model = activeModels.get(location);
        if (model != null) {
            try {
                model.getAnimationHandler().playAnimation(animationName, false);
            } catch (Exception e) {
                plugin.getLogger().warning("Error reproduciendo animación: " + animationName);
            }
        }
    }
    
    /**
     * Inicia la animación de spin
     */
    private void startSpinAnimation(Location location, Player player, SlotMachineModel.SlotResult result) {
        BukkitRunnable animationTask = new BukkitRunnable() {
            private int ticks = 0;
            private final int maxTicks = (int) (result.getWaitTime() * 20); // Convertir segundos a ticks
            
            @Override
            public void run() {
                ticks++;
                
                // Efectos durante la animación
                if (ticks % 10 == 0) {
                    spawnSpinEffects(location);
                }
                
                // Sonidos de animación
                if (ticks % 4 == 0) {
                    location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_HAT, 0.3f, 1.0f + (ticks * 0.02f));
                }
                
                // Finalizar animación
                if (ticks >= maxTicks) {
                    finishSpin(location, player, result);
                    this.cancel();
                }
            }
        };
        
        activeAnimations.put(location, animationTask);
        animationTask.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Finaliza el spin y otorga recompensas
     */
    private void finishSpin(Location location, Player player, SlotMachineModel.SlotResult result) {
        SlotMachineModel machine = activeMachines.get(location);
        if (machine == null) return;
        
        machine.setSpinning(false);
        activeAnimations.remove(location);
        
        // Efectos de finalización
        spawnFinishEffects(location, result.hasReward());
        
        // Reproducir animación de resultado en el modelo
        playModelAnimation(location, result.getAnimation());
        
        if (result.hasReward()) {
            // Dar recompensa
            giveReward(player, result);
            
            // Sonidos de victoria
            playSound(location, config.getWinSound());
            
            // Mensaje de victoria
            player.sendMessage(ChatColor.of("#B5EAD7") + "۞ ¡Has ganado " + 
                ChatColor.of("#FFD3A5") + result.getAmount() + "x " + result.getItemId() + 
                ChatColor.of("#B5EAD7") + "!");
                
        } else {
            // Sonidos de pérdida
            playSound(location, "dtools3:tools.casino.lose");
            
            player.sendMessage(ChatColor.of("#FF6B6B") + "¡No hay suerte esta vez!");
        }
        
        // Limpiar después de un tiempo
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stopUsing(location, player);
        }, 100L); // 5 segundos
    }
    
    /**
     * Detiene el uso de una slot machine
     */
    public void stopUsing(Location location, Player player) {
        SlotMachineModel machine = activeMachines.get(location);
        if (machine == null) return;
        
        if (machine.getCurrentUser() != null && machine.getCurrentUser().equals(player.getUniqueId())) {
            machine.setCurrentUser(null);
            playerMachines.remove(player.getUniqueId());
        }
    }
    
    /**
     * Remueve una slot machine
     */
    public boolean removeSlotMachine(Location location, Player remover) {
        SlotMachineModel machine = activeMachines.get(location);
        if (machine == null) {
            remover.sendMessage(ChatColor.of("#FF6B6B") + "۞ No hay una máquina en esta ubicación.");
            return false;
        }
        
        if (machine.isSpinning()) {
            remover.sendMessage(ChatColor.of("#FF6B6B") + "۞ No puedes remover una máquina que está en uso.");
            return false;
        }
        
        // Limpiar animaciones activas
        BukkitRunnable animation = activeAnimations.remove(location);
        if (animation != null) {
            animation.cancel();
        }
        
        // Limpiar modelo 3D
        ActiveModel model = activeModels.remove(location);
        if (model != null) {
            try {
                ModeledEntity modeledEntity = model.getModeledEntity();
                if (modeledEntity != null) {
                    modeledEntity.destroy();
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Error limpiando modelo ModelEngine: " + e.getMessage());
            }
        }
        
        // Limpiar usuario si existe
        if (machine.getCurrentUser() != null) {
            playerMachines.remove(machine.getCurrentUser());
        }
        
        // Remover máquina
        activeMachines.remove(location);
        location.getBlock().setType(Material.AIR);
        
        // Efectos de remoción
        spawnRemovalEffects(location);
        
        remover.sendMessage(ChatColor.of("#B5EAD7") + "۞ Slot Machine removida exitosamente!");
        plugin.getLogger().info("Slot Machine removed from " + locationToString(location) + " by " + remover.getName());
        
        return true;
    }
    
    /**
     * Crea el item de slot machine
     */
    public ItemStack createSlotMachineItem() {
        ItemStack item = new ItemStack(Material.ORANGE_GLAZED_TERRACOTTA);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.of("#FFD3A5") + "" + ChatColor.BOLD + "Slot Machine");
        meta.setLore(Arrays.asList(
            "",
            ChatColor.of("#C7CEEA") + "Coloca este bloque para crear",
            ChatColor.of("#C7CEEA") + "una máquina tragamonedas",
            "",
            ChatColor.of("#B5EAD7") + "• Requiere: " + ChatColor.of("#FFD3A5") + "Vithium Fichas",
            ChatColor.of("#B5EAD7") + "• Recompensas: " + ChatColor.of("#FFD3A5") + "Variadas",
            ""
        ));
        
        meta.setCustomModelData(1000);
        item.setItemMeta(meta);
        
        return item;
    }
    
    // Métodos de utilidad
    private boolean hasRequiredItem(Player player) {
        ItemStack requiredItem = EconomyItems.createVithiumToken();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(requiredItem)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean consumeRequiredItem(Player player) {
        ItemStack requiredItem = EconomyItems.createVithiumToken();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(requiredItem)) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }
                return true;
            }
        }
        return false;
    }
    
    private void giveReward(Player player, SlotMachineModel.SlotResult result) {
        ItemStack reward = itemCreator.createItem(result.getItemId(), result.getAmount());
        if (reward != null) {
            HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(reward);
            if (!remaining.isEmpty()) {
                for (ItemStack item : remaining.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
        }
    }
    
    private void playSound(Location location, String soundName) {
        try {
            if (soundName.startsWith("dtools3:")) {
                // Sonido personalizado del RESOURCE PACK
                // Minecraft maneja automáticamente los sonidos del resource pack
                plugin.getLogger().info("Playing custom sound: " + soundName + " at " + locationToString(location));
                
                // Fallback a sonidos vanilla si el resource pack no está cargado
                if (soundName.contains("bet")) {
                    location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.2f);
                } else if (soundName.contains("win")) {
                    location.getWorld().playSound(location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                } else if (soundName.contains("lose")) {
                    location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                }
            } else {
                // Sonidos vanilla de Minecraft
                Sound sound = Sound.valueOf(soundName.toUpperCase());
                location.getWorld().playSound(location, sound, 1.0f, 1.0f);
            }
        } catch (IllegalArgumentException e) {
            // Si el sonido no existe, usar sonido por defecto
            location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }
    
    private void spawnCreationEffects(Location location) {
        World world = location.getWorld();
        Location effectLoc = location.clone().add(0.5, 1, 0.5);
        
        world.spawnParticle(Particle.TOTEM_OF_UNDYING, effectLoc, 10, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticle(Particle.ELECTRIC_SPARK, effectLoc, 5, 0.3, 0.3, 0.3, 0.05);
    }
    
    private void spawnSpinEffects(Location location) {
        World world = location.getWorld();
        Location effectLoc = location.clone().add(0.5, 1.5, 0.5);
        
        world.spawnParticle(Particle.ENCHANT, effectLoc, 3, 0.3, 0.3, 0.3, 0.1);
    }
    
    private void spawnFinishEffects(Location location, boolean won) {
        World world = location.getWorld();
        Location effectLoc = location.clone().add(0.5, 1.5, 0.5);
        
        if (won) {
            world.spawnParticle(Particle.TOTEM_OF_UNDYING, effectLoc, 15, 0.5, 0.5, 0.5, 0.1);
            world.spawnParticle(Particle.ELECTRIC_SPARK, effectLoc, 10, 0.3, 0.3, 0.3, 0.05);
        } else {
            world.spawnParticle(Particle.SMOKE, effectLoc, 5, 0.2, 0.2, 0.2, 0.05);
        }
    }
    
    private void spawnRemovalEffects(Location location) {
        World world = location.getWorld();
        Location effectLoc = location.clone().add(0.5, 1, 0.5);
        
        world.spawnParticle(Particle.EXPLOSION, effectLoc, 1, 0, 0, 0, 0);
        world.spawnParticle(Particle.SMOKE, effectLoc, 10, 0.5, 0.5, 0.5, 0.1);
    }
    
    private String locationToString(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
    
    public void shutdown() {
        // Cancelar todas las animaciones activas
        for (BukkitRunnable animation : activeAnimations.values()) {
            if (animation != null) {
                animation.cancel();
            }
        }
        activeAnimations.clear();
        
        // Limpiar mapas
        activeMachines.clear();
        playerMachines.clear();
        
        // Limpiar modelos 3D
        for (ActiveModel model : activeModels.values()) {
            try {
                ModeledEntity modeledEntity = model.getModeledEntity();
                if (modeledEntity != null) {
                    modeledEntity.destroy();
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Error limpiando modelo en shutdown: " + e.getMessage());
            }
        }
        activeModels.clear();
        
        plugin.getLogger().info("SlotMachineHandler shutdown completed.");
    }
    
    // Getters
    public Map<Location, SlotMachineModel> getActiveMachines() { return activeMachines; }
    public SlotMachineModel getMachine(Location location) { return activeMachines.get(location); }
    public boolean hasMachine(Location location) { return activeMachines.containsKey(location); }
}
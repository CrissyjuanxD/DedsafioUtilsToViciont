package SlotMachine.listeners;

import SlotMachine.SlotMachineManager;
import SlotMachine.api.SlotMachineModel;
import SlotMachine.cache.smachine.SlotMachine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener para SlotMachine - Basado en DTools3
 */
public class SlotMachineListener implements Listener {
    
    private final JavaPlugin plugin;
    private final SlotMachineManager manager;
    
    public SlotMachineListener(JavaPlugin plugin, SlotMachineManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        
        // Verificar si es una slot machine
        if (!entity.hasMetadata("slot_machine")) {
            return;
        }
        
        event.setCancelled(true);
        
        // Obtener la slot machine
        SlotMachine slotMachine = manager.getSlotMachineTool().getDefaultSlotMachine();
        if (slotMachine == null) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ Error: SlotMachine no configurada.");
            return;
        }
        
        SlotMachineModel model = slotMachine.getActiveMachine(entity.getLocation());
        if (model == null) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ Error: Máquina no encontrada.");
            return;
        }
        
        if (model.isActive()) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ La máquina está en uso, espera a que termine.");
            return;
        }
        
        // Iniciar uso de la máquina
        manager.startUsing(slotMachine, model, player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Limpiar máquinas del jugador que se desconecta
        manager.cleanupPlayerMachines(player);
    }
}
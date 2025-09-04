package SlotMachine.listeners;

import SlotMachine.handlers.SlotMachineHandler;
import SlotMachine.models.SlotMachineModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener para eventos relacionados con las Slot Machines
 */
public class SlotMachineListener implements Listener {
    
    private final JavaPlugin plugin;
    private final SlotMachineHandler handler;
    
    public SlotMachineListener(JavaPlugin plugin, SlotMachineHandler handler) {
        this.plugin = plugin;
        this.handler = handler;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.ORANGE_GLAZED_TERRACOTTA) return;
        
        event.setCancelled(true);
        Player player = event.getPlayer();
        
        // Verificar si es una slot machine
        if (!handler.hasMachine(event.getClickedBlock().getLocation())) {
            return;
        }
        
        SlotMachineModel machine = handler.getMachine(event.getClickedBlock().getLocation());
        
        if (player.isSneaking()) {
            // Shift + Click = Remover máquina (solo para ops o creadores)
            if (player.isOp()) {
                handler.removeSlotMachine(event.getClickedBlock().getLocation(), player);
            } else {
                player.sendMessage(ChatColor.of("#FF6B6B") + "۞ No tienes permiso para remover esta máquina.");
            }
            return;
        }
        
        if (machine.isSpinning()) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ La máquina está girando, espera a que termine.");
            return;
        }
        
        if (machine.getCurrentUser() != null && !machine.getCurrentUser().equals(player.getUniqueId())) {
            Player currentUser = plugin.getServer().getPlayer(machine.getCurrentUser());
            if (currentUser != null && currentUser.isOnline()) {
                player.sendMessage(ChatColor.of("#FF6B6B") + "۞ Esta máquina está siendo usada por " + currentUser.getName());
                return;
            }
        }
        
        // Iniciar uso de la máquina
        if (handler.startUsing(event.getClickedBlock().getLocation(), player)) {
            // Ejecutar spin inmediatamente
            handler.executeSpin(event.getClickedBlock().getLocation(), player);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.ORANGE_GLAZED_TERRACOTTA) return;
        
        // Verificar si el item tiene el NBT correcto para ser una slot machine
        if (event.getItemInHand().hasItemMeta() && 
            event.getItemInHand().getItemMeta().hasCustomModelData() &&
            event.getItemInHand().getItemMeta().getCustomModelData() == 1000) {
            
            // Crear la slot machine
            if (!handler.createSlotMachine(event.getBlock().getLocation(), event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.ORANGE_GLAZED_TERRACOTTA) return;
        
        if (handler.hasMachine(event.getBlock().getLocation())) {
            event.setCancelled(true);
            
            if (event.getPlayer().isOp()) {
                handler.removeSlotMachine(event.getBlock().getLocation(), event.getPlayer());
            } else {
                event.getPlayer().sendMessage(ChatColor.of("#FF6B6B") + "۞ No puedes romper una Slot Machine. Usa Shift + Click para removerla.");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Limpiar máquinas del jugador que se desconecta
        for (SlotMachineModel machine : handler.getActiveMachines().values()) {
            if (machine.getCurrentUser() != null && machine.getCurrentUser().equals(player.getUniqueId())) {
                handler.stopUsing(machine.getLocation(), player);
            }
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        // Limpiar máquinas en chunks que se descargan
        handler.getActiveMachines().entrySet().removeIf(entry -> {
            if (entry.getKey().getChunk().equals(event.getChunk())) {
                SlotMachineModel machine = entry.getValue();
                if (machine.getCurrentUser() != null) {
                    Player player = plugin.getServer().getPlayer(machine.getCurrentUser());
                    if (player != null) {
                        handler.stopUsing(machine.getLocation(), player);
                    }
                }
                return true;
            }
            return false;
        });
    }
}
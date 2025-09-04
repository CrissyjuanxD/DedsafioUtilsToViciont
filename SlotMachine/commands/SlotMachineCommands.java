package SlotMachine.commands;

import SlotMachine.handlers.SlotMachineHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Comandos para el sistema de Slot Machine
 */
public class SlotMachineCommands implements CommandExecutor, TabCompleter {
    
    private final JavaPlugin plugin;
    private final SlotMachineHandler handler;
    
    public SlotMachineCommands(JavaPlugin plugin, SlotMachineHandler handler) {
        this.plugin = plugin;
        this.handler = handler;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        switch (command.getName().toLowerCase()) {
            case "spawnslotmachine":
                return handleSpawnSlotMachine(sender, args);
            case "giveslotmachine":
                return handleGiveSlotMachine(sender, args);
            default:
                return false;
        }
    }
    
    private boolean handleSpawnSlotMachine(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.of("#FF6B6B") + "Este comando solo puede ser ejecutado por jugadores.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("viciont.slotmachine.spawn")) {
            player.sendMessage(ChatColor.of("#FF6B6B") + "۞ No tienes permiso para usar este comando.");
            return true;
        }
        
        // Crear slot machine en la ubicación del jugador
        if (handler.createSlotMachine(player.getLocation().getBlock().getLocation(), player)) {
            player.sendMessage(ChatColor.of("#B5EAD7") + "۞ Slot Machine creada en tu ubicación!");
        }
        
        return true;
    }
    
    private boolean handleGiveSlotMachine(CommandSender sender, String[] args) {
        if (!sender.hasPermission("viciont.slotmachine.give")) {
            sender.sendMessage(ChatColor.of("#FF6B6B") + "۞ No tienes permiso para usar este comando.");
            return true;
        }
        
        Player target = null;
        int amount = 1;
        
        // Parsear argumentos
        if (args.length == 0) {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.of("#FF6B6B") + "Uso: /giveslotmachine [jugador] [cantidad]");
                return true;
            }
        } else if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                try {
                    amount = Integer.parseInt(args[0]);
                    if (sender instanceof Player) {
                        target = (Player) sender;
                    } else {
                        sender.sendMessage(ChatColor.of("#FF6B6B") + "Debes especificar un jugador desde la consola.");
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.of("#FF6B6B") + "Jugador no encontrado: " + args[0]);
                    return true;
                }
            }
        } else if (args.length == 2) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.of("#FF6B6B") + "Jugador no encontrado: " + args[0]);
                return true;
            }
            
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.of("#FF6B6B") + "Cantidad inválida: " + args[1]);
                return true;
            }
        }
        
        if (target == null) {
            sender.sendMessage(ChatColor.of("#FF6B6B") + "Jugador no encontrado.");
            return true;
        }
        
        if (amount <= 0 || amount > 64) {
            sender.sendMessage(ChatColor.of("#FF6B6B") + "La cantidad debe estar entre 1 y 64.");
            return true;
        }
        
        // Crear y dar el item
        ItemStack slotMachineItem = handler.createSlotMachineItem();
        slotMachineItem.setAmount(amount);
        
        HashMap<Integer, ItemStack> remaining = target.getInventory().addItem(slotMachineItem);
        if (!remaining.isEmpty()) {
            for (ItemStack item : remaining.values()) {
                target.getWorld().dropItemNaturally(target.getLocation(), item);
            }
        }
        
        // Mensajes
        target.sendMessage(ChatColor.of("#B5EAD7") + "۞ Has recibido " + amount + "x Slot Machine!");
        if (!sender.equals(target)) {
            sender.sendMessage(ChatColor.of("#B5EAD7") + "۞ Has dado " + amount + "x Slot Machine a " + target.getName());
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        switch (command.getName().toLowerCase()) {
            case "giveslotmachine":
                if (args.length == 1) {
                    // Nombres de jugadores
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                } else if (args.length == 2) {
                    // Cantidades sugeridas
                    completions.addAll(Arrays.asList("1", "2", "4", "8", "16", "32", "64"));
                }
                break;
        }
        
        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
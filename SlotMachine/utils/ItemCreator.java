package SlotMachine.utils;

import Commands.ItemsCommands;
import items.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Creador de items para SlotMachine - Integrado con ViciontHardcore3
 */
public class ItemCreator {
    
    private final JavaPlugin plugin;
    private final ItemsCommands itemsCommands;
    
    public ItemCreator(JavaPlugin plugin) {
        this.plugin = plugin;
        this.itemsCommands = new ItemsCommands((vct.hardcore3.ViciontHardcore3) plugin);
    }
    
    public ItemStack createItem(String itemId, int amount) {
        try {
            ItemStack item;
            
            switch (itemId.toLowerCase()) {
                // Totems
                case "doubletotem":
                    item = new DoubleLifeTotem(plugin).createDoubleLifeTotem();
                    break;
                case "lifetotem":
                    item = new LifeTotem(plugin).createLifeTotem();
                    break;
                case "spidertotem":
                    item = new SpiderTotem(plugin).createSpiderTotem();
                    break;
                case "infernaltotem":
                    item = new InfernalTotem(plugin).createInfernalTotem();
                    break;
                case "icetotem":
                    item = new EconomyIceTotem(plugin).createIceTotem();
                    break;
                case "flytotem":
                    item = new EconomyFlyTotem(plugin).createFlyTotem();
                    break;
                    
                // Economy Items
                case "vithiums":
                    item = EconomyItems.createVithiumCoin();
                    break;
                case "vithiums_fichas":
                    item = EconomyItems.createVithiumToken();
                    break;
                case "enderbag":
                    item = EconomyItems.createEnderBag();
                    break;
                case "panic_apple":
                    item = EconomyItems.createManzanaPanico();
                    break;
                    
                // Enderite Items
                case "enderite_ingot":
                    item = EndItems.createIngotEnderite();
                    break;
                case "enderite_nugget":
                    item = EndItems.createEnderiteNugget(amount);
                    break;
                case "enderite_fragment":
                    item = EndItems.createFragmentoEnderite();
                    break;
                case "end_amatist":
                    item = EndItems.createEndAmatist(amount);
                    break;
                    
                // Corrupted Items
                case "corrupted_netherite_ingot":
                    item = CorruptedNetheriteItems.createCorruptedNetheriteIngot();
                    break;
                case "corrupted_golden_apple":
                    item = CorruptedGoldenApple.createCorruptedGoldenApple();
                    break;
                case "corrupted_soul":
                    item = new CorruptedSoul(plugin).createCorruptedSoulEssence();
                    break;
                    
                // Emblems
                case "nether_emblem":
                    item = EmblemItems.createNetherEmblem();
                    break;
                case "overworld_emblem":
                    item = EmblemItems.createOverworldEmblem();
                    break;
                case "end_relic":
                    item = EmblemItems.createEndEmblem();
                    break;
                    
                // Special Items
                case "orbe_de_vida":
                    item = ReviveItems.createResurrectOrb();
                    break;
                case "specialtotem":
                    item = ItemsTotems.createSpecialTotem();
                    break;
                    
                // Vanilla items con cantidad personalizada
                case "gold_ingot":
                    item = new ItemStack(org.bukkit.Material.GOLD_INGOT, amount);
                    break;
                case "diamond":
                    item = new ItemStack(org.bukkit.Material.DIAMOND, amount);
                    break;
                case "emerald":
                    item = new ItemStack(org.bukkit.Material.EMERALD, amount);
                    break;
                case "netherite_ingot":
                    item = new ItemStack(org.bukkit.Material.NETHERITE_INGOT, amount);
                    break;
                    
                default:
                    plugin.getLogger().warning("Item desconocido en SlotMachine: " + itemId);
                    return null;
            }
            
            item.setAmount(amount);
            
            return item;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error creando item " + itemId + ": " + e.getMessage());
            return null;
        }
    }
}
package SlotMachine.utils;

import Armors.CopperArmor;
import Armors.CorruptedArmor;
import Armors.NightVisionHelmet;
import Blocks.CorruptedAncientDebris;
import Blocks.Endstalactitas;
import Blocks.GuardianShulkerHeart;
import Dificultades.CustomMobs.CustomBoat;
import Dificultades.CustomMobs.QueenBeeHandler;
import Dificultades.DayOneChanges;
import Enchants.EnhancedEnchantmentTable;
import Events.UltraWitherBattle.UltraWitherCompass;
import items.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utilidad para crear items personalizados
 * Reutiliza la lógica de tu ItemsCommands
 */
public class ItemCreator {
    
    private final JavaPlugin plugin;
    
    // Instancias de las clases de items (igual que en tu ItemsCommands)
    private final DoubleLifeTotem doubleLifeTotem;
    private final LifeTotem lifeTotem;
    private final SpiderTotem spiderTotem;
    private final InfernalTotem infernalTotem;
    private final EconomyIceTotem economyIceTotem;
    private final EconomyFlyTotem economyFlyTotem;
    private final BootNetheriteEssence bootNetheriteEssence;
    private final LegginsNetheriteEssence legginsNetheriteEssence;
    private final ChestplateNetheriteEssence chestplateNetheriteEssence;
    private final HelmetNetheriteEssence helmetNetheriteEssence;
    private final CorruptedUpgrades corruptedUpgrades;
    private final CorruptedSoul corruptedSoul;
    private final CorruptedAncientDebris corruptedAncientDebris;
    private final GuardianShulkerHeart guardianShulkerHeart;
    private final CustomBoat customBoat;
    private final TridenteEspectral tridenteEspectral;
    
    public ItemCreator(JavaPlugin plugin) {
        this.plugin = plugin;
        
        // Inicializar todas las instancias (igual que en ItemsCommands)
        this.doubleLifeTotem = new DoubleLifeTotem(plugin);
        this.lifeTotem = new LifeTotem(plugin);
        this.spiderTotem = new SpiderTotem(plugin);
        this.infernalTotem = new InfernalTotem(plugin);
        this.economyIceTotem = new EconomyIceTotem(plugin);
        this.economyFlyTotem = new EconomyFlyTotem(plugin);
        this.bootNetheriteEssence = new BootNetheriteEssence(plugin);
        this.legginsNetheriteEssence = new LegginsNetheriteEssence(plugin);
        this.chestplateNetheriteEssence = new ChestplateNetheriteEssence(plugin);
        this.helmetNetheriteEssence = new HelmetNetheriteEssence(plugin);
        this.corruptedUpgrades = new CorruptedUpgrades(plugin);
        this.corruptedSoul = new CorruptedSoul(plugin);
        this.corruptedAncientDebris = new CorruptedAncientDebris(plugin);
        this.guardianShulkerHeart = new GuardianShulkerHeart(plugin);
        this.customBoat = new CustomBoat(plugin);
        this.tridenteEspectral = new TridenteEspectral(plugin);
    }
    
    /**
     * Crea un item basado en su nombre
     */
    public ItemStack createItem(String itemName, int amount) {
        // Primero intentar items vanilla
        try {
            Material material = Material.valueOf(itemName.toUpperCase());
            return new ItemStack(material, amount);
        } catch (IllegalArgumentException e) {
            // No es item vanilla, intentar items custom
            return createCustomItem(itemName, amount);
        }
    }
    
    /**
     * Crea items personalizados (copiado de tu método createCustomItem)
     */
    private ItemStack createCustomItem(String itemName, int amount) {
        ItemStack item = null;
        Player target = null; // Para items que requieren target
        
        switch (itemName.toLowerCase()) {
            case "doubletotem":
                item = doubleLifeTotem.createDoubleLifeTotem();
                break;
            case "lifetotem":
                item = lifeTotem.createLifeTotem();
                break;
            case "spidertotem":
                item = spiderTotem.createSpiderTotem();
                break;
            case "infernaltotem":
                item = infernalTotem.createInfernalTotem();
                break;
            case "aguijon_abeja_reina":
                item = QueenBeeHandler.createAguijonAbejaReina();
                break;
            case "upgrade_vacio":
                item = UpgradeNTItems.createUpgradeVacio();
                break;
            case "fragmento_upgrade":
                item = UpgradeNTItems.createFragmentoUpgrade();
                break;
            case "duplicador":
                item = UpgradeNTItems.createDuplicador();
                break;
            case "fragmento_infernal":
                item = EmblemItems.createFragmentoInfernal();
                break;
            case "pepita_infernal":
                item = EmblemItems.createPepitaInfernal();
                break;
            case "corrupted_nether_star":
                item = EmblemItems.createcorruptedNetherStar();
                break;
            case "nether_emblem":
                item = EmblemItems.createNetherEmblem();
                break;
            case "overworld_emblem":
                item = EmblemItems.createOverworldEmblem();
                break;
            case "end_relic":
                item = EmblemItems.createEndEmblem();
                break;
            case "corrupted_steak":
                item = DayOneChanges.corruptedSteak();
                break;
            case "placa_diamante":
                item = EnhancedEnchantmentTable.createDiamondPlate();
                break;
            case "mesa_encantamientos_mejorada":
                item = EnhancedEnchantmentTable.createEnhancedEnchantmentTable();
                break;
            case "casco_night_vision":
                item = NightVisionHelmet.createNightVisionHelmet();
                break;
            case "corrupted_helmet_armor":
                item = CorruptedArmor.createCorruptedHelmet();
                break;
            case "corrupted_chestplate_armor":
                item = CorruptedArmor.createCorruptedChestplate();
                break;
            case "corrupted_leggings_armor":
                item = CorruptedArmor.createCorruptedLeggings();
                break;
            case "corrupted_boots_armor":
                item = CorruptedArmor.createCorruptedBoots();
                break;
            case "enderite_sword":
                item = EnderiteTools.createEnderiteSword();
                break;
            case "enderite_axe":
                item = EnderiteTools.createEnderiteAxe();
                break;
            case "enderite_pickaxe":
                item = EnderiteTools.createEnderitePickaxe();
                break;
            case "enderite_shovel":
                item = EnderiteTools.createEnderiteShovel();
                break;
            case "enderite_hoe":
                item = EnderiteTools.createEnderiteHoe();
                break;
            case "leggins_netherite_essence":
                item = legginsNetheriteEssence.createLegginsNetheriteEssence();
                break;
            case "boot_netherite_essence":
                item = bootNetheriteEssence.createBootNetheriteEssence();
                break;
            case "chestplate_netherite_essence":
                item = chestplateNetheriteEssence.createChestplateNetheriteEssence();
                break;
            case "helmet_netherite_essence":
                item = helmetNetheriteEssence.createHelmetNetheriteEssence();
                break;
            case "helmet_netherite_upgrade":
                item = corruptedUpgrades.createHelmetNetheriteUpgrade();
                break;
            case "chestplate_netherite_upgrade":
                item = corruptedUpgrades.createChestplateNetheriteUpgrade();
                break;
            case "leggins_netherite_upgrade":
                item = corruptedUpgrades.createLeggingsNetheriteUpgrade();
                break;
            case "boot_netherite_upgrade":
                item = corruptedUpgrades.createBootsNetheriteUpgrade();
                break;
            case "cooper_helmet":
                item = CopperArmor.createCopperHelmet();
                break;
            case "cooper_chestplate":
                item = CopperArmor.createCopperChestplate();
                break;
            case "cooper_leggings":
                item = CopperArmor.createCopperLeggings();
                break;
            case "cooper_boots":
                item = CopperArmor.createCopperBoots();
                break;
            case "corrupted_netherite_scrap":
                item = CorruptedNetheriteItems.createCorruptedScrapNetherite();
                break;
            case "corrupted_netherite_ingot":
                item = CorruptedNetheriteItems.createCorruptedNetheriteIngot();
                break;
            case "corrupted_powder":
                item = CorruptedMobItems.createCorruptedPowder();
                break;
            case "corrupted_bone_lime":
                item = CorruptedMobItems.createCorruptedBone(CorruptedMobItems.BoneVariant.LIME);
                break;
            case "corrupted_bone_green":
                item = CorruptedMobItems.createCorruptedBone(CorruptedMobItems.BoneVariant.GREEN);
                break;
            case "corrupted_bone_yellow":
                item = CorruptedMobItems.createCorruptedBone(CorruptedMobItems.BoneVariant.YELLOW);
                break;
            case "corrupted_bone_orange":
                item = CorruptedMobItems.createCorruptedBone(CorruptedMobItems.BoneVariant.ORANGE);
                break;
            case "corrupted_bone_red":
                item = CorruptedMobItems.createCorruptedBone(CorruptedMobItems.BoneVariant.RED);
                break;
            case "corrupted_rotten":
                item = CorruptedMobItems.createCorruptedMeet();
                break;
            case "corrupted_spidereyes":
                item = CorruptedMobItems.createCorruptedSpiderEye();
                break;
            case "corrupted_soul":
                item = corruptedSoul.createCorruptedSoulEssence();
                break;
            
            // BLOQUES
            case "corrupted_ancient_debris":
                item = corruptedAncientDebris.createcorruptedancientdebris();
                break;
            case "guardian_shulker_heart":
                item = guardianShulkerHeart.createGuardianShulkerHeart();
                break;
            case "endstalactitas":
                item = Endstalactitas.createEndstalactita();
                break;
            
            // VARIOS
            case "toxicspidereye":
                item = ItemsTotems.createToxicSpiderEye();
                break;
            case "infernalcreeperpowder":
                item = ItemsTotems.createInfernalCreeperPowder();
                break;
            case "whiteenderpearl":
                item = ItemsTotems.createWhiteEnderPearl();
                break;
            case "specialtotem":
                item = ItemsTotems.createSpecialTotem();
                break;
            case "customboat":
                item = customBoat.createBoatItem(target);
                break;
            case "fuel":
                item = customBoat.createFuelItem();
                break;
            case "varita_guardian_blaze":
                item = BlazeItems.createBlazeRod();
                break;
            case "polvo_guardian_blaze":
                item = BlazeItems.createGuardianBlazePowder();
                break;
            case "ultra_pocion_resistencia_fuego":
                item = BlazeItems.createPotionOfFireResistance();
                break;
            case "guardian_shulker_shell":
                item = EndItems.createGuardianShulkerShell();
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
            case "enderite_ingot":
                item = EndItems.createIngotEnderite();
                break;
            case "enderite_upgrades":
                item = EndItems.createEnderiteUpgrades();
                break;
            
            // Economy Items
            case "vithiums":
                item = EconomyItems.createVithiumCoin();
                break;
            case "vithiums_fichas":
                item = EconomyItems.createVithiumToken();
                break;
            case "mochila":
                item = EconomyItems.createNormalMochila();
                break;
            case "mochila_verde":
                item = EconomyItems.createGreenMochila();
                break;
            case "mochila_roja":
                item = EconomyItems.createRedMochila();
                break;
            case "mochila_azul":
                item = EconomyItems.createBlueMochila();
                break;
            case "mochila_morada":
                item = EconomyItems.createPurpleMochila();
                break;
            case "mochila_negra":
                item = EconomyItems.createBlackMochila();
                break;
            case "mochila_blanca":
                item = EconomyItems.createWhiteMochila();
                break;
            case "mochila_amarilla":
                item = EconomyItems.createYellowMochila();
                break;
            case "enderbag":
                item = EconomyItems.createEnderBag();
                break;
            case "gancho":
                item = EconomyItems.createGancho();
                break;
            case "panic_apple":
                item = EconomyItems.createManzanaPanico();
                break;
            case "yunque_nivel_1":
                item = EconomyItems.createYunqueReparadorNivel1();
                break;
            case "yunque_nivel_2":
                item = EconomyItems.createYunqueReparadorNivel2();
                break;
            case "icetotem":
                item = economyIceTotem.createIceTotem();
                break;
            case "flytotem":
                item = economyFlyTotem.createFlyTotem();
                break;
            
            // Otros Items
            case "corrupted_golden_apple":
                item = CorruptedGoldenApple.createCorruptedGoldenApple();
                break;
            case "apilate_gold_block":
                item = CorruptedGoldenApple.createApilateGoldBlock();
                break;
            case "orbe_de_vida":
                item = ReviveItems.createResurrectOrb();
                break;
            case "wither_compass":
                item = UltraWitherCompass.createUltraWitherCompass();
                break;
            case "icecrystal":
                item = ItemsTotems.createIceCrystal();
                break;
            case "tridente_espectral":
                item = tridenteEspectral.createSpectralTrident();
                break;
            default:
                return null;
        }
        
        if (item != null) {
            item.setAmount(amount);
        }
        
        return item;
    }
}
package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class PoisonEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public PoisonEnchantment() {
        super("Poison", "Apnuodija priešus lėtai pažeisdamas", 4,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.BOW, Material.CROSSBOW, Material.TRIDENT);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Chance to poison: 30% per level
            double poisonChance = 0.30 * level;
            
            if (random.nextDouble() < poisonChance) {
                if (event.getEntity() instanceof org.bukkit.entity.LivingEntity) {
                    org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) event.getEntity();
                    
                    // Apply poison effect
                    int duration = 20 * (3 + level * 2); // 3+ seconds, increases with level
                    int amplifier = Math.max(0, level - 1); // Poison strength
                    
                    target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.POISON,
                        duration,
                        amplifier
                    ));
                    
                    // Add nausea for higher levels
                    if (level >= 3) {
                        target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.CONFUSION,
                            20 * 5, // 5 seconds
                            0
                        ));
                    }
                    
                    // Add hunger effect for maximum level
                    if (level >= 4) {
                        target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.HUNGER,
                            duration,
                            1
                        ));
                    }
                    
                    // Visual effects - poison particles
                    target.getWorld().spawnParticle(
                        org.bukkit.Particle.ITEM_CRACK,
                        target.getLocation().add(0, 1, 0),
                        15 + (level * 3),
                        0.5, 1.0, 0.5,
                        0.1,
                        new org.bukkit.inventory.ItemStack(Material.SPIDER_EYE)
                    );
                    
                    target.getWorld().spawnParticle(
                        org.bukkit.Particle.SPELL_MOB,
                        target.getLocation().add(0, 1, 0),
                        10,
                        0.5, 1.0, 0.5,
                        0.1
                    );
                    
                    // Sound effect
                    target.getWorld().playSound(
                        target.getLocation(),
                        org.bukkit.Sound.ENTITY_SPIDER_HURT,
                        0.8f,
                        0.8f
                    );
                    
                    // Create poison cloud effect
                    createPoisonCloud(target.getLocation(), level);
                    
                    attacker.sendMessage("§2☠ Priešas apnuodytas!");
                    
                    // Notify target if it's a player
                    if (target instanceof Player) {
                        Player targetPlayer = (Player) target;
                        targetPlayer.sendMessage("§2☠ Esi apnuodytas!");
                    }
                }
            }
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Defensive poison - poison attackers when hit
        if (event.getEntity() instanceof Player && event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent attackEvent = (EntityDamageByEntityEvent) event;
            Player defender = (Player) event.getEntity();
            
            // Lower chance for defensive poison: 15% per level
            double poisonChance = 0.15 * level;
            
            if (random.nextDouble() < poisonChance) {
                if (attackEvent.getDamager() instanceof org.bukkit.entity.LivingEntity) {
                    org.bukkit.entity.LivingEntity attacker = (org.bukkit.entity.LivingEntity) attackEvent.getDamager();
                    
                    // Apply poison to attacker
                    attacker.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.POISON,
                        20 * (2 + level), // 2+ seconds
                        level - 1
                    ));
                    
                    // Visual effects
                    attacker.getWorld().spawnParticle(
                        org.bukkit.Particle.SPELL_MOB,
                        attacker.getLocation().add(0, 1, 0),
                        8,
                        0.3, 0.8, 0.3,
                        0.1
                    );
                    
                    defender.sendMessage("§2☠ Apnuodijote savo užpuoliką!");
                    
                    if (attacker instanceof Player) {
                        Player attackerPlayer = (Player) attacker;
                        attackerPlayer.sendMessage("§2☠ Gynėjas apnuodijo jus!");
                    }
                }
            }
        }
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Not directly applicable, but could add poison effect when mining certain blocks
        if (event.getBlock().getType() == Material.SPIDER_EYE || 
            event.getBlock().getType().toString().contains("MUSHROOM")) {
            
            // Give player brief poison immunity when breaking poison-related blocks
            event.getPlayer().addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE,
                20 * 5, // 5 seconds
                0
            ));
            
            event.getPlayer().sendMessage("§2☠ Nuodo atsparumas dėl patirties!");
        }
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    private void createPoisonCloud(org.bukkit.Location location, int level) {
        // Create a lingering poison effect area
        for (int i = 0; i < level * 3; i++) {
            org.bukkit.Bukkit.getScheduler().runTaskLater(
                lt.enchanter.EnchanterPlugin.getInstance(),
                () -> {
                    location.getWorld().spawnParticle(
                        org.bukkit.Particle.SPELL_MOB,
                        location.clone().add(
                            (random.nextDouble() - 0.5) * 3,
                            random.nextDouble() * 2,
                            (random.nextDouble() - 0.5) * 3
                        ),
                        3,
                        0.2, 0.2, 0.2,
                        0.05
                    );
                },
                i * 2L // Spread over time
            );
        }
    }
}
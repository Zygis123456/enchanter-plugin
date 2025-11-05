package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class LightningEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public LightningEnchantment() {
        super("Lightning", "Šansas iškviesti žaibą ant priešo", 3,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Chance to trigger lightning: 10% per level
            double lightningChance = 0.10 * level;
            
            if (random.nextDouble() < lightningChance) {
                // Strike lightning at target location
                org.bukkit.Bukkit.getScheduler().runTask(
                    lt.enchanter.EnchanterPlugin.getInstance(),
                    () -> {
                        // Strike lightning (visual effect only, no fire)
                        event.getEntity().getWorld().strikeLightningEffect(
                            event.getEntity().getLocation()
                        );
                        
                        // Add extra damage
                        double extraDamage = 2.0 * level;
                        event.getEntity().damage(extraDamage);
                        
                        // Stunning effect (briefly immobilize target)
                        if (event.getEntity() instanceof org.bukkit.entity.LivingEntity) {
                            org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) event.getEntity();
                            
                            // Add slowness effect
                            target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                                org.bukkit.potion.PotionEffectType.SLOW,
                                20 * 2, // 2 seconds
                                level - 1 // Intensity based on level
                            ));
                            
                            // Add blindness for dramatic effect
                            target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                                org.bukkit.potion.PotionEffectType.BLINDNESS,
                                20, // 1 second
                                0
                            ));
                        }
                        
                        // Visual and sound effects
                        event.getEntity().getWorld().spawnParticle(
                            org.bukkit.Particle.ELECTRIC_SPARK,
                            event.getEntity().getLocation(),
                            20,
                            1.0, 2.0, 1.0,
                            0.1
                        );
                        
                        // Send message to attacker
                        attacker.sendMessage("§e⚡ Žaibas trenkė priešą!");
                    }
                );
            }
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Lightning chance when mining
        double lightningChance = 0.05 * level;
        
        if (random.nextDouble() < lightningChance) {
            org.bukkit.Bukkit.getScheduler().runTask(
                lt.enchanter.EnchanterPlugin.getInstance(),
                () -> {
                    event.getBlock().getWorld().strikeLightningEffect(
                        event.getBlock().getLocation()
                    );
                    
                    // Boost mining (give speed effect)
                    event.getPlayer().addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.FAST_DIGGING,
                        20 * 10, // 10 seconds
                        level - 1
                    ));
                    
                    event.getPlayer().sendMessage("§e⚡ Žaibo jėga pagreitina tavo veiklą!");
                }
            );
        }
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
}
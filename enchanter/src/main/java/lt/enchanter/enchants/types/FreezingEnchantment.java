package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class FreezingEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public FreezingEnchantment() {
        super("Freezing", "Užšaldo priešus lėtindamas juos", 4,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Chance to freeze: 25% per level
            double freezeChance = 0.25 * level;
            
            if (random.nextDouble() < freezeChance) {
                if (event.getEntity() instanceof org.bukkit.entity.LivingEntity) {
                    org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) event.getEntity();
                    
                    // Apply freezing effects
                    int duration = 20 * (2 + level); // 2+ seconds based on level
                    
                    // Slowness effect
                    target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOW,
                        duration,
                        level - 1 // Intensity based on level
                    ));
                    
                    // Mining fatigue (slower attacks)
                    target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOW_DIGGING,
                        duration,
                        level - 1
                    ));
                    
                    // Visual effects - ice particles
                    target.getWorld().spawnParticle(
                        org.bukkit.Particle.SNOWBALL,
                        target.getLocation().add(0, 1, 0),
                        20 + (level * 5),
                        0.5, 1.0, 0.5,
                        0.1
                    );
                    
                    target.getWorld().spawnParticle(
                        org.bukkit.Particle.CLOUD,
                        target.getLocation().add(0, 0.5, 0),
                        10 + (level * 2),
                        0.3, 0.3, 0.3,
                        0.05
                    );
                    
                    // Sound effect
                    target.getWorld().playSound(
                        target.getLocation(),
                        org.bukkit.Sound.BLOCK_GLASS_BREAK,
                        0.8f,
                        2.0f
                    );
                    
                    // Create ice blocks around target temporarily
                    createTemporaryIce(target, level);
                    
                    attacker.sendMessage("§b❄ Priešas užšaldytas!");
                    
                    // Notify target if it's a player
                    if (target instanceof Player) {
                        Player targetPlayer = (Player) target;
                        targetPlayer.sendMessage("§b❄ Esi užšaldytas!");
                    }
                }
            }
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Freeze water when mining
        if (event.getBlock().getType() == Material.WATER) {
            event.getBlock().setType(Material.ICE);
            
            event.getPlayer().sendMessage("§b❄ Vanduo užšaldytas!");
        }
        
        // Chance to create ice path
        double iceChance = 0.10 * level;
        if (random.nextDouble() < iceChance) {
            // Create temporary ice blocks around the broken block
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) continue; // Skip center block
                    
                    org.bukkit.block.Block nearbyBlock = event.getBlock().getRelative(x, 0, z);
                    if (nearbyBlock.getType() == Material.WATER) {
                        nearbyBlock.setType(Material.ICE);
                        
                        // Schedule ice to melt after some time
                        org.bukkit.Bukkit.getScheduler().runTaskLater(
                            lt.enchanter.EnchanterPlugin.getInstance(),
                            () -> {
                                if (nearbyBlock.getType() == Material.ICE) {
                                    nearbyBlock.setType(Material.WATER);
                                }
                            },
                            20L * 30 // 30 seconds
                        );
                    }
                }
            }
        }
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    private void createTemporaryIce(org.bukkit.entity.LivingEntity target, int level) {
        if (level < 3) return; // Only at higher levels
        
        org.bukkit.Location loc = target.getLocation();
        
        // Create ice blocks around the target's feet
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue; // Skip center
                
                org.bukkit.block.Block block = loc.getWorld().getBlockAt(
                    loc.getBlockX() + x,
                    loc.getBlockY() - 1,
                    loc.getBlockZ() + z
                );
                
                Material originalType = block.getType();
                
                // Only replace air or certain blocks
                if (originalType == Material.AIR || originalType == Material.WATER) {
                    block.setType(Material.ICE);
                    
                    // Schedule removal of ice
                    org.bukkit.Bukkit.getScheduler().runTaskLater(
                        lt.enchanter.EnchanterPlugin.getInstance(),
                        () -> {
                            if (block.getType() == Material.ICE) {
                                block.setType(originalType);
                            }
                        },
                        20L * 5 // Remove after 5 seconds
                    );
                }
            }
        }
    }
}
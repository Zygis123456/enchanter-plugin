package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ExplosiveEnchantment extends CustomEnchantment {
    
    public ExplosiveEnchantment() {
        super("Explosive", "Sukelia sprogimą pažeidžiant priešą", 3,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD, 
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Create explosion at target location
            float power = 1.0f + (level * 0.5f); // Power increases with level
            
            // Schedule explosion for next tick to avoid canceling the damage event
            org.bukkit.Bukkit.getScheduler().runTask(
                lt.enchanter.EnchanterPlugin.getInstance(), 
                () -> {
                    event.getEntity().getWorld().createExplosion(
                        event.getEntity().getLocation(), 
                        power, 
                        false, // No block damage
                        false  // No fire
                    );
                    
                    // Add explosion effects
                    event.getEntity().getWorld().spawnParticle(
                        org.bukkit.Particle.EXPLOSION_LARGE,
                        event.getEntity().getLocation(),
                        level * 2
                    );
                    
                    // Play explosion sound
                    event.getEntity().getWorld().playSound(
                        event.getEntity().getLocation(),
                        org.bukkit.Sound.ENTITY_GENERIC_EXPLODE,
                        1.0f,
                        1.0f
                    );
                }
            );
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Create explosion when breaking blocks
        float power = 0.5f + (level * 0.3f);
        
        org.bukkit.Bukkit.getScheduler().runTask(
            lt.enchanter.EnchanterPlugin.getInstance(),
            () -> {
                event.getBlock().getWorld().createExplosion(
                    event.getBlock().getLocation(),
                    power,
                    false, // No block damage to prevent griefing
                    false  // No fire
                );
                
                // Add visual effects
                event.getBlock().getWorld().spawnParticle(
                    org.bukkit.Particle.EXPLOSION_NORMAL,
                    event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                    level * 3,
                    0.5, 0.5, 0.5,
                    0.1
                );
            }
        );
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
}
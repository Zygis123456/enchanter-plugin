package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LifestealEnchantment extends CustomEnchantment {
    
    public LifestealEnchantment() {
        super("Lifesteal", "Atgauna gyvybę pažeidžiant priešus", 5,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Calculate heal amount (10% of damage dealt per level)
            double damage = event.getFinalDamage();
            double healAmount = damage * (0.10 * level);
            
            // Heal the attacker
            double currentHealth = attacker.getHealth();
            double maxHealth = attacker.getMaxHealth();
            double newHealth = Math.min(currentHealth + healAmount, maxHealth);
            
            attacker.setHealth(newHealth);
            
            // Visual and audio effects
            attacker.getWorld().spawnParticle(
                org.bukkit.Particle.HEART,
                attacker.getLocation().add(0, 1, 0),
                level * 2,
                0.5, 0.5, 0.5,
                0.1
            );
            
            attacker.playSound(
                attacker.getLocation(),
                org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                0.5f,
                2.0f
            );
            
            // Send message to player if significant heal
            if (healAmount >= 1.0) {
                attacker.sendMessage("§a+ " + String.format("%.1f", healAmount) + " ❤");
            }
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
}
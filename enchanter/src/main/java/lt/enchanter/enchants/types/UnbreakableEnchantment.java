package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class UnbreakableEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public UnbreakableEnchantment() {
        super("Unbreakable", "Šansas, kad daiktas nepraras tvarumo", 5);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        // Prevent durability loss on weapon attack
        if (event.getDamager() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            
            preventDurabilityLoss(weapon, level);
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        // Prevent durability loss on armor
        if (event.getEntity() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getEntity();
            
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor != null && hasEnchantment(armor)) {
                    preventDurabilityLoss(armor, level);
                }
            }
        }
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Prevent durability loss on tool use
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        preventDurabilityLoss(tool, level);
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not needed for this enchantment
    }
    
    private void preventDurabilityLoss(ItemStack item, int level) {
        if (item == null || item.getType() == Material.AIR) return;
        
        // Calculate chance based on level (20% per level, max 100% at level 5)
        double chance = Math.min(0.20 * level, 1.0);
        
        if (random.nextDouble() < chance) {
            // Restore 1 durability point to offset the loss
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                int currentDamage = damageable.getDamage();
                if (currentDamage > 0) {
                    damageable.setDamage(currentDamage - 1);
                    item.setItemMeta(meta);
                }
            }
        }
    }
    
    private boolean hasEnchantment(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;
        
        for (String line : meta.getLore()) {
            if (line.startsWith("§d" + getName())) {
                return true;
            }
        }
        return false;
    }
}
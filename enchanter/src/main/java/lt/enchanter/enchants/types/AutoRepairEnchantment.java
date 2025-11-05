package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class AutoRepairEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public AutoRepairEnchantment() {
        super("AutoRepair", "Automatiškai taiso daiktą laikui bėgant", 3);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            attemptRepair(weapon, level, player);
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Repair armor pieces
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor != null && hasEnchantment(armor)) {
                    attemptRepair(armor, level, player);
                }
            }
        }
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        attemptRepair(tool, level, player);
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Repair on use
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        attemptRepair(item, level, player);
    }
    
    private void attemptRepair(ItemStack item, int level, Player player) {
        if (item == null || item.getType() == Material.AIR) return;
        
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable)) return;
        
        Damageable damageable = (Damageable) meta;
        int currentDamage = damageable.getDamage();
        
        if (currentDamage == 0) return; // Item is already at full durability
        
        // Chance to repair: 5% per level per use
        double repairChance = 0.05 * level;
        
        if (random.nextDouble() < repairChance) {
            // Repair 1-3 points based on level
            int repairAmount = random.nextInt(level) + 1;
            int newDamage = Math.max(0, currentDamage - repairAmount);
            
            damageable.setDamage(newDamage);
            item.setItemMeta(meta);
            
            // Visual effects
            player.getWorld().spawnParticle(
                org.bukkit.Particle.VILLAGER_HAPPY,
                player.getLocation().add(0, 1, 0),
                5,
                0.5, 0.5, 0.5,
                0.1
            );
            
            player.playSound(
                player.getLocation(),
                org.bukkit.Sound.BLOCK_ANVIL_USE,
                0.3f,
                2.0f
            );
            
            // Send subtle message
            if (repairAmount > 1) {
                player.sendMessage("§e⚒ Daiktas automatiškai sutaisytas!");
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
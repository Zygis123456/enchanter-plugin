package lt.enchanter.listeners;

import lt.enchanter.EnchanterPlugin;
import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnchantmentListener implements Listener {
    
    private final EnchanterPlugin plugin;
    
    public EnchantmentListener(EnchanterPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        
        // Handle attacker enchantments
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            ItemStack weapon = attacker.getInventory().getItemInMainHand();
            
            if (weapon != null && !weapon.getType().isAir()) {
                List<CustomEnchantment> enchantments = plugin.getEnchantmentManager().getItemEnchantments(weapon);
                
                for (CustomEnchantment enchantment : enchantments) {
                    int level = plugin.getEnchantmentManager().getEnchantmentLevel(weapon, enchantment.getName());
                    
                    try {
                        enchantment.onAttack(event, level);
                    } catch (Exception e) {
                        plugin.getLogger().warning("Error executing enchantment " + enchantment.getName() + " onAttack: " + e.getMessage());
                    }
                }
            }
        }
        
        // Handle defender enchantments (armor)
        if (event.getEntity() instanceof Player) {
            Player defender = (Player) event.getEntity();
            
            // Check all armor pieces
            for (ItemStack armorPiece : defender.getInventory().getArmorContents()) {
                if (armorPiece != null && !armorPiece.getType().isAir()) {
                    List<CustomEnchantment> enchantments = plugin.getEnchantmentManager().getItemEnchantments(armorPiece);
                    
                    for (CustomEnchantment enchantment : enchantments) {
                        int level = plugin.getEnchantmentManager().getEnchantmentLevel(armorPiece, enchantment.getName());
                        
                        try {
                            enchantment.onDefend(event, level);
                        } catch (Exception e) {
                            plugin.getLogger().warning("Error executing enchantment " + enchantment.getName() + " onDefend: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        
        // Handle general damage events (not just entity vs entity)
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Check all armor pieces for defensive enchantments
            for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
                if (armorPiece != null && !armorPiece.getType().isAir()) {
                    List<CustomEnchantment> enchantments = plugin.getEnchantmentManager().getItemEnchantments(armorPiece);
                    
                    for (CustomEnchantment enchantment : enchantments) {
                        int level = plugin.getEnchantmentManager().getEnchantmentLevel(armorPiece, enchantment.getName());
                        
                        try {
                            enchantment.onDefend(event, level);
                        } catch (Exception e) {
                            plugin.getLogger().warning("Error executing enchantment " + enchantment.getName() + " onDefend: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        if (tool != null && !tool.getType().isAir()) {
            List<CustomEnchantment> enchantments = plugin.getEnchantmentManager().getItemEnchantments(tool);
            
            for (CustomEnchantment enchantment : enchantments) {
                int level = plugin.getEnchantmentManager().getEnchantmentLevel(tool, enchantment.getName());
                
                try {
                    enchantment.onBreakBlock(event, level);
                } catch (Exception e) {
                    plugin.getLogger().warning("Error executing enchantment " + enchantment.getName() + " onBreakBlock: " + e.getMessage());
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item != null && !item.getType().isAir()) {
            List<CustomEnchantment> enchantments = plugin.getEnchantmentManager().getItemEnchantments(item);
            
            for (CustomEnchantment enchantment : enchantments) {
                int level = plugin.getEnchantmentManager().getEnchantmentLevel(item, enchantment.getName());
                
                try {
                    enchantment.onInteract(event, level);
                } catch (Exception e) {
                    plugin.getLogger().warning("Error executing enchantment " + enchantment.getName() + " onInteract: " + e.getMessage());
                }
            }
        }
    }
    
    // Additional event for player join - to show welcome message about custom enchants
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Send welcome message after a short delay
        org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§6✨ Šiame serveryje veikia custom enchantmentai!");
            player.sendMessage("§7Naudokite §e/listenchants §7kad pamatytumėte visus galimus enchantmentus.");
            player.sendMessage("§7Naudokite §e/enchant <pavadinimas> [lygis] §7kad pritaikytumėte enchantmentą.");
        }, 60L); // 3 second delay
    }
}
package lt.enchanter.enchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CustomEnchantment {
    
    protected final String name;
    protected final String description;
    protected final int maxLevel;
    protected final List<Material> compatibleMaterials;
    
    public CustomEnchantment(String name, String description, int maxLevel, Material... materials) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.compatibleMaterials = Arrays.asList(materials);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public List<Material> getCompatibleMaterials() {
        return compatibleMaterials;
    }
    
    public boolean isCompatible(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        
        // If no specific materials are defined, allow all items
        if (compatibleMaterials.isEmpty()) return true;
        
        return compatibleMaterials.contains(item.getType());
    }
    
    public ItemStack applyToItem(ItemStack item, int level) {
        if (!isCompatible(item)) return item;
        if (level < 1 || level > maxLevel) level = 1;
        
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        
        if (meta == null) return newItem;
        
        // Get current lore
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        
        // Remove existing enchantment if present
        lore.removeIf(line -> line.startsWith("§d" + name));
        
        // Add new enchantment lore
        String enchantLore = "§d" + name + " §b" + level;
        lore.add(enchantLore);
        
        // Add description as separate line
        lore.add("§7" + description);
        
        meta.setLore(lore);
        newItem.setItemMeta(meta);
        
        return newItem;
    }
    
    // Abstract methods to be implemented by specific enchantments
    public abstract void onAttack(org.bukkit.event.entity.EntityDamageByEntityEvent event, int level);
    public abstract void onDefend(org.bukkit.event.entity.EntityDamageEvent event, int level);
    public abstract void onBreakBlock(org.bukkit.event.block.BlockBreakEvent event, int level);
    public abstract void onInteract(org.bukkit.event.player.PlayerInteractEvent event, int level);
}
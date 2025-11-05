package lt.enchanter.enchants;

import lt.enchanter.enchants.types.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentManager {
    
    private final Map<String, CustomEnchantment> enchantments;
    
    public EnchantmentManager() {
        enchantments = new HashMap<>();
        registerEnchantments();
    }
    
    private void registerEnchantments() {
        // Register all custom enchantments
        registerEnchantment(new UnbreakableEnchantment());
        registerEnchantment(new ExplosiveEnchantment());
        registerEnchantment(new LifestealEnchantment());
        registerEnchantment(new AutoRepairEnchantment());
        registerEnchantment(new LightningEnchantment());
        registerEnchantment(new TeleportEnchantment());
        registerEnchantment(new FreezingEnchantment());
        registerEnchantment(new PoisonEnchantment());
    }
    
    private void registerEnchantment(CustomEnchantment enchantment) {
        enchantments.put(enchantment.getName().toLowerCase(), enchantment);
    }
    
    public CustomEnchantment getEnchantment(String name) {
        return enchantments.get(name.toLowerCase());
    }
    
    public Set<String> getEnchantmentNames() {
        return enchantments.keySet();
    }
    
    public Collection<CustomEnchantment> getAllEnchantments() {
        return enchantments.values();
    }
    
    public boolean hasEnchantment(ItemStack item, String enchantName) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;
        
        List<String> lore = meta.getLore();
        String enchantLore = "§d" + enchantName;
        
        for (String line : lore) {
            if (line.startsWith(enchantLore)) {
                return true;
            }
        }
        return false;
    }
    
    public int getEnchantmentLevel(ItemStack item, String enchantName) {
        if (item == null || !item.hasItemMeta()) return 0;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return 0;
        
        List<String> lore = meta.getLore();
        String enchantLore = "§d" + enchantName;
        
        for (String line : lore) {
            if (line.startsWith(enchantLore)) {
                // Extract level from lore (format: "§dEnchantName §bLevel")
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    try {
                        String levelStr = parts[1].replace("§b", "").trim();
                        return Integer.parseInt(levelStr);
                    } catch (NumberFormatException e) {
                        return 1; // Default level
                    }
                }
                return 1; // Default level
            }
        }
        return 0;
    }
    
    public ItemStack applyEnchantment(ItemStack item, String enchantName, int level) {
        CustomEnchantment enchantment = getEnchantment(enchantName);
        if (enchantment == null) return item;
        
        return enchantment.applyToItem(item, level);
    }
    
    public List<CustomEnchantment> getItemEnchantments(ItemStack item) {
        List<CustomEnchantment> itemEnchants = new ArrayList<>();
        
        if (item == null || !item.hasItemMeta()) return itemEnchants;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return itemEnchants;
        
        List<String> lore = meta.getLore();
        
        for (CustomEnchantment enchant : getAllEnchantments()) {
            if (hasEnchantment(item, enchant.getName())) {
                itemEnchants.add(enchant);
            }
        }
        
        return itemEnchants;
    }
}
package lt.enchanter.commands;

import lt.enchanter.EnchanterPlugin;
import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ListEnchantsCommand implements CommandExecutor {
    
    private final EnchanterPlugin plugin;
    
    public ListEnchantsCommand(EnchanterPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enchanter.list")) {
            sender.sendMessage("§cNeturite teisės peržiūrėti enchantų sąrašo!");
            return true;
        }
        
        // Check if player wants to see enchants on their current item
        if (args.length > 0 && args[0].equalsIgnoreCase("item") && sender instanceof Player) {
            return showItemEnchants((Player) sender);
        }
        
        // Show all available enchantments
        sender.sendMessage("§6=== Galimi Custom Enchantmentai ===");
        sender.sendMessage("");
        
        int count = 0;
        for (CustomEnchantment enchant : plugin.getEnchantmentManager().getAllEnchantments()) {
            count++;
            
            // Enchantment name and max level
            sender.sendMessage("§d" + count + ". " + enchant.getName() + " §b(Max: " + enchant.getMaxLevel() + ")");
            
            // Description
            sender.sendMessage("   §7" + enchant.getDescription());
            
            // Compatible materials (if specific ones are defined)
            if (!enchant.getCompatibleMaterials().isEmpty() && enchant.getCompatibleMaterials().size() <= 10) {
                StringBuilder materials = new StringBuilder("   §eSuberinami daiktai: ");
                boolean first = true;
                for (org.bukkit.Material material : enchant.getCompatibleMaterials()) {
                    if (!first) materials.append(", ");
                    materials.append(material.toString().toLowerCase().replace("_", " "));
                    first = false;
                }
                sender.sendMessage(materials.toString());
            } else if (!enchant.getCompatibleMaterials().isEmpty()) {
                sender.sendMessage("   §eSuberinamas su specifiniais daiktais");
            } else {
                sender.sendMessage("   §eSuberinamas su visais daiktais");
            }
            
            sender.sendMessage("");
        }
        
        sender.sendMessage("§6Viso enchantų: §e" + count);
        sender.sendMessage("§7Naudojimas: /enchant <enchantmentas> [lygis] [žaidėjas]");
        
        if (sender instanceof Player) {
            sender.sendMessage("§7Daikto enchantai: /listenchants item");
        }
        
        return true;
    }
    
    private boolean showItemEnchants(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item.getType().isAir()) {
            player.sendMessage("§cNelaikai jokio daikto rankoje!");
            return true;
        }
        
        List<CustomEnchantment> itemEnchants = plugin.getEnchantmentManager().getItemEnchantments(item);
        
        if (itemEnchants.isEmpty()) {
            player.sendMessage("§eŠis daiktas neturi custom enchantmentų.");
            return true;
        }
        
        player.sendMessage("§6=== Daikto Enchantmentai ===");
        player.sendMessage("§7Daiktas: §e" + item.getType().toString().toLowerCase().replace("_", " "));
        player.sendMessage("");
        
        for (CustomEnchantment enchant : itemEnchants) {
            int level = plugin.getEnchantmentManager().getEnchantmentLevel(item, enchant.getName());
            
            player.sendMessage("§d• " + enchant.getName() + " §b" + level + "§7/" + enchant.getMaxLevel());
            player.sendMessage("  §7" + enchant.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage("§7Viso enchantų ant daikto: §e" + itemEnchants.size());
        
        return true;
    }
}
package lt.enchanter.commands;

import lt.enchanter.EnchanterPlugin;
import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantCommand implements CommandExecutor, TabCompleter {
    
    private final EnchanterPlugin plugin;
    
    public EnchantCommand(EnchanterPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cNaudojimas: /enchant <enchantment> [level] [player]");
            sender.sendMessage("§7Galimi enchantai: " + String.join(", ", plugin.getEnchantmentManager().getEnchantmentNames()));
            return true;
        }
        
        String enchantName = args[0];
        int level = 1;
        Player targetPlayer = null;
        
        // Parse level
        if (args.length >= 2) {
            try {
                level = Integer.parseInt(args[1]);
                if (level < 1) level = 1;
            } catch (NumberFormatException e) {
                sender.sendMessage("§cNeteisingas lygis! Naudojamas lygis 1.");
                level = 1;
            }
        }
        
        // Parse target player
        if (args.length >= 3) {
            if (!sender.hasPermission("enchanter.enchant.others")) {
                sender.sendMessage("§cNeturite teisės enchanti kitų žaidėjų daiktus!");
                return true;
            }
            
            targetPlayer = Bukkit.getPlayer(args[2]);
            if (targetPlayer == null) {
                sender.sendMessage("§cŽaidėjas '" + args[2] + "' nerastas!");
                return true;
            }
        } else if (sender instanceof Player) {
            targetPlayer = (Player) sender;
        } else {
            sender.sendMessage("§cTik žaidėjai gali naudoti šią komandą be tikslo!");
            return true;
        }
        
        // Get the enchantment
        CustomEnchantment enchantment = plugin.getEnchantmentManager().getEnchantment(enchantName);
        if (enchantment == null) {
            sender.sendMessage("§cNežinomas enchantmentas: " + enchantName);
            sender.sendMessage("§7Galimi enchantai: " + String.join(", ", plugin.getEnchantmentManager().getEnchantmentNames()));
            return true;
        }
        
        // Check level limits
        if (level > enchantment.getMaxLevel()) {
            level = enchantment.getMaxLevel();
            sender.sendMessage("§eMaximalus lygis šiam enchantui yra " + level + ". Naudojamas maksimalus lygis.");
        }
        
        // Get the item in hand
        ItemStack item = targetPlayer.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage("§c" + targetPlayer.getName() + " nelaiko jokio daikto rankoje!");
            return true;
        }
        
        // Check compatibility
        if (!enchantment.isCompatible(item)) {
            sender.sendMessage("§cŠis enchantmentas nesuderinamas su šiuo daiktu!");
            if (!enchantment.getCompatibleMaterials().isEmpty()) {
                sender.sendMessage("§7Suderinami daiktai: " + 
                    enchantment.getCompatibleMaterials().stream()
                        .map(material -> material.toString().toLowerCase().replace("_", " "))
                        .collect(Collectors.joining(", ")));
            }
            return true;
        }
        
        // Apply the enchantment
        ItemStack enchantedItem = plugin.getEnchantmentManager().applyEnchantment(item, enchantName, level);
        targetPlayer.getInventory().setItemInMainHand(enchantedItem);
        
        // Success messages
        String enchantDisplayName = "§d" + enchantment.getName() + " " + level;
        
        if (sender.equals(targetPlayer)) {
            sender.sendMessage("§aEnchantmentas " + enchantDisplayName + " §asėkmingai pritaikytas!");
        } else {
            sender.sendMessage("§aEnchantmentas " + enchantDisplayName + " §asėkmingai pritaikytas žaidėjui " + targetPlayer.getName());
            targetPlayer.sendMessage("§aJūsų daiktas buvo enchantintas su " + enchantDisplayName + "§a!");
        }
        
        // Visual effects
        targetPlayer.getWorld().spawnParticle(
            org.bukkit.Particle.ENCHANTMENT_TABLE,
            targetPlayer.getLocation().add(0, 1, 0),
            20,
            0.5, 1.0, 0.5,
            1.0
        );
        
        targetPlayer.playSound(
            targetPlayer.getLocation(),
            org.bukkit.Sound.BLOCK_ENCHANTMENT_TABLE_USE,
            1.0f,
            1.0f
        );
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Complete enchantment names
            String partial = args[0].toLowerCase();
            for (String enchantName : plugin.getEnchantmentManager().getEnchantmentNames()) {
                if (enchantName.toLowerCase().startsWith(partial)) {
                    completions.add(enchantName);
                }
            }
        } else if (args.length == 2) {
            // Complete levels
            CustomEnchantment enchantment = plugin.getEnchantmentManager().getEnchantment(args[0]);
            if (enchantment != null) {
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    completions.add(String.valueOf(i));
                }
            }
        } else if (args.length == 3) {
            // Complete player names (if has permission)
            if (sender.hasPermission("enchanter.enchant.others")) {
                String partial = args[2].toLowerCase();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(partial)) {
                        completions.add(player.getName());
                    }
                }
            }
        }
        
        return completions;
    }
}
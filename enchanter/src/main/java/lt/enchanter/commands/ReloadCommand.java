package lt.enchanter.commands;

import lt.enchanter.EnchanterPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    
    private final EnchanterPlugin plugin;
    
    public ReloadCommand(EnchanterPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enchanter.reload")) {
            sender.sendMessage("§cNeturite teisės atnaujinti plugino konfigūraciją!");
            return true;
        }
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Reload the plugin configuration
            plugin.reloadPluginConfig();
            
            long endTime = System.currentTimeMillis();
            long reloadTime = endTime - startTime;
            
            sender.sendMessage("§a✓ Enchanter plugin konfigūracija sėkmingai atnaujinta!");
            sender.sendMessage("§7Atnaujinimo trukmė: " + reloadTime + "ms");
            sender.sendMessage("§7Galimi enchantai: " + plugin.getEnchantmentManager().getEnchantmentNames().size());
            
            // Log the reload
            plugin.getLogger().info(sender.getName() + " atnaujino plugin konfigūraciją");
            
        } catch (Exception e) {
            sender.sendMessage("§cKlaida atnaujinant konfigūraciją: " + e.getMessage());
            plugin.getLogger().severe("Klaida atnaujinant konfigūraciją: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true;
    }
}
package lt.enchanter;

import lt.enchanter.commands.EnchantCommand;
import lt.enchanter.commands.ListEnchantsCommand;
import lt.enchanter.commands.ReloadCommand;
import lt.enchanter.enchants.EnchantmentManager;
import lt.enchanter.listeners.EnchantmentListener;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchanterPlugin extends JavaPlugin {
    
    private EnchantmentManager enchantmentManager;
    private static EnchanterPlugin instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize enchantment manager
        enchantmentManager = new EnchantmentManager();
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        // Save default config
        saveDefaultConfig();
        
        getLogger().info("Enchanter plugin has been enabled!");
        getLogger().info("Available custom enchantments: " + enchantmentManager.getEnchantmentNames().size());
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Enchanter plugin has been disabled!");
    }
    
    private void registerCommands() {
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("enchanterreload").setExecutor(new ReloadCommand(this));
        getCommand("listenchants").setExecutor(new ListEnchantsCommand(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EnchantmentListener(this), this);
    }
    
    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }
    
    public static EnchanterPlugin getInstance() {
        return instance;
    }
    
    public void reloadPluginConfig() {
        reloadConfig();
        enchantmentManager = new EnchantmentManager();
        getLogger().info("Enchanter configuration reloaded!");
    }
}
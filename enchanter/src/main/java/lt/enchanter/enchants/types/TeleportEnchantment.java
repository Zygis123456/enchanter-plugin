package lt.enchanter.enchants.types;

import lt.enchanter.enchants.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class TeleportEnchantment extends CustomEnchantment {
    
    private final Random random = new Random();
    
    public TeleportEnchantment() {
        super("Teleport", "Šansas teleportuotis už priešo nugaros", 2,
                Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    }
    
    @Override
    public void onAttack(EntityDamageByEntityEvent event, int level) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            
            // Chance to teleport: 15% per level
            double teleportChance = 0.15 * level;
            
            if (random.nextDouble() < teleportChance) {
                // Calculate position behind the target
                Location targetLoc = event.getEntity().getLocation();
                Vector direction = targetLoc.getDirection().normalize();
                
                // Get position 2 blocks behind the target
                Location teleportLoc = targetLoc.clone().subtract(direction.multiply(2));
                teleportLoc.setY(targetLoc.getY());
                
                // Ensure the location is safe
                if (isSafeLocation(teleportLoc)) {
                    // Teleport effects
                    attacker.getWorld().spawnParticle(
                        org.bukkit.Particle.PORTAL,
                        attacker.getLocation(),
                        20,
                        0.5, 1.0, 0.5,
                        0.5
                    );
                    
                    attacker.teleport(teleportLoc);
                    
                    attacker.getWorld().spawnParticle(
                        org.bukkit.Particle.PORTAL,
                        teleportLoc,
                        20,
                        0.5, 1.0, 0.5,
                        0.5
                    );
                    
                    // Sound effects
                    attacker.playSound(
                        attacker.getLocation(),
                        org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT,
                        1.0f,
                        1.0f
                    );
                    
                    // Give brief invisibility for tactical advantage
                    attacker.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.INVISIBILITY,
                        20, // 1 second
                        0
                    ));
                    
                    attacker.sendMessage("§5⚡ Teleportavais už priešo nugaros!");
                }
            }
        }
    }
    
    @Override
    public void onDefend(EntityDamageEvent event, int level) {
        if (event.getEntity() instanceof Player) {
            Player defender = (Player) event.getEntity();
            
            // Chance to teleport away when taking damage: 10% per level
            double escapeChance = 0.10 * level;
            
            if (random.nextDouble() < escapeChance) {
                // Find a safe location nearby
                Location currentLoc = defender.getLocation();
                
                for (int attempts = 0; attempts < 10; attempts++) {
                    double angle = random.nextDouble() * 2 * Math.PI;
                    double distance = 5 + (random.nextDouble() * 5); // 5-10 blocks away
                    
                    double x = currentLoc.getX() + Math.cos(angle) * distance;
                    double z = currentLoc.getZ() + Math.sin(angle) * distance;
                    
                    Location teleportLoc = new Location(currentLoc.getWorld(), x, currentLoc.getY(), z);
                    
                    if (isSafeLocation(teleportLoc)) {
                        // Teleport effects
                        defender.getWorld().spawnParticle(
                            org.bukkit.Particle.PORTAL,
                            currentLoc,
                            15,
                            0.5, 1.0, 0.5,
                            0.5
                        );
                        
                        defender.teleport(teleportLoc);
                        
                        defender.getWorld().spawnParticle(
                            org.bukkit.Particle.PORTAL,
                            teleportLoc,
                            15,
                            0.5, 1.0, 0.5,
                            0.5
                        );
                        
                        defender.playSound(
                            teleportLoc,
                            org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT,
                            1.0f,
                            1.2f
                        );
                        
                        defender.sendMessage("§5⚡ Pabėgai naudojant teleportaciją!");
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void onBreakBlock(BlockBreakEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event, int level) {
        // Not applicable for this enchantment
    }
    
    private boolean isSafeLocation(Location loc) {
        if (loc.getWorld() == null) return false;
        
        // Check if the location has solid ground and clear space above
        Location groundLoc = loc.clone();
        groundLoc.setY(Math.floor(groundLoc.getY()));
        
        // Check for solid ground
        if (!groundLoc.getBlock().getType().isSolid()) {
            groundLoc.subtract(0, 1, 0);
            if (!groundLoc.getBlock().getType().isSolid()) {
                return false;
            }
        }
        
        // Check for clear space above (2 blocks high)
        Location aboveLoc = groundLoc.clone().add(0, 1, 0);
        if (aboveLoc.getBlock().getType().isSolid()) return false;
        
        aboveLoc.add(0, 1, 0);
        if (aboveLoc.getBlock().getType().isSolid()) return false;
        
        return true;
    }
}
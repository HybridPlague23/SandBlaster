package me.hybridplague.sandblaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlasterListener implements Listener {

	private String format(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	Map<String, Long> cooldowns = new HashMap<String, Long>();
	
    /**
    *
    * @param centerBlock Define the center of the sphere
    * @param radius Radius of your sphere
    * @param hollow If your sphere should be hollow (you only get the blocks outside) just put in "true" here
    * @return Returns the locations of the blocks in the sphere
    *
    */
   public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
    
       List<Location> circleBlocks = new ArrayList<Location>();

       int bx = centerBlock.getBlockX();
       int by = centerBlock.getBlockY();
       int bz = centerBlock.getBlockZ();
    
       for(int x = bx - radius; x <= bx + radius; x++) {
           for(int y = by - radius; y <= by + radius; y++) {
               for(int z = bz - radius; z <= bz + radius; z++) {
                
                   double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                
                   if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                    
                       Location l = new Location(centerBlock.getWorld(), x, y, z);
                       if (l.getBlock().getType() == Material.SAND) {
                    	   circleBlocks.add(l);
                       }
                    
                   }
                
               }
           }
       }
    
       return circleBlocks;
   }
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if (p.getGameMode() == GameMode.CREATIVE) return;
		
		if (p.getInventory().getItemInMainHand() == null) return;
		if (p.getInventory().getItemInMainHand().getType() != Material.NETHERITE_SHOVEL) return;
		if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;
		if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(format("&e&lSand Blaster"))) return;
		
		if (e.getBlock().getType() != Material.SAND) {
			e.setCancelled(true);
			p.sendMessage(format("&cYou can only use this on sand blocks."));
			return;
		}
		
		if (cooldowns.containsKey(p.getName())) {
			if (cooldowns.get(p.getName()) > System.currentTimeMillis()) {
				e.setCancelled(true);
				long timeleft = (cooldowns.get(p.getName()) - System.currentTimeMillis()) / 1000;
				p.sendMessage(format("&cPlease wait " + timeleft + " more seconds before using the Sand Blaster again."));
				return;
			}
		}
		
		int total = 0;
		String current = ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace("Sand Blaster ", ""));
				
		Location loc = e.getBlock().getLocation();
		int radius = 5;
		
		for (Location l : generateSphere(loc, radius, false)) {
			total = total + 1;
			loc.getWorld().getBlockAt(l).setType(Material.AIR);
			
			if (p.getInventory().firstEmpty() == -1) {
				loc.getWorld().dropItemNaturally(l, new ItemStack(Material.SAND));
			} else {
				p.getInventory().addItem(new ItemStack(Material.SAND));
			}
			
			
		}

		int newTotal = Integer.parseInt(current) + total;
		ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
		meta.setDisplayName(format("&e&lSand Blaster &7" + newTotal));
		
		p.getInventory().getItemInMainHand().setItemMeta(meta);
		
		/*for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
			for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
				for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
					
					if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.SAND) {
						loc.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
						loc.getWorld().dropItemNaturally(new Location(loc.getWorld(), x, y, z), new ItemStack(Material.SAND));
					}
					
				}
			}
		}*/
		
		cooldowns.put(p.getName(), System.currentTimeMillis() + (15 * 1000));
		
	}
	
	
}

package me.hybridplague.sandblaster;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GetBlaster implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You much be a player to run this command.");
			return true;
		}

		Player p = (Player) sender;
		if (!p.hasPermission("sandblaster.get")) {
			p.sendMessage(ChatColor.RED + "Insufficient Permissions.");
			return true;
		}
		
		if (p.getInventory().firstEmpty() == -1) {
			p.getLocation().getWorld().dropItem(p.getLocation(), blaster());
			p.sendMessage(format("&aThe &e&lSand Blaster &ahas been dropped to your feet."));
			return true;
		}
		
		p.getInventory().addItem(blaster());
		p.sendMessage(format("&aYou have received the &e&lSand Blaster"));
		
		return true;
	}
	
	private String format(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	private ItemStack blaster() {
		ItemStack item = new ItemStack(Material.NETHERITE_SHOVEL);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		
		meta.setDisplayName(format("&e&lSand Blaster &70"));
		
		lore.add("");
		lore.add(format("  &4• &fBreak the ground to destroy all sand"));
		lore.add(format("   &fwithin a &e5 block radius&f!"));
		lore.add("");
		lore.add(format("  &4• &b15 second cooldown"));
		lore.add("");
		lore.add(format("&4&l! WARNING ! &cMay cause FPS lag &4&l! WARNING !"));
		
		meta.setLore(lore);
		
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
		return item;
	}
	
}

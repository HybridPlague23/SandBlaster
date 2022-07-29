package me.hybridplague.sandblaster;

import org.bukkit.plugin.java.JavaPlugin;

public class SandBlaster extends JavaPlugin {

	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new BlasterListener(), this);
		getCommand("getblaster").setExecutor(new GetBlaster());
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
}

package io.github.fysac.betterclocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterClocks extends JavaPlugin implements Listener {
	private InventoryUpdateTask inventoryUpdater;
	
	@Override
	public void onEnable(){
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new CheckTimeListener(), this);
		
		inventoryUpdater = new InventoryUpdateTask(this);
		inventoryUpdater.start();
	}
	
	@Override
	public void onDisable(){
		inventoryUpdater.stop();
	}
	
	public static List<String> generateLore(World world){
		List<String> lore = new ArrayList<String>();
		long time = world.getTime();

		// Get minutes from tick value
		String mins = String.valueOf((float) time % 1000 / 1000 * 60);

		// Chop off decimal places from minutes (yes, it has to be done this way)
		String minutes = String.valueOf(Float.valueOf(mins).intValue());

		// Add leading and trailing zeros
		if (Integer.valueOf(minutes)  < 10 && Integer.valueOf(minutes) > 0){
			minutes = "0" + minutes; 
		}
		else if (Integer.valueOf(minutes) == 0){
			minutes = minutes + "0";
		}

		// Get hours from tick value
		long hours = time / 1000 + 6;
		if (hours > 24){
			hours -= 24;
		}

		// Convert to 12-hour format
		long twelve_hours = hours;
		String day_half = "AM";

		if (hours > 12 && hours < 24){
			twelve_hours = hours - 12;
			day_half = "PM";
		}

		lore.add("Current time: ");
		lore.add(hours + ":" + minutes + " (" + twelve_hours + ":" + minutes + " " + day_half + ")");
		return lore;
	}
}
package io.github.fysac.betterclocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterClocks extends JavaPlugin implements Listener {
	List <Inventory> inventories = new ArrayList<Inventory>();

	public void onEnable(){
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this, this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				updatePlayerInventories();
				updateOtherInventories();
			}
		}, 0, 2);
	}

	@EventHandler()
	public void onInventoryOpen(InventoryOpenEvent event){
		inventories.add(event.getInventory());
	}

	@EventHandler()
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.WATCH) {
			Action action = event.getAction();
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				player.sendMessage(ChatColor.GOLD + "[" + ChatColor.RESET + ChatColor.RED + "BetterClocks" + 
						ChatColor.RESET + ChatColor.GOLD + "] The current time is: " + generateLore(player.getWorld()) + ".");
			}
		}
	}

	public void updatePlayerInventories(){
		for (Player p : Bukkit.getServer().getOnlinePlayers()){
			for (ItemStack item : p.getInventory().getContents()){
				if (item != null && item.getType() == Material.WATCH){
					ItemMeta meta = item.getItemMeta();
					meta.setLore(generateLore(p.getWorld()));
					item.setItemMeta(meta);
				}
			}
		}
	}

	public void updateOtherInventories(){
		for (Inventory i : inventories){
			if (i.getViewers().isEmpty()){
				inventories.remove(i);
				break;
			}
			else{
				World world = i.getViewers().get(0).getWorld();
				for (ItemStack item : i.getContents()){
					if (item != null && item.getType() == Material.WATCH){
						ItemMeta meta = item.getItemMeta();
						meta.setLore(generateLore(world));
						item.setItemMeta(meta);
					}
				}
			}
		}
	}
	
	public List<String> generateLore(World world){
		long time = world.getTime();
		List<String> lore = new ArrayList<String>();

		lore.add("Current time: ");

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

		lore.add(hours + ":" + minutes);
		lore.add(twelve_hours + ":" + minutes + " " + day_half);
		return lore;
	}
}
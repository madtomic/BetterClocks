package io.github.fysac.betterclocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class InventoryUpdateTask implements Listener {
	private Plugin plugin;
	private int id;
	private List <Inventory> inventories = new ArrayList<Inventory>();
	
	public InventoryUpdateTask(Plugin plugin){
		this.plugin = plugin;
	}
	
	public void start(){
		id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				updatePlayerInventories();
				updateContainers();
			}
		}, 0, 2);
	}

	public void stop(){
		plugin.getServer().getScheduler().cancelTask(id);
	}
	
	@EventHandler()
	public void onInventoryOpen(InventoryOpenEvent event){
		inventories.add(event.getInventory());
	}
	
	public void updatePlayerInventories(){
		for (Player p : Bukkit.getServer().getOnlinePlayers()){
			for (ItemStack item : p.getInventory().getContents()){
				if (item != null && item.getType() == Material.WATCH){
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(BetterClocks.generateLore(p.getWorld()).get(1));
					// meta.setLore(generateLore(p.getWorld()));
					item.setItemMeta(meta);
				}
			}
		}
	}

	public void updateContainers(){
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
						meta.setLore(BetterClocks.generateLore(world));
						item.setItemMeta(meta);
					}
				}
			}
		}
	}
}
package io.github.fysac.betterclocks;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CheckTimeListener implements Listener {
	@EventHandler()
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.WATCH) {
			Action action = event.getAction();
			
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				List<String> currentTime = BetterClocks.generateLore(player.getWorld());
				player.sendMessage(ChatColor.GOLD + "[" + ChatColor.RESET + ChatColor.RED
					+ "BetterClocks" + ChatColor.RESET + ChatColor.GOLD
					+ "] " + currentTime.get(0) + currentTime.get(1) + ".");
			}
		}
	}
}

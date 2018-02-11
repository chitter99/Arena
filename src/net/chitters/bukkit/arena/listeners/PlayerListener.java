package net.chitters.bukkit.arena.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.ArenaCreator;

public class PlayerListener implements Listener {
	final MainArena plugin;
	
	public PlayerListener(MainArena p) {
		plugin = p;
	}
	
	//Wand Tool
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		//Wand Event
		Player player = event.getPlayer();
		
		if(!plugin.isCreatingArena(player)) {
			return;
		}
		
		ArenaCreator creator = plugin.getArenaCreator(player);
		
		if(!player.getItemInHand().equals(creator.getWandHandler().getWandItem())) {
			return;
		}
		
		if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			creator.getWandHandler().setPos1(event.getClickedBlock().getLocation());
			plugin.sendMessage(player, plugin.getMessageHandler().getKey("wandSetPos1"));
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			creator.getWandHandler().setPos2(event.getClickedBlock().getLocation());
			plugin.sendMessage(player, plugin.getMessageHandler().getKey("wandSetPos2"));
		}
		
		event.setCancelled(true);
	}
	
	
}

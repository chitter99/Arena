package net.chitters.bukkit.arena.objects;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.handlers.WandHandler;

public class ArenaCreator {
	final MainArena plugin;
	
	String name;
	Arena arena;
	
	Player player;
	WandHandler wand;
	
	public ArenaCreator(MainArena pl, Player p, Arena a) {
		plugin = pl;
		player = p;
		arena = a;
		name = p.getName();
		wand = new WandHandler(plugin);
	}
	
	public String getName() {
		return name;
	}
	public Arena getArena() {
		return arena;
	}
	public Player getPlayer() {
		return player;
	}
	public WandHandler getWandHandler() {
		return wand;
	}
	
	public void stop() {
		plugin.stopEdit(this);
	}
	
}

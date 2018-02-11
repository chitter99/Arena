package net.chitters.bukkit.arena.objects;

import net.chitters.bukkit.arena.MainArena;
import net.md_5.bungee.api.ChatColor;

public class ArenaType {
	final MainArena plugin;
	
	String name;
	String display;
	
	public ArenaType(MainArena p) {
		plugin = p;
	}
	
	public MainArena getPlugin() {
		return plugin;
	}
	public String getName() {
		return name;
	}
	public String getDisplay() {
		return display;
	}
	public void setName(String x) {
		name = x;
	}
	public void setDisplay(String x) {
		display = x;
	}
	
	public Arena getNewArena(ArenaProperty pr) {
		return getNewArena(plugin, pr);
	}
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return null;
	}
	public void setup() {
		
	}
}

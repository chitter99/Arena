package net.chitters.bukkit.arena.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.chitters.bukkit.arena.MainArena;

public class WandHandler {
	final MainArena plugin;
	
	public WandHandler(MainArena p) {
		plugin = p;
	}
	
	private Location pos1;
	private Location pos2;
	
	public Location getPos1() {
		return pos1;
	}
	public Location getPos2() {
		return pos2;
	}
	public void setPos1(Location loc) {
		pos1 = loc;
	}
	public void setPos2(Location loc) {
		pos2 = loc;
	}
	public boolean bothSet() {
		if(pos1 != null && pos2 != null) return true;
		return false;
	}
	public ItemStack getWandItem() {
		//return new ItemStack(Material.getMaterial(plugin.getConfig().getInt("creation.wandItem")));
		return new ItemStack(Material.STICK);
	}
	
}

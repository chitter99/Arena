package net.chitters.bukkit.arena.objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ArenaSpawnPoint {
	String name;
	World world;
	
	double x;
	double y;
	double z;
	
	public ArenaSpawnPoint(String p, World a, double b, double c, double d) {
		name = p;
		world = a;
		x = b;
		y = c;
		z = d;
	}
	public ArenaSpawnPoint(String p, Location l) {
		name = p;
		world = l.getWorld();
		x = l.getX();
		y = l.getY();
		z = l.getZ();
	}
	
	public Location getLocation() {
		return new Location(world, x, y, z);
	}
	public String getName() {
		return name;
	}
	
	
	public World getWorld() {
		return world;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public void teleport(Player player) {
		player.teleport(getLocation().add(0.0D, 1.0D, 0.0D));
	}
	public void teleport(Entity entity) {
		entity.teleport(getLocation().add(0.0D, 1.0D, 0.0D));
	}
	
}

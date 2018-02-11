package net.chitters.bukkit.arena.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ArenaArea {
	
	public ArenaArea(Location p1, Location p2) {
		World world = p1.getWorld();
		
		int minx;
		int maxx;
		if(p1.getBlockX() >= p2.getBlockX()) {
			minx = p2.getBlockX();
			maxx = p1.getBlockX();
		} else {
			minx = p1.getBlockX();
			maxx = p2.getBlockX();
		}
		
		int miny;
		int maxy;
		if(p1.getBlockY() >= p2.getBlockY()) {
			miny = p2.getBlockY();
			maxy = p1.getBlockY();
		} else {
			miny = p1.getBlockY();
			maxy = p2.getBlockY();
		}
		
		int minz;
		int maxz;
		if(p1.getBlockZ() >= p2.getBlockZ()) {
			minz = p2.getBlockZ();
			maxz = p1.getBlockZ();
		} else {
			minz = p1.getBlockZ();
			maxz = p2.getBlockZ();
		}
		
		pos1 = new Location(world, minx, miny, minz);
		pos2 = new Location(world, maxx, maxy, maxz);
	}
	
	Location pos1;
	Location pos2;
	
	public void setPos1(Location loc) {
		pos1 = loc;
	}
	public void setPos2(Location loc) {
		pos2 = loc;
	}
	public Location getPos1() {
		return pos1;
	}
	public Location getPos2() {
		return pos2;
	}
	
	public Location getMin() {
		return getPos1();
	}
	public Location getMax() {
		return getPos2();
	}
	
	public boolean checkLocIn(Location l) {
		int x1 = (int) getPos1().getBlockX();
		int y1 = (int) getPos1().getBlockY();
		int z1 = (int) getPos1().getBlockZ();
		int x2 = (int) getPos2().getBlockX();
		int y2 = (int) getPos2().getBlockY();
		int z2 = (int) getPos2().getBlockZ();
		int miny = (int) Math.min(y1, y2) - 1;
		int maxy = (int) Math.max(y1, y2) + 1;
		int minz = (int) Math.min(z1, z2) - 1;
		int maxz = (int) Math.max(z1, z2) + 1;
		int minx = (int) Math.min(x1, x2) - 1;
		int maxx = (int) Math.max(x1, x2) + 1;
		if(l.getWorld().getName().equalsIgnoreCase(getPos1().getWorld().getName()))
		{
			if(l.getY() > miny && l.getY() < maxy)
			{
				if(l.getZ() > minz && l.getZ() < maxz)
				{
					if(l.getX() > minx && l.getX() < maxx)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void setBlockTypes(Material mat) {
		World world = pos1.getWorld();
		Location min = getMin();
		Location max = getMax();
		for (int x = min.getBlockX(); x < max.getBlockX() + 1; x++)
		{
			for (int y = min.getBlockY(); y < max.getBlockY() + 1; y++)
			{
				for (int z = min.getBlockZ(); z < max.getBlockZ() + 1; z++)
				{
					Block block = world.getBlockAt(x, y, z);
					block.setType(mat);
				}
			}
		}
	}
	
}

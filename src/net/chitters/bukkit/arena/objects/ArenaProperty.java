package net.chitters.bukkit.arena.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class ArenaProperty {
	String name;
	String display;
	
	World world;
	ArenaType type;
	
	HashMap <String, ArenaSpawnPoint> spawnPoints = new HashMap<String, ArenaSpawnPoint>();
	HashMap <String, ArenaArea> areas = new HashMap<String, ArenaArea>();
	HashMap <String, Object> rules = new HashMap<String, Object>();
	HashMap <String, ArenaSpawnPoint[]> spawnPointLists = new HashMap<String, ArenaSpawnPoint[]>();
	
	public ArenaProperty() {
		
	}
	
	public String getName() {
		return name;
	}
	public String getDisplay() {
		return ChatColor.translateAlternateColorCodes('&', display);
	}
	public World getWorld() {
		return world;
	}
	
	public void setName(String x) {
		name = x;
	}
	public void setDisplay(String x) {
		display = x;
	}
	public void setWorld(World x) {
		world = x;
	}
	
	public ArenaSpawnPoint getSpawnPoint(String spawnpoint)
	{
		for(Entry<String, ArenaSpawnPoint> entry : spawnPoints.entrySet()) {
			if(entry.getKey().equals(spawnpoint.toLowerCase())) return entry.getValue();
		}
		return null;
	}
	public boolean isSpawnPoint(String spawnpoint)
	{
		if(getSpawnPoint(spawnpoint) == null) return false;
		return true;
	}
	public void addSpawnPoint(String name, Location loc) {
		ArenaSpawnPoint sp = new ArenaSpawnPoint(name, world, loc.getX(), loc.getY(), loc.getZ());
		spawnPoints.put(name, sp);
	}
	public HashMap<String, ArenaSpawnPoint> getSpawnPoints() {
		return spawnPoints;
	}
	
	public ArenaArea getArea(String name) {
		return areas.get(name.toLowerCase());
	}
	public boolean isArea(String name) {
		if(getArea(name.toLowerCase()) == null) return false;
		return true;
	}
	public void addArea(String name, Location pos1, Location pos2) {
		areas.put(name.toLowerCase(), new ArenaArea(pos1, pos2));
	}
	public HashMap<String, ArenaArea> getAreas() {
		return areas;
	}
	
	public ArenaSpawnPoint[] getSpawnPointList(String key) {
		return spawnPointLists.get(key);
	}
	public boolean isSpawnPointList(String key) {
		if(getSpawnPointList(key) == null) return false;
		return true;
	}
	public void addSpawnPointList(String key, ArenaSpawnPoint[] value) {
		spawnPointLists.put(key, value);
	}
	public HashMap <String, ArenaSpawnPoint[]> getSpawnPointLists() {
		return spawnPointLists;
	}
	public void addToSpawnPointList(String key, ArenaSpawnPoint value) {
		ArenaSpawnPoint[] n = null;
		if(!isSpawnPointList(key)) {
			n = new ArenaSpawnPoint[]{value};
		} else {
			ArenaSpawnPoint[] old = getSpawnPointList(key);
			n = new ArenaSpawnPoint[old.length + 1];
			
			int i = 0;
			for(ArenaSpawnPoint p : old) {
				n[i] = p;
				i++;
			}
			n[i] = value;
		}
		
		spawnPointLists.put(key, n);
	}
	
	public HashMap<String, Object> getRules() {
		return rules;
	}
	public void setRule(String key, Object value) {
		rules.put(key.toLowerCase(), value);
	}
	public String getRuleString(String key) {
		return (String) rules.get(key.toLowerCase());
	}
	public int getRuleInt(String key) {
		return (int) rules.get(key.toLowerCase());
	}
	public float getRuleFloat(String key) {
		return (float) rules.get(key.toLowerCase());
	}
	public boolean getRuleBoolean(String key) {
		return (boolean) rules.get(key.toLowerCase());
	}
	public boolean isRule(String key) {
		if(rules.get(key.toLowerCase()) != null) return true;
		return false;
	}
	public Object getRule(String key) {
		key = key.toLowerCase();
		Object rule = rules.get(key);
		if(rule.getClass().equals(Integer.class)) {
			return getRuleInt(key);
		}
		if(rule.getClass().equals(Float.class)) {
			return getRuleFloat(key);
		}
		if(rule.getClass().equals(Boolean.class)) {
			return getRuleBoolean(key);
		}
		return rule.toString();
	}
	
}

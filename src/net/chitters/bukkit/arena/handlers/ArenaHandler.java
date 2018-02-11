package net.chitters.bukkit.arena.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaArea;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaType;

public class ArenaHandler {
	final MainArena plugin;
	
	public ArenaHandler(MainArena p) {
		plugin = p;
	}
	
	public MainArena getPlugin() {
		return plugin;
	}
	
	public File arenasFile;
	public FileConfiguration arenasConfig;
	
	public void loadArenasConfig()
	{
		if(arenasFile == null) {
			arenasFile = new File(getPlugin().getDataFolder(), "arenas.yml");
			if(!arenasFile.exists()) {
				try {
					arenasFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
	}
	public FileConfiguration getArenasConfig()
	{
		if(arenasConfig == null) loadArenasConfig();
		return arenasConfig;
	}
	
	protected HashMap<String, Arena> arenasMap;
	
	public void createArena(String name, ArenaType type, World world) {
		ArenaProperty pr = new ArenaProperty();
		pr.setName(name.toLowerCase());
		pr.setDisplay(name);
		pr.setWorld(world);
		
		Arena arena = type.getNewArena(getPlugin(), pr);
		
		arenasMap.put(name.toLowerCase(), arena);
		saveArenas();
	}
	public void loadArenas() {
		arenasMap = new HashMap<String, Arena>();
		
		for(String path : this.getArenasConfig().getKeys(false)) {
			ArenaType type = getArenaType(getArenasConfig().getString(path + ".type"));
			
			if(type == null) {
				System.err.println("Error, type can't be null!");
				continue;
			}
			
			ArenaProperty property = new ArenaProperty();
			
			//Propertys
			property.setName(this.getArenasConfig().getString(path + ".name"));
			property.setDisplay(this.getArenasConfig().getString(path + ".display"));
			if(this.getArenasConfig().isSet(path + ".world")) property.setWorld(getPlugin().getServer().getWorld(this.getArenasConfig().getString(path + ".world")));
			
			//Spawn Points
			if(getArenasConfig().isSet(path + ".spawnpoints")) {
				for(String point : getArenasConfig().getConfigurationSection(path + ".spawnpoints").getKeys(false)) {
					double x = getArenasConfig().getDouble(path + ".spawnpoints." + point + ".x");
					double y = getArenasConfig().getDouble(path + ".spawnpoints." + point + ".y");
					double z = getArenasConfig().getDouble(path + ".spawnpoints." + point + ".z");
					Location loc = new Location(property.getWorld(), x, y, z);
					property.addSpawnPoint(point.toLowerCase(), loc);
				}
			}
			
			//Areas
			if(this.getArenasConfig().isSet(path + ".areas")) {
				for(String area : this.getArenasConfig().getConfigurationSection(path + ".areas").getKeys(false)) {
					Location pos1 = new Location(property.getWorld(), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos1.x"), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos1.y"), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos1.z"));
					Location pos2 = new Location(property.getWorld(), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos2.x"), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos2.y"), this.getArenasConfig().getDouble(path + ".areas." + area + ".pos2.z"));
					property.addArea(area.toLowerCase(), pos1, pos2);
				}
			}
			
			//Spawn Points Lists
			if(getArenasConfig().isSet(path + ".spawnpointslists")) {
				for(String list : getArenasConfig().getConfigurationSection(path + ".spawnpointslists").getKeys(false)) {
					List <ArenaSpawnPoint> points = new ArrayList<ArenaSpawnPoint>();
					for(String point : getArenasConfig().getConfigurationSection(path + ".spawnpointslists." + list).getKeys(false)) {
						double x = getArenasConfig().getDouble(path + ".spawnpointslists." + list + "." + point + ".x");
						double y = getArenasConfig().getDouble(path + ".spawnpointslists." + list + "." + point + ".y");
						double z = getArenasConfig().getDouble(path + ".spawnpointslists." + list + "." + point + ".z");
						Location loc = new Location(property.getWorld(), x, y, z);
						points.add(new ArenaSpawnPoint("", loc));
					}
					property.addSpawnPointList(list, points.toArray(new ArenaSpawnPoint[points.size()]));
				}
			}		
			
			//Rules
			if(this.getArenasConfig().isSet(path + ".rules")) {
				for(String rule : this.getArenasConfig().getConfigurationSection(path + ".rules").getKeys(false)) {
					String key = rule;
					Object value = this.getArenasConfig().get(path + ".rules." + key);
					property.setRule(key, value);
				}
			}
			
			arenasMap.put(property.getName().toLowerCase(), type.getNewArena(property));
			arenasMap.get(property.getName().toLowerCase()).checkStatus();
		}
		System.out.println("Loaded Arenas (" + arenasMap.size() + ")");
	}
	public void saveArenas() {
		for(Entry<String, Arena> entry : arenasMap.entrySet()) {
			String name = entry.getKey();
			Arena arena = entry.getValue();
			ArenaProperty property = arena.getProperty();
			String path = name.toLowerCase();
			
			getArenasConfig().set(path + ".name", property.getName());
			getArenasConfig().set(path + ".display", property.getDisplay());
			getArenasConfig().set(path + ".type", arena.getType().getName());
			if(property.getWorld() != null) getArenasConfig().set(path + ".world", property.getWorld().getName());
			
			if(property.getSpawnPoints() != null) {
				for(Entry<String, ArenaSpawnPoint> e : property.getSpawnPoints().entrySet()) {
					getArenasConfig().set(path + ".spawnpoints." + e.getKey() + ".x", e.getValue().getX());
					getArenasConfig().set(path + ".spawnpoints." + e.getKey() + ".y", e.getValue().getY());
					getArenasConfig().set(path + ".spawnpoints." + e.getKey() + ".z", e.getValue().getZ());
				}
			}
			if(property.getAreas() != null) {
				for(Entry<String, ArenaArea> e : property.getAreas().entrySet()) {
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos1.x", e.getValue().getPos1().getX());
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos1.y", e.getValue().getPos1().getY());
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos1.z", e.getValue().getPos1().getZ());
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos2.x", e.getValue().getPos2().getX());
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos2.y", e.getValue().getPos2().getY());
					getArenasConfig().set(path + ".areas." + e.getKey() + ".pos2.z", e.getValue().getPos2().getZ());
				}
			}
			if(property.getSpawnPointLists() != null) {
				for(Entry<String, ArenaSpawnPoint[]> e : property.getSpawnPointLists().entrySet()) {
					int i = 0;
					for(ArenaSpawnPoint p : e.getValue()) {
						getArenasConfig().set(path + ".spawnpointslists." + e.getKey() + "." + i + ".x", p.getX());
						getArenasConfig().set(path + ".spawnpointslists." + e.getKey() + "." + i + ".y", p.getY());
						getArenasConfig().set(path + ".spawnpointslists." + e.getKey() + "." + i + ".z", p.getZ());
						i++;
					}
				}
			}
			if(property.getRules().size() != 0) {
				for(Entry<String, Object> e : property.getRules().entrySet()) {
					getArenasConfig().set(path + ".rules." + e.getKey(), e.getValue());
				}
			}
		}
		try {
			getArenasConfig().save(arenasFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getPlugin().getLogger().info("Successfully saved Arenas to config");
	}
	public HashMap<String, Arena> getAllArenas() {
		return arenasMap;
	}
	
	public Arena getArena(String name) {
		return arenasMap.get(name);
	}
	public boolean getIsArena(String name) {
		if(getArena(name).equals(null)) return false;
		return true;
	}
	
	protected List<ArenaType> arenaTypes = new ArrayList<ArenaType>();
	
	public ArenaType getArenaType(String name) {
		for(ArenaType type : arenaTypes) {
			if(type.getName().equals(name)) return type;
		}
		return null;
	}
	public boolean getIsArenaType(String name) {
		if(getArenaType(name).equals(null)) return false;
		return true;
	}
	public void registerArenaType(ArenaType type) {
		arenaTypes.add(type);
	}
	public void setupArenaTypes() {
		for(ArenaType t : arenaTypes) {
			t.setup();
		}
	}
}

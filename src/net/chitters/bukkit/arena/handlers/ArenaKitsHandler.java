package net.chitters.bukkit.arena.handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.ArenaKit;

public class ArenaKitsHandler {
	final MainArena plugin;
	
	public ArenaKitsHandler(MainArena p) {
		plugin = p;
	}
	public MainArena getPlugin() {
		return plugin;
	}
	
	public File kitsFile;
	public FileConfiguration kitsConfig;
	
	public void loadKitsConfig()
	{
		if(kitsFile == null) {
			kitsFile = new File(getPlugin().getDataFolder(), "kits.yml");
			if(!kitsFile.exists()) {
				try {
					kitsFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
	}
	public FileConfiguration getKitsConfig()
	{
		if(kitsConfig == null) loadKitsConfig();
		return kitsConfig;
	}
	
	public void loadKits() {
		if(getKitsConfig() == null) loadKitsConfig();
		for(String path : this.getKitsConfig().getKeys(false)) {
			String name = getKitsConfig().getString(path + ".name");
			
			if(name == null) {
				System.err.println("Error, name can't be null! " + path);
				continue;
			}
			
			ArenaKit kit = new ArenaKit();
			kit.name = name;
			kit.display = getKitsConfig().getString(path + ".display");
			
			if(getKitsConfig().isInt(path + ".icon")) {
				kit.icon = Material.getMaterial(getKitsConfig().getInt(path + ".icon"));
			} else if(getKitsConfig().isString(path + ".icon")) {
				kit.icon = Material.getMaterial(getKitsConfig().getString(path + ".icon"));
			} else {
				kit.icon = Material.DIRT;
			}
			
			if(getKitsConfig().isBoolean(path + ".needsPermission")) kit.needsPermission = getKitsConfig().getBoolean(path + ".needsPermission");
			
			kit.description = getKitsConfig().getString(path + ".description");
			
			//Armor
			if(getKitsConfig().isConfigurationSection(path + ".armor.helmet")) kit.helmet = getPlugin().getMisc().getItemStackFromConfig(getKitsConfig(), path + ".armor.helmet");
			if(getKitsConfig().isConfigurationSection(path + ".armor.chestplate")) kit.chestplate = getPlugin().getMisc().getItemStackFromConfig(getKitsConfig(), path + ".armor.chestplate");
			if(getKitsConfig().isConfigurationSection(path + ".armor.leggings")) kit.leggings = getPlugin().getMisc().getItemStackFromConfig(getKitsConfig(), path + ".armor.leggings");
			if(getKitsConfig().isConfigurationSection(path + ".armor.boots")) kit.boots = getPlugin().getMisc().getItemStackFromConfig(getKitsConfig(), path + ".armor.boots");
			
			//Inventory
			if(getKitsConfig().isConfigurationSection(path + ".inventory")) {
				for (Map.Entry<String, Object> entry : getKitsConfig().getConfigurationSection(path + ".inventory").getValues(false).entrySet()) {
					kit.addItem(getPlugin().getMisc().getItemStackFromConfig(getKitsConfig(), path + ".inventory." + entry.getKey()));
				}
			}
			
			//Effects
			if(getKitsConfig().isConfigurationSection(path + ".effects")) {
				for (Map.Entry<String, Object> entry : getKitsConfig().getConfigurationSection(path + ".effects").getValues(false).entrySet()) {
				    PotionEffectType type = PotionEffectType.getByName(entry.getKey());
				    if(type == null) {
				    	System.out.println(entry.getKey() + " is not a Potion Effect!");
				    	continue;
				    }
				    int a = (int) entry.getValue();
				    kit.addEffect(new PotionEffect(type, Integer.MAX_VALUE, a));
				}
			}
			
			addKit(kit);
		}
		System.out.println("Loaded Kits (" + kits.size() + ")");
	}

	HashMap<String, ArenaKit> kits = new HashMap<String, ArenaKit>();
	public ArenaKit getKit(String name) {
		return kits.get(name.toLowerCase());
	}
	public ArenaKit getKitByDisplayname(String name) {
		for(ArenaKit k : kits.values()) {
			if(k.getDisplay().equalsIgnoreCase(name)) return k;
		}
		return null;
	}
	public void addKit(ArenaKit kit) {
		kits.put(kit.name.toLowerCase(), kit);
	}
	public HashMap<String, ArenaKit> getKits() {
		return kits;
	}
}

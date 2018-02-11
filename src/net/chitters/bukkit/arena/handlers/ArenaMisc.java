package net.chitters.bukkit.arena.handlers;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.chitters.bukkit.arena.MainArena;

public class ArenaMisc {
	final MainArena plugin;
	public ArenaMisc(MainArena p) {
		plugin = p;
	}

	public ItemStack getItemStackFromConfig(FileConfiguration conf, String path) {
		Material material = Material.AIR;
		if(conf.isString(path + ".id")) {
			material = Material.getMaterial(conf.getString(path + ".id"));
		} else if(conf.isInt(path + ".id")) {
			material = Material.getMaterial(conf.getInt(path + ".id"));
		}
		
		ItemStack item = new ItemStack(material);
		
		if(conf.isString(path + ".damage")) item.setDurability(Short.parseShort(conf.getString(path + ".damage")));
		if(conf.isInt(path + ".amount")) item.setAmount(conf.getInt(path + ".amount"));
		
		//Enchantments
		if(conf.isConfigurationSection(path + ".enchantments")) {
			for (Map.Entry<String, Object> entry : conf.getConfigurationSection(path + ".enchantments").getValues(false).entrySet()) {
			    String ench = entry.getKey();
			    int level = conf.getInt(path + ".enchantments." + entry.getKey());
			    
			    item.addUnsafeEnchantment(Enchantment.getByName(ench), level);
			}
		}
		return item;
	}
}

package net.chitters.bukkit.arena.handlers;

import org.bukkit.ChatColor;

import net.chitters.bukkit.arena.MainArena;

public class MessageHandler {
	final MainArena plugin;
	
	String langKey = "en";
	
	public MessageHandler(MainArena p) {
		plugin = p;
		langKey = plugin.getConfig().getString("lang.usedKey");
	}
	
	public String getKey(String key)
	{
		String value = plugin.getConfig().getString("lang." + langKey + "." + key);
		if(value == null) {
			//return "Lang " + key + " not found in " + langKey;
			value = plugin.getConfig().getString("lang.en." + key);
		}
		return ChatColor.translateAlternateColorCodes('&', value);
	}
	public String getKey(String key, String... other)
	{
		String value = getKey(key);
		int i = 0;
		for(String x : other) {
			value = value.replace("{" + i + "}", x);
			i++;
		}
		return value;
	}
	public void addMessage(String key, String value) {
		plugin.getConfig().addDefault("lang.en." + key, value);
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}
}

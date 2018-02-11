package net.chitters.bukkit.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.MainArena;

public class Cmd {
	public String name;
	public String description;
	public boolean isPlayerOnly = false;
	public CommandHandler handler;
	public List<String> alias = new ArrayList<String>();
	
	public Cmd(CommandHandler h) {
		handler = h;
	}
	
	public boolean checkAlias(String alias) {
		for(String obj : this.alias) {
			if(obj.equalsIgnoreCase(alias)) return true;
		}
		return false;
	}
	public void addAlias(String name) {
		alias.add(name);
	}
	public void sendMessage(Player player, String msg) {
		handler.plugin.sendMessage(player, msg);
	}
	public String getMessage(String key) {
		return handler.plugin.getMessageHandler().getKey(key);
	}
	public String getMessage(String key, String... other) {
		String value = handler.plugin.getMessageHandler().getKey(key);
		int i = 0;
		for(String x : other) {
			value = value.replace("{" + i + "}", x);
			i++;
		}
		return value;
	}
	
	public MainArena getPlugin() {
		return handler.plugin;
	}
	
	//Methodes
	public void perform(Player player, String[] args) { }
	
}

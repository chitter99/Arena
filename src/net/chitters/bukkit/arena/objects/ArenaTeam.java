package net.chitters.bukkit.arena.objects;

import net.md_5.bungee.api.ChatColor;

public enum ArenaTeam {
	NONE("None", ChatColor.WHITE, "", "lobby"),
	RED("Red", ChatColor.RED, "red", "redlobby"),
	BLUE("Blue", ChatColor.BLUE, "blue", "bluelobby");
	
	private String name;
	private ChatColor color;
	private String lobbypoint;
	private String spawnpoint;
	
	ArenaTeam(String name, ChatColor color, String spawnpoint, String lobbypoint) {
		this.name = name;
		this.color = color;
		this.spawnpoint = spawnpoint;
		this.lobbypoint = lobbypoint;
	}
	
	public String getName() {
		return name;
	}
	public ChatColor getColor() {
		return color;
	}
	public String getSpawnPoint() {
		return spawnpoint;
	}
	public String getLobbyPoint() {
		return lobbypoint;
	}
}

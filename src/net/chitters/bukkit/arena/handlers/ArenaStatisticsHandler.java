package net.chitters.bukkit.arena.handlers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.listeners.ArenaStatisticListener;

public class ArenaStatisticsHandler {
	final MainArena plugin;
	protected ArenaStatisticListener playerListener;
	
	public ArenaStatisticsHandler(MainArena p) {
		plugin = p;
	}
	public MainArena getPlugin() {
		return plugin;
	}
	
	public void registerListeners() {
		playerListener = new ArenaStatisticListener(getPlugin());
	}
	
	public File playerStatisticsFile;
	public FileConfiguration playerStatisticsConfig;
	
	public void save() {
		savePlayerStatistics();
	}
	
	public void loadStatisticsFolder() {
		File folder = new File(getPlugin().getDataFolder().getPath() + File.separator + "statistics");
		if(!folder.exists()) {
			folder.mkdir();
		}
	}
	public void loadPlayerStatisticsConfig()
	{
		if(playerStatisticsFile == null) {
			loadStatisticsFolder();
			playerStatisticsFile = new File(getPlugin().getDataFolder().getPath() + File.separator + "statistics" + File.separator + "players.yml");
			if(!playerStatisticsFile.exists()) {
				try {
					playerStatisticsFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		playerStatisticsConfig = YamlConfiguration.loadConfiguration(playerStatisticsFile);
	}
	public FileConfiguration getPlayerStatisticsConfig()
	{
		if(playerStatisticsConfig == null) loadPlayerStatisticsConfig();
		return playerStatisticsConfig;
	}
	public void savePlayerStatistics() {
		try {
			getPlayerStatisticsConfig().save(playerStatisticsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPlayerStatistic(String player, String key) {
		return getPlayerStatisticsConfig().getInt(player + "." + key);
	}
	public void increasePlayerStatistic(String player, String key, int value) {
		getPlayerStatisticsConfig().set(player + "." + key, getPlayerStatistic(player, key) + value);
	}
	public void setPlayerStatistic(String player, String key, int newvalue) {
		getPlayerStatisticsConfig().set(player + "." + key, newvalue);
	}
	
}

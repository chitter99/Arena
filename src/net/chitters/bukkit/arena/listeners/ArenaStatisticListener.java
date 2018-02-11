package net.chitters.bukkit.arena.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.events.ArenaPlayerDieEvent;
import net.chitters.bukkit.arena.events.ArenaPlayerJoinEvent;
import net.chitters.bukkit.arena.events.ArenaPlayerKillEvent;

public class ArenaStatisticListener implements Listener {
	final MainArena plugin;
	
	public ArenaStatisticListener(MainArena p) {
		plugin = p;
		p.getServer().getPluginManager().registerEvents(this, p);
	}
	public MainArena getPlugin() {
		return plugin;
	}
	
	@EventHandler
	public void onPlayerJoinsArena(ArenaPlayerJoinEvent e) {
		
	}
	
	@EventHandler
	public void onPlayerKill(ArenaPlayerKillEvent e) {
		getPlugin().getStatisticsHandler().increasePlayerStatistic(e.getKiller().getPlayer().getName(), "global.kills", 1);
		getPlugin().getStatisticsHandler().increasePlayerStatistic(e.getKiller().getPlayer().getName(), "arenas." + e.getArena().getProperty().getName() + ".kills", 1);
	}
	
	@EventHandler
	public void onPlayerDies(ArenaPlayerDieEvent e) {
		getPlugin().getStatisticsHandler().increasePlayerStatistic(e.getPlayer().getPlayer().getName(), "global.deaths", 1);
		getPlugin().getStatisticsHandler().increasePlayerStatistic(e.getPlayer().getPlayer().getName(), "arenas." + e.getArena().getProperty().getName() + ".deaths", 1);
	}
	
	
}

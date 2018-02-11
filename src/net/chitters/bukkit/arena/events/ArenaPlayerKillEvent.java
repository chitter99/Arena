package net.chitters.bukkit.arena.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;

public class ArenaPlayerKillEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private ArenaPlayer player;
	private ArenaPlayer killer;
	private Arena arena;
	private PlayerDeathEvent event;
	
	public ArenaPlayerKillEvent(ArenaPlayer p, ArenaPlayer k, Arena a, PlayerDeathEvent e) {
		player = p;
		killer = k;
		arena = a;
		event = e;
	}
	
	public ArenaPlayer getPlayer() {
		return player;
	}
	public Arena getArena() {
		return arena;
	}
	public PlayerDeathEvent getEvent() {
		return event;
	}
	public ArenaPlayer getKiller() {
		return killer;
	}
	
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

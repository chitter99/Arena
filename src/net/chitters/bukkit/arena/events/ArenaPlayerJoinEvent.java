package net.chitters.bukkit.arena.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;

public class ArenaPlayerJoinEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private ArenaPlayer player;
	private Arena arena;
	
	public ArenaPlayerJoinEvent(ArenaPlayer p, Arena a) {
		player = p;
		arena = a;
	}
	
	public ArenaPlayer getPlayer() {
		return player;
	}
	public Arena getArena() {
		return arena;
	}
	
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

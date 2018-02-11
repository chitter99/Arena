package net.chitters.bukkit.arena.objects;

import org.bukkit.scheduler.BukkitRunnable;

public class ArenaGameScheduler extends BukkitRunnable  {
	Arena arena;
	
	public ArenaGameScheduler(Arena a) {
		arena = a;
	}
	
	@Override
	public void run() {
		arena.onGameTick();
	}
}

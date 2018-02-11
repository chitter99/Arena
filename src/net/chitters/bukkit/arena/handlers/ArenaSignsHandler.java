package net.chitters.bukkit.arena.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaSign;
import net.chitters.bukkit.arena.objects.ArenaStatus;

public class ArenaSignsHandler {
	final MainArena plugin;
	
	HashMap<Location, ArenaSign> signs = new HashMap<Location, ArenaSign>();
	
	public ArenaSignsHandler(MainArena p) {
		plugin = p;
		
		loadSignsFromConfig();
		startUpdateTimer();
	}
	
	class UpdateTask extends TimerTask {
		private final ArenaSignsHandler handler;
		public UpdateTask(ArenaSignsHandler a) {
			handler = a;
		}
		@Override
		public void run() {	
			for(ArenaSign s : handler.signs.values()) {
				s.update();
			}
		}
	}
	Timer updateTimer;
	
	public void startUpdateTimer() {
		updateTimer = new Timer();
		updateTimer.schedule(new UpdateTask(this), 0, 1000);
	}
	public void stopUpdateTimer() {
		updateTimer.cancel();
	}
	
	public void registerNewArenaSign(Sign sign, Arena arena) {
		if(sign.getLines().length < 3) return;
		
		ArenaSign s = new ArenaSign();
		s.block = sign;
		s.position = sign.getLocation();
		s.arena = arena;
		
		Location l = s.position;
		String path = l.getWorld().getName() + "x=" + l.getBlockX() + "y=" + l.getBlockY() + "z=" + l.getBlockZ();
		plugin.getSignsConfig().set(path + ".arena", s.arena.getProperty().getName());
		
		plugin.getSignsConfig().set(path + ".locaiton.world", s.position.getWorld().getName());
		plugin.getSignsConfig().set(path + ".locaiton.x", s.position.getBlockX());
		plugin.getSignsConfig().set(path + ".locaiton.y", s.position.getBlockY());
		plugin.getSignsConfig().set(path + ".locaiton.z", s.position.getBlockZ());
		
		plugin.saveSingsFile();
		signs.put(s.position, s);
	}
	public void removeArenaSign(Location l) {
		ArenaSign sign = signs.get(l);
		if(sign == null) return;
		plugin.getSignsConfig().set(l.getWorld().getName() + "x=" + l.getBlockX() + "y=" + l.getBlockY() + "z=" + l.getBlockZ(), null);
		plugin.saveSingsFile();
		signs.remove(l);
	}
	
	public ArenaSign getSign(Block block) {
		if(block.getState() instanceof Sign) return getSign((Sign) block.getState());
		return null;
	}
	public ArenaSign getSign(Sign block) {
		return signs.get(block.getLocation());
	}
	public boolean getIsArenaSign(Location l) {
		return signs.containsKey(l);
	}
	
	public void loadSignsFromConfig() {
		for(String path : plugin.getSignsConfig().getKeys(false)) {
			ArenaSign sing = new ArenaSign();
			
			Location loc = new Location(plugin.getServer().getWorld(plugin.getSignsConfig().getString(path + ".locaiton.world")), plugin.getSignsConfig().getDouble(path + ".locaiton.x"), plugin.getSignsConfig().getDouble(path + ".locaiton.y"), plugin.getSignsConfig().getDouble(path + ".locaiton.z"));
			if(!(loc.getBlock().getState() instanceof Sign)) continue;
			
			Sign block = (Sign) loc.getBlock().getState();
			sing.block = block;
			
			Arena arena = plugin.getArena(plugin.getSignsConfig().getString(path + ".arena"));
			if(arena == null) continue;
			
			sing.arena = arena;
			
			this.signs.put(loc, sing);
		}
		System.out.println("Loaded Sings (" + signs.size() + ")");
	}
}

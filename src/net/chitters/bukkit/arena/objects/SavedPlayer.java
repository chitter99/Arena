package net.chitters.bukkit.arena.objects;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SavedPlayer {
	String name;
	GameMode gm;
	Double health;
	int level;
	Float exp;
	int foodLvl;
	int fireTicks;
	ItemStack[] inv;
	ItemStack[] armor;
	Collection<PotionEffect> activeEffects;
	
	Location loc;
	
	public SavedPlayer(Player player) {
		name = player.getName();
		inv = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		health = player.getHealth();
		activeEffects = player.getActivePotionEffects();
		gm = player.getGameMode();
		level = player.getLevel();
		exp = player.getExp();
		foodLvl = player.getFoodLevel();
		fireTicks = player.getFireTicks();
		loc = player.getLocation();
	}
	public void load(Player player) {
		//player.setGameMode(gm);
		player.setHealth(health);
		player.setLevel(level);
		player.setExp(exp);
		player.setFoodLevel(foodLvl);
		player.setFireTicks(fireTicks);
		player.getInventory().setContents(inv);
		player.getInventory().setArmorContents(armor);
		player.updateInventory();
		
		//Effects
		for(PotionEffect ef : player.getActivePotionEffects()) {
			player.removePotionEffect(ef.getType());
		}
		for(PotionEffect ef : activeEffects) {
			player.addPotionEffect(ef);
		}
		
		player.teleport(loc);
	}
	
	public String getName() {
		return name;
	}
}

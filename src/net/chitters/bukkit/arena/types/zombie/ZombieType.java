package net.chitters.bukkit.arena.types.zombie;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;

public class ZombieType extends ArenaType {
	
	public ZombieType(MainArena p) {
		super(p);
		
		setName("zom");
		setDisplay(ChatColor.DARK_RED + "Zombie");
	}
	
	@Override
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return new ZombieArena(p, pr);
	}
	@Override
	public void setup() {
		getPlugin().getMessageHandler().addMessage("zombie.welcome", "Welcome in a Zombie Arena!");
		getPlugin().getMessageHandler().addMessage("zombie.announcWinnerRed", "The Zombies has won!");
		getPlugin().getMessageHandler().addMessage("zombie.announcWinnerBlue", "The Humans has won!");
		getPlugin().getMessageHandler().addMessage("zombie.playerInfected", "{0} is a Zombie now!");
		getPlugin().getMessageHandler().addMessage("zombie.patientZero", "&cYou are patient zero!");
	}
}

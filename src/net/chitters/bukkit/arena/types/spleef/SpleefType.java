package net.chitters.bukkit.arena.types.spleef;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;

public class SpleefType extends ArenaType {
	
	public SpleefType(MainArena p) {
		super(p);
		
		setName("spleef");
		setDisplay(ChatColor.DARK_PURPLE + "Spleef");
	}
	
	@Override
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return new SpleefArena(p, pr);
	}
	@Override
	public void setup() {
		getPlugin().getMessageHandler().addMessage("spleef.welcome", "Welcome in a Spleef Arena!");
		getPlugin().getMessageHandler().addMessage("spleef.announcWinner", "Player {0} has won the game!");
	}
}

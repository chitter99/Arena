package net.chitters.bukkit.arena.types.ffa;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;

public class FfaType extends ArenaType {
	
	public FfaType(MainArena p) {
		super(p);
		
		setName("ffa");
		setDisplay(ChatColor.AQUA + "Free 4 All");
	}
	
	@Override
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return new FfaArena(p, pr);
	}
	@Override
	public void setup() {
		getPlugin().getMessageHandler().addMessage("ffa.welcome", "Welcome in a FFA Arena!");
		getPlugin().getMessageHandler().addMessage("ffa.announcWinner", "Player {0} has won the game!");
		getPlugin().getMessageHandler().addMessage("ffa.lifesLeft", "You have {0} lifes left!");
	}
}

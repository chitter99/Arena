package net.chitters.bukkit.arena.types.tdm;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;

public class TdmType extends ArenaType {
	
	public TdmType(MainArena p) {
		super(p);
		
		setName("tdm");
		setDisplay(ChatColor.BLUE + "Team Death Match");
	}
	
	@Override
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return new TdmArena(p, pr);
	}
	@Override
	public void setup() {
		getPlugin().getMessageHandler().addMessage("tdm.welcome", "Welcome in a TDM Arena!");
		getPlugin().getMessageHandler().addMessage("tdm.announcWinner", "Team {0} has won the game!");
		getPlugin().getMessageHandler().addMessage("tdm.teamScored", "Team {0} scored ({1}/{2})!");
	}
}

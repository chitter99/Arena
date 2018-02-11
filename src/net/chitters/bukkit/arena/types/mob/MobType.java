package net.chitters.bukkit.arena.types.mob;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;

public class MobType extends ArenaType {
	
	public MobType(MainArena p) {
		super(p);
		
		setName("mob");
		setDisplay(ChatColor.DARK_GREEN + "Mob Arena");
	}
	
	@Override
	public Arena getNewArena(MainArena p, ArenaProperty pr) {
		return new MobArena(p, pr);
	}
	@Override
	public void setup() {
		getPlugin().getMessageHandler().addMessage("mob.welcome", "Welcome in a Mob Arena!");
		getPlugin().getMessageHandler().addMessage("mob.announcWinner", "Player {0} has won the game!");
		getPlugin().getMessageHandler().addMessage("mob.nextWave", "Wave {0} starts now!");
	}
}

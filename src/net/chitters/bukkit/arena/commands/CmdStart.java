package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdStart extends Cmd {

	public CmdStart(CommandHandler h) {
		super(h);
		
		this.name = "start";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		ArenaPlayer aplayer = getPlugin().getArenaPlayer(player);
		if(aplayer == null) return;
		
		Arena arena = aplayer.getArena();
		arena.startGame();
		aplayer.sendMessage("Start Arena!");
	}
	
}

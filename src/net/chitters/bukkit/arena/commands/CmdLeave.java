package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdLeave extends Cmd {

	public CmdLeave(CommandHandler h) {
		super(h);
		
		this.name = "leave";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.leave")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(!getPlugin().isInArena(player)) {
			sendMessage(player, getMessage("errorNotJoined"));
			return;
		}
		
		ArenaPlayer aplayer = getPlugin().getArenaPlayer(player);
		aplayer.leave();
	}
	
}

package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaStatus;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdOpen extends Cmd {

	public CmdOpen(CommandHandler h) {
		super(h);
		
		this.name = "open";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.open")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingArenaName"));
			return;
		}
		
		Arena arena = getPlugin().getArena(args[0]);
		
		if(arena == null) {
			sendMessage(player, getMessage("errorArenaExist"));
			return;
		}
		
		arena.checkStatus();
		sendMessage(player, getMessage("openedArena"));
	}
}

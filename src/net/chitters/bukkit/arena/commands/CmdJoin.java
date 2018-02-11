package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdJoin extends Cmd {

	public CmdJoin(CommandHandler h) {
		super(h);
		
		this.name = "join";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.join")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(getPlugin().isInArena(player) && !(args.length >= 2)) {
			sendMessage(player, getMessage("errorAlreadyJoined"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingArenaName"));
			return;
		}
		
		Arena arena = getPlugin().getArena(args[0]);
		
		if(arena == null) {
			sendMessage(player, getMessage("errorValidArenaName"));
			return;
		}
		
		if(!arena.readyToJoin()) {
			sendMessage(player, getMessage("errorArenaReady"));
			return;
		}
		
		ArenaPlayer aplayer = null;
		if(args.length >= 2) {
			if(!getPlugin().checkHasPermission(player, "arena.join.other")) {
				sendMessage(player, getMessage("errorMissingPermission"));
				return;
			}
			Player other = getPlugin().getServer().getPlayer(args[1]);
			if(other == null) return;
			aplayer = arena.join(other);
		} else {
			aplayer = arena.join(player);
		}
		
		if(aplayer == null) {
			sendMessage(player, getMessage("errorJoin"));
			return;
		}
		
	}
	
}

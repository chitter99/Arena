package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.ArenaType;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdCreate extends Cmd {

	public CmdCreate(CommandHandler h) {
		super(h);
		
		this.name = "create";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.create")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(args.length < 1) {
			sendMessage(player, getMessage("errorMissingArenaName"));
			return;
		}
		if(args.length < 2) {
			sendMessage(player, getMessage("errorMissingArenaType"));
			return;
		}
		
		ArenaType type = getPlugin().getArenaType(args[1]);
		
		if(type == null) {
			sendMessage(player, getMessage("errorValidArenaType"));
			return;
		}
		
		getPlugin().getArenaHandler().createArena(args[0], type, player.getWorld());

		sendMessage(player, getMessage("creatingArenaDone"));
	}
	
}

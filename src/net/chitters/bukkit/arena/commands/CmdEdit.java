package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdEdit extends Cmd {

	public CmdEdit(CommandHandler h) {
		super(h);
		
		this.name = "edit";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.edit")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorAlreadyCreator"));
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
		
		getPlugin().startEdit(player, arena);
		sendMessage(player, getMessage("editDone", arena.getProperty().getDisplay()));
		
	}
}

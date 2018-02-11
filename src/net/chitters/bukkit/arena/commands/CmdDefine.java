package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdDefine extends Cmd {

	public CmdDefine(CommandHandler h) {
		super(h);
		
		this.name = "define";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.edit.define")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingAreaName"));
			return;
		}
		
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		
		if(!creator.getWandHandler().bothSet()) {
			sendMessage(player, getMessage("errorWandMissingPosBoth"));
			return;
		}
		
		Arena arena = creator.getArena();
		String area = args[0].toLowerCase();
		
		arena.getProperty().addArea(area, creator.getWandHandler().getPos1(), creator.getWandHandler().getPos2());
		
		sendMessage(player, getMessage("defineAreaDone", area));
		
	}
}

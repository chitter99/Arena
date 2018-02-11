package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.arena.objects.ArenaStatus;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdSave extends Cmd {

	public CmdSave(CommandHandler h) {
		super(h);
		
		this.name = "save";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		Arena arena = creator.getArena();
		
		getPlugin().stopEdit(creator);
		sendMessage(player, getMessage("editSaved", arena.getProperty().getDisplay()));
		
		if(arena.getStatus().equals(ArenaStatus.IDLE)) {
			sendMessage(player, getMessage("creatingArenaDoneAll"));
		}
		
	}
}

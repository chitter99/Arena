package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdAddpoint extends Cmd {

	public CmdAddpoint(CommandHandler h) {
		super(h);
		
		this.name = "addpoint";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.edit.addpoint")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingPointListName"));
			return;
		}
		
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		Arena arena = creator.getArena();
		
		arena.getProperty().addToSpawnPointList(args[0], new ArenaSpawnPoint("", player.getLocation()));
		
		sendMessage(player, getMessage("pointAddDone", args[0]));
		
	}
}

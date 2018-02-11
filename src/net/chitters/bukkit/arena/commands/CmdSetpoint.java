package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdSetpoint extends Cmd {

	public CmdSetpoint(CommandHandler h) {
		super(h);
		
		this.name = "spawnpoint";
		this.addAlias("sp");
		this.addAlias("setpoint");
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.edit.setpoint")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingPointName"));
			return;
		}
		
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		Arena arena = creator.getArena();
		
		arena.getProperty().addSpawnPoint(args[0], player.getLocation());
		
		sendMessage(player, getMessage("pointSetDone", args[0]));
		
	}
}

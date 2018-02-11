package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdReload extends Cmd {

	public CmdReload(CommandHandler h) {
		super(h);
		
		this.name = "reload";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		
		sendMessage(player, "Reloading...");
		
		//Currently this does nothing...
		
		sendMessage(player, "Done!");
		
	}
}
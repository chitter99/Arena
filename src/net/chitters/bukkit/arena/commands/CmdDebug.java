package net.chitters.bukkit.arena.commands;

import java.util.Map.Entry;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaStatus;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdDebug extends Cmd {

	public CmdDebug(CommandHandler h) {
		super(h);
		
		this.name = "debug";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		
		if(args.length <= 0) return;
		
		if(args[0].equalsIgnoreCase("config")) {
			
			getPlugin().getConfig().set("exmaple", "hello world!");
			getPlugin().saveConfig();
			
		}
	}
}
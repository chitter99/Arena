package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaStatus;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;
import net.md_5.bungee.api.ChatColor;

public class CmdInfo extends Cmd {

	public CmdInfo(CommandHandler h) {
		super(h);
		
		this.name = "info";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.info")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingArenaName"));
			return;
		}
		
		Arena arena = getPlugin().getArena(args[0]);
		
		sendMessage(player, ChatColor.BOLD + "Arena Info:");
		sendMessage(player, "Name: " + arena.getProperty().getName());
		sendMessage(player, "Display: " + arena.getProperty().getDisplay());
		sendMessage(player, "Type: " + arena.getType().getDisplay());
		sendMessage(player, "World: " + arena.getProperty().getWorld().getName());
		if(arena.getStatus().equals(ArenaStatus.CLOSED)) {
			sendMessage(player, "Enabled: false");
		} else {
			sendMessage(player, "Enabled: true");
		}
		sendMessage(player, "Status: " + arena.getStatus().getName());
		if(!arena.getStatus().equals(ArenaStatus.CLOSED)) sendMessage(player, "Players: " + arena.getPlayerCount());
		
	}
	
}

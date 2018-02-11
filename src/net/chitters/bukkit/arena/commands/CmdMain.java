package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;
import net.md_5.bungee.api.ChatColor;

public class CmdMain extends Cmd {

	public CmdMain(CommandHandler h) {
		super(h);
		
		this.name = "main";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		
		player.sendMessage(ChatColor.YELLOW + "-------------[ Arena Main Command ]-------------");
		
		for(Cmd cmd : handler.getCmds()) {
			player.sendMessage("/" +cmd.name);
		}
		
	}
	
}

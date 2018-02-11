package net.chitters.bukkit.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.MainArena;

public class CommandHandler {
	private List<Cmd> commands = new ArrayList<Cmd>();
	public MainArena plugin;
	
	public CommandHandler(MainArena p) {
		plugin = p;
	}
	
	public void sendMessage(Player player, String msg)
	{
		player.sendMessage(msg);
	}
	public String getKey(String key)
	{
		switch (key.toLowerCase()) {
		case "commandinvalid":
			return "Sry, use a valid command.";
		case "commandonlyplayer":
			return "Sry, you can only use this command as a player.";
		case "commanderrorfound":
			return "Error, cmd is null.";
		default:
			return "Error loading Message";
		}
	}
	
	public void addCmd(Cmd cmd) {
		commands.add(cmd);
	}
	public Cmd getCmd(String name)
	{
		for(Cmd cmd : commands) {
			if(cmd.name.equalsIgnoreCase(name)) return cmd;
		}
		for(Cmd cmd : commands) {
			if(cmd.checkAlias(name)) return cmd;
		}
		return null;
	}
	public boolean isCmd(String name)
	{
		if(getCmd(name) != null) return true;
		return false;
	}
	public List<Cmd> getCmds() {
		return commands;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length <= 0)
		{
			args = new String[] {"main"};
		}
		
		String subCmd = args[0];
		boolean isPlayer = false;
		//int arLeng = args.length - 1;
		
		if(sender instanceof Player) isPlayer = true;
		
		if(!isCmd(subCmd)) {
			sendMessage((Player) sender, getKey("commandinvalid"));
			return true;
		}
		
		Cmd cmd = getCmd(subCmd);
		
		if(cmd == null) {
			sendMessage((Player) sender, getKey("commanderrorfound"));
			return true;
		}
		
		if(cmd.isPlayerOnly && !(isPlayer)) {
			sendMessage((Player) sender, getKey("commandonlyplayer"));
			return true;
		}
		
		String[] nargs = new String[0];
		
		if(args.length > 1)  {
			List<String> argL = new ArrayList<String>();
			for(String arg : args) {
				argL.add(arg);
			}
			argL.remove(0);
			nargs = argL.toArray(nargs);
		}
		
		cmd.perform((Player) sender, nargs);
		
		return true;
	}
	
}

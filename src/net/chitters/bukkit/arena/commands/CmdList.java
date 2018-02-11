package net.chitters.bukkit.arena.commands;

import java.util.Map.Entry;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdList extends Cmd {

	public CmdList(CommandHandler h) {
		super(h);
		
		this.name = "list";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.list")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorListExist"));
			return;
		}
		
		switch (args[0].toLowerCase()) {
			case "arenas":
				sendMessage(player, "----------["+args[0]+"]---------");
				for(Entry<String, Arena> e : getPlugin().getArenaHandler().getAllArenas().entrySet()) {
					sendMessage(player, e.getValue().getProperty().getDisplay());
				}
				break;
			default:
				sendMessage(player, getMessage("errorListExist"));
				break;
		}		
		
	}
	
}

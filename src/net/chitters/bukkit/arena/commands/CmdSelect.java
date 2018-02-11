package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;
import net.md_5.bungee.api.ChatColor;

public class CmdSelect extends Cmd {

	public CmdSelect(CommandHandler h) {
		super(h);
		
		this.name = "select";
		addAlias("kit");
	}
	
	@Override
	public void perform(Player player, String[] args) {		
		ArenaPlayer aplayer = getPlugin().getArenaPlayer(player);
		if(aplayer == null) {
			return;
		}
		
		if(args.length <= 0) {
			//sendMessage(player, getMessage("errorMissingKitName"));
			aplayer.openKitSelector();
			return;
		}
		
		ArenaKit kit = getPlugin().getKit(args[0]);
		if(kit == null) {
			aplayer.sendMessage("This kit doesn't exist!");
			return;
		}
		
		if(kit.getNeedsPermission() && !getPlugin().checkHasPermission(player, kit.getKitPermission())) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		aplayer.setNextKit(kit);
		aplayer.sendMessage(getMessage("kitSelected", kit.getDisplay() + ChatColor.GOLD));
	}
	
}

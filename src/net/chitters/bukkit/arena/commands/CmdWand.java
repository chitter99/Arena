package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdWand extends Cmd {

	public CmdWand(CommandHandler h) {
		super(h);
		
		this.name = "wand";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		
		player.getInventory().addItem(creator.getWandHandler().getWandItem());
		player.updateInventory();
		sendMessage(player, getMessage("wandAdded"));
	}
}

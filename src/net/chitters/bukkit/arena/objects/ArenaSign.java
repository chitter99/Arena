package net.chitters.bukkit.arena.objects;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ArenaSign {
	public Sign block;
	public Location position;
	public Arena arena;
	
	public void update() {
		block.setLine(0, ChatColor.WHITE + "[" + ChatColor.DARK_RED + "Arena" + ChatColor.WHITE + "]");
		block.setLine(1, ChatColor.WHITE + arena.getProperty().getDisplay() + " (" + arena.getType().getName() + ")");
		
		if(arena.getStatus().equals(ArenaStatus.CLOSED)) {
			block.setLine(2, arena.getPlugin().getMessageHandler().getKey("singClosed"));
		}
		if(arena.getStatus().equals(ArenaStatus.IDLE)) {
			block.setLine(2, arena.getPlugin().getMessageHandler().getKey("singIdle"));
		}
		if(arena.getStatus().equals(ArenaStatus.INGAME)) {
			block.setLine(2, arena.getPlugin().getMessageHandler().getKey("singInGame") + " (" + arena.getPlayerCount() + "/" + arena.getMaximalPlayers() +")");
		}
		if(arena.getStatus().equals(ArenaStatus.INLOBBY)) {
			block.setLine(2, arena.getPlugin().getMessageHandler().getKey("singInLobby") + " (" + arena.getPlayerCount() + "/" + arena.getMaximalPlayers() +")");
		}
		if(arena.getStatus().equals(ArenaStatus.STOPING)) {
			block.setLine(2, arena.getPlugin().getMessageHandler().getKey("singStoping"));
		}
		block.setLine(3, ChatColor.WHITE + "");
		block.update();
	}
	
	public void run(Player player) {
		if(!arena.readyToJoin()) {
			arena.getPlugin().sendMessage(player, arena.getPlugin().getMessageHandler().getKey("errorArenaReady"));
			return;
		}
		arena.join(player);
	}
	
}

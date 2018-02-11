package net.chitters.bukkit.arena.types.tdm;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.chitters.bukkit.arena.objects.ArenaType;

public class TdmArena extends Arena {
	int scoreRed = 0;
	int scoreBlue = 0;
	
	Team redTeam = getBoard().registerNewTeam("Red");
	Team blueTeam = getBoard().registerNewTeam("Blue");
	
	public TdmArena(MainArena p, ArenaProperty pr) {
		super(p, pr);
		
		redTeam.setPrefix(ChatColor.RED + "");
		blueTeam.setPrefix(ChatColor.BLUE + "");
		
		addRule("GameWinScore", 25);
		
		addReqSpawnPoint("red");
		addReqSpawnPoint("blue");
		addReqSpawnPoint("redlobby");
		addReqSpawnPoint("bluelobby");
	}
	
	@Override
	public ArenaType getType() {
		return new TdmType(plugin);
	}	
	@Override
	public ArenaTeam getNewTeam() {
		return super.getNewTeam();
	}
	
	@Override
	public void announcWinner() {
		broadcast(getMessage("tdm.announcWinner", teamWon.getColor() + teamWon.getName() + ChatColor.GOLD));
	}
	@Override
	public void onPlayerLeaveGame(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.RED)) {
			redTeam.removePlayer(player.getPlayer());
		}
		if(player.getTeam().equals(ArenaTeam.BLUE)) {
			blueTeam.removePlayer(player.getPlayer());
		}
	}
	@Override
	public void onPlayerLeaveLobby(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.RED)) {
			redTeam.removePlayer(player.getPlayer());
		}
		if(player.getTeam().equals(ArenaTeam.BLUE)) {
			blueTeam.removePlayer(player.getPlayer());
		}
	}
	@Override
	public void onPlayerJoinLobby(ArenaPlayer player) {
		sendToPlayers(getMessage("tdm.welcome"));
		if(player.getTeam().equals(ArenaTeam.RED)) {
			redTeam.addPlayer(player.getPlayer());
		}
		if(player.getTeam().equals(ArenaTeam.BLUE)) {
			blueTeam.addPlayer(player.getPlayer());
		}
		
		player.openKitSelector();
	}
	@Override
	public void onPlayerKilledInGame(ArenaPlayer player, ArenaPlayer attacker, ItemStack usedItem) {
		if(attacker.getTeam().equals(ArenaTeam.RED)) {
			scoreRed++;
			sendToPlayers(getMessage("tdm.teamScored", ChatColor.RED + "RED" + ChatColor.GOLD, Integer.toString(scoreRed), Integer.toString(getProperty().getRuleInt("GameWinScore"))));
		}
		if(attacker.getTeam().equals(ArenaTeam.BLUE)) {
			scoreBlue++;
			sendToPlayers(getMessage("tdm.teamScored", ChatColor.BLUE + "BLUE" + ChatColor.GOLD, Integer.toString(scoreBlue), Integer.toString(getProperty().getRuleInt("GameWinScore"))));
		}
		
		if(scoreRed >= getProperty().getRuleInt("GameWinScore")) setWinerTeam(ArenaTeam.RED);
		if(scoreBlue >= getProperty().getRuleInt("GameWinScore")) setWinerTeam(ArenaTeam.BLUE);
	}
	@Override
	public void onGameStart() {
		for(ArenaPlayer player : players) {
			player.giveKit();
		}
	}
	@Override
	public void onPlayerRespawnGame(ArenaPlayer player) {
		player.giveKit();
	}
	
	int gameTimer = 0;
	
	@Override
	public void onGameTimerTask() {
		gameTimer++;
		setTimeStatus(gameTimer);
	}
}

package net.chitters.bukkit.arena.types.ffa;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.chitters.bukkit.arena.objects.ArenaType;

public class FfaArena extends Arena {
	public FfaArena(MainArena p, ArenaProperty pr) {
		super(p, pr);
		
		addReqSpawnPoint("lobby");
		
		getProperty().setRule("allowTeamKilling", true);
		addRule("Lifes", 3);
	}
	
	@Override
	public ArenaType getType() {
		return new FfaType(plugin);
	}	
	@Override
	public ArenaTeam getNewTeam() {
		return ArenaTeam.NONE;
	}
	
	@Override
	public boolean checkSetup() {
		return getProperty().isSpawnPointList("spawnpoints");
	}
	@Override
	public void announcWinner() {
		broadcast(getMessage("ffa.announcWinner", players.get(0).getPlayer().getDisplayName() + ChatColor.GOLD));
	}
	
	@Override
	public ArenaSpawnPoint getLobbySpawnPointForNone(ArenaPlayer player) {
		return getProperty().getSpawnPoint("lobby");
	}
	@Override
	public ArenaSpawnPoint getArenaSpawnPointForNone(ArenaPlayer player) {
		Random rn = new Random();
		ArenaSpawnPoint[] points = getProperty().getSpawnPointList("spawnpoints");
		return points[rn.nextInt(points.length)];
	}
	@Override
	public void onPlayerJoinLobby(ArenaPlayer player) {
		player.sendMessage(getMessage("ffa.welcome"));
		player.openKitSelector();
	}
	@Override
	public void onPlayerKilledInGame(ArenaPlayer player, ArenaPlayer attacker, ItemStack usedItem) {
		if(player.deaths < getProperty().getRuleInt("Lifes")) {
			player.sendMessage(getMessage("ffa.lifesLeft", Integer.toString(getProperty().getRuleInt("Lifes") - player.deaths)));
		}
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
		
		if(player.deaths >= getProperty().getRuleInt("Lifes")) {
			player.kick();
			if(getPlayerCount() == 1) {
				setWinerTeam(ArenaTeam.NONE);
			}
		}
	}
	
	int gameTimer = 0;
	
	@Override
	public void onGameTimerTask() {
		gameTimer++;
		setTimeStatus(gameTimer);
	}
}

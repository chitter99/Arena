package net.chitters.bukkit.arena.types.zombie;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.chitters.bukkit.arena.objects.ArenaType;

public class ZombieArena extends Arena {
	Team redTeam = getBoard().registerNewTeam("Red");
	Team blueTeam = getBoard().registerNewTeam("Blue");
	ArenaKit zombie;
	ArenaPlayer firstZombie;
	
	public ZombieArena(MainArena p, ArenaProperty pr) {
		super(p, pr);
		
		redTeam.setPrefix(ChatColor.RED + "");
		blueTeam.setPrefix(ChatColor.BLUE + "");
		
		zombie = new ArenaKit();
		zombie.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
		zombie.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
		zombie.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
		zombie.addEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0));
		zombie.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 4));
		zombie.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
		
		addRule("GameTimeOut", 600);
		
		addReqSpawnPoint("zombie");
		addReqSpawnPoint("humans");
		addReqSpawnPoint("lobby");
	}
	
	@Override
	public ArenaType getType() {
		return new ZombieType(plugin);
	}	
	@Override
	public ArenaTeam getNewTeam() {
		return ArenaTeam.BLUE;
	}
	@Override
	public ArenaSpawnPoint getGameSpawnPointForTeam(ArenaTeam team) {
		if(team.equals(ArenaTeam.RED)) {
			return getProperty().getSpawnPoint("zombie");
		} else {
			return getProperty().getSpawnPoint("humans");
		}
	}
	@Override
	public ArenaSpawnPoint getLobbySpawnPointForTeam(ArenaTeam team) {
		return getProperty().getSpawnPoint("lobby");
	}
	
	public void checkForWinner() {
		if(getBoard().getTeam("Red").getSize() == 0) {
			setWinerTeam(ArenaTeam.BLUE);
		}
		if(getBoard().getTeam("Blue").getSize() == 0) {
			setWinerTeam(ArenaTeam.RED);
		}
	}
	
	@Override
	public void announcWinner() {
		if(teamWon.equals(ArenaTeam.RED)) {
			broadcast(getMessage("zombie.announcWinnerRed"));
		}
		if(teamWon.equals(ArenaTeam.BLUE)) {
			broadcast(getMessage("zombie.announcWinnerBlue"));
		}
	}
	@Override
	public void onPlayerLeaveGame(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.RED)) redTeam.removePlayer(player.getPlayer());
		if(player.getTeam().equals(ArenaTeam.BLUE)) blueTeam.removePlayer(player.getPlayer());
		checkForWinner();
	}
	@Override
	public void onPlayerLeaveLobby(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.RED)) redTeam.removePlayer(player.getPlayer());
		if(player.getTeam().equals(ArenaTeam.BLUE)) blueTeam.removePlayer(player.getPlayer());
	}
	@Override
	public void onPlayerJoinLobby(ArenaPlayer player) {
		sendToPlayers(getMessage("zombie.welcome"));
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
		if(player.getTeam().equals(ArenaTeam.BLUE)) {
			player.setTeam(ArenaTeam.RED);
			
			blueTeam.removePlayer(player.getPlayer());
			redTeam.addPlayer(player.getPlayer());
			
			sendToPlayers(getMessage("zombie.playerInfected", player.getPlayer().getDisplayName()));
		}
	}
	@Override
	public void onGameStart() {
		gameTimer = 0;
				
		Random rnd = new Random();
		int rndP = rnd.nextInt(getPlayerCount() - 1);
		
		firstZombie = players.get(rndP);
		firstZombie.setTeam(ArenaTeam.RED);
		firstZombie.sendMessage(getMessage("zombie.patientZero"));
		blueTeam.removePlayer(firstZombie.getPlayer());
		redTeam.addPlayer(firstZombie.getPlayer());
		
		for(ArenaPlayer player : players) {
			if(player.getTeam().equals(ArenaTeam.RED)) {
				player.setNextKit(zombie);
			}
			player.giveKit();
		}
	}
	@Override
	public void onPlayerRespawnGame(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.RED)) {
			player.setNextKit(zombie);
		}
		player.giveKit();
	}
	
	int gameTimer;
	
	@Override
	public void onGameTimerTask() {
		gameTimer++;
		setTimeStatus(gameTimer);
		
		if(gameTimer >= getProperty().getRuleInt("GameTimeOut")) {
			setWinerTeam(ArenaTeam.BLUE);
		} else {
			checkForWinner();
		}
		
	}
}

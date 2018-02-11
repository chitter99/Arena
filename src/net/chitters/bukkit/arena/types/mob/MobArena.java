package net.chitters.bukkit.arena.types.mob;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.md_5.bungee.api.ChatColor;
import sun.java2d.pipe.SpanClipRenderer;

public class MobArena extends Arena {
	public MobArena(MainArena p, ArenaProperty pr) {
		super(p, pr);
		
		addReqSpawnPoint("lobby");
		addReqSpawnPoint("game");
		
		getProperty().setRule("allowTeamKilling", false);
		setMinimalPlayers(1);
	}
	
	@Override
	public ArenaType getType() {
		return new MobType(plugin);
	}	
	@Override
	public ArenaTeam getNewTeam() {
		return ArenaTeam.NONE;
	}
	@Override
	public boolean checkSetup() {
		return getProperty().isSpawnPointList("mobspawns");
	}
	
	@Override
	public void announcWinner() {
		broadcast(getMessage("mob.announcWinner", players.get(0).getPlayer().getDisplayName() + ChatColor.GOLD));
	}
	@Override
	public ArenaSpawnPoint getLobbySpawnPointForNone(ArenaPlayer player) {
		return getProperty().getSpawnPoint("lobby");
	}
	@Override
	public ArenaSpawnPoint getArenaSpawnPointForNone(ArenaPlayer player) {
		return getProperty().getSpawnPoint("game");
	}
	@Override
	public void onPlayerJoinLobby(ArenaPlayer player) {
		sendToPlayers(getMessage("mob.welcome"));
		player.openKitSelector();
	}
	@Override
	public void onPlayerDieInGame(ArenaPlayer player, DamageCause damageCause) {
		if(getPlayerCount() <= 1) {
			setWinerTeam(player.getTeam());
		} else {
			player.kick();
		}
	}
	@Override
	public void onGameStart() {
		for(ArenaPlayer player : players) {
			player.giveKit();
		}
		gameTimer = 10;
		wave = 0;
		timeoutToNext = 30;
		spawnMobs = false;
	}
	@Override
	public void onPlayerRespawnGame(ArenaPlayer player) {
		player.giveKit();
	}
	
	int gameTimer = 0;
	
	int wave = 0;
	int timeoutToNext = 30;
	boolean spawnMobs = false;
	
	@Override
	public void onGameTimerTask() {
		gameTimer--;
		setTimeStatus(gameTimer);
		
		if(gameTimer <= 0) {
			//Next Wave starts
			wave++;
			gameTimer += timeoutToNext;
			sendToPlayers(getMessage("mob.nextWave", Integer.toString(wave)));
			
			spawnMobs = true;
		}
	}
	@Override
	public void onGameTick() {
		if(spawnMobs) {
			Random rn = new Random();
			ArenaSpawnPoint[] points = getProperty().getSpawnPointList("mobspawns");
			for(int i=2*wave; i>0; i--) {
				ArenaSpawnPoint point = points[rn.nextInt(points.length)];
				Entity entity = point.getWorld().spawnEntity(point.getLocation(), EntityType.ZOMBIE);
				entity.setCustomName(ChatColor.GREEN + "Zombie");
				entity.setCustomNameVisible(true);
			}
			spawnMobs = false;
		}
	}
}

package net.chitters.bukkit.arena.types.spleef;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaArea;
import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.chitters.bukkit.arena.objects.ArenaType;

public class SpleefArena extends Arena {
	public SpleefArena(MainArena p, ArenaProperty pr) {
		super(p, pr);
		
		addReqSpawnPoint("lobby");
		addReqArea("blockZone");
		addReqArea("offZone");
		
		addRule("blockId", 49);
		
		getProperty().setRule("allowTeamKilling", true);
		
		spleefKit = new ArenaKit();
		spleefKit.addItem(new ItemStack(Material.DIAMOND_HOE));
	}
	
	ArenaKit spleefKit;
	
	@Override
	public ArenaType getType() {
		return new SpleefType(plugin);
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
		broadcast(getMessage("spleef.announcWinner", players.get(0).getPlayer().getDisplayName() + ChatColor.RED));
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
		sendToPlayers(getMessage("spleef.welcome"));
	}
	@Override
	public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
		if(e.getClickedBlock().getType().equals(Material.getMaterial(getProperty().getRuleInt("blockId")))) {
			if(getProperty().getArea("blockZone").checkLocIn(e.getClickedBlock().getLocation())) {
				e.getClickedBlock().breakNaturally(new ItemStack(Material.AIR));
			}
		}
	}
	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		ArenaArea offZone = getProperty().getArea("offZone");
		if(offZone.checkLocIn(e.getPlayer().getLocation())) {
			//if(!e.getPlayer().isDead()) e.getPlayer().setHealth(0.0);
			player.kick();
		}
	}
	
	@Override
	public void onGameStart() {
		for(ArenaPlayer player : players) {
			player.setNextKit(spleefKit);
			player.giveKit();
		}
	}
	@Override
	public void onPlayerRespawnGame(ArenaPlayer player) {
		if(getPlayerCount() == 1) {
			setWinerTeam(player.getTeam());
		} else {
			player.kick();
		}
	}
	@Override
	public void onFirstPlayerJoinLobby(ArenaPlayer player) {
		getProperty().getArea("blockZone").setBlockTypes(Material.getMaterial(getProperty().getRuleInt("blockId")));
	}
	
	int gameTimer = 0;
	
	@Override
	public void onGameTimerTask() {
		gameTimer++;
		setTimeStatus(gameTimer);
	}
}

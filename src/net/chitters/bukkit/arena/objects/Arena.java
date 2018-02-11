package net.chitters.bukkit.arena.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.events.ArenaPlayerJoinEvent;
import net.md_5.bungee.api.ChatColor;

public class Arena {
	protected final MainArena plugin;
	
	protected ArenaProperty property;
	protected ArenaStatus status = ArenaStatus.CLOSED;
	
	protected List<SavedPlayer> backedUpPlayers = new ArrayList<SavedPlayer>();
	
	public Arena(MainArena p, ArenaProperty pr) {
		plugin = p;
		property = pr;
		
		addRule("allowTeamKilling", false);
		addRule("spawnProtection", 3); //In Secs
		addRule("lobbyTimeout", 60);
		addRule("minimalPlayers", 2);
		addRule("maximalPlayers", 6);
		addRule("defaultKit", "fighter");
		addRule("needsPermission", false);
		
		addReqArea("arena");
		addReqArea("lobby");
	}
	
	public void setProperty(ArenaProperty pr) {
		property = pr;
	}
	public void setStatus(ArenaStatus st) {
		status = st;
	}
	
	//Config
	public int getMinimalPlayers() {
		return getProperty().getRuleInt("minimalPlayers");
	}
	public int getMaximalPlayers() {
		return getProperty().getRuleInt("maximalPlayers");
	}
	public void setMinimalPlayers(int n) {
		getProperty().setRule("minimalPlayers", n);
	}
	public void setMaximalPlayers(int n) {
		getProperty().setRule("maximalPlayers", n);
	}
	
	public ArenaStatus getStatus() {
		return this.status;
	}	
	public MainArena getPlugin()
	{
		return plugin;
	}
	public ArenaProperty getProperty() {
		return property;
	}
	
	public String getMessage(String key) {
		return plugin.getMessageHandler().getKey(key);
	}
	public String getMessage(String key, String... other) {
		String value = plugin.getMessageHandler().getKey(key);
		int i = 0;
		for(String x : other) {
			value = value.replace("{" + i + "}", x);
			i++;
		}
		return value;
	}
	
	//BackedUpPlayers
	public void savePlayer(Player player) {
		if(isSavedPlayer(player)) removeSavedPlayer(player);
		backedUpPlayers.add(new SavedPlayer(player));
	}
	public void removeSavedPlayer(Player player) {
		int i = 0;
		for(SavedPlayer sp : backedUpPlayers) {
			if(sp.getName().equalsIgnoreCase(player.getName())) backedUpPlayers.remove(i);
			i++;
		}
	}
	public boolean isSavedPlayer(Player player) {
		for(SavedPlayer sp : backedUpPlayers) {
			if(sp.getName().equalsIgnoreCase(player.getName())) return true;
		}
		return false;
	}
	public void loadPlayer(Player player) {
		int i = 0;
		for(SavedPlayer sp : backedUpPlayers) {
			if(sp.getName().equalsIgnoreCase(player.getName())) {
				sp.load(player);
				backedUpPlayers.remove(i);
				return;
			}
			i++;
		}
	}
	
	//Players
	public List<ArenaPlayer> players = new ArrayList<ArenaPlayer>();
	public void sendToPlayers(String message) {
		for(ArenaPlayer player : players) {
			player.sendMessage(message);
		}
	}
	public int getPlayerCount() {
		return players.size();
	}

	public ArenaPlayer getPlayer(String name)
	{
		for(ArenaPlayer aplayer : this.players) {
			if(name.equals(aplayer.getPlayer().getName())) return aplayer;
		}
		return null;
	}
	public ArenaPlayer getPlayer(Player player) {
		return getPlayer(player.getName());
	}
	public ArenaPlayer join(Player player)
	{
		if(isInArena(player) || !readyToJoin() || plugin.isInArena(player)) return null;
		if(getProperty().getRuleBoolean("needsPermission")) {
			if(!plugin.checkHasPermission(player, "arena.access." + getProperty().getName())) {
				plugin.sendMessage(player, getMessage("errorJoinNoPermission"));
				return null;
			}
		}
		//player.setGameMode(GameMode.SURVIVAL);
		ArenaPlayer aplayer = new ArenaPlayer(player, this);
		
		aplayer.setTeam(getNewTeam());
		
		savePlayer(player);
		clearPlayer(player);
		player.setScoreboard(getBoard());
		
		if(getStatus() == ArenaStatus.IDLE) {
			setStatus(ArenaStatus.INLOBBY);
			startLobbyTimer();
			onFirstPlayerJoinLobby(aplayer);
		}
		
		players.add(aplayer);
		getPlugin().getServer().getPluginManager().callEvent(new ArenaPlayerJoinEvent(aplayer, this));
		
		if(aplayer.getTeam().equals(ArenaTeam.NONE)) {
			aplayer.teleport(getLobbySpawnPointForNone(aplayer));
		} else {
			aplayer.teleport(getLobbySpawnPointForTeam(aplayer.getTeam()));
			aplayer.sendMessage(getMessage("lobbyChooseTeam", aplayer.getTeam().getColor() + aplayer.getTeam().getName() + ChatColor.GOLD));
		}
		
		sendToPlayers(getMessage("joinLobby", player.getDisplayName()));
		onPlayerJoinLobby(aplayer);	
		
		return aplayer;
	}
	public void leave(ArenaPlayer player) {
		leave(player.getPlayer());
	}
	public void leave(Player player)
	{
		if(!isInArena(player)) return;
		ArenaPlayer aplayer = getPlayer(player);
		
		kick(aplayer);
	}
	public boolean isInArena(Player player)
	{
		for(ArenaPlayer aplayer : this.players) {
			if(player.equals(aplayer.getPlayer())) return true;
		}
		return false;
	}
	public boolean isInArena(ArenaPlayer player)
	{
		return this.players.contains(player);
	}
	public ArenaSpawnPoint getPlayerSpawnPoint(ArenaPlayer player) {
		if(getStatus().equals(ArenaStatus.INGAME)) {
			
		}
		return null;
	}
	public void kick(ArenaPlayer player) {
		kick(player, "");
	}
	public void kick(ArenaPlayer player, String msg) {
		if(!isInArena(player)) return;
		if(getStatus() == ArenaStatus.INLOBBY) {
			onPlayerLeaveLobby(player);
			players.remove(player);
			
			if(getPlayerCount() <= 0) {
				cancelLobbyTimer();
				setStatus(ArenaStatus.IDLE);
			}
			
			clearPlayer(player.getPlayer());
			loadPlayer(player.getPlayer());
			
			player.sendMessage(msg);
		}
		if(getStatus() == ArenaStatus.INGAME) {
			onPlayerLeaveGame(player);
			players.remove(player);
			
			clearPlayer(player.getPlayer());
			loadPlayer(player.getPlayer());
			
			player.sendMessage(msg);
		}
		if(getStatus() == ArenaStatus.STOPING) {
			players.remove(player);
			
			clearPlayer(player.getPlayer());
			loadPlayer(player.getPlayer());
			
			if(getStatus().equals(ArenaStatus.STOPING)) {
				if(player.getTeam().equals(teamWon)) {
					player.reward();
				}
			}
			
			player.sendMessage(msg);
		}
	}
	public void kickAll() {
		ArenaPlayer[] pl = new ArenaPlayer[players.size()];
		pl = players.toArray(pl);
		for(ArenaPlayer player : pl) {
			kick(player);
		}
	}
	
	public void clearPlayer(Player player) {
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setExp(0F);
		player.setLevel(0);
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		for(PotionEffect ef : player.getActivePotionEffects()) {
			player.removePotionEffect(ef.getType());
		}
	}
	
	//Count/Timer
	public void setTimeStatus(int s) {
		for(ArenaPlayer player : players) {
			player.getPlayer().setLevel(s);
		}
	}
	public void setTimeStatus(ArenaTeam team, int s) {
		for(ArenaPlayer player : players) {
			if(player.getTeam().equals(team)) player.getPlayer().setLevel(s);
		}
	}
	
	//Settings/Rules
	List <String> requiredAreas = new ArrayList<String>();
	List <String> requiredSpawnpoints = new ArrayList<String>();
	
	public void addReqArea(String name) {
		requiredAreas.add(name);
	}
	public boolean isReqArea(String name) {
		return requiredAreas.contains(name);
	}
	public List<String> getReqAreas() {
		return requiredAreas;
	}
	
	public void addReqSpawnPoint(String name) {
		requiredSpawnpoints.add(name);
	}
	public boolean isReqSpawnPoint(String name) {
		return requiredSpawnpoints.contains(name);
	}
	public List<String> getReqSpawnPoint() {
		return requiredSpawnpoints;
	}
	
	public void addRule(String x, Object y) {
		if(!getProperty().isRule(x)) getProperty().setRule(x, y);
	}
	
	public void checkStatus() {
		status = ArenaStatus.CLOSED;
		for(String area : getReqAreas()) {
			if(!getProperty().isArea(area)) return;
		}
		for(String point : getReqSpawnPoint()) {
			if(!getProperty().isSpawnPoint(point)) return;
		}
		if(!checkSetup()) return;
		status = ArenaStatus.IDLE;
	}
	public boolean readyToJoin() {
		if(status.equals(ArenaStatus.IDLE) || status.equals(ArenaStatus.INLOBBY)) return true;
		return false;
	}	
	public boolean readyToStart() {
		if(getPlayerCount() < getProperty().getRuleInt("minimalPlayer")) return false;
		return true;
	}
	
	//Scoreboard
	protected ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
	protected Scoreboard board = scoreboardManager.getNewScoreboard();
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}
	public Scoreboard getBoard() {
		return board;
	}
	
	//Tasks 
	class LobbyTask extends TimerTask {
		private final Arena arena;
		private int left = 0;
		private int current = 0;
		public LobbyTask(Arena a, int l) {
			arena = a;
			left = l;
		}
		@Override
		public void run() {	
			if(arena.getStatus().equals(ArenaStatus.INLOBBY)) {
				current++;
				arena.setTimeStatus((left - current));
				if(current > left) {
					cancel();
					arena.sendToPlayers("Time is up!");
					if(readyToStart()) {
						startGame();
					} else {
						kickAll();
					}
				} else {
					arena.onLobbyTimerTask();
				}
			}
		}
	}
	class GameTask extends TimerTask {
		private final Arena arena;
		public GameTask(Arena a) {
			arena = a;
		}
		@Override
		public void run() {	
			if(arena.getStatus().equals(ArenaStatus.INGAME)) {
				if(arena.getPlayerCount() < getProperty().getRuleInt("minimalPlayer")) {
					arena.setWinerTeam((((ArenaPlayer) players.toArray()[0])).getTeam());
				} else {
					arena.onGameTimerTask();
				}
			}
		}
	}
	
	Timer lobbyTimer;
	Timer gameTimer;
	
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	int gameTaskId;
	
	public void startLobbyTimer() {
		lobbyTimer = new Timer();
		lobbyTimer.schedule(new LobbyTask(this, getProperty().getRuleInt("lobbyTimeout")), 0, 1000);
	}
	public void cancelLobbyTimer() {
		if(lobbyTimer == null) return;
		lobbyTimer.cancel();
		lobbyTimer = null;
	}
	public void startGameTimer() {
		gameTimer = new Timer();
		gameTimer.schedule(new GameTask(this), 0, 1000);
	}
	public void cancelGameTimer() {
		if(!gameTimer.equals(null)) {
			gameTimer.cancel();
			gameTimer = null;
		}
	}
	public void startGameTask() {
		gameTaskId = scheduler.scheduleSyncRepeatingTask(getPlugin(), new ArenaGameScheduler(this), 0L, 20L);
	}
	public void cancelGameTask() {
		scheduler.cancelTask(gameTaskId);
		gameTaskId = 0;
	}
	
	public void startGame() {
		if(getStatus().equals(ArenaStatus.INGAME)) stopGame();
		if(getStatus().equals(ArenaStatus.INLOBBY)) cancelLobbyTimer();
		
		setTimeStatus(0);
		setStatus(ArenaStatus.INGAME);
		
		onGameStart();
		
		for(ArenaPlayer player : players) {
			if(player.getTeam().equals(ArenaTeam.NONE)) {
				player.teleport(getArenaSpawnPointForNone(player));
			} else {
				player.teleport(getGameSpawnPointForTeam(player.getTeam()));
			}
		}
		
		startGameTimer();
		startGameTask();
	}
	public void stopGame() {
		if(!getStatus().equals(ArenaStatus.INGAME)) return;
		setStatus(ArenaStatus.STOPING);
		cancelGameTimer();
		cancelGameTask();
		reset();
	}
	
	public void reset() {
		if(players.size() > 0) kickAll();
		
		if(getStatus() == ArenaStatus.INLOBBY) cancelLobbyTimer();
		if(getStatus() == ArenaStatus.INGAME) stopGame();
		
		onReset();
		setStatus(ArenaStatus.IDLE);
	}
	
	//Methods
	public ArenaTeam getNewTeam() {
		if(getPlayerCount() == 0) return ArenaTeam.RED;
		if(((double) getPlayerCount() / 2) % 0 == 0) {
			return ArenaTeam.RED;
		} else {
			return ArenaTeam.BLUE;
		}
	}
	public ArenaType getType() {
		return null;
	}
	public ArenaSpawnPoint getPlayerGameSpawnPoint(ArenaPlayer player) {
		if(player.getTeam().equals(ArenaTeam.NONE)) {
			return getArenaSpawnPointForNone(player);
		} else {
			return getGameSpawnPointForTeam(player.getTeam());
		}
	}
	public ArenaArea getCurrentArea() {
		if(getStatus().equals(ArenaStatus.INLOBBY)) {
			return getProperty().getArea("lobby");
		}
		if(getStatus().equals(ArenaStatus.INGAME)) {
			return getProperty().getArea("arena");
		}
		return null;
	}
	public void broadcast(String message) {
		getPlugin().broadcast(message);
	}
	public String getDeathMessage(DamageCause cause) {
		switch(cause) {
			case FALL: return "{0} feel over a clip";
			case PROJECTILE: return "{0} has been shot";
			default: return "{0} has died!";
		}
	}
	public ArenaKit getDefaultKit() {
		return getPlugin().getKit(getProperty().getRuleString("defaultKit"));
	}
	public Collection<ArenaKit> getKits() {
		return getPlugin().getKits().values();
	}
	public Inventory getKitInventory() {
		return getPlugin().getServer().createInventory(null, 9*3, "Select your Kit");
	}
	
	public ArenaTeam teamWon = null;
	public void setWinerTeam(ArenaTeam team) {
		System.out.println("Team " + team.getName() + " has won in " + getProperty().getDisplay());
		teamWon = team;
		announcWinner();
		
		stopGame();
		
		onGameEnd();
		kickAll();
	}
	public void announcWinner() {
		broadcast("Team " + teamWon.getColor() + teamWon.getName() + ChatColor.GOLD + " has won!");
	}

	public ArenaSpawnPoint getLobbySpawnPointForTeam(ArenaTeam team) {
		return getProperty().getSpawnPoint(team.getLobbyPoint());
	}
	public ArenaSpawnPoint getGameSpawnPointForTeam(ArenaTeam team) {
		return getProperty().getSpawnPoint(team.getSpawnPoint());
	}
	public ArenaSpawnPoint getLobbySpawnPointForNone(ArenaPlayer player) {
		return null;
	}
	public ArenaSpawnPoint getArenaSpawnPointForNone(ArenaPlayer player) {
		return null;
	}
	public boolean checkSetup() {
		return true;
	}
	
	//Events
	public void onFirstPlayerJoinLobby(ArenaPlayer player) { }
	public void onPlayerJoinLobby(ArenaPlayer player) { }
	public void onPlayerLeaveLobby(ArenaPlayer player) { }
	public void onPlayerJoinGame(ArenaPlayer player) { }
	public void onPlayerRespawnGame(ArenaPlayer player) { }
	public void onPlayerLeaveGame(ArenaPlayer player) { }
	public void onPlayerDieInGame(ArenaPlayer player, DamageCause damageCause) { }
	public void onPlayerKilledInGame(ArenaPlayer player, ArenaPlayer attacker, ItemStack usedItem) { }
	public void onLobbyTimerTask() { }
	public void onGameTimerTask() { }
	public void onGameTick() { }
	public void onGameStart() { }
	public void onGameEnd() { }
	public void onReset() { }
	
	public void onBlockBreak(BlockBreakEvent e) { }
	public void onPlayerInteract(PlayerInteractEvent e) { }
	public void onPlayerMove(PlayerMoveEvent e) { }
}

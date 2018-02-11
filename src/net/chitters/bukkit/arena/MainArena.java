package net.chitters.bukkit.arena;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.chitters.bukkit.arena.commands.CmdAddpoint;
import net.chitters.bukkit.arena.commands.CmdClose;
import net.chitters.bukkit.arena.commands.CmdCreate;
import net.chitters.bukkit.arena.commands.CmdDebug;
import net.chitters.bukkit.arena.commands.CmdDefine;
import net.chitters.bukkit.arena.commands.CmdEdit;
import net.chitters.bukkit.arena.commands.CmdInfo;
import net.chitters.bukkit.arena.commands.CmdJoin;
import net.chitters.bukkit.arena.commands.CmdLeave;
import net.chitters.bukkit.arena.commands.CmdList;
import net.chitters.bukkit.arena.commands.CmdMain;
import net.chitters.bukkit.arena.commands.CmdOpen;
import net.chitters.bukkit.arena.commands.CmdReload;
import net.chitters.bukkit.arena.commands.CmdRule;
import net.chitters.bukkit.arena.commands.CmdSave;
import net.chitters.bukkit.arena.commands.CmdSelect;
import net.chitters.bukkit.arena.commands.CmdSetpoint;
import net.chitters.bukkit.arena.commands.CmdStart;
import net.chitters.bukkit.arena.commands.CmdWand;
import net.chitters.bukkit.arena.handlers.ArenaHandler;
import net.chitters.bukkit.arena.handlers.ArenaKitsHandler;
import net.chitters.bukkit.arena.handlers.ArenaMisc;
import net.chitters.bukkit.arena.handlers.ArenaSignsHandler;
import net.chitters.bukkit.arena.handlers.ArenaStatisticsHandler;
import net.chitters.bukkit.arena.handlers.MessageHandler;
import net.chitters.bukkit.arena.listeners.ArenaPlayerListener;
import net.chitters.bukkit.arena.listeners.ArenaStatisticListener;
import net.chitters.bukkit.arena.listeners.PlayerListener;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaArea;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.arena.objects.ArenaSpawnPoint;
import net.chitters.bukkit.arena.objects.ArenaType;
import net.chitters.bukkit.arena.types.ffa.FfaType;
import net.chitters.bukkit.arena.types.mob.MobType;
import net.chitters.bukkit.arena.types.spleef.SpleefType;
import net.chitters.bukkit.arena.types.tdm.TdmType;
import net.chitters.bukkit.arena.types.zombie.ZombieType;
import net.chitters.bukkit.command.CommandHandler;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class MainArena extends JavaPlugin {
	
	@Override
	public void onEnable() {
		//Load Misc
		misc = new ArenaMisc(this);
		
		loadConfig();
		
		//Loading Arenas
		arenaHandler = new ArenaHandler(this);
		
		arenaHandler.registerArenaType(new TdmType(this));
		arenaHandler.registerArenaType(new FfaType(this));
		arenaHandler.registerArenaType(new MobType(this));
		arenaHandler.registerArenaType(new SpleefType(this));
		arenaHandler.registerArenaType(new ZombieType(this));
		
		arenaHandler.loadArenas();
		
		//Loadind Kits
		arenaKitsHandler = new ArenaKitsHandler(this);
		arenaKitsHandler.loadKits();
		
		//Stats
		statisticsHandler = new ArenaStatisticsHandler(this);
		statisticsHandler.loadPlayerStatisticsConfig();
		statisticsHandler.registerListeners();
		
		loadSingsConfig();
		
		setupEconomy();
		setupPermissions();
		
		//Handler
		commandHandler = new CommandHandler(this);
		messagesHandler = new MessageHandler(this);
		singsHandler = new ArenaSignsHandler(this);
		
		//Cmds
		commandHandler.addCmd(new CmdMain(commandHandler));
		commandHandler.addCmd(new CmdList(commandHandler));
		commandHandler.addCmd(new CmdReload(commandHandler));
		commandHandler.addCmd(new CmdDebug(commandHandler));
		commandHandler.addCmd(new CmdInfo(commandHandler));
		
		commandHandler.addCmd(new CmdCreate(commandHandler));
		commandHandler.addCmd(new CmdEdit(commandHandler));
		commandHandler.addCmd(new CmdSetpoint(commandHandler));
		commandHandler.addCmd(new CmdAddpoint(commandHandler));
		commandHandler.addCmd(new CmdRule(commandHandler));
		commandHandler.addCmd(new CmdDefine(commandHandler));
		commandHandler.addCmd(new CmdSave(commandHandler));
		commandHandler.addCmd(new CmdWand(commandHandler));
		
		commandHandler.addCmd(new CmdClose(commandHandler));
		commandHandler.addCmd(new CmdOpen(commandHandler));
		
		commandHandler.addCmd(new CmdJoin(commandHandler));
		commandHandler.addCmd(new CmdLeave(commandHandler));
		commandHandler.addCmd(new CmdStart(commandHandler));
		commandHandler.addCmd(new CmdSelect(commandHandler));
		
		//Listeners
		playerListener = new PlayerListener(this);
		arenaPlayerListener = new ArenaPlayerListener(this);
		
		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(arenaPlayerListener, this);
		
		getArenaHandler().setupArenaTypes();
	}
	@Override
	public void onDisable() {
		for(Arena arena : getArenaHandler().getAllArenas().values()) {
			arena.reset();
		}
		getStatisticsHandler().save();
		getSingsHandler().stopUpdateTimer();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return commandHandler.onCommand(sender, command, label, args);
	}
	
	//Handler
	protected CommandHandler commandHandler;
	protected MessageHandler messagesHandler;
	protected ArenaSignsHandler singsHandler;
	protected ArenaHandler arenaHandler;
	protected ArenaKitsHandler arenaKitsHandler;
	protected ArenaMisc misc;
	protected ArenaStatisticsHandler statisticsHandler;

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
	public MessageHandler getMessageHandler() {
		return messagesHandler;
	}
	public ArenaSignsHandler getSingsHandler() {
		return singsHandler;
	}
	public ArenaHandler getArenaHandler() {
		return arenaHandler;
	}
	public ArenaKitsHandler getArenaKitsHandler() {
		return arenaKitsHandler;
	}
	public ArenaMisc getMisc() {
		return misc;
	}
	public ArenaStatisticsHandler getStatisticsHandler() {
		return statisticsHandler;
	}
	
	//Listeners
	PlayerListener playerListener;
	ArenaPlayerListener arenaPlayerListener;
	
	public File kitsFile;
	public FileConfiguration kitsConfig;
	
	public File singsFile;
	public FileConfiguration singsConfig;
		
	public void loadSingsConfig()
	{
		if(singsFile == null) {
			singsFile = new File(getDataFolder(), "sings.yml");
			if(!singsFile.exists()) {
				try {
					singsFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		singsConfig = YamlConfiguration.loadConfiguration(singsFile);
	}
	public FileConfiguration getSignsConfig()
	{
		if(singsConfig == null) loadSingsConfig();
		return singsConfig;
	}	
	public void saveSingsFile() {
		try {
			getSignsConfig().save(singsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		//Signs
		getConfig().addDefault("signs.cmdFirstLine", "[arena]");
		getConfig().addDefault("signs.title", "&4Arena");
		
		//Creation
		getConfig().addDefault("creation.wandItem", 280);
		
		//Block
		getConfig().addDefault("block.whitlistedCommands", new String[]{"arena","ar","a"});
		
		//Reward
		getConfig().addDefault("reward.giveMoney", true);
		getConfig().addDefault("reward.moneyPerKill", 10.0);	
		getConfig().addDefault("reward.moneyPerWin", 50.0);
		
		//Lang
		getConfig().addDefault("lang.usedKey", "en");
		
		getConfig().addDefault("lang.en.creatingArenaDone", "Done creating arena!");
		getConfig().addDefault("lang.en.creatingArenaDoneAll", "Arena is ready to join!");
		getConfig().addDefault("lang.en.defineAreaDone", "Area {0} defined!");
		getConfig().addDefault("lang.en.editDone", "You are editing {0} now!");
		getConfig().addDefault("lang.en.editSaved", "Your changes in {0} were saved!");
		getConfig().addDefault("lang.en.propertyAddDone", "Done!");
		getConfig().addDefault("lang.en.pointSetDone", "Added spawn point {0}!");
		getConfig().addDefault("lang.en.pointAddDone", "Added spawn point to list {0}!");
		
		getConfig().addDefault("lang.en.wandAdded", "Added wand item to your inventory!");
		getConfig().addDefault("lang.en.wandSetPos1", "Pos1 set!");
		getConfig().addDefault("lang.en.wandSetPos2", "Pos2 set!");
		
		getConfig().addDefault("lang.en.propertyDisplay", "{0}({2}) equals to {1} !");
		
		getConfig().addDefault("lang.en.lobbyChooseTeam", "You are in Team {0}!");
		getConfig().addDefault("lang.en.joinLobby", "{0} joined the lobby!");
		
		getConfig().addDefault("lang.en.closedArena", "You closed the Arena!");
		getConfig().addDefault("lang.en.openedArena", "You opended the Arena!");
		
		getConfig().addDefault("lang.en.kitSelected", "You selected {0} as kit!");
		
		getConfig().addDefault("lang.en.creatingSignDone", "You created a Arena sign!");
		getConfig().addDefault("lang.en.singClosed", "&7Closed");
		getConfig().addDefault("lang.en.singStoping", "&6Stoping");
		getConfig().addDefault("lang.en.singInLobby", "&2Lobby");
		getConfig().addDefault("lang.en.singInGame", "&cPlaying");
		getConfig().addDefault("lang.en.singIdle", "&6Ready to Join");
		
		getConfig().addDefault("lang.en.errorJoinNoPermission", "Sorry you don't have the required permission for this arena!");
		getConfig().addDefault("lang.en.errorMissingPermission", "Sorry you don't have the required permission!");
		getConfig().addDefault("lang.en.errorMissingArenaName", "Error, missing arena name!");
		getConfig().addDefault("lang.en.errorMissingArenaType", "Error, missing arena type!");
		getConfig().addDefault("lang.en.errorMissingPlayerName", "Error, missing player name!");
		getConfig().addDefault("lang.en.errorMissingAreaName", "Error, missing area name!");
		getConfig().addDefault("lang.en.errorMissingPropertyName", "Error, missing property name!");
		getConfig().addDefault("lang.en.errorMissingPointName", "Error, missing point name!");
		getConfig().addDefault("lang.en.errorMissingPointListName", "Error, missing list name!");
		
		getConfig().addDefault("lang.en.errorValidArenaName", "Error, invalid arena name!");
		getConfig().addDefault("lang.en.errorValidArenaType", "Error, invalid arena type!");
		getConfig().addDefault("lang.en.errorValidPlayerName", "Error, invalid player name!");
		getConfig().addDefault("lang.en.errorValidAreaName", "Error, invalid area name!");
		getConfig().addDefault("lang.en.errorValidPropertyType", "Error, invalid property type!");
		
		getConfig().addDefault("lang.en.errorArenaExist", "Error, this arena doesn't exist!");
		getConfig().addDefault("lang.en.errorListExist", "Error, this list doesn't exist!");
		getConfig().addDefault("lang.en.errorPropertyExist", "Error, this property doesn't exist!");
		
		getConfig().addDefault("lang.en.errorAlreadyJoined", "Error, you are already in a arena!");
		getConfig().addDefault("lang.en.errorNotJoined", "Error, you are not in a arena!");
		getConfig().addDefault("lang.en.errorJoin", "Error, something went wrong!");
		getConfig().addDefault("lang.en.errorArenaReady", "Error, this arena isn't ready to join!");
		
		getConfig().addDefault("lang.en.errorNoCreator", "Error, you aren't editing a arena!");
		getConfig().addDefault("lang.en.errorAlreadyCreator", "Error, you are already editing a arena!");
		
		getConfig().addDefault("lang.en.errorWandMissingPosBoth", "Please set pos1 and pos2!");
		getConfig().addDefault("lang.en.errorWandMissingPos1", "Please set pos1!");
		getConfig().addDefault("lang.en.errorWandMissingPos2", "Please set pos2");
		
		getConfig().addDefault("lang.en.blockDrop", "You can't drop items when you are in the Arena.");
		getConfig().addDefault("lang.en.blockCommand", "You can't use command when you are ingame.");
		
		getConfig().options().copyDefaults(true); 
		saveConfig();
	}

	//Arena
	public void createArena(String name, ArenaType type, World world) {
		getArenaHandler().createArena(name, type, world);
	}
	public Arena getArena(String name) {
		return getArenaHandler().getArena(name);
	}
	public boolean getIsArena(String name) {
		return getArenaHandler().getIsArena(name);
	}
	
	//ArenaType
	public ArenaType getArenaType(String name) {
		return getArenaHandler().getArenaType(name);
	}
	public boolean getIsArenaType(String name) {
		return getArenaHandler().getIsArenaType(name);
	}
	
	//ArenaCreator
	public List<ArenaCreator> editPlayers = new ArrayList<ArenaCreator>();
	public void startEdit(Player player, Arena arena) {
		editPlayers.add(new ArenaCreator(this, player, arena));
	}
	public void stopEdit(ArenaCreator player) {
		getArenaHandler().saveArenas();
		player.getArena().checkStatus();
		editPlayers.remove(player);
	}
	public void stopEdit(String player) {
		for(ArenaCreator pa : editPlayers) {
			if(pa.getName().equals(player)) stopEdit(pa);
		}
	}
	public ArenaCreator getArenaCreator(Player player) {
		for(ArenaCreator pa : editPlayers) {
			if(pa.getName().equals(player.getName())) return pa;
		}
		return null;
	}
	public boolean isCreatingArena(Player player) {
		if(getArenaCreator(player) == null) return false;
		return true;
	}
	
	//ArenaPlayer
	public ArenaPlayer getArenaPlayer(String player) {
		for(Entry<String, Arena> enitiy : getArenaHandler().getAllArenas().entrySet()) {
			if(enitiy.getValue().getPlayer(player) != null) return enitiy.getValue().getPlayer(player);
		}
		return null;
	}
	public ArenaPlayer getArenaPlayer(Player player) {
		return getArenaPlayer(player.getName());
	}
	public boolean isInArena(String player) {
		if(getArenaPlayer(player) != null) return true;
		return false;
	}
	public boolean isInArena(Player player) {
		if(getArenaPlayer(player) != null) return true;
		return false;
	}
	
	//Kits
	public ArenaKit getKit(String name) {
		return getArenaKitsHandler().getKit(name);
	}
	public ArenaKit getKitByDisplayname(String name) {
		return getArenaKitsHandler().getKitByDisplayname(name);
	}
	public void addKit(ArenaKit kit) {
		getArenaKitsHandler().addKit(kit);
	}
	public HashMap<String, ArenaKit> getKits() {
		return getArenaKitsHandler().getKits();
	}
	
	//Permissions
	public static Permission permission = null;
	public static Economy economy = null;
	
	//Economy
	public Economy getEcon() {
		return economy;
	}
	
	public boolean checkHasPermission(Player player, String per) {
		return permission.has(player, per);
	}
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	//Messages/Formates
	public void sendMessage(Player to, String message)
	{
		to.sendMessage(formatText(message));
	}
	public void sendMessage(CommandSender to, String message)
	{
		to.sendMessage(formatText(message));
	}
	public String formatText(String input)
	{
		return ChatColor.translateAlternateColorCodes('&', "&7[&cArena&7]&6 " + input);
	}
	public void broadcast(String message)
	{
		getServer().broadcastMessage(formatText(message));
	}
}

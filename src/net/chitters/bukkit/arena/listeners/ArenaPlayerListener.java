package net.chitters.bukkit.arena.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;

import net.chitters.bukkit.arena.MainArena;
import net.chitters.bukkit.arena.events.ArenaPlayerDieEvent;
import net.chitters.bukkit.arena.events.ArenaPlayerKillEvent;
import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaArea;
import net.chitters.bukkit.arena.objects.ArenaKit;
import net.chitters.bukkit.arena.objects.ArenaPlayer;
import net.chitters.bukkit.arena.objects.ArenaSign;
import net.chitters.bukkit.arena.objects.ArenaStatus;
import net.chitters.bukkit.arena.objects.ArenaTeam;
import net.md_5.bungee.api.ChatColor;

public class ArenaPlayerListener implements Listener {
	final MainArena plugin;
	
	public ArenaPlayerListener(MainArena p) {
		plugin = p;
	}
	
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player != null) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player != null) {
			e.setCancelled(true);
			player.getArena().onBlockBreak(e);
		}
		
		if(e.getBlock().getState() instanceof Sign) {
			if(plugin.getSingsHandler().getIsArenaSign(e.getBlock().getLocation())) {
				if(!plugin.checkHasPermission(e.getPlayer(), "arena.sign.destroy")) {
					e.setCancelled(true);
				} else {
					plugin.getSingsHandler().removeArenaSign(e.getBlock().getLocation());
				}
			}
		}
	}	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer((Player) e.getEntity());
		if(player != null) {
			Arena arena = player.getArena();
			if(arena.getStatus().equals(ArenaStatus.INLOBBY)) {
				player.sendMessage("How did you do that?");
			}
			if(arena.getStatus().equals(ArenaStatus.INGAME)) {
				e.setDroppedExp(0);
				e.setKeepLevel(true);
				e.setDeathMessage("");
				e.getDrops().clear();
				arena.broadcast(arena.getDeathMessage(e.getEntity().getLastDamageCause().getCause()).replace("{0}", player.getPlayer().getDisplayName()));
				
				if(e.getEntity().getLastDamageCause().getEntityType().equals(EntityType.PLAYER) && e.getEntity().getKiller() instanceof Player) {
					ArenaPlayer attacker = plugin.getArenaPlayer((Player) e.getEntity().getKiller());
					if(attacker == null) {
						arena.onPlayerDieInGame(player, e.getEntity().getLastDamageCause().getCause());
					} else {
						player.deaths++;
						attacker.kills++;
						arena.onPlayerKilledInGame(player, attacker, attacker.getPlayer().getItemInHand());
						plugin.getServer().getPluginManager().callEvent(new ArenaPlayerKillEvent(player, attacker, player.getArena(), e));
					}
				} else {
					arena.onPlayerDieInGame(player, e.getEntity().getLastDamageCause().getCause());
					plugin.getServer().getPluginManager().callEvent(new ArenaPlayerDieEvent(player, player.getArena(), e));
				}
			}
		}
	}	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		final ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player != null) {
			Arena arena = player.getArena();
			if(arena.getStatus().equals(ArenaStatus.INLOBBY)) {
				if(player.getTeam().equals(ArenaTeam.NONE)) {
					e.setRespawnLocation(arena.getLobbySpawnPointForNone(player).getLocation());
				} else {
					e.setRespawnLocation(arena.getLobbySpawnPointForTeam(player.getTeam()).getLocation());
				}
			}
			if(arena.getStatus().equals(ArenaStatus.INGAME)) {
				e.setRespawnLocation(arena.getPlayerGameSpawnPoint(player).getLocation());
				arena.onPlayerRespawnGame(player);
			}
			player.respawned = true;
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player != null) {
			Arena arena = player.getArena();
			if(arena.getStatus().equals(ArenaStatus.INLOBBY) || arena.getStatus().equals(ArenaStatus.INGAME)) {
				ArenaArea area = arena.getCurrentArea();
				if(!area.checkLocIn(e.getPlayer().getLocation())) {
					Location n = player.getLastSavePosition();
					n.setYaw(n.getYaw() + 180);
					player.teleport(n);
				} else {
					player.setLastSavePosition(player.getPlayer().getLocation());
				}
			}
			if(arena.getStatus().equals(ArenaStatus.INGAME)) {
				player.checkEffects();
				if(player.respawned && arena.getProperty().getRuleInt("spawnProtection") > 0) {
					player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, arena.getProperty().getRuleInt("spawnProtection") * 20, 10));
					player.respawned = false;
				}
			}
			player.getPlayer().setFoodLevel(20);
			player.getArena().onPlayerMove(e);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player == null) return;
		player.kick();
	}
	@EventHandler
	public void onArenaPlayerInteract(PlayerInteractEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player == null) return;
		player.getArena().onPlayerInteract(e);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(e.getCause().equals(TeleportCause.COMMAND) || e.getCause().equals(TeleportCause.UNKNOWN) || e.getCause().equals(TeleportCause.NETHER_PORTAL) || e.getCause().equals(TeleportCause.END_PORTAL)) {
			ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
			if(player == null) return;
			e.setCancelled(true);
			player.sendMessage("Your teleport has been blocked!");
		}
	}
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player == null) return;
		
		String command = e.getMessage().replaceAll("/", "").split(" ")[0];
		for(String cmd : plugin.getConfig().getStringList("block.whitlistedCommands")) {
			if(command.equals(cmd)) return;
		}
		
		if(!plugin.checkHasPermission(player.getPlayer(), "arena.bypass.command")) {
			player.sendMessage(plugin.getMessageHandler().getKey("blockCommand"));
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		ArenaPlayer player = plugin.getArenaPlayer(e.getPlayer());
		if(player == null) return;
		player.sendMessage(plugin.getMessageHandler().getKey("blockDrop"));
		e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		if(!(e.getDamager() instanceof Player)) return;
		
		ArenaPlayer player = plugin.getArenaPlayer((Player) e.getEntity());
		if(player == null) return;
		
		ArenaPlayer damager = plugin.getArenaPlayer((Player) e.getDamager());
		if(damager == null) {
			plugin.sendMessage((Player) e.getDamager(), plugin.getMessageHandler().getKey("blockDamagingFromNonArenaPlayer"));
			e.setCancelled(true);
			return;
		}
		
		if(player.getTeam().equals(damager.getTeam()) && !player.getArena().getProperty().getRuleBoolean("allowTeamKilling")) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{	
		ItemStack usedItem = e.getCurrentItem();
		ArenaPlayer player = plugin.getArenaPlayer((Player) e.getWhoClicked());
		
		if(player == null) return;
		
		if(e.getClickedInventory() == null) return;
		if(!e.getClickedInventory().getName().equals(player.getArena().getKitInventory().getName())) return;
		e.setCancelled(true);
		
		ArenaKit sKit = plugin.getKitByDisplayname(usedItem.getItemMeta().getDisplayName());
		if(sKit == null) return;
		
		player.sendMessage(plugin.getMessageHandler().getKey("kitSelected", sKit.display + ChatColor.GOLD));
		player.setNextKit(sKit);
		player.getPlayer().closeInventory();
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(!(e.getBlock().getState() instanceof Sign)) return;
		
		Sign sign = (Sign) e.getBlock().getState();
		
		if(e.getLines().length < 3) return;
		if(e.getLine(0).equalsIgnoreCase("[arena]")) {
			if(!plugin.checkHasPermission(e.getPlayer(), "arena.sign.create")) {
				plugin.sendMessage(e.getPlayer(), plugin.getMessageHandler().getKey("errorMissingPermission"));
				return;
			}
			
			Arena arena = plugin.getArena(e.getLine(2));
			if(arena == null) {
				plugin.sendMessage(e.getPlayer(), plugin.getMessageHandler().getKey("errorArenaExist"));
				e.setCancelled(true);
				return;
			}
			
			plugin.sendMessage(e.getPlayer(), plugin.getMessageHandler().getKey("creatingSignDone"));
			plugin.getSingsHandler().registerNewArenaSign(sign, arena);
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(e.getClickedBlock().getState() instanceof Sign) {
			ArenaSign sign = plugin.getSingsHandler().getSign((Sign) e.getClickedBlock().getState());
			if(sign == null) return;
			sign.run(e.getPlayer());
			e.setCancelled(true);
		}
	}
}

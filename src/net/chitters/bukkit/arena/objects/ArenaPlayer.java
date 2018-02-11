package net.chitters.bukkit.arena.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class ArenaPlayer {
	private Player player;
	private Arena arena;
	private ArenaTeam team;
	
	private Location lastSavePostion;
	
	public ArenaPlayer(Player arg1, Arena arg2) {
		this.player = arg1;
		this.arena = arg2;
		
		nextKit = arena.getDefaultKit();
	}
	
	public Player getPlayer()
	{
		return player;
	}
	public Arena getArena()
	{
		return arena;
	}
	public ArenaTeam getTeam() {
		return team;
	}
	public ArenaSpawnPoint getSpawnPoint() {
		return getArena().getPlayerGameSpawnPoint(this);
	}
	
	public void setTeam(ArenaTeam n) {
		team = n;
	}
	
	public void sendMessage(String msg)
	{
		if(msg == "") return;
		arena.getPlugin().sendMessage(getPlayer(), msg);
	}
	public void teleport(ArenaSpawnPoint point) {
		point.teleport(getPlayer());
	}
	public void teleport(Location point) {
		getPlayer().teleport(point);
	}
	public void leave() {
		getArena().leave(player);
	}
	public void kick() {
		getArena().kick(this);
	}
	
	public boolean respawned = true;
	public int kills = 0;
	public int deaths = 0;
	public void reward() {
		if(arena.getPlugin().getConfig().getBoolean("reward.giveMoney")) {
			arena.getPlugin().getEcon().depositPlayer(getPlayer(), arena.getPlugin().getConfig().getDouble("reward.moneyPerWin"));
			arena.getPlugin().getEcon().depositPlayer(getPlayer(), arena.getPlugin().getConfig().getDouble("reward.moneyPerKill") * kills);
		}
	}
	
	ArenaKit currentKit;
	ArenaKit nextKit;
	public void giveKit() {
		if(currentKit == null && nextKit == null) return;
		if(!(nextKit == null)) {
			currentKit = nextKit;
			nextKit = null;
		}
		
		getPlayer().getInventory().clear();
		getPlayer().setHealth(20);
		getPlayer().setFoodLevel(20);
		for(PotionEffect effect : getPlayer().getActivePotionEffects()) {
			getPlayer().removePotionEffect(effect.getType());
		}
		
		//Armor
		getPlayer().getInventory().setHelmet(currentKit.helmet);
		getPlayer().getInventory().setChestplate(currentKit.chestplate);
		getPlayer().getInventory().setLeggings(currentKit.leggings);
		getPlayer().getInventory().setBoots(currentKit.boots);
		
		//Items
		for(ItemStack item : currentKit.items) {
			getPlayer().getInventory().addItem(item);
		}
		
		//Effects
		/*for(PotionEffect effect : currentKit.effects) {
			getPlayer().addPotionEffect(effect);
		}*/
	}
	public void checkEffects() {
		if(currentKit == null || currentKit.effects == null) return;
		for(PotionEffect effect : currentKit.effects) {
			if(!getPlayer().hasPotionEffect(effect.getType())) {
				getPlayer().addPotionEffect(effect);
			}
		}
	}
	public void openKitSelector() {
		Inventory inv = arena.getKitInventory();
		
		for(ArenaKit kit : arena.getKits()) {
			if(kit.getNeedsPermission() && !arena.getPlugin().checkHasPermission(getPlayer(), kit.getKitPermission())) continue;
			inv.addItem(kit.getResItemStack());
		}
		
		getPlayer().openInventory(inv);
	}
	
	public void setNextKit(ArenaKit k) {
		nextKit = k;
	}
	public void setKit(ArenaKit k) {
		currentKit = k;
	}
	public ArenaKit getKit() {
		return currentKit;
	}
	
	public Location getLastSavePosition() {
		return lastSavePostion;
	}
	public void setLastSavePosition(Location l) {
		lastSavePostion = l;
	}
}

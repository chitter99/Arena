package net.chitters.bukkit.arena.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class ArenaKit {
	public String name;
	public String display;
	
	public Material icon = Material.DIRT;
	public String description = "";
	
	public boolean needsPermission = false;
	
	public List<ItemStack> items = new ArrayList<ItemStack>();
	public List<PotionEffect> effects = new ArrayList<PotionEffect>();
	
	public ItemStack helmet = new ItemStack(Material.AIR);
	public ItemStack chestplate = new ItemStack(Material.AIR);
	public ItemStack leggings = new ItemStack(Material.AIR);
	public ItemStack boots = new ItemStack(Material.AIR);
	
	public void addItem(ItemStack item) {
		items.add(item);
	}
	public void addEffect(PotionEffect effect) {
		effects.add(effect);
	}
	
	public void giveKitTo(ArenaPlayer player) {
		player.setKit(this);
		player.giveKit();
	}
	
	public ItemStack getResItemStack() {
		ItemStack i = new ItemStack(icon);
		
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(getDisplay());
		
		List<String> lore = new ArrayList<String>();
		for(String l : description.split("/b")) {
			lore.add(ChatColor.GRAY + l);
		}
		
		meta.setLore(lore);
		i.setItemMeta(meta);
		
		return i;
	}
	public String getDisplay() {
		return ChatColor.translateAlternateColorCodes('&', display);
	}
	public boolean getNeedsPermission() {
		return needsPermission;
	}
	public String getKitPermission() {
		return "arena.kit." + name;
	}
}

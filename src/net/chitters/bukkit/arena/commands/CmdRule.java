package net.chitters.bukkit.arena.commands;

import org.bukkit.entity.Player;

import net.chitters.bukkit.arena.objects.Arena;
import net.chitters.bukkit.arena.objects.ArenaCreator;
import net.chitters.bukkit.arena.objects.ArenaProperty;
import net.chitters.bukkit.command.Cmd;
import net.chitters.bukkit.command.CommandHandler;

public class CmdRule extends Cmd {

	public CmdRule(CommandHandler h) {
		super(h);
		
		this.name = "rule";
	}
	
	@Override
	public void perform(Player player, String[] args) {
		if(!getPlugin().checkHasPermission(player, "arena.edit.rule")) {
			sendMessage(player, getMessage("errorMissingPermission"));
			return;
		}
		
		if(!getPlugin().isCreatingArena(player)) {
			sendMessage(player, getMessage("errorNoCreator"));
			return;
		}
		
		if(args.length <= 0) {
			sendMessage(player, getMessage("errorMissingPropertyName"));
			return;
		}
		
		String rule = args[0];
		ArenaCreator creator = getPlugin().getArenaCreator(player);
		Arena arena = creator.getArena();
		ArenaProperty property = arena.getProperty();
		
		if(!property.isRule(rule)) {
			sendMessage(player, getMessage("errorPropertyExist"));
			return;
		}
		
		if(args.length == 1) {
			sendMessage(player, getMessage("propertyDisplay", rule, property.getRule(rule).toString(), property.getRule(rule).getClass().getSimpleName()));
			return;
		}
		
		if(args[1].equalsIgnoreCase("set")) {
			String value = "";
			if(args.length > 2) {
				value = args[2].toString();
			}
			
			try {
				Object x = property.getRule(rule);
				if(x.getClass().equals(Integer.class)) {
					property.setRule(rule, Integer.parseInt(value));
				}
				if(x.getClass().equals(Float.class)) {
					property.setRule(rule, Float.parseFloat(value));
				}
				if(x.getClass().equals(Boolean.class)) {
					property.setRule(rule, Boolean.parseBoolean(value));
				}
				if(x.getClass().equals(String.class)) {
					property.setRule(rule, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendMessage(player, getMessage("errorValidPropertyType"));
				return;
			}
			
			sendMessage(player, "Done!");
			
		}
		
		if(args[1].equalsIgnoreCase("add")) {
			
			
			
		}
		
	}
}

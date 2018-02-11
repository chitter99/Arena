package net.chitters.bukkit.arena.objects;

public enum ArenaStatus {
	CLOSED("closed"),
	IDLE("idle"),
	INLOBBY("in lobby"),
	STOPING("stoping"),
	INGAME("in game");
	
	String name;
	
	private ArenaStatus(String arg1) {
		name = arg1;
	}
	
	public String getName() {
		return name;
	}
}

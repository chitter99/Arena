package net.chitters.bukkit.arena.objects;

public enum ArenaGameStatus {
	GAME_NULL("game_null"),
	GAME_READY_TO_JOIN("game_ready_to_join"),
	GAME_READY_TO_START("game_ready_to_start"),
	GAME_IN_PROGRESS("game_in_progress"),
	GAME_RESETING("game_reseting"),
	GAME_CLOSED("game_close");
	
	public String name;
	
	private ArenaGameStatus(String name)
	{
		this.name = name;
	}
	
	static public ArenaGameStatus getFromString(String ar)
	{
		for(ArenaGameStatus st : ArenaGameStatus.values()) {
			if(st.name.equalsIgnoreCase(ar)) return st;
		}
		return ArenaGameStatus.GAME_NULL;
	}
	
}

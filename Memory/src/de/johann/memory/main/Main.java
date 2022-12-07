package de.johann.memory.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import de.johann.memory.commands.MemoryCommand;
import de.johann.memory.game.Game;
import de.johann.memory.listeners.InventoryClickListener;
import de.johann.memory.listeners.InventoryCloseListener;


public class Main extends JavaPlugin {
	
	public static final String PREFIX_NORMAL = "§3§l| §r§bMemory§3» §7",
							   PREFIX_ERROR = "§4§l| §r§cMemory§4» §7";
	
	public final Sound SOUND_ERROR = Sound.BLOCK_NOTE_BLOCK_BASS,
							  SOUND_WINNER = Sound.ENTITY_PLAYER_LEVELUP,
							  SOUND_DRAW = Sound.ENTITY_VILLAGER_AMBIENT,
							  SOUND_CHALLANGE = Sound.ENTITY_VILLAGER_WORK_FLETCHER,
							  SOUND_GAME_OPEN = Sound.ENTITY_VILLAGER_WORK_MASON,
							  SOUND_LOSER = Sound.ENTITY_VILLAGER_HURT,
							  SOUND_LOSER_2 = Sound.ENTITY_VILLAGER_NO,
							  SOUND_LOSER_3 = Sound.ENTITY_VILLAGER_AMBIENT,
							  SOUND_FOUND_PAIR = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
	
	private HashMap<Integer, Game> gamesID;
	private Game game;
	
	@Override
	public void onEnable() {
		System.out.println("[Memory] wurde erfolgreich geladen");
		gamesID = new HashMap<>();
		
		getCommand("memory").setExecutor(new MemoryCommand(this));
		
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this), this);
		Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(this), this);
		
	}
	
	public void onDisable() {
		
		System.out.println("[Memory] wurde erfolgreich heruntergefahren");
	}
	
	public HashMap<Integer, Game> getGames() {
		return gamesID;
	}
	
}
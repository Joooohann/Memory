package de.johann.memory.game;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.johann.memory.main.Main;

public class Game {
	
	private Main plugin;
	private Player player;
	private GameInventory gameInventory;
	private int gameID;
	private int tries;
	
	private boolean isReturning;
	private boolean hasEnded;
	
	private ArrayList <Material> allItems = new ArrayList<>();
	private ArrayList <Material> items = new ArrayList<>();
	
	private ArrayList <Material> gameItems = new ArrayList<>();
	private ArrayList <Material> gameItemsSlots = new ArrayList<>();
	private ArrayList <Material> foundItems = new ArrayList<>();
	private ArrayList <Integer> uncoveredSlots = new ArrayList<>();
	
	public Game(Main plugin, Player player, int gameID) {
		this.plugin = plugin;
		this.player = player;
		this.gameID = gameID;
		tries = 0;
		this.gameInventory = new GameInventory(plugin, this, gameID);
		isReturning = false;
		hasEnded = false;
		
		// set item method
		gameItemsSlots = shuffleItems();
		
	}
	
	public ArrayList<Material> shuffleItems() {
		// get 12 game-items from this list
		allItems.add(Material.DIAMOND); allItems.add(Material.ENCHANTING_TABLE); allItems.add(Material.STONE_PICKAXE); allItems.add(Material.LANTERN);
		allItems.add(Material.CROSSBOW); allItems.add(Material.ENDER_EYE); allItems.add(Material.REDSTONE_TORCH); allItems.add(Material.STICKY_PISTON);
		allItems.add(Material.BEE_NEST); allItems.add(Material.RABBIT_FOOT); allItems.add(Material.PAINTING); allItems.add(Material.NOTE_BLOCK);
		allItems.add(Material.FIREWORK_ROCKET); allItems.add(Material.GOLDEN_HELMET); allItems.add(Material.NAUTILUS_SHELL); allItems.add(Material.BELL);
		allItems.add(Material.NAME_TAG); allItems.add(Material.CARROT); allItems.add(Material.SPYGLASS); allItems.add(Material.CAKE);
		allItems.add(Material.SADDLE); allItems.add(Material.COBWEB); allItems.add(Material.STONECUTTER); allItems.add(Material.KELP);
		
		Collections.shuffle(allItems);
		if(allItems.size() < 12) return null;
		for(int i = 0; i < 12; i++) {
			items.add(allItems.get(i));
		}
		
		for(int i = 0; i < items.size(); i++) {
			gameItems.add(items.get(i));
		}
		items.addAll(items);
		Collections.shuffle(items);
		
		return items;
	}
	
	public int calculateSlotToIndex(int slot) {
		int index = 0;
		
		if(slot >= 2 && slot <= 6) {
			index = slot - 2;
		}
		else if(slot >= 11 && slot <= 15) {
			index = slot - 6;
		}
		else if(slot >= 20 && slot <= 21) {
			index = slot - 10;
		}
		else if(slot >= 23 && slot <= 24) {
			index = slot - 11;
		}
		else if(slot >= 29 && slot <= 33) {
			index = slot - 15;
		}
		else if(slot >= 38 && slot <= 42) {
			index = slot - 19;
		}
		return index;
	}
	
	public int calculateIndexToSlot(int index) {
		int slot = 0;
		
		if(index >= 0 && index <= 4) {
			slot = index + 2;
		}
		else if(index >= 5 && index <= 9) {
			slot = index + 6;
		}
		else if(index >= 10 && index <= 11) {
			slot = index + 10;
		}
		else if(index >= 12 && index <= 13) {
			slot = index + 11;
		}
		else if(index >= 14 && index <= 18) {
			slot = index + 15;
		}
		else if(index >= 19 && index <= 23) {
			slot = index + 19;
		}
		return slot;
	}
	
	public void openInventory(Player player) {
		player.openInventory(gameInventory.getInventory());
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getGameID() {
		return gameID;
	}
	
	public GameInventory getGameInventory() {
		return gameInventory;
	}
	
	public ArrayList<Material> getGameItems() {
		return gameItems;
	}
	
	public ArrayList<Material> getGameItemsSlots() {
		return gameItemsSlots;
	}
	
	public ArrayList<Material> getItems() {
		return items;
	}
	
	public ArrayList<Material> getFoundItems() {
		return foundItems;
	}
	
	public ArrayList<Integer> getUncoveredSlots() {
		return uncoveredSlots;
	}
	
	public void addUncoveredSlots(int integer) {
		uncoveredSlots.add(integer);
	}
	
	public boolean isReturning() {
		return isReturning;
	}
	
	public void setReturning(boolean bool) {
		isReturning = bool;
	}
	
	public int getTries() {
		return tries;
	}
	
	public void setTries(int tries) {
		this.tries = tries;
	}
	
	public boolean getHasEnded() {
		return hasEnded;
	}
	
	public void setHasEnded(boolean bool) {
		hasEnded = bool;
	}
	
}

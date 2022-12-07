package de.johann.memory.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


import de.johann.memory.main.Main;
import de.johann.memory.util.ItemBuilder;

public class GameInventory {
	
	private Main plugin;
	private final Inventory inventory;
	private final Game game;
	private Player player;
	private final int gameID;
	private int ID;
	
	ItemStack gray = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7 ").build();
	ItemStack paperItem = new ItemBuilder(Material.PAPER).setDisplayName("§7Klicke zum Aufdecken").build();
	
	public GameInventory(Main plugin, Game game, int gameID) {
		this.plugin = plugin;
		this.game = game;
		this.gameID = gameID;
		this.inventory = Bukkit.createInventory(null, 9*5,"§8Memory Spielbrett-ID: " + gameID);
		setupInventory();
	}
	
	public void setupInventory() {
		for(int i = 0; i <= 44; i++) {
			if((i >= 2 && i <= 6) || (i >= 11 && i <= 15) || (i >= 20 && i <= 24) || (i >= 29 && i <= 33) || (i >= 38 && i <= 42))
				continue;
			inventory.setItem(i, gray);
		}
		inventory.setItem(22, gray);
		
		// create playerhead
        	ItemStack playerHead = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
		SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
		playerHeadMeta.setOwner(game.getPlayer().getName());
		playerHeadMeta.setDisplayName("§7Spieler: §b§l" + game.getPlayer().getName());
		List<String> loreList = new ArrayList<>();
		loreList.add("§7"); loreList.add("§7Finde aus den 24 Kärtchen"); loreList.add("§7durch Aufdecken jeweils ein"); loreList.add("§7Paar");
		playerHeadMeta.setLore(loreList);
		playerHead.setItemMeta(playerHeadMeta);
		
		// create gameitems
		ItemStack triesItem = new ItemBuilder(Material.CHEST).setDisplayName("§7Insgesamt aufgedeckte Karten: §b§l" + game.getTries()).setLore("§7","§7Du kannst beliebig viele","§7Kärtchen aufdecken. Umso weniger","§7du am Ende gebraucht hast, umso", "§7besser!").build();
		ItemStack foundPairsItem = new ItemBuilder(Material.PAPER).setDisplayName("§7Gefundene Paare: §b§l" + game.getFoundItems().size() + "/12").setLore("§7","§7Finde alle 12 Paare, um","§7das Spiel zu gewinnen!").build();
		ItemStack giveUpItem = new ItemBuilder(Material.TOTEM_OF_UNDYING).setDisplayName("§c§lAufgeben & Spiel auflösen").setLore("§7","§7Du verlierst automatisch das", "§7Spiel, erhälst aber die Spiel-","§7kärtchen-Anordnung!").build();
		
		// set items
		inventory.setItem(9, playerHead);
		inventory.setItem(17, foundPairsItem);
		inventory.setItem(27, triesItem);
		inventory.setItem(35, giveUpItem);
		
	}
	
	public void updateInventoryStatsItems() {
		// create gameitems
		ItemStack triesItem = new ItemBuilder(Material.CHEST).setDisplayName("§7Insgesamt aufgedeckte Karten: §b§l" + game.getTries()).setLore("§7","§7Du kannst beliebig viele","§7Kärtchen aufdecken. Umso weniger","§7du am Ende gebraucht hast, umso", "§7besser!").build();
		ItemStack foundPairsItem = new ItemBuilder(Material.PAPER).setDisplayName("§7Gefundene Paare: §b§l" + game.getFoundItems().size() + "/12").setLore("§7","§7Finde alle 12 Paare, um","§7das Spiel zu gewinnen!").build();
		ItemStack foundAllPairsItem = new ItemBuilder(Material.PAPER).setDisplayName("§7Gefundene Paare: §b§l§n" + game.getFoundItems().size() + "/12").setLore("§7","§7Finde alle 12 Paare, um","§7das Spiel zu gewinnen!").build();
		
		// set items
		if(game.getFoundItems().size() >= 12) {
			inventory.setItem(17, foundAllPairsItem);
		} else {
			inventory.setItem(17, foundPairsItem);
		}
		
		inventory.setItem(27, triesItem);
	}
	
	public void setItem(int slot, ItemStack itemStack) {
		inventory.setItem(slot, itemStack);
	}
	
	public void showItems() {
		for(int i = 0; i < game.getGameItemsSlots().size(); i++) {
			if(i >= 0 && i <= 4) {
				checkIfPaperItem(i, i+2);
			}
			else if(i >= 5 && i <= 9) {
				checkIfPaperItem(i, i+6);
			}
			else if(i >= 10 && i <= 11) {
				checkIfPaperItem(i, i+10);
			}
			else if(i >= 12 && i <= 13) {
				checkIfPaperItem(i, i+11);
			}
			else if(i >= 14 && i <= 18) {
				checkIfPaperItem(i, i+15);
			}
			else if(i >= 19 && i <= 23) {
				checkIfPaperItem(i, i+19);
			}

		}
	}
	
	// this method is actually not needed
	public void showItem(int slot, Material material) {
		for(int i = 0; i < game.getGameItemsSlots().size(); i++) {
			if(game.getGameItemsSlots().contains(game.getFoundItems())) {
				game.getGameInventory().setItem(game.calculateIndexToSlot(i), new ItemStack(game.getGameItemsSlots().get(i)));
			} else {
				game.getGameInventory().setItem(game.calculateIndexToSlot(i), paperItem);
			}
			
		}
		
	}
	
	public void showEndItems() {
		for(int i = 0; i < game.getGameItemsSlots().size(); i++) {
			
			if(game.getFoundItems().contains(game.getGameItemsSlots().get(i))) {
				
				ItemStack itemStack = new ItemStack(game.getGameItemsSlots().get(i));
				ItemMeta itemStackMeta = itemStack.getItemMeta();
				itemStackMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				itemStackMeta.setDisplayName("§aItem gefunden");
				itemStackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				itemStack.setItemMeta(itemStackMeta);
				
				game.getGameInventory().setItem(game.calculateIndexToSlot(i), itemStack);
				
			} else {
				setItem(game.calculateIndexToSlot(i), new ItemBuilder(game.getGameItemsSlots().get(i)).setDisplayName("§cItem nicht gefunden").build());
			}
			
		}
		ItemStack newGame = new ItemBuilder(Material.LIME_DYE).setDisplayName("§a§lNeues Spiel").setLore("§7","§7Starte ein neues Memory-","§7Spielbrett mit einer neuen","§7Anordnung").build();
		setItem(35, newGame);
	}
	
	public void checkIfPaperItem(int i, int slot) {
		if(game.getFoundItems().contains(game.getGameItemsSlots().get(i))) {
			inventory.setItem(slot, new ItemBuilder(game.getGameItemsSlots().get(i)).setDisplayName("§bBereits gefunden").build());
		} else {
			if(inventory.getItem(slot) != null) return;
			inventory.setItem(slot, paperItem);
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
}

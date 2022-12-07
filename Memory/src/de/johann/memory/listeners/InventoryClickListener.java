package de.johann.memory.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.johann.memory.game.Game;
import de.johann.memory.main.Main;
import de.johann.memory.util.ItemBuilder;

public class InventoryClickListener implements Listener {
	
	private Main plugin;
	private Player currentPlayer;
	
	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleInventoryClick(InventoryClickEvent event) {
		String inventoryTitle = event.getView().getTitle();
		Player player = (Player) event.getWhoClicked();
		
		if(inventoryTitle == null) return;
		if(!inventoryTitle.contains("Memory Spielbrett-ID")) return;
		if(event.getClickedInventory() == null) return;
		if(event.getClickedInventory().getType() == InventoryType.PLAYER) return;
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;
		event.setCancelled(true);
		
		// get game by inventory name
		int gameID0 = Integer.parseInt(inventoryTitle.replaceAll("[\\D]", ""));
	    int gameID = Integer.parseInt(Integer.toString(gameID0).substring(1));
		
		Game game = plugin.getGames().get(gameID);
		
		if(game.getPlayer() != player) {
			player.sendMessage(Main.PREFIX_ERROR + "Du kannst auf dieses Spielbrett nicht zugreifen");
			player.playSound(player.getLocation(), plugin.SOUND_ERROR, 2f, 1f);
			if(event.getCurrentItem().getType() == Material.LIME_DYE) {
				int gameAmount = plugin.getGames().size();
				Game newGame = new Game(plugin, player, gameAmount);
				plugin.getGames().put(gameAmount, newGame);
				player.playSound(player.getLocation(), plugin.SOUND_GAME_OPEN, 2f, 1f);
				
				newGame.openInventory(player);
				new BukkitRunnable() {
		        	public void run() {
		        		newGame.getGameInventory().showItems();
		            }
		        }.runTaskLater(plugin, 20);
			}
			return;
		}
		
		int slot = event.getSlot();
		ItemStack itemStack = event.getCurrentItem();
		
		if(slot != 9 && slot != 17 && slot != 27 && slot != 35) {
			// game item clicked
			
			if(itemStack.getType() == Material.PAPER) {
				if(game.getUncoveredSlots().size() < 2 && !game.isReturning()) {
					
					game.getUncoveredSlots().add(slot);
					game.setTries(game.getTries() + 1);
					game.getGameInventory().updateInventoryStatsItems();
					
					// set the item
					event.getInventory().setItem(slot, new ItemStack(game.getGameItemsSlots().get(game.calculateSlotToIndex(slot))));
					event.getInventory().setItem(slot, new ItemBuilder(game.getGameItemsSlots().get(game.calculateSlotToIndex(slot))).setDisplayName("§7" + game.getGameItemsSlots().get(game.calculateSlotToIndex(slot)).toString()).build());
					
					// pair was found
					if(game.getUncoveredSlots().size() >= 2) {
						
						if(game.getGameItemsSlots().get(game.calculateSlotToIndex(game.getUncoveredSlots().get(0))) == game.getGameItemsSlots().get(game.calculateSlotToIndex(game.getUncoveredSlots().get(1)))) {
							game.setReturning(false);
							player.sendMessage(Main.PREFIX_NORMAL + "Super, du hast ein Paar gefunden!");
							player.playSound(player.getLocation(), plugin.SOUND_FOUND_PAIR, 2f, 1.2f);
							
							// update inventory items
							game.getFoundItems().add(game.getGameItemsSlots().get(game.calculateSlotToIndex(slot)));
							game.getGameInventory().updateInventoryStatsItems();
							
							// game over
							if(game.getFoundItems().size() == 12) {
								// set end & show items
								player.sendMessage(Main.PREFIX_NORMAL + "Du hast das Memory gelöst!");
								player.playSound(player.getLocation(), plugin.SOUND_WINNER, 2f, 1f);
								game.setHasEnded(true);
								new BukkitRunnable() {
						        	public void run() {
						        		game.getGameInventory().showEndItems();
						            }
						        }.runTaskLater(plugin, 30);
								
								return;
							}
							
							// set the found to air
							new BukkitRunnable() {
					        	public void run() {
					        		event.getInventory().setItem(game.getUncoveredSlots().get(0), new ItemStack(Material.AIR));
					        		event.getInventory().setItem(game.getUncoveredSlots().get(1), new ItemStack(Material.AIR));
					        		game.getUncoveredSlots().clear();
					            }
					        }.runTaskLater(plugin, 10);
							
						} else {
							game.setReturning(true);
						}
					}
					
				} else {
					// must turn over the cards before a new turn
					player.sendMessage(Main.PREFIX_ERROR + "Du musst die Kärtchen nun wieder mit einem Klick umdrehen");
					player.playSound(player.getLocation(), plugin.SOUND_ERROR, 2f, 1f);
				}
				
			} else {
				if(game.isReturning()) {
					if(game.getFoundItems().contains(event.getCurrentItem().getType())) return;
					if(game.getUncoveredSlots().size() == 2) {
						if(!game.getUncoveredSlots().contains(slot)) return;
						game.getUncoveredSlots().remove(game.getUncoveredSlots().indexOf(slot));
						event.getInventory().setItem(slot, new ItemBuilder(Material.PAPER).setDisplayName("§7Klicke zum Aufdecken").build());
						
					} else if(game.getUncoveredSlots().size() == 1) {
						if(!game.getUncoveredSlots().contains(slot)) return;
						game.getUncoveredSlots().remove(game.getUncoveredSlots().indexOf(slot));
						game.setReturning(false);
						event.getInventory().setItem(slot, new ItemBuilder(Material.PAPER).setDisplayName("§7Klicke zum Aufdecken").build());
					}
				} else {
					
				}
			}
			
			
			
		} else {
			// one of the other four game-"setting"-items was clicked
			if(itemStack.getType() == Material.TOTEM_OF_UNDYING) {
				if(!game.getHasEnded()) {
					game.setHasEnded(true);
					game.getGameInventory().showEndItems();
					player.playSound(player.getLocation(), plugin.SOUND_LOSER, 2f, 1.2f);
					player.sendMessage(Main.PREFIX_NORMAL + "Spiel aufgegeben & beendet");
				} else {
					player.sendMessage(Main.PREFIX_ERROR + "Das Spiel ist bereits beendet");
					player.playSound(player.getLocation(), plugin.SOUND_ERROR, 2f, 1f);
				}
				
			} else if(itemStack.getType() == Material.LIME_DYE) {
				if(!game.getHasEnded()) return;
				
				int gameAmount = plugin.getGames().size();
				Game newGame = new Game(plugin, player, gameAmount);
				plugin.getGames().put(gameAmount, newGame);
				player.playSound(player.getLocation(), plugin.SOUND_GAME_OPEN, 2f, 1f);
				
				newGame.openInventory(player);
				new BukkitRunnable() {
		        	public void run() {
		        		newGame.getGameInventory().showItems();
		            }
		        }.runTaskLater(plugin, 20);
			}
		}
	}
	
}
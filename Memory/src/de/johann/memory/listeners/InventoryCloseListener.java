package de.johann.memory.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import de.johann.memory.main.Main;
import de.johann.memory.game.Game;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class InventoryCloseListener implements Listener {
	
	private Main plugin;
	public InventoryCloseListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String inventoryTitle = event.getView().getTitle();
		
		try {
			
			// removes first number from string
			int gameID0 = Integer.parseInt(inventoryTitle.replaceAll("[\\D]", ""));
		    int gameID = Integer.parseInt(Integer.toString(gameID0).substring(1));
		    
		    
		    Game game = plugin.getGames().get(gameID);
		    if(game == null) return;
		    
		    if(game.getHasEnded()) {
		    	return;
		    }
		    
		    // send textcomponent
		    TextComponent clickable = new TextComponent(Main.PREFIX_NORMAL + "Klicke hier, um das Spielbrett erneut zu öffnen");
			clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/memory open " + gameID));
			clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Öffne das Spielbrett erneut")));
			player.spigot().sendMessage(clickable);
			
		} catch(NumberFormatException exception) {
			
		}
	}
	
}

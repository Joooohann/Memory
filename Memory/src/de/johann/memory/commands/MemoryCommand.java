package de.johann.memory.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.johann.memory.game.Game;
import de.johann.memory.main.Main;

public class MemoryCommand implements CommandExecutor {
	
	private Main plugin;
	
	public MemoryCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("game")) {
				int gameAmount = plugin.getGames().size();
				
				// create new game & open the inventory
				Game newGame = new Game(plugin, player, gameAmount);
				plugin.getGames().put(gameAmount, newGame);
				
				newGame.openInventory(player);
				player.playSound(player.getLocation(), plugin.SOUND_GAME_OPEN, 2f, 1f);
				
				// possible actionbar message
				// String string = "§aEine Actionbar-Nachricht";
				// player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(string));
				
				// show the items delayed
				new BukkitRunnable() {
		        	public void run() {
		        		newGame.getGameInventory().showItems();
		            }
		        }.runTaskLater(plugin, 20);
			}
			else if(args[0].equalsIgnoreCase("ids")) {
				// make all IDs a string
				String ids = "";
				ArrayList<String> idList = new ArrayList<>();
				
				for (Integer key : plugin.getGames().keySet()) {
					idList.add("" + key);
				}
				ids = String.join(", ", idList);
				
				if(idList.size() != 0) {
					player.sendMessage(Main.PREFIX_NORMAL + "Spielbrett-IDs: " + ids);
				} else {
					player.sendMessage(Main.PREFIX_NORMAL + "Spielbrett-IDs: Es wurden noch keine Spiele gespielt");
				}
				
			} 
			else
				sendPlayerUsage(player);
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("open")) {
				if(args[1].length() > 1000) return false;
				
				int gameID = Integer.parseInt(args[1]);
				if(gameID > 1000) return false;
				
				if(gameID < plugin.getGames().size()) {
					Game game = plugin.getGames().get(gameID);
					game.openInventory(player);
					
				} else {
					player.sendMessage(Main.PREFIX_NORMAL + "Dieses Spielbrett existiert nicht");
					player.playSound(player.getLocation(), plugin.SOUND_ERROR, 2f, 1f);
				}
				
			}
			else 
				sendPlayerUsage(player);
		}
		else 
			sendPlayerUsage(player);
		
		return false;
	}
	
	public void sendPlayerUsage(Player player) {
		player.sendMessage(Main.PREFIX_NORMAL + "Benutzung:");
		player.sendMessage(Main.PREFIX_NORMAL + "/memory <game>");
		player.sendMessage(Main.PREFIX_NORMAL + "/memory <ids>");
		player.sendMessage(Main.PREFIX_NORMAL + "/memory <open> <SpielID>");
	}
}
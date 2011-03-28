package retsrif.Melt;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Melt extends JavaPlugin {

	@Override
	public void onDisable() {
		System.out.println("[Melt] disabled.");
	}

	@Override
	public void onEnable() {
		System.out.println("[Melt] enabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			String commandName = cmd.getName().toLowerCase();
			
			if(commandName.equalsIgnoreCase("melt")) {
				if(args.length == 1) {
					String numString = args[0];
					
					try {
						int radius = Integer.parseInt(numString);
						
						
						Block center = player.getWorld().getBlockAt(player.getLocation());
						
						final ArrayList<Block> blockList = new ArrayList<Block>();
						
						//Thanks to SpaceManiac for this!
						for (int deltaX = -radius; deltaX <= radius; ++deltaX) {
							for (int deltaY = -radius; deltaY <= radius; ++deltaY) {
								for (int deltaZ = -radius; deltaZ <= radius; ++deltaZ) {
									// This loops over all blocks within a CUBE of radius 5.
									Block block = player.getWorld().getBlockAt(center.getX() + deltaX, center.getY() + deltaY, center.getZ() + deltaZ);
									blockList.add(block);
									}
								}
							}
						
						for(int i=0; i<blockList.size(); i++) {
							Block bm = blockList.get(i);
							if(bm.getType() == Material.ICE) {
								bm.setType(Material.WATER);
								blockList.remove(i);
							}
						}
						
						player.sendMessage(ChatColor.GREEN + "All ice blocks in a radius of " + ChatColor.GREEN + radius + " are melted!");
						
					}
					catch (NumberFormatException nfe) {
						nfe.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}

}

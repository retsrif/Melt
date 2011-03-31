package retsrif.Melt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Melt extends JavaPlugin {
	
	public static PermissionHandler Permissions;
	private int limit = 30;

	@Override
	public void onDisable() {
		System.out.println("[Melt] disabled.");
	}

	@Override
	public void onEnable() {
		System.out.println("[Melt] enabled.");
		setupPermissions();
		
		File f = new File(getDataFolder(), "limit.txt");		// should be /plugins/Melt/limit.txt
		if (!f.exists()) {
			System.out.println("[Melt] Could not find limit.txt - radius limit defaulted to 30");
			return;
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			try { limit = Integer.parseInt(in.readLine()); }
			catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				System.out.println("[Melt] Hint: seems like your limit.txt is not a number");
				return;
			}
			System.out.println("[Melt] Radius limit set to "+limit);
		} catch (Exception e) {
			System.out.println("[Melt] Could not read limit.txt - radius limit defaulted to 30");
			limit = 30;
			return;
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			String commandName = cmd.getName().toLowerCase();
			
			if(commandName.equalsIgnoreCase("melt")) {
				if (!(Permissions == null || Permissions.has(player, "melt.use"))) {
					player.sendMessage(ChatColor.DARK_GREEN + "You can't use " + ChatColor.GREEN + "/melt" + ChatColor.DARK_GREEN + "!");
				}
				if(args.length == 1) {
					String numString = args[0];
					
					try {
						int radius = Integer.parseInt(numString);
						
						if (radius > limit) {
							if (!Permissions.has(player, "melt.ignorelimit")) {
								player.sendMessage(ChatColor.DARK_GREEN + "The radius must equal or be less than " + ChatColor.GREEN + limit + ChatColor.DARK_GREEN + " blocks!");
								return true;
							}
						}
						
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
						
						for(int j=0; j<=1; j++) {		//run this 2 times so it removes all ice
							for(int i=0; i<blockList.size(); i++) {
								Block bm = blockList.get(i);
								if(bm.getType() == Material.ICE) {
									bm.setType(Material.WATER);
									blockList.remove(i);
								}
							}
						}
						
						player.sendMessage(ChatColor.DARK_GREEN + "All ice blocks in a radius of " + ChatColor.GREEN + radius + ChatColor.DARK_GREEN + " are melted!");
						
					}
					catch (NumberFormatException nfe) {
						nfe.printStackTrace();
						return true;
					}
				}
			}
		}
		return false;
	}
	
    private void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (test != null) {
                Permissions = ((Permissions)test).getHandler();
            } else {
            	System.out.println("[Melt] Permissions plugin not detected. Everyone can use Melt, but can't ignore the limit.");
            }
        }
    }

}

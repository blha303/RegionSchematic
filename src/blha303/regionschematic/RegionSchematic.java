// Copyright © 2013 The RegionSchematic Team (Steven Smith (blha303))
// This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.
// Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:
// 1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.
// 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
// 3. This notice may not be removed or altered from any source distribution.

package blha303.regionschematic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
//import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMe;

public class RegionSchematic extends JavaPlugin {
	Logger log = this.getLogger();
	WorldEditPlugin worldedit;
	Towny towny;
	TownyUniverse tuniv;
	PlotMe plotme;
	GriefPrevention gp;
//	WorldGuardPlugin worldguard;
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		
		// WorldEdit setup -- Without this, the plugin won't start
		Plugin wep = pm.getPlugin("WorldEdit");
		if(wep == null || !(wep instanceof WorldEditPlugin) || !wep.isEnabled()) {
			severe("Cannot find WorldEdit. Plugin disabled.");
			pm.disablePlugin(this);
		} else {
			worldedit = (WorldEditPlugin)wep;
		}
		// WorldGuard setup -- Unneeded, due to WGBukkit
/*		Plugin wgp = pm.getPlugin("WorldGuard");
		if(wgp == null || !(wgp instanceof WorldGuardPlugin) || !wgp.isEnabled()) {
			severe("Cannot find WorldGuard.");
			worldguard = null;
		} else {
			worldguard = (WorldGuardPlugin)wgp;
		} */
		
		// Towny setup
		Plugin townyp = pm.getPlugin("Towny");
		if(townyp == null || !(townyp instanceof Towny) || !townyp.isEnabled()) {
			severe("Cannot find Towny.");
			towny = null;
		} else {
			towny = (Towny)townyp;
		}
		
		// Plotme setup
		Plugin plotmep = pm.getPlugin("PlotMe");
		if(plotmep == null || !(plotmep instanceof PlotMe) || !plotmep.isEnabled()) {
			severe("Cannot find PlotMe");
			plotme = null;
		} else {
			plotme = (PlotMe)plotmep;
		}
		
		// GriefPrevention setup
		Plugin griefp = pm.getPlugin("GriefPrevention");
		if(griefp == null || !(griefp instanceof GriefPrevention) || !griefp.isEnabled()) {
			severe("Cannot find GriefPrevention");
			gp = null;
		} else {
			gp = (GriefPrevention)gp;
		}
	}
	
	public void onDisable() {
		info("Shutting down.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String plugin = "";
		Player player = null;
		Location selectionmin;
		Location selectionmax;
		if (args.length >= 1 && sender.hasPermission("regionschematic.export")) {
			plugin = args[0];
		} else {
			return false;
		}
		if (args.length >= 2) {
			if (sender.hasPermission("regionschematic.export.other")) {
				player = getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(String.format("[%s] %s is not a valid player name", getDescription().getName(), args[1]));
				}
			} else {
				sender.sendMessage(String.format("[%s] You don't have permission to select a player other than yourself.", getDescription().getName()));
			}
		} else {
			if (sender instanceof Player) {
				player = (Player)sender;
			} else {
				player = null;
			}
		}
		if (plugin.equalsIgnoreCase("worldguard")) {
			
		} else if (plugin.equalsIgnoreCase("plotme")) {
			if (player == null) {
				sender.sendMessage(String.format("[%s] Specify a player.", getDescription().getName()));
			}
			if (PlotManager.isPlotWorld(player)) {
				HashMap<String, Plot> plots = PlotManager.getPlots(player);
				List<Plot> plotsownedbyplayer = new ArrayList<Plot>();
				Plot targetplot;
				boolean foundplot = false;
				for(Plot plot : plots.values()) {
					if (plot.owner == player.getName()) {
						plotsownedbyplayer.add(plot);
					}
				}
				for (Plot plot : plotsownedbyplayer) {
					if (PlotManager.getPlayersInPlot(player.getWorld(), plot.id).contains(player)) {
						targetplot = plot;
						foundplot = true;
					}
				}
				if (foundplot) {
					// TODO: Get plot location data
				}
			}
		} else if (plugin.equalsIgnoreCase("towny")) {
			// TODO: Towny region code
		} else if (plugin.equalsIgnoreCase("griefprevention")) {
			// TODO: GriefPrevention region code
		} else {
			sender.sendMessage(String.format("[%s] Possible plugins are: WorldGuard, PlotMe, Towny, GriefPrevention", getDescription().getName()));
			return false;
		}
		
		return false;
	}
	
	private void info(String msg) {
		log.info(msg);
	}
	
	private void warning(String msg) {
		log.warning(msg);
	}
	
	private void severe(String msg) {
		log.severe(msg);
	}
}

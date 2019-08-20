package com.Sagacious_.ChatcolorGUI.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.ChatcolorGUI.Core;

public class SelectionHandler {
	
	private File dir;
	
	private HashMap<UUID, ChatColor> selected = new HashMap<UUID, ChatColor>();
	
	public SelectionHandler() {
		dir = new File(Core.getInstance().getDataFolder(), "data");
		if(!dir.exists()) {
			dir.mkdir();
		}else {
			for(File f : dir.listFiles()) {
				selected.put(UUID.fromString(f.getName().replaceAll(".yml", "")), ChatColor.valueOf(YamlConfiguration.loadConfiguration(f).getString("color")));
			}
		}
	}
	
	public void saveAll() {
		for(Entry<UUID, ChatColor> s : selected.entrySet()) {
			File f = new File(dir, s.getKey().toString() + ".yml");
			if(!f.exists()) {
				try {
					PrintWriter pw = new PrintWriter(new FileWriter(f));
					pw.println("color: '" + s.getValue().name() + "'");
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
				conf.set("color", s.getValue().name());
				try {
					conf.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		selected.clear();
	}
	
	public ChatColor getSelected(Player p) {
		if(selected.containsKey(p.getUniqueId())) {
		   return selected.get(p.getUniqueId());
		}
		return null;
	}
	
	public void setSelected(Player p, ChatColor c) {
		if(c == null && selected.containsKey(p.getUniqueId())) {
			selected.remove(p.getUniqueId());
		}else {
			selected.put(p.getUniqueId(), c);
		}
	}

}

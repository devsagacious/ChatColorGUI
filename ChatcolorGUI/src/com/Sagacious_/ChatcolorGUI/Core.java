package com.Sagacious_.ChatcolorGUI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.Sagacious_.ChatcolorGUI.handler.ChatHandler;
import com.Sagacious_.ChatcolorGUI.handler.CommandHandler;
import com.Sagacious_.ChatcolorGUI.handler.GUIHandler;
import com.Sagacious_.ChatcolorGUI.handler.SelectionHandler;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public SelectionHandler sh;
	public GUIHandler gui;
	
	@Override
	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);saveDefaultConfig();
		sh = new SelectionHandler();
		new CommandHandler();
		Bukkit.getPluginManager().registerEvents(new ChatHandler(), Core.getInstance());
		gui = new GUIHandler();
		Bukkit.getPluginManager().registerEvents(gui, Core.getInstance());
	}

	@Override
	public void onDisable() {
		sh.saveAll();
	}
}

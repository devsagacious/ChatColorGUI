package com.Sagacious_.ChatcolorGUI.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.ChatcolorGUI.Core;

public class CommandHandler implements CommandExecutor{
	
	public CommandHandler() {
		Core.getInstance().getCommand("chatcolor").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			Core.getInstance().gui.open(p);
			return true;
		}
		return false;
	}

}

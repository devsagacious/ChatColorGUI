package com.Sagacious_.ChatcolorGUI.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.Sagacious_.ChatcolorGUI.Core;

public class ChatHandler implements Listener{

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(Core.getInstance().sh.getSelected(p)!=null) {
			e.setMessage(Core.getInstance().sh.getSelected(p) + e.getMessage());
		}
	}
}

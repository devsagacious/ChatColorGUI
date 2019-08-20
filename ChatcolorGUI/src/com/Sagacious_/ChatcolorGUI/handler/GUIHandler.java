package com.Sagacious_.ChatcolorGUI.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Sagacious_.ChatcolorGUI.Core;


public class GUIHandler implements Listener{
	
	private List<ChatColor> colors = new ArrayList<ChatColor>(Arrays.asList(ChatColor.values()));
	private List<ChatColor> blacklist = new ArrayList<ChatColor>();
	private Material is;
	private short is_short = -1;
	
	private String inventory;
	private String format;
	
	private String buy_display;
	private List<String> buy_lore;
	
	private String selected;
	private String deselected;
	
	private Sound to_play;
	private Sound to_play_buy;
	
	private String disp_append;;
	
	public GUIHandler() {
		for(String s : Core.getInstance().getConfig().getStringList("blacklist")) {
			ChatColor c = ChatColor.valueOf(s);
			if(c!=null) {
				blacklist.add(c);
			}
		}
		is = Material.valueOf(Core.getInstance().getConfig().getString("buy-itemstack"));
		if(Core.getInstance().getConfig().getInt("buy-itemstack-short")>0) {
			is_short = Short.valueOf((short)Core.getInstance().getConfig().getInt("buy-itemstack-short"));
		}
		inventory = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("inventory-title"));
		format = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("format"));
		buy_display = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("buy-display"));
	    buy_lore = Core.getInstance().getConfig().getStringList("buy-lore");
	    selected = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("selected"));
		deselected = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("deselected"));
		to_play = Sound.valueOf(Core.getInstance().getConfig().getString("inventory-sound"));
		to_play_buy = Sound.valueOf(Core.getInstance().getConfig().getString("inventory-buy-sound"));
		disp_append = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("selected-append-displayname"));
	}
	
	public void open(Player p) {
		List<ChatColor> player_colors = new ArrayList<ChatColor>();
		for(ChatColor c : colors) {
			if(p.hasPermission("chatcolorgui." + c.name())) {
				if(!blacklist.contains(c)) {
				player_colors.add(c);
				}
			}
		}
		Inventory inv = Bukkit.createInventory(null, size(player_colors.size()), inventory.replaceAll("%owned%", ""+player_colors.size()));
		int i = 0;
		for(ChatColor c : player_colors) {
			ItemStack stack = new ItemStack(Material.WOOL, 1, Short.valueOf(getDataValue(c)));
			ItemMeta im = stack.getItemMeta();
			im.setDisplayName(format.replaceAll("%color%", "§" + c.getChar()).replaceAll("%color_name%", StringUtils.capitalize(c.name().toLowerCase().replaceAll("_", " "))));
			if(Core.getInstance().sh.getSelected(p)!=null&&Core.getInstance().sh.getSelected(p).equals(c)) {
				im.setDisplayName(im.getDisplayName()+" "+disp_append);
			}
			stack.setItemMeta(im);
			inv.setItem(i, stack);
			i+=1;
		}
		if(player_colors.size()!=colors.size()-blacklist.size()) {
			ItemStack stack = new ItemStack(is, 1, (is_short==(short)-1?(short)0:Short.valueOf(is_short)));
		    ItemMeta im = stack.getItemMeta();
		    im.setDisplayName(buy_display);
		    List<String> lore = new ArrayList<String>();
		    for(String l : buy_lore) {
		    	lore.add(ChatColor.translateAlternateColorCodes('&', l));
		    }
		    im.setLore(lore);
		    stack.setItemMeta(im);
		    inv.setItem(i, stack);
		}
		p.openInventory(inv);
	}

	private int size(int owned) {
		if(owned<9) {
			return 9;
		}
		if(owned<18) {
			return 18;
		}
		return 27;
	}
	
	private short getDataValue(ChatColor color) {
		if(color.equals(ChatColor.AQUA)) {
			return 3;
		}
		if(color.equals(ChatColor.BLACK)) {
			return 15;
		}
		if(color.equals(ChatColor.BLUE) || color.equals(ChatColor.DARK_BLUE)) {
			return 11;
		}
		if(color.equals(ChatColor.DARK_AQUA)) {
			return 9;
		}
		if(color.equals(ChatColor.DARK_GRAY)) {
			return 7;
		}
		if(color.equals(ChatColor.DARK_GREEN)) {
			return 13;
		}
		if(color.equals(ChatColor.DARK_PURPLE)) {
			return 10;
		}
		if(color.equals(ChatColor.DARK_RED) || color.equals(ChatColor.RED)) {
			return 14;
		}
		if(color.equals(ChatColor.GOLD) || color.equals(ChatColor.YELLOW)) {
			return 4;
		}
		if(color.equals(ChatColor.GRAY)) {
			return 8;
		}
		if(color.equals(ChatColor.GREEN)) {
			return 5;
		}
		if(color.equals(ChatColor.LIGHT_PURPLE)) {
			return 6;
		}
		return 0;
	}
	
	@EventHandler
	public void onUse(InventoryClickEvent e) {
		if(e.getWhoClicked()instanceof Player) {
			List<ChatColor> player_colors = new ArrayList<ChatColor>();
			for(ChatColor c : colors) {
				if(((Player)e.getWhoClicked()).hasPermission("chatcolorgui." + c.name())) {
					if(!blacklist.contains(c)) {
					player_colors.add(c);
					}
				}
			}
		if(e.getInventory().getTitle().equals(inventory.replaceAll("%owned%", ""+player_colors.size()))) {
			if(e.getCurrentItem()!=null&&!e.getCurrentItem().getType().equals(Material.AIR)) {
				e.setCancelled(true);
				Player p = (Player)e.getWhoClicked();
				if(e.getCurrentItem().getType().equals(Material.WOOL)) {
					ChatColor c = player_colors.get(e.getSlot());
					if(to_play != null) {
						p.playSound(p.getLocation(), to_play, 0.3F, 0.3F);
					}
					p.closeInventory();
					if(Core.getInstance().sh.getSelected(p)!=null&&Core.getInstance().sh.getSelected(p).equals(c)) {
						p.sendMessage(deselected.replaceAll("%color%", "§" + c.getChar()).replaceAll("%color_name%", StringUtils.capitalize(c.name().toLowerCase().replaceAll("_", " "))));
						Core.getInstance().sh.setSelected(p, null);
					}else {
						p.sendMessage(selected.replaceAll("%color%", "§" + c.getChar()).replaceAll("%color_name%", StringUtils.capitalize(c.name().toLowerCase().replaceAll("_", " "))));
						Core.getInstance().sh.setSelected(p, c);
					}
				}
				else if(e.getCurrentItem().getType().equals(is)) {
					if(to_play_buy!=null) {
						p.playSound(p.getLocation(), to_play_buy, 0.3F, 0.3F);
					}
					p.closeInventory();
				}
			}
			}
		}
	}
}

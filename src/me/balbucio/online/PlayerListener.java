package me.balbucio.online;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PostLoginEvent evt) {
		if(Main.getInstance().db.getInt("max") < BungeeCord.getInstance().getOnlineCount()) {
			Main.getInstance().db.set("max", BungeeCord.getInstance().getOnlineCount());
		}
		for(ProxiedPlayer player : Main.getInstance().toggle) {
			player.sendMessage(Main.getInstance().messages.getString("togglemessage").replace("&", "§").replace("{player}", evt.getPlayer().getName()).replace("{count}", String.valueOf(BungeeCord.getInstance().getOnlineCount())));
		}
	}
}

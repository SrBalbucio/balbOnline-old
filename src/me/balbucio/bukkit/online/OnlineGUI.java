package me.balbucio.bukkit.online;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.balbucio.bukkit.online.Spigot;

public class OnlineGUI implements Listener {

	private Inventory inv;

	public OnlineGUI(int count, int maxi, YamlConfiguration config) {
		inv = Bukkit.createInventory(null, 9, (String) config.get("guititle"));
		ItemStack online = new ItemStack(Material.COMPASS);
		ItemMeta meta = online.getItemMeta();
		meta.setDisplayName(config.getString("gui.onlineitem.title") != null ? config.getString("gui.onlineitem.title")
				: "§bInformações");
		meta.setLore(Arrays.asList(config.getString("gui.onlineitem.lore").replace("{count}", String.valueOf(count))
				.replace("{this}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size())).split("\n")));
		online.setItemMeta(meta);
		inv.setItem(2, online);
		ItemStack max = new ItemStack(Material.DIAMOND);
		int diferenca = maxi - count;
		meta.setDisplayName(config.getString("gui.maxitem.title"));
		meta.setLore(Arrays.asList(config.getString("gui.maxitem.lore").replace("{count}", String.valueOf(maxi))
				.replace("{diferenca}", String.valueOf(diferenca)).split("\n")));
		max.setItemMeta(meta);
		inv.setItem(6, max);
	}

	public void open(Player player) {
		player.openInventory(inv);
	}

	private void destroy() {
		this.inv = null;
	}
}

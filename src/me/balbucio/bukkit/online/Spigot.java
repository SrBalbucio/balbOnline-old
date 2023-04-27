package me.balbucio.bukkit.online;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class Spigot extends JavaPlugin implements PluginMessageListener, Listener {

	private File folder = new File("plugins/balbOnline");
	private File msgs = new File(folder, "messages.yml");

	public YamlConfiguration messages;
	public String[] playerList;

	private static Spigot instance;
	public static List<Inventory> onlineInventorys = new ArrayList<>();

	public void onEnable() {
		checkIfBungee();
		loadConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getMessenger().registerIncomingPluginChannel(this, "bonline:channel", this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getLogger().info(ChatColor.GREEN + "Ativado com sucesso!");
		getLogger().info("§2Very nice!");
		startScheduler();
	}

	private void loadConfig() {
		try {
			if (!folder.exists()) {
				folder.mkdir();
			}
			if (!msgs.exists()) {
				Files.copy(this.getResource("messages.yml"), msgs.toPath());
			}
			messages = YamlConfiguration.loadConfiguration(msgs);
		} catch (Exception e) {
			e.printStackTrace();
			this.getServer().getConsoleSender().sendMessage("§c[balbOnline] §aNão foi possível carregar os arquivos!");
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent evt) {
		if (evt.getView().getTitle().equals(messages.getString("guititle"))) {
			evt.setCancelled(true);
		}
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase("OpenOnlineGui")) {
			String data = in.readUTF();
			int data2 = in.readInt();
			int data3 = in.readInt();
			new OnlineGUI(data2, data3, messages).open(player);
		} else if (subChannel.equals("PlayerList")) {
			String server = in.readUTF();
			playerList = in.readUTF().split(", ");
		}
	}

	private void checkIfBungee() {
		if (!getServer().spigot().getConfig().getBoolean("settings.bungeecord")) {
			getLogger().severe("Oh nao! Este servidor nao é BungeeCord!");
			getLogger().severe("Para o plugin funcionar ele depende de um servidor BungeeCord!");
			getLogger().severe("Caso ja o tenha, nao se esqueca de ativa-lo no spigot.yml.");
			getLogger().severe(ChatColor.RED + "§cPlugin desativado! Motivo > BungeeCord nao ativo.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public static Spigot getInstance() {
		return instance;
	}

	public static void setInstance(Spigot instance) {
		Spigot.instance = instance;
	}

	private void startScheduler() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				loadConfig();
			}
		}, 0, 20 * 30);
	}
}

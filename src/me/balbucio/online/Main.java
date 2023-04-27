package me.balbucio.online;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.nio.file.*;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	
	private static Main instance;
	public List<ProxiedPlayer> toggle = new ArrayList<>();
	// new Recoded
	private File folder = new File("plugins/balbOnline");
	public File config = new File("plugins/balbOnline", "config.yml");
	public File msgs = new File("plugins/balbOnline", "messages.yml");
	public File database = new File("plugins/balbOnline", "database.yml");
	public Configuration configuration, messages, db;
	
	@Override
	public void onEnable() {
		setInstance(this);
		loadConfig();
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Online());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new PlayerListener());
		BungeeCord.getInstance().getConsole()
				.sendMessage(new TextComponent("[BalbucioOnline] §eConectando ao Spigot..."));
		getProxy().registerChannel("bonline:channel");
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("[BalbucioOnline] §2Ativado com sucesso!"));
		BungeeCord.getInstance().getConsole()
				.sendMessage(new TextComponent("[BalbucioOnline] §cEm caso de problemas me contate via Discord!"));
	}
	

	public void open(ProxiedPlayer player, String menu) {
		if (BungeeCord.getInstance().getOnlineCount() > 0 && configuration.getBoolean("spigot.use")) {

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("OpenOnlineGui");
			out.writeUTF(menu);
			out.writeInt(BungeeCord.getInstance().getOnlineCount());
			out.writeInt(db.getInt("max"));

			player.getServer().getInfo().sendData("bonline:channel", out.toByteArray());
		}
	}

	public static Main getInstance() {
		return instance;
	}

	private static void setInstance(final Main instance) {
		Main.instance = instance;
	}

	public void loadConfig() {
		try {
			if (!folder.exists()) {
				folder.mkdir();
			}

			if (!config.exists()) {
				Files.copy(this.getResourceAsStream("config.yml"), config.toPath());
			}
			if (!msgs.exists()) {
				Files.copy(this.getResourceAsStream("messages.yml"), msgs.toPath());
			}
			if (!database.exists()) {
				Files.copy(this.getResourceAsStream("database.yml"), database.toPath());
			}
			configuration = YamlConfiguration.getProvider(YamlConfiguration.class).load(config);
			messages = YamlConfiguration.getProvider(YamlConfiguration.class).load(msgs);
			db = YamlConfiguration.getProvider(YamlConfiguration.class).load(database);
		} catch (Exception e) {
			e.printStackTrace();
			BungeeCord.getInstance().getConsole()
					.sendMessage("§c[balbOnline] §aNão foi possível carregar os arquivos!");
		}
	}
}

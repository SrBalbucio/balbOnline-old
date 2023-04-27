package me.balbucio.online;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Online extends Command {

	public Online() {
		super("online", "bonline.use", "bonline");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		String prefix = Main.getInstance().messages.getString("prefix");
		if (!sender.hasPermission("bonline.use")) {
			if (Main.getInstance().configuration.getBoolean("nopermissionmessage"))
				sender.sendMessage(Main.getInstance().messages.getString("nopermission").replace("&", "§")
						.replace("{prefix}", prefix));
			return;
		}

		int count = BungeeCord.getInstance().getOnlineCount();
		if (args.length == 0) {
			if (Main.getInstance().configuration.getBoolean("spigot.use")) {
				Main.getInstance().open((ProxiedPlayer) sender, "Geral");
				return;
			}
			sender.sendMessage(Main.getInstance().messages.getString("defaultmessage").replace("&", "§")
					.replace("{prefix}", prefix).replace("{count}", String.valueOf(count)));
			return;
		}

		String arg = args[0];
		if (arg.equalsIgnoreCase("list") || arg.equalsIgnoreCase("listar")) {
			TextComponent message = new TextComponent(Main.getInstance().messages.getString("listar").replace("&", "§")
					.replace("{prefix}", prefix).replace("{players}", ""));

			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
				TextComponent text = new TextComponent(player.getName());
				String server = player.getServer().getInfo().getName();
				text.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
						new ComponentBuilder(Main.getInstance().messages.getString("playerhover").replace("&", "§")
								.replace("{server}", server).replace("{ping}", String.valueOf(player.getPing()))
								.replace("{uniqueid}", player.getUUID())).create()));
				message.addExtra(text);
			}
			sender.sendMessage(message);
		} else if (arg.equalsIgnoreCase("toggle")) {
			if (Main.getInstance().toggle.contains((ProxiedPlayer) sender)) {
				Main.getInstance().toggle.remove((ProxiedPlayer) sender);
				sender.sendMessage(Main.getInstance().messages.getString("toggledesativado").replace("&", "§")
						.replace("{prefix}", prefix));
			} else {
				Main.getInstance().toggle.add((ProxiedPlayer) sender);
				sender.sendMessage(Main.getInstance().messages.getString("toggleativado").replace("&", "§")
						.replace("{prefix}", prefix));
			}
		} else if (arg.equalsIgnoreCase("this") || arg.equalsIgnoreCase("server")) {
			sender.sendMessage(Main.getInstance().messages.getString("servermessage").replace("&", "§")
					.replace("{prefix}", prefix)
					.replace("{server}", ((ProxiedPlayer) sender).getServer().getInfo().getName()).replace("{count}",
							String.valueOf(((ProxiedPlayer) sender).getServer().getInfo().getPlayers().size())));
		} else if (arg.equalsIgnoreCase("thislist") || arg.equalsIgnoreCase("serverlist")) {
			TextComponent message = new TextComponent(Main.getInstance().messages.getString("serverlistar")
					.replace("&", "§").replace("{prefix}", prefix).replace("{players}", "")
					.replace("{server}", ((ProxiedPlayer) sender).getServer().getInfo().getName()));

			for (ProxiedPlayer player : ((ProxiedPlayer) sender).getServer().getInfo().getPlayers()) {
				TextComponent text = new TextComponent(" " + player.getName());
				String server = player.getServer().getInfo().getName();
				text.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
						new ComponentBuilder(Main.getInstance().messages.getString("playerhover").replace("&", "§")
								.replace("{server}", server).replace("{ping}", String.valueOf(player.getPing()))
								.replace("{uniqueid}", player.getUUID())).create()));
				message.addExtra(text);
			}
			sender.sendMessage(message);
		} else if (arg.equalsIgnoreCase("top") || arg.equalsIgnoreCase("max")) {
			sender.sendMessage(Main.getInstance().messages.getString("maxplayers").replace("&", "§").replace("{count}",
					String.valueOf(Main.getInstance().db.getInt("max"))).replace("{prefix}", prefix));
		} else if (arg.equalsIgnoreCase("reload")) {
			Main.getInstance().loadConfig();
			sender.sendMessage("§aPlugin recarregado!");
		} else if (arg.equalsIgnoreCase("help") || arg.equalsIgnoreCase("ajuda")) {
			sender.sendMessage("§a§lbalbOnline §7- Comandos:");
			sender.sendMessage("§7Criado por Sr_Balbucio! - v2");
			sender.sendMessage("§b/online §7- Mostra a quantidade de players online.");
			sender.sendMessage("§b/online list §7- Mostra os players online.");
			sender.sendMessage("§b/online toggle §7- Ativa/desativa o aviso de entrada.");
			sender.sendMessage("§b/online server §7- Mostra a quantidade de players naquele servidor.");
			sender.sendMessage("§b/online serverlist §7- Mostra os players naquele servidor.");
			sender.sendMessage("§b/online reload §7- Recarrega os arquivos.");
			sender.sendMessage("§b/online <server> §7- Mostra a quantidade de players do servidor.");
		} else {
			ServerInfo info = BungeeCord.getInstance().getServerInfo(arg);
			if (info != null) {
				sender.sendMessage(Main.getInstance().messages.getString("serverdefault").replace("{prefix}", prefix)
						.replace("&", "§").replace("{server}", info.getName())
						.replace("{count}", String.valueOf(info.getPlayers().size())));
				return;
			}
			sender.sendMessage(
					Main.getInstance().messages.getString("notfound").replace("&", "§").replace("{prefix}", prefix));
		}
	}

}

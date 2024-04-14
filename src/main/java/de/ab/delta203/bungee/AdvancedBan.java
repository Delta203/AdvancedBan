package de.ab.delta203.bungee;

import de.ab.delta203.bungee.commands.*;
import de.ab.delta203.bungee.files.FileManager;
import de.ab.delta203.bungee.listeners.Disconnect;
import de.ab.delta203.bungee.listeners.Login;
import de.ab.delta203.bungee.listeners.Switch;
import de.ab.delta203.bungee.modules.ban.Ban;
import de.ab.delta203.bungee.modules.chatlog.ChatLog;
import de.ab.delta203.bungee.modules.check.Check;
import de.ab.delta203.bungee.modules.mute.Mute;
import de.ab.delta203.bungee.modules.query.Query;
import de.ab.delta203.bungee.modules.report.Report;
import de.ab.delta203.bungee.mysql.MySQlManager;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.Arrays;

public class AdvancedBan extends Plugin {

  public static AdvancedBan plugin;
  public static String prefix = "";
  public static Configuration config;
  public static Configuration chatFilter;
  public static Configuration messages;
  public static MySQlManager mysql;

  private PlayerInfoHandler playerInfoHandler;

  @Override
  public void onEnable() {
    plugin = this;
    initConfigs();
    initMySQl();

    playerInfoHandler = new PlayerInfoHandler(mysql.connection);
    playerInfoHandler.resetServers();
    playerInfoHandler.resetLoginKeys();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Disconnect(mysql.connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Login(mysql.connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Switch(mysql.connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(this, new MainCommand("advancedban"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(this, new ABPanelCommand("abpanel"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(this, new ToggleBanCommand("togglebannotify"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(this, new ToggleMuteCommand("togglemutenotify"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(this, new ToggleReportCommand("togglereportnotify"));

    new Ban(this, mysql.connection).registerModule();
    new ChatLog(this, mysql.connection).registerModule();
    new Check(this).registerModule();
    new Mute(this, mysql.connection).registerModule();
    new Query(mysql.connection).registerModule();
    new Report(this, mysql.connection).registerModule();

    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("loaded.plugin")));
  }

  @Override
  public void onDisable() {
    playerInfoHandler.resetServers();
    playerInfoHandler.resetLoginKeys();
  }

  private void initConfigs() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
    FileManager chatFilterYml = new FileManager("chatfilter.yml");
    chatFilterYml.create();
    chatFilterYml.load();
    chatFilter = chatFilterYml.get();
    String language = config.getString("language");
    for (String l : Arrays.asList("EN", "DE")) {
      FileManager messagesYml = new FileManager("messages/messages_" + l + ".yml");
      messagesYml.create();
      messagesYml.load();
      if (language.equals(l)) messages = messagesYml.get();
    }
    prefix = messages.getString("prefix");
    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("loaded.files")));
  }

  private void initMySQl() {
    String url = config.getString("mysql.url");
    int port = config.getInt("mysql.port");
    String database = config.getString("mysql.database");
    String user = config.getString("mysql.user");
    String password = config.getString("mysql.password");
    mysql = new MySQlManager(url, port, database, user, password);
    mysql.connect();
    mysql.createTable();
  }
}

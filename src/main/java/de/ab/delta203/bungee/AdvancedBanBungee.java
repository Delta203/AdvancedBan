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
import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.ServerType;
import de.ab.delta203.core.mysql.MySQlManager;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class AdvancedBanBungee extends Plugin {

  public static AdvancedBanBungee plugin;

  private PlayerInfoHandler playerInfoHandler;

  @Override
  public void onEnable() {
    plugin = this;
    AdvancedBan.serverType = ServerType.BUNGEECORD;
    initConfigs();
    if (!initMySQl()) {
      ProxyServer.getInstance()
          .getConsole()
          .sendMessage(
              new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.invalid")));
      ProxyServer.getInstance()
          .getConsole()
          .sendMessage(
              new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.invalid")));
      return;
    }

    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    playerInfoHandler.resetServers();
    playerInfoHandler.resetLoginKeys();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Disconnect(AdvancedBan.mysql.connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Login(AdvancedBan.mysql.connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, new Switch(AdvancedBan.mysql.connection));
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

    new Ban(this, AdvancedBan.mysql.connection).registerModule();
    new ChatLog(this, AdvancedBan.mysql.connection).registerModule();
    new Check(this).registerModule();
    new Mute(this, AdvancedBan.mysql.connection).registerModule();
    new Query(AdvancedBan.mysql.connection).registerModule();
    new Report(this, AdvancedBan.mysql.connection).registerModule();

    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("loaded.plugin"))
                        .replace("%type%", AdvancedBan.serverType.toString())));
  }

  @Override
  public void onDisable() {
    playerInfoHandler.resetServers();
    playerInfoHandler.resetLoginKeys();
  }

  private void initConfigs() {
    AdvancedBan.messages = new HashMap<>();
    AdvancedBan.config = new HashMap<>();
    AdvancedBan.chatFilter = new HashMap<>();

    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    Configuration config = configYml.get();
    ArrayList<String> configKeys = new ArrayList<>();
    getConfigKeysRecursive("", config, configKeys);
    for (String key : configKeys) {
      AdvancedBan.config.put(key, config.get(key));
    }

    FileManager chatFilterYml = new FileManager("chatfilter.yml");
    chatFilterYml.create();
    chatFilterYml.load();
    Configuration chatFilter = chatFilterYml.get();
    ArrayList<String> chatFilterKeys = new ArrayList<>();
    getConfigKeysRecursive("", chatFilter, chatFilterKeys);
    for (String key : chatFilterKeys) {
      AdvancedBan.chatFilter.put(key, chatFilter.get(key));
    }

    String language = (String) AdvancedBan.config.get("language");
    for (String l : Arrays.asList("EN", "DE")) {
      FileManager messagesYml = new FileManager("messages/messages_" + l + ".yml");
      messagesYml.create();
      messagesYml.load();
      if (language.equals(l)) {
        Configuration messages = messagesYml.get();
        ArrayList<String> messagesKeys = new ArrayList<>();
        getConfigKeysRecursive("", messages, messagesKeys);
        for (String key : messagesKeys) {
          AdvancedBan.messages.put(key, messages.get(key));
        }
      }
    }
    AdvancedBan.prefix = (String) AdvancedBan.messages.get("prefix");

    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.files")));
  }

  private boolean initMySQl() {
    String host = (String) AdvancedBan.config.get("mysql.host");
    int port = (int) AdvancedBan.config.get("mysql.port");
    String database = (String) AdvancedBan.config.get("mysql.database");
    String user = (String) AdvancedBan.config.get("mysql.user");
    String password = (String) AdvancedBan.config.get("mysql.password");
    AdvancedBan.mysql = new MySQlManager(host, port, database, user, password);
    if (AdvancedBan.mysql.connect()) {
      ProxyServer.getInstance()
          .getConsole()
          .sendMessage(
              new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.connected")));
      if (AdvancedBan.mysql.createTable())
        ProxyServer.getInstance()
            .getConsole()
            .sendMessage(
                new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.tables")));
      return true;
    }
    return false;
  }

  private void getConfigKeysRecursive(String prefix, Configuration config, ArrayList<String> keys) {
    for (String key : config.getKeys()) {
      Object value = config.get(key);
      if (value instanceof Configuration subConfig) {
        getConfigKeysRecursive(prefix + key + ".", subConfig, keys);
      } else {
        keys.add(prefix + key);
      }
    }
  }
}

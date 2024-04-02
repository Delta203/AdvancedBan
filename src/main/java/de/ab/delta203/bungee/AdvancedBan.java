package de.ab.delta203.bungee;

import de.ab.delta203.bungee.files.FileManager;
import de.ab.delta203.bungee.mysql.MySQlManager;
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

  @Override
  public void onEnable() {
    plugin = this;
    initConfigs();
    initMySQl();
    initCommandQuery();

    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("loaded.plugin")));
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

  private void initCommandQuery() {
    if (!config.getBoolean("query.enabled")) return;
    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("loaded.query")));
  }
}

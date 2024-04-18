package de.ab.delta203.spigot;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.ServerType;
import de.ab.delta203.core.mysql.MySQlManager;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import de.ab.delta203.spigot.commands.*;
import de.ab.delta203.spigot.files.FileManager;
import de.ab.delta203.spigot.listeners.Disconnect;
import de.ab.delta203.spigot.listeners.Join;
import de.ab.delta203.spigot.listeners.Login;
import de.ab.delta203.spigot.listeners.Teleport;
import de.ab.delta203.spigot.modules.ban.Ban;
import de.ab.delta203.spigot.modules.chatlog.ChatLog;
import de.ab.delta203.spigot.modules.check.Check;
import de.ab.delta203.spigot.modules.mute.Mute;
import de.ab.delta203.spigot.modules.query.Query;
import de.ab.delta203.spigot.modules.report.Report;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedBanSpigot extends JavaPlugin {

  public static AdvancedBanSpigot plugin;

  private PlayerInfoHandler playerInfoHandler;

  @Override
  public void onEnable() {
    plugin = this;
    AdvancedBan.serverType = ServerType.SPIGOT;
    initConfigs();
    if (!initMySQl()) {
      Bukkit.getConsoleSender()
          .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.invalid"));
      Bukkit.getConsoleSender()
          .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.invalid"));
      return;
    }

    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    playerInfoHandler.resetServers();
    playerInfoHandler.resetLoginKeys();

    Bukkit.getPluginManager().registerEvents(new Disconnect(AdvancedBan.mysql.connection), this);
    Bukkit.getPluginManager().registerEvents(new Join(AdvancedBan.mysql.connection), this);
    Bukkit.getPluginManager().registerEvents(new Login(AdvancedBan.mysql.connection), this);
    Bukkit.getPluginManager().registerEvents(new Teleport(AdvancedBan.mysql.connection), this);
    Objects.requireNonNull(getCommand("advancedban")).setExecutor(new MainCommand());
    Objects.requireNonNull(getCommand("abpanel")).setExecutor(new ABPanelCommand());
    Objects.requireNonNull(getCommand("togglebannotify")).setExecutor(new ToggleBanCommand());
    Objects.requireNonNull(getCommand("togglemutenotify")).setExecutor(new ToggleMuteCommand());
    Objects.requireNonNull(getCommand("togglereportnotify")).setExecutor(new ToggleReportCommand());

    new Ban(this, AdvancedBan.mysql.connection).registerModule();
    new ChatLog(this, AdvancedBan.mysql.connection).registerModule();
    new Check(this).registerModule();
    new Mute(this, AdvancedBan.mysql.connection).registerModule();
    new Query(AdvancedBan.mysql.connection).registerModule();
    new Report(this, AdvancedBan.mysql.connection).registerModule();

    Bukkit.getConsoleSender()
        .sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("loaded.plugin"))
                    .replace("%type%", AdvancedBan.serverType.toString()));
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

    Bukkit.getConsoleSender()
        .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.files"));
  }

  private boolean initMySQl() {
    String host = (String) AdvancedBan.config.get("mysql.host");
    int port = (int) AdvancedBan.config.get("mysql.port");
    String database = (String) AdvancedBan.config.get("mysql.database");
    String user = (String) AdvancedBan.config.get("mysql.user");
    String password = (String) AdvancedBan.config.get("mysql.password");
    AdvancedBan.mysql = new MySQlManager(host, port, database, user, password);
    if (AdvancedBan.mysql.connect()) {
      Bukkit.getConsoleSender()
          .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.connected"));
      if (AdvancedBan.mysql.createTable())
        Bukkit.getConsoleSender()
            .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mysql.tables"));
      return true;
    }
    return false;
  }

  private void getConfigKeysRecursive(String prefix, Configuration config, ArrayList<String> keys) {
    for (String key : config.getKeys(true)) {
      Object value = config.get(key);
      if (value instanceof Configuration subConfig) {
        getConfigKeysRecursive(prefix + key + ".", subConfig, keys);
      } else {
        keys.add(prefix + key);
      }
    }
  }
}

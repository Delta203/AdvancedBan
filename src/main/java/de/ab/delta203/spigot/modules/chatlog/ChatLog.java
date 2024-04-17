package de.ab.delta203.spigot.modules.chatlog;

import de.ab.delta203.spigot.modules.chatlog.listeners.Chat;
import java.sql.Connection;

import de.ab.delta203.spigot.modules.chatlog.listeners.Disconnect;
import de.ab.delta203.spigot.modules.chatlog.listeners.Join;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public record ChatLog(Plugin plugin, Connection connection) {

  private void initListeners() {
    Bukkit.getPluginManager().registerEvents(new Chat(connection), plugin);
    Bukkit.getPluginManager().registerEvents(new Disconnect(connection), plugin);
    Bukkit.getPluginManager().registerEvents(new Join(connection), plugin);
  }

  public void registerModule() {
    initListeners();
  }
}

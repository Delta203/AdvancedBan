package de.ab.delta203.bungee.modules.chatlog;

import de.ab.delta203.bungee.modules.chatlog.listeners.Chat;
import de.ab.delta203.bungee.modules.chatlog.listeners.Disconnect;
import de.ab.delta203.bungee.modules.chatlog.listeners.Switch;
import java.sql.Connection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public record ChatLog(Plugin plugin, Connection connection) {

  private void initListeners() {
    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Chat(connection));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(plugin, new Disconnect(connection));
    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Switch(connection));
  }

  public void registerModule() {
    initListeners();
  }
}

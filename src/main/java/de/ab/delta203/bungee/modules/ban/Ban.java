package de.ab.delta203.bungee.modules.ban;

import java.sql.Connection;

import de.ab.delta203.bungee.modules.ban.commands.BanCommand;
import de.ab.delta203.bungee.modules.ban.commands.TempBanCommand;
import de.ab.delta203.bungee.modules.ban.commands.UnBanCommand;
import de.ab.delta203.bungee.modules.ban.listeners.Login;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public record Ban(Plugin plugin, Connection connection) {

  private void initListeners() {
    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Login(connection));
  }

  private void initCommands() {
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new BanCommand("ban"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new TempBanCommand("tempban"));
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new UnBanCommand("unban"));
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

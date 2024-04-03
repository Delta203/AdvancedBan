package de.ab.delta203.bungee.modules.report;

import de.ab.delta203.bungee.modules.report.commands.Commands;
import de.ab.delta203.bungee.modules.report.listeners.Switch;
import java.sql.Connection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public record Report(Plugin plugin, Connection connection) {

  private void initListeners() {
    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Switch(connection));
  }

  private void initCommands() {
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new Commands("report"));
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

package de.ab.delta203.bungee.modules.mute;

import de.ab.delta203.bungee.modules.mute.commands.MuteCommand;
import de.ab.delta203.bungee.modules.mute.commands.TempMuteCommand;
import de.ab.delta203.bungee.modules.mute.commands.UnMuteCommand;
import de.ab.delta203.bungee.modules.mute.listeners.Chat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.Connection;

public record Mute(Plugin plugin, Connection connection) {

  private void initListeners() {
    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new Chat(connection));
  }

  private void initCommands() {
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new MuteCommand("mute"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new TempMuteCommand("tempmute"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new UnMuteCommand("unmute"));
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

package de.ab.delta203.bungee.modules.check;

import de.ab.delta203.bungee.modules.check.commands.CheckCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public record Check(Plugin plugin) {

  private void initCommands() {
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CheckCommand("check"));
  }

  public void registerModule() {
    initCommands();
  }
}

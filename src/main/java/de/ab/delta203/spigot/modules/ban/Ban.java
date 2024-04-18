package de.ab.delta203.spigot.modules.ban;

import de.ab.delta203.spigot.modules.ban.commands.BanCommand;
import de.ab.delta203.spigot.modules.ban.commands.TempBanCommand;
import de.ab.delta203.spigot.modules.ban.commands.UnBanCommand;
import de.ab.delta203.spigot.modules.ban.listeners.Login;
import java.sql.Connection;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public record Ban(Plugin plugin, Connection connection) {

  private void initListeners() {
    Bukkit.getPluginManager().registerEvents(new Login(connection), plugin);
  }

  private void initCommands() {
    Objects.requireNonNull(plugin.getServer().getPluginCommand("ban"))
        .setExecutor(new BanCommand());
    Objects.requireNonNull(plugin.getServer().getPluginCommand("tempban"))
        .setExecutor(new TempBanCommand());
    Objects.requireNonNull(plugin.getServer().getPluginCommand("unban"))
        .setExecutor(new UnBanCommand());
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

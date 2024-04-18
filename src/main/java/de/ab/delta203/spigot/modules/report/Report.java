package de.ab.delta203.spigot.modules.report;

import de.ab.delta203.spigot.modules.report.commands.ReportCommand;
import de.ab.delta203.spigot.modules.report.listeners.Chat;
import de.ab.delta203.spigot.modules.report.listeners.Join;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.Objects;

public record Report(Plugin plugin, Connection connection) {

  private void initListeners() {
    Bukkit.getPluginManager().registerEvents(new Chat(connection), plugin);
    Bukkit.getPluginManager().registerEvents(new Join(connection), plugin);
  }

  private void initCommands() {
    Objects.requireNonNull(plugin.getServer().getPluginCommand("report"))
        .setExecutor(new ReportCommand());
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

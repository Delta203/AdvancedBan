package de.ab.delta203.spigot.modules.mute;

import de.ab.delta203.spigot.modules.mute.commands.MuteCommand;
import de.ab.delta203.spigot.modules.mute.commands.TempMuteCommand;
import de.ab.delta203.spigot.modules.mute.commands.UnMuteCommand;
import de.ab.delta203.spigot.modules.mute.listeners.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.Objects;

public record Mute(Plugin plugin, Connection connection) {

  private void initListeners() {
    Bukkit.getPluginManager().registerEvents(new Chat(connection), plugin);
  }

  private void initCommands() {
    Objects.requireNonNull(plugin.getServer().getPluginCommand("mute"))
        .setExecutor(new MuteCommand());
    Objects.requireNonNull(plugin.getServer().getPluginCommand("tempmute"))
        .setExecutor(new TempMuteCommand());
    Objects.requireNonNull(plugin.getServer().getPluginCommand("unmute"))
        .setExecutor(new UnMuteCommand());
  }

  public void registerModule() {
    initListeners();
    initCommands();
  }
}

package de.ab.delta203.spigot.modules.check;

import de.ab.delta203.spigot.modules.check.commands.CheckCommand;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public record Check(Plugin plugin) {

  private void initCommands() {
    Objects.requireNonNull(plugin.getServer().getPluginCommand("check"))
        .setExecutor(new CheckCommand());
  }

  public void registerModule() {
    initCommands();
  }
}

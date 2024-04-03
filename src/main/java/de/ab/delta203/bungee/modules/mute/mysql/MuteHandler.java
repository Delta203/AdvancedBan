package de.ab.delta203.bungee.modules.mute.mysql;

import de.ab.delta203.bungee.AdvancedBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MuteHandler {

  private final Connection connection;

  public MuteHandler(Connection connection) {
    this.connection = connection;
  }

  public void mute(String uuid, String from, long end, String reason) {
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_Mutes (PlayerUUID, FromUUID, End, Reason) VALUES (?,?,?,?)");
      ps.setString(1, uuid);
      ps.setString(2, from);
      ps.setLong(3, end);
      ps.setString(4, reason);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void unmute(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("DELETE FROM AB_Mutes WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

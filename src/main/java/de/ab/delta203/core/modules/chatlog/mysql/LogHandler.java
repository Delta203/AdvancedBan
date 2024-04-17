package de.ab.delta203.core.modules.chatlog.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogHandler {

  private final Connection connection;

  public LogHandler(Connection connection) {
    this.connection = connection;
  }

  public boolean isMuted(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT FromUUID FROM AB_Mutes WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void record(String uuid, String server, String message) {
    long millis = System.currentTimeMillis();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_Chat (CurrentMillis, PlayerUUID, Server, Message) VALUES (?,?,?,?)");
      ps.setLong(1, millis);
      ps.setString(2, uuid);
      ps.setString(3, server);
      ps.setString(4, message);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

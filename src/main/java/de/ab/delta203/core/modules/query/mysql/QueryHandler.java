package de.ab.delta203.core.modules.query.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryHandler {

  private final Connection connection;

  public QueryHandler(Connection connection) {
    this.connection = connection;
  }

  public ArrayList<String> getCommands() {
    ArrayList<String> commands = new ArrayList<>();
    try {
      PreparedStatement ps = connection.prepareStatement("SELECT * FROM AB_CommandQuery");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        commands.add(rs.getString("SenderUUID") + "|" + rs.getString("Command"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    clear();
    return commands;
  }

  private void clear() {
    try {
      PreparedStatement ps = connection.prepareStatement("DELETE FROM AB_CommandQuery WHERE 1");
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

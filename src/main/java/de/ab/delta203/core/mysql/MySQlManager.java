package de.ab.delta203.core.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <b>MySQl Manager</b><br>
 * This is content from Spigot Utils <small>(Useful classes for making a spigot plugin)</small>.
 *
 * @see <a href="https://github.com/Delta203/SpigotUtils">Spigot Utils</a>
 * @author Delta203
 * @version 1.0
 */
public class MySQlManager {

  private final String host;
  private final int port;
  private final String database;
  private final String name;
  private final String password;

  /** The connection object for MySQl. */
  public Connection connection;

  /**
   * Register a MySQlManager to create a MySQl connection and operate with a MySQl database.
   *
   * @param host the host of the MySQl server
   * @param port the port of the MySQl server
   * @param database the name of the MySQl database
   * @param name the username for authentication
   * @param password the password for authentication
   */
  public MySQlManager(String host, int port, String database, String name, String password) {
    this.host = host;
    this.port = port;
    this.database = database;
    this.name = name;
    this.password = password;
  }

  /**
   * Checks if the connection to the MySQl database is established.
   *
   * @return the connection is established
   */
  public boolean isConnected() {
    return connection != null;
  }

  /** Connects to the MySQl database using the provided data. */
  public boolean connect() {
    if (isConnected()) return false;
    try {
      connection =
          DriverManager.getConnection(
              "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true",
              name,
              password);
      return true;
    } catch (SQLException e) {
    }
    return false;
  }

  /** Disconnects from the MySQl database. */
  public void disconnect() {
    if (!isConnected()) return;
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** Creates a table in the MySQl database if it does not exist. */
  public boolean createTable() {
    try {
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_PlayerInfo (CurrentMillis BIGINT, PlayerUUID VARCHAR(100), "
                  + "PlayerName VARCHAR(100), Server VARCHAR(100), LoginKey VARCHAR(100), Notify_Ban BOOLEAN, "
                  + "Notify_Mute BOOLEAN, Notify_Report BOOLEAN)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_PlayerHistory (CurrentMillis BIGINT, PlayerUUID VARCHAR(100), "
                  + "FromUUID VARCHAR(100), Type VARCHAR(100), Duration VARCHAR(100), Reason LONGTEXT)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_Bans (PlayerUUID VARCHAR(100), PlayerIp VARCHAR(100), "
                  + "FromUUID VARCHAR(100), End BIGINT, Reason LONGTEXT)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_Mutes (PlayerUUID VARCHAR(100), FromUUID VARCHAR(100), "
                  + "End BIGINT, Reason LONGTEXT)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_Reports (CurrentMillis BIGINT, PlayerUUID VARCHAR(100), "
                  + "FromUUID VARCHAR(100), Server VARCHAR(100), Reason LONGTEXT, InProgress BOOLEAN)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_Chat (CurrentMillis BIGINT, PlayerUUID VARCHAR(100), "
                  + "Server VARCHAR(100), Message LONGTEXT)")
          .executeUpdate();
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS AB_CommandQuery (SenderUUID VARCHAR(100), Command LONGTEXT)")
          .executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}

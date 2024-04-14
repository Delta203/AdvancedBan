<?php
  if (!isset($_GET["player"])) header("Location: " . $root);
  $name = htmlspecialchars($_GET["player"]);
  $sql = "INSERT INTO AB_CommandQuery (SenderUUID, Command) VALUES ('$session_uuid', 'unban $name')";
  $connection->query($sql);
  header("Location: " . $root . "/?p=check&player=" . $name . "&unbanned");
?>

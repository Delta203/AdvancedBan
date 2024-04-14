<?php
  if (!isset($_GET["uuid"])) header("Location: " . $root);
  if (!isset($_GET["player"])) header("Location: " . $root);
  if (!isset($_GET["punishment"])) header("Location: " . $root);
  $uuid = htmlspecialchars($_GET["uuid"]);
  $name = htmlspecialchars($_GET["player"]);
  $punishment = htmlspecialchars($_GET["punishment"]);
  if (!array_key_exists($punishment, $punishment_all)) header("Location: " . $root);
  $command = $punishment_all[$punishment];
  $command = str_replace("%player%", $name, $command);
  $sql = "DELETE FROM AB_Reports WHERE PlayerUUID = '$uuid'";
  $connection->query($sql);
  $sql = "INSERT INTO AB_CommandQuery (SenderUUID, Command) VALUES ('$session_uuid', '$command')";
  $connection->query($sql);
  header("Location: " . $root . "/?success");
?>

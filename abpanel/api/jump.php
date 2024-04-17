<?php
if (!isset($_GET["player"])) header("Location: " . $root);
if (!isset($_GET["server"])) header("Location: " . $root);
$uuid = htmlspecialchars($_GET["player"]);
$server = htmlspecialchars($_GET["server"]);
$command = "server " . $server;
if ($server_type == "SPIGOT") $command = "tp " . getName($uuid);
$sql = "INSERT INTO AB_CommandQuery (SenderUUID, Command) VALUES ('$session_uuid', '$command')";
$connection->query($sql);
header("Location: " . $root . "/?p=report&uuid=" . $uuid . "&jumped");
?>

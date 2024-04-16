<?php
if (!isset($_GET["player"])) header("Location: " . $root);
if (!isset($_GET["server"])) header("Location: " . $root);
$uuid = htmlspecialchars($_GET["player"]);
$server = htmlspecialchars($_GET["server"]);
$sql = "INSERT INTO AB_CommandQuery (SenderUUID, Command) VALUES ('$session_uuid', 'server $server')";
$connection->query($sql);
header("Location: " . $root . "/?p=report&uuid=" . $uuid . "&jumped");
?>

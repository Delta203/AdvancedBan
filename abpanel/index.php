<?php
// config files and api
require_once("config.php");
require_once("punishments.php");
require_once("messages/messages_". $language . ".php");
require_once("api/handler.php");
// page
$p = "index";
if (isset($_GET["p"])) $p = htmlspecialchars($_GET["p"]);
// mysql connection
$connection = new mysqli($mysql_host, $mysql_username, $mysql_password, $mysql_database);
if ($connection->connect_error) die("MySQl connection failed: " . $connection->connect_error);
// login session
session_start();
if (isset($_GET["uuid"]) && isset($_GET["key"])) {
  // new session
  $uuid = htmlspecialchars($_GET["uuid"]);
  $key = htmlspecialchars($_GET["key"]);
  $_SESSION["user"] = $uuid . "|" . $key;
  header("Location: " . $root);
}
$session_uuid = "-";
$session_key = "-";
$session_valid = isSessionValid();
if (!$session_valid) {
  unset($_SESSION["user"]);
  $p = "error";
} else {
  $session = explode("|", $_SESSION["user"]);
  $session_uuid = $session[0];
  $session_key = substr($session[1], 0, 4) . "*";
}
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <link rel="icon" href="images/favicon.ico">
    <link href="style.css" rel="stylesheet">
    <title><?php echo($messages_title); ?></title>

    <?php if ($https_redirect) { ?>
    <!-- http redirect -->
    <script>
      var loc = window.location.href + "";
      if (loc.indexOf("http://") == 0) {
        window.location.href = loc.replace("http://", "https://");
      }
    </script>
    <!-- http redirect -->
    <?php } ?>
  </head>
  <body>
    <?php if (file_exists("menus/" . $p . ".php") || $p == "closeReport" || $p == "getReport" || $p == "jump" || $p == "logout"
      || $p == "punish" || $p == "unban" || $p == "unmute") {
      if (!$session_valid) {
        include("menus/error.php");
      } else {
        include("menus/assets/navbar.php");
        if ($p == "closeReport" || $p == "getReport" || $p == "jump" || $p == "logout" || $p == "punish" || $p == "unban"
          || $p == "unmute") {
          include("api/" . $p . ".php");
        } else {
          include("menus/" . $p . ".php");
        }
      }
    } else {
      include("menus/notfound.php");
    } ?>
    <?php include("menus/assets/footer.php"); ?>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script>
      function copyToClipboard(value) {
        navigator.clipboard.writeText(value);
      }
    </script>
    <script>
      setTimeout(function() {
        try {
          bootstrap.Alert.getOrCreateInstance(document.querySelector(".alert")).close();
        } catch (error) {}
      }, 2000);
    </script>
  </body>
</html>

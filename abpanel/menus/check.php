<?php
if (!isset($_GET["player"])) header("Location: " . $root);
$content = htmlspecialchars($_GET["player"]);
$uuid = "-";
if (strlen($content) == 36) {
  $uuid = $content;
} else {
  $uuid = getUUID($content);
}
$name = getName($uuid);
$isBanned = isBanned($uuid);
$isMuted = isMuted($uuid);

$page = 1;
if (isset($_GET["page"])) $page = htmlspecialchars($_GET["page"]);
$offset = ($page-1) * $max_historylist;
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <?php if ($isBanned || $isMuted) echo("<ul class='nav nav-tabs justify-content-end'>") ?>
        <?php if ($isBanned) { ?>
          <li class="nav-item">
            <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=unban&player=" . $name); ?>">
              <?php echo($messages_check_unban); ?></a>
          </li>
        <?php } if ($isMuted) { ?>
          <li class="nav-item">
            <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=unmute&player=" . $name); ?>">
              <?php echo($messages_check_unmute); ?></a>
          </li>
        <?php } ?>
      <?php if ($isBanned || $isMuted) echo("</ul>") ?>

      <?php if (isset($_GET["unbanned"])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          <?php echo(str_replace("%player%", $name, $messages_alert_unbanned)); ?>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php } ?>
      <?php if (isset($_GET["unmuted"])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          <?php echo(str_replace("%player%", $name, $messages_alert_unmuted)); ?>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php } ?>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <div class="col d-flex align-items-start">
          <div class="icon-square text-body-emphasis bg-body-secondary d-inline-flex align-items-center justify-content-center fs-4
            flex-shrink-0 me-3">
            <image src="https://minotar.net/helm/<?php if ($uuid == "-") { echo("MHF_Question"); } else { echo($uuid); } ?>/100"
              width="80" height="80">
          </div>
          <div>
            <h1><?php echo($messages_check); ?></h1>
            <p class="fs-5 text-muted">
              <?php echo(str_replace("%player%", $name, $messages_check_name)); ?>
              <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($name); ?>');">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
                  <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1
                    0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2
                    0 0 1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
                </svg>
              </a>
            </p>
          </div>
        </div>
        <p class="fs-5 text-muted">
          <?php echo(str_replace("%uuid%", $uuid, $messages_check_uuid)); ?>
          <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($uuid); ?>');">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
              <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1 0 0
                0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2 0 0
                1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
            </svg>
          </a><br>
          <?php echo($messages_check_namemc); ?>
          <a class="text-decoration-none" href="https://de.namemc.com/search?q=<?php if ($uuid == "-") {
            echo("MHF_Question"); } else { echo($uuid); } ?>" target="_blank"><?php echo($name); ?></a><br>
          <?php echo($messages_check_reports); ?>
          <a class="text-decoration-none" href="<?php echo($root . "/?p=report&uuid=" . $uuid); ?>">
            <?php echo(getReportsCount($uuid)); ?></a><br>
          <?php echo(str_replace("%date%", millisecondsToDate(getRegisterMillis($uuid)), $messages_check_registered)); ?>
        </p>
        <p class="fs-5 text-muted">
          <?php echo(str_replace("%server%", getServer($uuid), $messages_check_server)); ?><br>
          <?php echo($messages_check_banned . " "); if ($isBanned) { echo("<b class='text-danger'>true</b>"); } else {
            echo("<b class='text-success'>false</b>"); } ?><br>
          <?php echo($messages_check_muted . " "); if ($isMuted) { echo("<b class='text-danger'>true</b>"); } else {
            echo("<b class='text-success'>false</b>"); } ?>
        </p>
      </div>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1><?php echo($messages_check_ban_mute); ?></h1>
        <p class="fs-5 text-muted">
          <?php echo(str_replace("%bans%", getBansCount($uuid), $messages_check_ban_mute_bans)); ?><br>
          <?php echo(str_replace("%mutes%", getMutesCount($uuid), $messages_check_ban_mute_mutes)); ?>
        </p>
        <table class="table table-striped mt-3">
          <thead>
            <tr>
              <th><?php echo($messages_date); ?></th>
              <th><?php echo($messages_type); ?></th>
              <th><?php echo($messages_from); ?></th>
              <th><?php echo($messages_reason); ?></th>
              <th><?php echo($messages_duration); ?></th>
            </tr>
          </thead>
          <tbody>
            <?php
            $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' ORDER BY CurrentMillis DESC LIMIT $max_historylist OFFSET $offset";
            $result = $connection->query($sql);
            while ($row = $result->fetch_assoc()) { ?>
              <tr>
                <td><?php echo(millisecondsToDate($row["CurrentMillis"])); ?></td>
                <th><?php echo($row["Type"]); ?></th>
                <td><?php echo(getName($row["FromUUID"])); ?></td>
                <td><?php echo($row["Reason"]); ?></td>
                <td><?php echo($row["Duration"]); ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
          <div class="btn-group me-2" role="group">
            <?php
            $pages = getHistoryCount($uuid) / $max_historylist;
            for ($i = 1; $i <= $pages+1; $i++) { ?>
              <a class="btn btn-outline-secondary <?php if ($i == $page) echo("active"); ?>"
                href="<?php echo($root); ?>/?p=check&player=<?php echo($name); ?>&page=<?php echo($i); ?>"><?php echo($i); ?></a>
            <?php } ?>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <?php include("assets/sidebar.php") ?>
    </div>
  </div>
</div>

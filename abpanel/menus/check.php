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
  $offset = ($page-1) * 10;
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <?php if ($isBanned || $isMuted) echo("<ul class='nav nav-tabs justify-content-end'>") ?>
        <?php if ($isBanned) { ?>
          <li class="nav-item">
            <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=unban&player=" . $name); ?>">Unban Player</a>
          </li>
        <?php } if ($isMuted) { ?>
          <li class="nav-item">
            <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=unmute&player=" . $name); ?>">Unmute Player</a>
          </li>
        <?php } ?>
      <?php if ($isBanned || $isMuted) echo("</ul>") ?>
      <?php if(isset($_GET['unbanned'])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          You have successfully unbanned <b><?php echo($name); ?></b>!<br>
          Wait a few seconds for the unban to process.
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php } ?>
      <?php if(isset($_GET['unmuted'])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          You have successfully unmuted <b><?php echo($name); ?></b>!<br>
          Wait a few seconds for the unmute to process.
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php } ?>
      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <div class="col d-flex align-items-start">
          <div class="icon-square text-body-emphasis bg-body-secondary d-inline-flex align-items-center justify-content-center fs-4 flex-shrink-0 me-3">
            <image src="https://minotar.net/helm/<?php if ($uuid == "-") { echo("MHF_Question"); } else { echo($uuid); } ?>/100" width="80" height="80">
          </div>
          <div>
            <h1>Check Player</h1>
            <p class="fs-5 text-muted">
              Name: <b><?php echo($name); ?></b>
              <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($name); ?>');"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
              </svg></a>
            </p>
          </div>
        </div>
        <p class="fs-5 text-muted">
          UUID: <b><?php echo($uuid); ?></b>
          <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($uuid); ?>');"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
          </svg></a><br>
          NameMC Profile: <a class="text-decoration-none" href="https://de.namemc.com/search?q=<?php if ($uuid == "-") { echo("MHF_Question"); } else { echo($uuid); } ?>" target="_blank"><?php echo($name); ?></a><br>
          Reports: <a class="text-decoration-none" href="<?php echo($root . "/?p=report&uuid=" . $uuid); ?>"><?php echo(getReportsCount($uuid)); ?></a><br>
          Registered: <b><?php echo(millisecondsToDate(getRegisterMillis($uuid))); ?></b>
        </p>
        <p class="fs-5 text-muted">
          Server: <b><?php echo(getServer($uuid)); ?></b><br>
          Banned: <?php if ($isBanned) { echo("<b class='text-danger'>true</b>"); } else { echo("<b class='text-success'>false</b>"); } ?><br>
          Muted: <?php if ($isMuted) { echo("<b class='text-danger'>true</b>"); } else { echo("<b class='text-success'>false</b>"); } ?><br>
        </p>
      </div>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1>Ban & Mute History</h1>
        <p class="fs-5 text-muted">
          Bans: <b><?php echo(getBansCount($uuid)); ?></b><br>
          Mutes: <b><?php echo(getMutesCount($uuid)); ?></b><br>
        </p>
        <table class="table table-striped mt-3">
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>From</th>
              <th>Reason</th>
              <th>Duration</th>
            </tr>
          </thead>
          <tbody>
            <?php
            $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' ORDER BY CurrentMillis DESC LIMIT 10 OFFSET $offset";
            $result = $connection->query($sql);
            while ($row = $result->fetch_assoc()) { ?>
              <tr>
                <td><?php echo(millisecondsToDate($row['CurrentMillis'])); ?></td>
                <th><?php echo($row['Type']); ?></th>
                <td><?php echo(getName($row['FromUUID'])); ?></td>
                <td><?php echo($row['Reason']); ?></td>
                <td><?php echo($row['Duration']); ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
          <div class="btn-group me-2" role="group">
            <a class="btn btn-outline-secondary" href="<?php echo($root); ?>/?p=check&player=<?php echo($name); ?>&page=1">1</a>
            <?php
              $pages = getHistoryCount($uuid) / 10;
              for ($i = 2; $i <= $pages+1; $i++) { ?>
                <a class="btn btn-outline-secondary" href="<?php echo($root); ?>/?p=check&player=<?php echo($name); ?>&page=<?php echo($i); ?>"><?php echo($i); ?></a>
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

<script>
function copyToClipboard(value) {
  navigator.clipboard.writeText(value);
}
</script>

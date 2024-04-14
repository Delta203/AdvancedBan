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
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
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
          Banned: <?php if (isBanned($uuid)) { echo("<b class='text-danger'>true</b>"); } else { echo("<b class='text-success'>false</b>"); } ?><br>
          Muted: <?php if (isMuted($uuid)) { echo("<b class='text-danger'>true</b>"); } else { echo("<b class='text-success'>false</b>"); } ?><br>
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
            $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' ORDER BY CurrentMillis DESC";
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

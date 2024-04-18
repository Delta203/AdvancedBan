<?php
if (!isset($_GET["uuid"])) header("Location: " . $root);
$uuid = htmlspecialchars($_GET["uuid"]);
$report = getLatestReport($uuid);
if ($report == null) header("Location: " . $root);
$name = getName($uuid);
$fromUUID = $report["FromUUID"];
$fromName = getName($fromUUID);
$currentMillis = $report["CurrentMillis"];
$date = millisecondsToDate($currentMillis);
$server = $report["Server"];
$reason = $report["Reason"];
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <ul class="nav nav-tabs justify-content-end">
        <?php if ($session_uuid != $admin_uuid) { ?>
          <li class="nav-item">
            <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=jump&player=" . $uuid . "&server=" . $server); ?>">
              <?php echo($messages_report_jump); ?>
            </a>
          </li>
        <?php } ?>
        <li class="nav-item">
          <a class="nav-link text-body-secondary" href="<?php echo($root . "/?p=closeReport&player=" . $uuid); ?>">
            <?php echo($messages_report_close); ?>
          </a>
        </li>
      </ul>

      <?php if(isset($_GET["jumped"])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          <?php echo(str_replace("%server%", $server, $messages_alert_jumped)); ?>
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
            <h1><?php echo($messages_report); ?></h1>
            <p class="fs-5 text-muted">
              <?php echo(str_replace("%reason%", $reason, $messages_report_reason)); ?>
            </p>
          </div>
        </div>
        <p class="fs-5 text-muted">
          <?php echo($messages_report_name); ?> <a class="text-decoration-none" href="<?php echo($root . "/?p=check&player=" . $uuid); ?>" target="_blank">
            <?php echo($name); ?></a>
          <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($name); ?>');">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
              <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1 0 0
                0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2 0 0
                1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
          </svg></a><br>
          <?php echo(str_replace("%uuid%", $uuid, $messages_report_uuid)); ?>
          <a class="text-body-secondary" href="" onclick="copyToClipboard('<?php echo($uuid); ?>');">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-copy" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M4 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v8a1 1 0 0 0
              1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 5a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1v-1h1v1a2 2 0 0 1-2 2H2a2 2 0 0
              1-2-2V6a2 2 0 0 1 2-2h1v1z"/>
          </svg></a><br>
          <?php echo(str_replace("%date%", $date, $messages_report_date)); ?>
        </p>
        <p class="fs-5 text-muted">
          <?php echo(str_replace("%server%", $server, $messages_report_server)); ?><br>
          <?php echo(str_replace("%from%", $fromName, $messages_report_from)); ?><br>
          <?php echo(str_replace("%fromUUID%", $fromUUID, $messages_report_from_uuid)); ?>
        </p>
      </div>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <p class="fs-5 text-muted">
          <?php echo(str_replace("%player%", $name, $messages_report_punish)); ?>
        </p>
        <form method="get">
          <input type="hidden" name="p" value="punish" required>
          <input type="hidden" name="uuid" value="<?php echo($uuid); ?>" required>
          <input type="hidden" name="name" value="<?php echo($name); ?>" required>
          <div class="mb-3">
            <?php foreach(array_keys($punishments_ban) as $ban) { ?>
              <input type="radio" class="btn-check" name="punishment" id="btnradio<?php echo($ban); ?>" value="<?php echo($ban); ?>"
                autocomplete="off" required>
              <label class="btn btn-outline-primary" for="btnradio<?php echo($ban); ?>"><?php echo($ban); ?></label>
            <?php } ?>
          </div>
          <div class="mb-3">
            <?php foreach(array_keys($punishments_mute) as $mute) { ?>
              <input type="radio" class="btn-check" name="punishment" id="btnradio<?php echo($mute); ?>" value="<?php echo($mute); ?>"
                autocomplete="off" required>
              <label class="btn btn-outline-primary" for="btnradio<?php echo($mute); ?>"><?php echo($mute); ?></label>
            <?php } ?>
          </div>
          <div class="row">
            <div class="col-md-6">
              <div class="form-check">
                <input class="form-check-input" type="radio" name="checked" id="flexRadioDefault" required>
                <label class="form-check-label" for="flexRadioDefault">
                  <?php echo($messages_report_punish_checked); ?>
                </label>
              </div>
            </div>
            <div class="col-md-6">
              <div class="d-grid gap-2 justify-content-md-end">
                <button class="btn btn-outline-danger px-4 rounded-pill" type="submit">
                  <?php echo($messages_report_punish_button); ?>
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>

      <?php if (getReportsCount($uuid) > 1) {?>
        <div class="bg-body-tertiary py-5 p-5 mb-5">
          <?php
          $sql = "SELECT * FROM AB_Reports WHERE PlayerUUID = '$uuid' AND FromUUID != '$fromUUID' ORDER BY CurrentMillis DESC";
          $result = $connection->query($sql);
          while ($row = $result->fetch_assoc()) { ?>
            <div class="row">
              <div class="col-md-6">
                <p class="fs-5 text-muted">
                  <?php echo(str_replace("%reason%", $row["Reason"], $messages_report_reason)); ?><br>
                  <?php echo(str_replace("%date%", millisecondsToDate($row["CurrentMillis"]), $messages_report_date)); ?><br>
                  <?php echo($messages_report_chat); ?> <a class="text-decoration-none"
                    href="<?php echo($root); ?>/?p=chatlog&player=<?php echo($uuid); ?>&millis=<?php echo($row["CurrentMillis"]); ?>"
                    target="_blank"><?php echo($row["CurrentMillis"]); ?></a>
                </p>
              </div>
              <div class="col-md-6">
                <p class="fs-5 text-muted">
                  <?php echo(str_replace("%from%", getName($row["FromUUID"]), $messages_report_from)); ?><br>
                  <?php echo(str_replace("%fromUUID%", $row["FromUUID"], $messages_report_from_uuid)); ?>
                </p>
              </div>
            </div>
            <hr>
          <?php } ?>
        </div>
      <?php } ?>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1>
          <?php echo($messages_report_chatlog); ?>
          <a class="text-body-secondary text-decoration-none" href="#collapseChatLog" data-bs-toggle="collapse" role="button"
            aria-expanded="false" aria-controls="collapseLogin">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-down-fill"
              viewBox="0 0 16 16">
              <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/>
            </svg>
          </a>
        </h1>
        <div>
          <div class="collapse show" id="collapseChatLog">
            <table class="table table-striped mt-3">
              <thead>
                <tr>
                  <th><?php echo($messages_date); ?></th>
                  <th><?php echo($messages_server); ?></th>
                  <th><?php echo($messages_from); ?></th>
                  <th><?php echo($messages_message); ?></th>
                </tr>
              </thead>
              <tbody>
                <?php
                $start = $currentMillis - $chatlog_minutes_before * 60000;
                $end = $currentMillis + $chatlog_minutes_after * 60000;
                $sql = "SELECT * FROM AB_Chat WHERE Server = '$server' AND CurrentMillis >= '$start' AND CurrentMillis <= '$end' ORDER BY CurrentMillis";
                $result = $connection->query($sql);
                while ($row = $result->fetch_assoc()) { ?>
                  <tr>
                    <td><?php echo(millisecondsToDate($row["CurrentMillis"])); ?></td>
                    <td><?php echo($row["Server"]); ?></td>
                    <td><?php if (getName($row["PlayerUUID"]) == $name) { echo("<b>" . getName($row["PlayerUUID"]) . "</b>"); } else {
                      echo(getName($row["PlayerUUID"])); } ?></td>
                    <td><?php if (str_contains($row["Message"], "/report")) { echo("<u>" . $row["Message"] . "</u>"); } else {
                      echo($row["Message"]); } ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <?php include("assets/sidebar.php") ?>
    </div>
  </div>
</div>

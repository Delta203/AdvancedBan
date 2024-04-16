<?php
if (!isset($_GET["player"])) header("Location: " . $root);
if (!isset($_GET["millis"])) header("Location: " . $root);
$uuid = htmlspecialchars($_GET["player"]);
$currentMillis = htmlspecialchars($_GET["millis"]);
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1><?php echo($messages_report_chatlog); ?></h1>
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
            $sql = "SELECT * FROM AB_Chat WHERE CurrentMillis >= '$start' AND CurrentMillis <= '$end' ORDER BY CurrentMillis";
            $result = $connection->query($sql);
            while ($row = $result->fetch_assoc()) { ?>
              <tr>
                <td><?php echo(millisecondsToDate($row['CurrentMillis'])); ?></td>
                <td><?php echo($row['Server']); ?></td>
                <td><?php echo(getName($row['PlayerUUID'])); ?></td>
                <td><?php if (str_contains($row['Message'], "/report")) { echo("<u>" . $row['Message'] . "</u>"); } else {
                  echo($row['Message']); } ?></td>
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

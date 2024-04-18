<?php
$page = 1;
if (isset($_GET["page"])) $page = htmlspecialchars($_GET["page"]);
$offset = ($page-1) * $max_mutelist;
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1><?php echo($messages_mutelist) ?></h1>
        <table class="table table-striped mt-3">
          <thead>
            <tr>
              <th><?php echo($messages_reason) ?></th>
              <th><?php echo($messages_name) ?></th>
            </tr>
          </thead>
          <tbody>
            <?php
            $sql = "SELECT Reason, PlayerUUID FROM AB_Mutes LIMIT $max_mutelist OFFSET $offset";
            $result = $connection->query($sql);
            while ($row = $result->fetch_assoc()) {
              $name = getName($row["PlayerUUID"]); ?>
              <tr>
                <td><?php echo($row["Reason"]); ?></td>
                <td><a class="text-decoration-none" href="<?php echo($root); ?>/?p=check&player=<?php echo($name); ?>">
                  <?php echo($name); ?></a></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
          <div class="btn-group me-2" role="group">
            <?php
            $pages = getGlobaMutesCount() / $max_mutelist;
            for ($i = 1; $i <= $pages+1; $i++) { ?>
              <a class="btn btn-outline-secondary <?php if ($i == $page) echo("active"); ?>"
                href="<?php echo($root); ?>/?p=mutelist&page=<?php echo($i); ?>"><?php echo($i); ?></a>
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

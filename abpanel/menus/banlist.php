<?php
$page = 1;
if (isset($_GET["page"])) $page = htmlspecialchars($_GET["page"]);
$offset = ($page-1) * 10;
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8">
      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1>Banned Players</h1>
        <table class="table table-striped mt-3">
          <thead>
            <tr>
              <th>Reason</th>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            <?php
            $sql = "SELECT Reason, PlayerUUID FROM AB_Bans LIMIT 10 OFFSET $offset";
            $result = $connection->query($sql);
            while ($row = $result->fetch_assoc()) {
              $name = getName($row['PlayerUUID']); ?>
              <tr>
                <td><?php echo($row['Reason']); ?></td>
                <td><a class="text-decoration-none" href="<?php echo($root); ?>/?p=check&player=<?php echo($name); ?>"><?php echo($name); ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
          <div class="btn-group me-2" role="group">
            <a class="btn btn-outline-secondary" href="<?php echo($root); ?>/?p=mutelist&page=1">1</a>
            <?php
              $pages = getGlobaMutesCount() / 10;
              for ($i = 2; $i <= $pages+1; $i++) { ?>
                <a class="btn btn-outline-secondary" href="<?php echo($root); ?>/?p=mutelist&page=<?php echo($i); ?>"><?php echo($i); ?></a>
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

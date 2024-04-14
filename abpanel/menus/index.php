<?php
  $reports = getGlobalReportsCount();
?>

<div class="container mt-3">
  <div class="row">
    <div class="col-md-8 text-center">
      <?php if(isset($_GET['success'])) { ?>
        <div class="alert alert-success alert-dismissible fade show my-0" role="alert">
          You have successfully completed the report!
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php } ?>
      <div class="bg-body-tertiary py-5 mb-5">
        <image src="images/<?php if ($reports == 0) { echo("orchid.png"); } else { echo("chest.png"); }?>" width="200" height="200">
        <h1>Manage Reports</h1>
        <p class="mx-auto fs-5 text-muted">
          There are currently <?php echo($reports); ?> open reports.
        </p>
        <a class="d-inline-flex btn btn-success btn-lg px-4 rounded-pill <?php if ($reports == 0) echo("disabled"); ?>" href="?p=getReport">
          Open Report
        </a>
      </div>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1>Check Player</h1>
        <p class="mx-auto fs-5 text-muted">
          Search for player UUID or name.
        </p>
        <form class="input-group mb-3" method="get">
          <input type="hidden" name="p" value="check" required>
          <input type="text" name="player" class="form-control mx-3" placeholder="Player" required>
          <button class="d-inline-flex align-items-center btn btn-outline-secondary btn-lg px-4 rounded-pill" type="submit">
            Search
          </button>
        </form>
      </div>

      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <div class="row">
          <div class="col-md-6">
            <p class="mx-auto fs-5 text-muted">Global Bans:</p>
            <h1><a class="link-dark link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover" href="<?php echo($root); ?>/?p=banlist"><?php echo(getGlobalBansCount()); ?></a></h1>
          </div>
          <div class="col-md-6">
            <p class="mx-auto fs-5 text-muted">Global Mutes:</p>
            <h1><a class="link-dark link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover" href="<?php echo($root); ?>/?p=mutelist"><?php echo(getGlobaMutesCount()); ?></a></h1>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <?php include("assets/sidebar.php") ?>
    </div>
  </div>
</div>

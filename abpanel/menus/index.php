<div class="container mt-2">
  <div class="row">
    <div class="col-md-8 text-center">
      <div class="bg-body-tertiary py-5 mb-5">
        <image src="images/chest.png" width="200" height="200">
        <h1>Manage Reports</h1>
        <p class="col-lg-8 mx-auto fs-5 text-muted">
          There are currently 10 open reports.
        </p>
        <button class="d-inline-flex btn btn-success btn-lg px-4 rounded-pill" type="button">
          Open Report
        </button>
      </div>
      <div class="bg-body-tertiary py-5 p-5 mb-5">
        <h1>Check Player</h1>
        <p class="col-lg-8 mx-auto fs-5 text-muted">
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
    </div>
    <div class="col-md-4">
      <?php include("sidebar.php") ?>
    </div>
  </div>
</div>

<div class="container pt-5">
  <div class="row">
    <div class="col-md-8">
      <h1><?php echo($messages_invalid_session); ?></h1>
      <p><?php echo($messages_invalid_session_info); ?></p>
    </div>

    <div class="col-md-4">
      <p class="text-end">
        <a class="text-body-secondary text-decoration-none" href="#collapseLogin" data-bs-toggle="collapse" role="button"
          aria-expanded="false" aria-controls="collapseLogin">
          <?php echo($messages_login); ?>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-down-fill"
            viewBox="0 0 16 16">
            <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/>
          </svg>
        </a>
      </p>
      <div>
        <div class="collapse" id="collapseLogin">
          <form method="get" autocomplete="off">
            <div class="input-group mb-3">
              <input type="text" name="uuid" class="form-control" placeholder="<?php echo($messages_uuid); ?>" required>
            </div>
            <div class="input-group">
              <input type="password" name="key" class="form-control" placeholder="<?php echo($messages_key); ?>" required>
              <button class="btn btn-outline-secondary" type="submit"><?php echo($messages_login); ?></button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

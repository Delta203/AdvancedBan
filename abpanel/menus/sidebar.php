<div class="bg-body-tertiary text-left px-5 py-3 pt-4">
  <div class="col d-flex align-items-start">
    <div class="icon-square text-body-emphasis bg-body-secondary d-inline-flex align-items-center justify-content-center fs-4 flex-shrink-0 me-3">
      <image src="https://minotar.net/helm/<?php echo($session_uuid); ?>/32" width="32" height="32">
    </div>
    <div>
      <p>
        Logged user: <b><?php echo(getName($session_uuid)); ?></b><br>
        UUID: <b><?php echo($session_uuid); ?></b><br>
        Session: <b><?php echo($session_key); ?></b><br>
        Server: <b><?php echo(getServer($session_uuid)); ?></b><br>
      </p>
    </div>
  </div>
</div>

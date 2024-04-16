<!-- sidebar -->
<div class="bg-body-tertiary text-left px-5 py-3 pt-4">
  <div class="col d-flex align-items-start">
    <div class="icon-square text-body-emphasis bg-body-secondary d-inline-flex align-items-center justify-content-center fs-4
      flex-shrink-0 me-3">
      <image src="https://minotar.net/helm/<?php echo($session_uuid); ?>/32" width="32" height="32">
    </div>
    <div>
      <p>
        <?php echo(str_replace("%player%", getName($session_uuid), $messages_sidebar_logged)); ?><br>
        <?php echo(str_replace("%uuid%", $session_uuid, $messages_sidebar_uuid)); ?><br>
        <?php echo(str_replace("%key%", $session_key, $messages_sidebar_session)); ?><br>
        <?php echo(str_replace("%server%", getServer($session_uuid), $messages_sidebar_server)); ?>
      </p>
    </div>
  </div>
</div>
<div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
  <a class="text-decoration-none" href="<?php echo($root); ?>/?p=logout"><?php echo($messages_logout); ?></a>
</div>
<!-- sidebar -->

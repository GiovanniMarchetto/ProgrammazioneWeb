<template>
  <div>
    <b-toast id="msg_e" title="ERROR" autoHideDelay="5000" variant="danger">
      {{ msg_error }}
    </b-toast>
    <b-toast id="msg_s" title="Success" autoHideDelay="5000" variant="success">
      {{ msg_success }}
    </b-toast>
    <b-toast id="msg_w" title="Warning!" autoHideDelay="5000" variant="warning">
      {{ msg_warning }}
    </b-toast>
  </div>
</template>

<script>
export default {
  name: "Messages",
  props: ["msg_error", "msg_success", "msg_warning"],
};
</script>

<template>
  <div v-show="attesa === true">
    <b-button block variant="light" disabled>
      <b-spinner small></b-spinner>
      Caricamento...
    </b-button>
  </div>
</template>

<script>
export default { name: "Spinner", props: ["attesa"] };
</script>

<style></style>

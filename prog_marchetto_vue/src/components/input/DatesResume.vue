<template>
  <b-container fluid>
    <b-form-group
      label="Filtro per periodo"
      label-size="lg"
      description="I file caricati il giorno d'inizio del periodo sono inclusi,
        quelli caricati il giorno di conclusione del periodo sono esclusi."
    >
      <b-form-datepicker
        id="dateFromInput"
        name="dateFromInput"
        type="date"
        v-model="dateFromInput"
        required
      ></b-form-datepicker>

      <b-form-datepicker
        id="dateToInput"
        name="dateToInput"
        type="date"
        v-model="dateToInput"
        required
      ></b-form-datepicker>
    </b-form-group>
  </b-container>
</template>

<script>
export default {
  name: "DatesResume",
  props: ["dateFrom", "dateTo"],
  computed: {
    dateFromInput: {
      get: function() {
        return this.dateFrom;
      },
      set: function(newValue) {
        this.$emit("update:dateFrom", newValue);
      },
    },
    dateToInput: {
      get: function() {
        return this.dateTo;
      },
      set: function(newValue) {
        this.$emit("update:dateTo", newValue);
      },
    },
  },
};
</script>

<style></style>

<template>
  <b-container fluid>
    <b-row>
      <b-col sm="6" class="my-1">
        <b-pagination
          v-model="currentPage"
          :total-rows="totalRows"
          :per-page="perPage"
          align="fill"
          first-number
          last-number
          prev-text="Prev"
          next-text="Next"
        ></b-pagination
      ></b-col>
      <b-col sm="4" class="my-1">
        <b-form-group
          label="Per page:"
          :label-for="perPageSelectId"
          label-cols-sm="3"
        >
          <b-form-select
            :id="perPageSelectId"
            v-model="perPage"
            :options="pageOptions"
          ></b-form-select>
        </b-form-group>
      </b-col>
    </b-row>

    <b-table
      striped
      stacked="sm"
      head-variant="dark"
      table-variant="secondary"
      sticky-header="600px"
      responsive
      :current-page="currentPage"
      :per-page="perPage"
      :items="items"
      :fields="fields"
      :tbody-tr-class="highlightNew"
      :busy="caricamentoDati"
    >
      <template #table-busy>
        <div class="text-center text-primary my-2">
          <b-spinner class="align-middle"></b-spinner>
          <strong>Caricamento...</strong>
        </div>
      </template>

      <template #table-caption>{{ descrizioneTabella }}</template>

      <template #cell(logo)="row">
        <b-img
          v-bind="logoProps"
          :src="`${row.item.logo}`"
          alt="Logo uploader"
        ></b-img
      ></template>

      <template #cell(details)="row">
        <b-button @click="row.toggleDetails">
          {{ row.detailsShowing ? "Hide" : "Show" }} Details
        </b-button>
      </template>

      <template #cell(files)="row">
        <b-button
          @click="$emit('mostraFiles', row.item.username)"
          variant="info"
          >Mostra File</b-button
        >
      </template>

      <template #cell(scarica)="row">
        <b-button @click="$emit('download-file', row.item.id)">
          Download
        </b-button>
      </template>

      <template #row-details="row">
        <b-card>
          <b-row>
            <b-col sm="3"><b>Username:</b></b-col>
            <b-col>{{ row.item.username }}</b-col>
          </b-row>

          <b-row>
            <b-col sm="3"><b>Email:</b></b-col>
            <b-col>{{ row.item.email }}</b-col>
          </b-row>
        </b-card>
      </template>
    </b-table>

    <div v-if="items.length === 0"><p>Nessun elemento</p></div>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "Table",
  props: ["items", "fields", "caricamentoDati", "descrizioneTabella"],
  data() {
    return {
      currentPage: 1,
      perPage: 5,
      perPageSelectId: "",
      pageOptions: [5, 10, 15, { value: 100, text: "Troppi" }],
      logoProps: {
        rounded: "circle",
      },
    };
  },
  methods: {
    highlightNew(item, type) {
      if (!item || type !== "row") return;
      if (item.dataVisualizzazione === "") return "table-warning";
    },
  },

  computed: {
    totalRows() {
      return this.items.length;
    },
  },
  created() {
    this.perPageSelectId = uuidv4();
  },
};
</script>

<style scoped>
.btn {
  margin: 0px;
}
img {
  height: 5em;
  width: 5em;
}
</style>

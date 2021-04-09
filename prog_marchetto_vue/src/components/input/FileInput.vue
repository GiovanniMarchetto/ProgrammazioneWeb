<template>
  <b-container fluid>
    <b-form-group id="fileInput-group" label="File:" label-for="fileInput">
      <b-form-file
        @change.native="trasf64"
        id="fileInput"
        type="file"
        name="fileInput"
        v-model="fileInput"
        placeholder="Scegli un file o trascinalo qui..."
        required
      />
    </b-form-group>

    <b-form-group
      id="nameFileInput-group"
      label="Nome del file:"
      label-for="nameFileInput"
    >
      <b-form-input
        id="nameFileInput"
        type="text"
        name="nameFileInput"
        maxlength="100"
        v-model="nameFileInput"
        placeholder="name of file"
        required
      />
      <b-form-text>
        Non serve specificare l'estensione del file.
      </b-form-text>
    </b-form-group>

    <b-form-group
      id="hashtagInput-group"
      label="Hashtag:"
      label-for="hashtagInput"
    >
      <b-form-input
        id="hashtagInput"
        type="text"
        name="hashtagInput"
        maxlength="100"
        v-model="hashtagInput"
        placeholder="some hashtag"
      />
    </b-form-group>
  </b-container>
</template>

<script>
export default {
  name: "FileInput",
  props: ["file", "nameFile", "hashtag", "extension"],
  data() {
    return {
      fileInput: null,
    };
  },
  watch: {
    fileStringInput: function() {
      if (this.fileStringInput === "") this.fileInput = null;
    },
  },
  computed: {
    fileStringInput: {
      get: function() {
        return this.file;
      },
      set: function(newValue) {
        this.$emit("update:file", newValue);
      },
    },
    nameFileInput: {
      get: function() {
        return this.nameFile;
      },
      set: function(newValue) {
        this.$emit("update:nameFile", newValue);
      },
    },
    hashtagInput: {
      get: function() {
        return this.hashtag;
      },
      set: function(newValue) {
        this.$emit("update:hashtag", newValue);
      },
    },
  },

  methods: {
    trasf64(imgObj) {
      const reader = new FileReader();
      reader.onload = (elem) => {
        this.fileStringInput = elem.target.result.replace(
          /^data:.+;base64,/,
          ""
        );
      };
      reader.onerror = function(error) {
        console.log("Error: ", error);
      };
      reader.readAsDataURL(imgObj.target.files[0]);

      let nameObject = imgObj.target.files[0].name;
      let lastDot = nameObject.lastIndexOf(".");
      this.$emit("update:extension", nameObject.substring(lastDot));
      //https://stackoverflow.com/questions/43708127/javascript-get-the-filename-and-extension-from-input-type-file
    },
  },
};
</script>

<style></style>

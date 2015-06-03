(function() {
  var fileinput = $("#fileinput");
  fileinput.on("change", function(e) {
    var file = e.target.files[0];
    if(!file.type.match(/text/))
      return;

    var reader = new FileReader();
    reader.onload = function(e) {
      //TODO
    };
    reader.readAsText(file, "utf-8");
  });
})();

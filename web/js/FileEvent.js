(function() {
  var fileinput = $("#fileinput");
  fileinput.on("change", function(e) {
    var file = e.target.files[0];
    if(!file.type.match(/text/))
      return;

    var reader = new FileReader();
    reader.onload = function(e) {
      var text = e.target.result.split("\n");
      var generation = parseInt(text[0].replace(/.*gen:(\d+) /, "$1"));
      var genes = parseInt(text[0].replace(/.*genes:(\d+) /, "$1"));
      for (var i = 0; i < generation; i++) {
        var line = text[2 + i*(genes + 1)];
        line = line.replace(/.*\{(.*)\}/, "$1")
          .split(", ")
          .map(function( num ){ return parseInt( num, 10 ) });
        data.data[i] = line;
      }
      data.num = 0;

      $("#label").text("Generation : " + (data.num+1));
      $("#slider").slider({
        min: 1, max: data.data.length, value: 0,
        enabled: true, tooltip: 'always'
      });

      updatePager();
      initRender();
    };
    reader.readAsText(file, "utf-8");
  });
})();

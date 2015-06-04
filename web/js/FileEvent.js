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
        var num = parseInt(line.replace(/.*\: (-?\d+)\.0.*/, "$1"));
        line = line.replace(/.*\{(.*)\}/, "$1")
          .split(", ")
          .map(function( num ){ return parseInt( num, 10 ) });
        data.data[i] = line;
        data.fit[i] = num;
      }
      data.num = 0;

      $("autobtn").removeAttr("disabled");
      $("#slider").slider({
        min: 1, max: data.data.length, value: 1,
        enabled: true, tooltip: 'always'
      });
      $("#slider").slider('setValue', 0);

      updateGeneration(0);
      updatePager();
      if($("#view").children("canvas")) {
        updateMesh();
      } else {
        initRender();
      }
    };
    reader.readAsText(file, "utf-8");
  });
})();

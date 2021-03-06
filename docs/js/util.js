var updateMesh;
$(init);

function updatePager() {
  if(data.num == 0) {
    $("#prev").addClass("disabled");
  } else {
    $("#prev").removeClass("disabled");
  }
  if(data.num >= data.data.length-1) {
    $("#next").addClass("disabled");
  } else {
    $("#next").removeClass("disabled");
  }
}

function init() {
  /* auto button */
  var timer = -1;
  var $auto = $("<span>");
  $auto.addClass("glyphicon glyphicon-forward");
  $auto.attr("aria-hidden", true);
  var $stop = $("<span>");
  $stop.addClass("glyphicon glyphicon-pause");
  $stop.attr("aria-hidden", true);
  $("#autobtn").on("click", function() {
    var $this = $(this);
    if(timer == -1) {
      $this.removeClass("btn-primary");
      $this.addClass("btn-info");
      $this.empty();
      $this.append($stop);
      timer = setInterval('$("#next").click()', 500);
    } else {
      $this.removeClass("btn-info");
      $this.addClass("btn-primary");
      $this.empty();
      $this.append($auto);
      clearInterval(timer);
      timer = -1;
    }
  });

  /* pager */
  $("#prev").on("click", function() {
    updateGeneration(-1);
    updateMesh();
    updatePager();
  });

  $("#next").on("click", function() {
    updateGeneration(1);
    updateMesh();
    updatePager();
    if(data.num == data.data.length - 1) {
      $("#autobtn").click();
    }
  });

  /* slider */
  $("#slider").slider({
    min: 0,
    max: 0,
    value: 0,
    enabled: false,
    id: "slider"
  });

  if(data.data.length > 0) {
    $("#autobtn").removeAttr("disabled");
    $("#slider").slider({
      min: 1, max: data.data.length, value: 1,
      enabled: true, tooltip: 'always'
    });
    updateGeneration(0);
    initRender();
  }

  $("#slider").slider().on('change', function(e) {
    data.num = e.value.newValue - 1;
    updateGeneration(0);
    updateMesh();
    updatePager();
  });
  updatePager();
}

function updateGeneration(d) {
  if(d == undefined) return;
  if((data.num == 0 && d < 0) ||
    (data.num == data.data.length -1 && d > 0)) return;

  var s = $("#slider").slider();
  var n = s.slider("getValue");
  s.slider("setValue", n+d);
  data.num += d;
  $("#label").text("Generation : " + (data.num+1) + ", Fitness : " + data.fit[data.num]);
}

function rgbToHex(r, g, b) {
  return parseInt('0x' +
    (function(n) {
      return new Array(7 - n.length).join('0') + n;
    })((r << 16 | g << 8 | b).toString(16))
  , 16);
}

function lrgb(i, c) {
  var l = rgbTable.length;
  return rgbTable[parseInt(parseFloat(i) / 255 * (l-1))][c] * 255;
}

var data = {
  num: 0,
  fit: [0],
  data: [
    [103, -105, -57, 119, -121, -9, 87, 87, 87, -25, -89, 55,
      119, -121, -73, 23, -121, -105, 7, 71, 72, -25, -25, 55,
      119, 103, 23, -9, -25, -121, -57, -9, -41, -9, 71, 119]
  ]
};

var weight = [
  [0, 1, 2, 3, 3, 2, 1, 0],
  [1, 4, 5, 6, 6, 5, 4, 1],
  [2, 5, 7, 8, 8, 7, 5, 2],
  [3, 6, 8, 9, 9, 8, 6, 3],
  [3, 6, 8, 9, 9, 8, 6, 3],
  [2, 5, 7, 8, 8, 7, 5, 2],
  [1, 4, 5, 6, 6, 5, 4, 1],
  [0, 1, 2, 3, 3, 2, 1, 0]
];

var rgbTable = [
  { R:0 ,G:0 ,B:0.99999 }
  ,{ R:0 ,G:0.0796 ,B:0.99998 }
  ,{ R:0 ,G:0.1586 ,B:0.99998 }
  ,{ R:0 ,G:0.2358 ,B:0.99998 }
  ,{ R:0 ,G:0.3108 ,B:0.99998 }
  ,{ R:0 ,G:0.383 ,B:0.99998 }
  ,{ R:0 ,G:0.4514 ,B:0.99996 }
  ,{ R:0 ,G:0.516 ,B:0.99996 }
  ,{ R:0 ,G:0.5762 ,B:0.99994 }
  ,{ R:0 ,G:0.6318 ,B:0.99994 }
  ,{ R:0 ,G:0.6826 ,B:0.9999 }
  ,{ R:0 ,G:0.7286 ,B:0.99986 }
  ,{ R:0 ,G:0.7698 ,B:0.9998 }
  ,{ R:0 ,G:0.8064 ,B:0.9996 }
  ,{ R:0 ,G:0.8384 ,B:0.9996 }
  ,{ R:0 ,G:0.8664 ,B:0.9994 }
  ,{ R:0 ,G:0.8904 ,B:0.999 }
  ,{ R:0 ,G:0.9108 ,B:0.9986 }
  ,{ R:0 ,G:0.9282 ,B:0.998 }
  ,{ R:0 ,G:0.9426 ,B:0.9974 }
  ,{ R:0 ,G:0.9544 ,B:0.9962 }
  ,{ R:0 ,G:0.9642 ,B:0.9948 }
  ,{ R:0 ,G:0.9722 ,B:0.993 }
  ,{ R:0 ,G:0.9786 ,B:0.9906 }
  ,{ R:0 ,G:0.9836 ,B:0.9876 }
  ,{ R:0 ,G:0.9876 ,B:0.9836 }
  ,{ R:0 ,G:0.9906 ,B:0.9786 }
  ,{ R:0 ,G:0.993 ,B:0.9722 }
  ,{ R:0 ,G:0.9948 ,B:0.9642 }
  ,{ R:0 ,G:0.9962 ,B:0.9544 }
  ,{ R:0 ,G:0.9974 ,B:0.9426 }
  ,{ R:0 ,G:0.998 ,B:0.9282 }
  ,{ R:0 ,G:0.9986 ,B:0.9108 }
  ,{ R:0 ,G:0.999 ,B:0.8904 }
  ,{ R:0 ,G:0.9994 ,B:0.8664 }
  ,{ R:0 ,G:0.9996 ,B:0.8384 }
  ,{ R:0 ,G:0.9996 ,B:0.8064 }
  ,{ R:0 ,G:0.9998 ,B:0.7698 }
  ,{ R:0 ,G:0.99986 ,B:0.7286 }
  ,{ R:0 ,G:0.9999 ,B:0.6826 }
  ,{ R:0 ,G:0.99994 ,B:0.6318 }
  ,{ R:0 ,G:0.99996 ,B:0.5762 }
  ,{ R:0 ,G:0.99998 ,B:0.516 }
  ,{ R:0 ,G:0.99998 ,B:0.4514 }
  ,{ R:0 ,G:0.99998 ,B:0.383 }
  ,{ R:0 ,G:0.99994 ,B:0.3108 }
  ,{ R:0 ,G:0.99996 ,B:0.2358 }
  ,{ R:0 ,G:0.99998 ,B:0.1586 }
  ,{ R:0 ,G:0.99998 ,B:0.0796 }
  ,{ R:0 ,G:0.99999 ,B:0 }
  ,{ R:0 ,G:0.999994 ,B:0 }
  ,{ R:0.0796 ,G:0.99999 ,B:0 }
  ,{ R:0.1586 ,G:0.99998 ,B:0 }
  ,{ R:0.2358 ,G:0.99998 ,B:0 }
  ,{ R:0.3108 ,G:0.99998 ,B:0 }
  ,{ R:0.383 ,G:0.99998 ,B:0 }
  ,{ R:0.4514 ,G:0.99998 ,B:0 }
  ,{ R:0.516 ,G:0.99996 ,B:0 }
  ,{ R:0.5762 ,G:0.99996 ,B:0 }
  ,{ R:0.6318 ,G:0.99994 ,B:0 }
  ,{ R:0.6826 ,G:0.99994 ,B:0 }
  ,{ R:0.7286 ,G:0.9999 ,B:0 }
  ,{ R:0.7698 ,G:0.99986 ,B:0 }
  ,{ R:0.8064 ,G:0.9998 ,B:0 }
  ,{ R:0.8384 ,G:0.9996 ,B:0 }
  ,{ R:0.8664 ,G:0.9996 ,B:0 }
  ,{ R:0.8904 ,G:0.9994 ,B:0 }
  ,{ R:0.9108 ,G:0.999 ,B:0 }
  ,{ R:0.9282 ,G:0.9986 ,B:0 }
  ,{ R:0.9426 ,G:0.998 ,B:0 }
  ,{ R:0.9544 ,G:0.9974 ,B:0 }
  ,{ R:0.9642 ,G:0.9962 ,B:0 }
  ,{ R:0.9722 ,G:0.9948 ,B:0 }
  ,{ R:0.9786 ,G:0.993 ,B:0 }
  ,{ R:0.9836 ,G:0.9906 ,B:0 }
  ,{ R:0.9876 ,G:0.9876 ,B:0 }
  ,{ R:0.9906 ,G:0.9836 ,B:0 }
  ,{ R:0.993 ,G:0.9786 ,B:0 }
  ,{ R:0.9948 ,G:0.9722 ,B:0 }
  ,{ R:0.9962 ,G:0.9642 ,B:0 }
  ,{ R:0.9974 ,G:0.9544 ,B:0 }
  ,{ R:0.998 ,G:0.9426 ,B:0 }
  ,{ R:0.9986 ,G:0.9282 ,B:0 }
  ,{ R:0.999 ,G:0.9108 ,B:0 }
  ,{ R:0.9994 ,G:0.8904 ,B:0 }
  ,{ R:0.9996 ,G:0.8664 ,B:0 }
  ,{ R:0.9996 ,G:0.8384 ,B:0 }
  ,{ R:0.9998 ,G:0.8064 ,B:0 }
  ,{ R:0.99986 ,G:0.7698 ,B:0 }
  ,{ R:0.9999 ,G:0.7286 ,B:0 }
  ,{ R:0.99994 ,G:0.6826 ,B:0 }
  ,{ R:0.99996 ,G:0.6318 ,B:0 }
  ,{ R:0.99998 ,G:0.5762 ,B:0 }
  ,{ R:0.99998 ,G:0.516 ,B:0 }
  ,{ R:0.99998 ,G:0.4514 ,B:0 }
  ,{ R:0.99994 ,G:0.383 ,B:0 }
  ,{ R:0.99996 ,G:0.3108 ,B:0 }
  ,{ R:0.99998 ,G:0.2358 ,B:0 }
  ,{ R:0.99998 ,G:0.1586 ,B:0 }
  ,{ R:0.99999 ,G:0.0796 ,B:0 }
  ,{ R:0.999994 ,G:0 ,B:0 }
];

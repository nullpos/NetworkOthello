function initRender() {
  /* 3d */
  var renderer,
    scene = [],
    geometry = [],
    material = [],
    mesh = [],
    camera = [],
    controls,
    omesh = [];

  initRenderer();
  initScene();
  initMesh();
  initCamera();

  function initRenderer() {
    renderer = new THREE.WebGLRenderer({
      antialias: true
    });
    //renderer = new THREE.CanvasRenderer({ antialias: true });
    renderer.setClearColor(0xEEEEEE);
    renderer.setSize(window.innerWidth - 50,
      window.innerWidth / 2);
    renderer.clear();
    $("#view").append(renderer.domElement);

    window.onresize = updateWindowSize;

    function updateWindowSize() {
      renderer.setSize(window.innerWidth - 50,
        window.innerWidth / 2);
    }
  }

  function initScene() {
    var lighta = new THREE.DirectionalLight(0xffffff);
    var lightb = new THREE.DirectionalLight(0xffffff);
    lighta.position.set(0, -300, 500);
    lightb.position.set(0, 300, -500);
    scene = new THREE.Scene();
    scene.add(lighta);
    scene.add(lightb);
  }

  function initMesh() {
    for (var v = 0; v < 3; v++) {
      mesh[v] = new Array();
      omesh[v] = new Array();
      for (var i = 0; i < 8; i++) {
        for (var j = 0; j < 8; j++) {

          /* cells */
          var px = i * 50 - 200 + (v - 1) * 500 + 25;
          var py = j * 50 - 200 + 25;
          var zi = v * 12 + weight[i][j];
          var z = data.data[data.num][zi] + 128;

          geometry[zi] = new THREE.BoxGeometry(50, 50, 1);
          material[zi] = new THREE.MeshLambertMaterial({
            color: rgbToHex(lrgb(z, 'R'), lrgb(z, 'G'), lrgb(z, 'B'))
          });
          mesh[v][i*8+j] = new THREE.Mesh(geometry[zi], material[zi]);
          mesh[v][i*8+j].position.set(px, py, z / 2);
          mesh[v][i*8+j].scale.z = (z == 0) ? 1 : z;
          scene.add(mesh[v][i*8+j]);
        }
      }

      /* text */
      var tx = (v - 1) * 500 - 50;
      var ty = 300;
      var textgeo = new THREE.TextGeometry(v, {
        size: 100,
        font: "helvetiker",
        weight: "bold",
        style: "normal"
      });
      var textmate = new THREE.MeshLambertMaterial({
        color: 0xAAAAAA
      });
      var textmesh = new THREE.Mesh(textgeo, textmate);
      textmesh.position.set(tx, ty, 0);
      scene.add(textmesh);

      /* other */
      var geo = [],
        mat = [];
      for (var i = 0; i < 2; i++) {
        var cx = (v - 1) * 500 - 100 + i * 200;
        var cy = -300;
        var czi = v * 12 + i;
        var cz = data.data[data.num][czi] + 128;
        geo[i] = new THREE.BoxGeometry(200, 50, 1);
        mat[i] = new THREE.MeshLambertMaterial({
          color: rgbToHex(lrgb(cz, 'R'), lrgb(cz, 'G'), lrgb(cz, 'B'))
        });
        omesh[v][i] = new THREE.Mesh(geo[i], mat[i]);
        omesh[v][i].position.set(cx, cy, cz / 2);
        omesh[v][i].scale.z = (cz == 0) ? 1 : cz;
        scene.add(omesh[v][i]);

        var text = (i == 0) ? "Score" : "Put";
        var div = (i == 0) ? -70 : -30;
        var ctextgeo = new THREE.TextGeometry(text, {
          size: 40,
          font: "helvetiker",
          weight: "bold",
          style: "normal"
        });
        var ctextmesh = new THREE.Mesh(ctextgeo, textmate);
        ctextmesh.position.set(cx + div, cy - 100, 0);
        scene.add(ctextmesh);
      }
    }
  }

  function initCamera() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 10000);
    camera.position.set(0, -400, 1500);
    camera.lookAt(new THREE.Vector3(0, 0, 0));
    controls = new THREE.TrackballControls(camera, $("#view")[0]);
    scene.add(camera);
  }

  updateMesh = function() {
    for (var v = 0; v < 3; v++) {
      for (var i = 0; i < 8; i++) {
        for (var j = 0; j < 8; j++) {
          /* cells */
          var zi = v * 12 + weight[i][j];
          var gz = data.data[data.num][zi] + 128;
          mesh[v][i*8+j].scale.z = gz;
          mesh[v][i*8+j].position.z = gz / 2;
          mesh[v][i*8+j].material.setValues({
            color: rgbToHex(lrgb(gz, 'R'), lrgb(gz, 'G'), lrgb(gz, 'B'))
          });
        }
      }

      for (var i = 0; i < 2; i++) {
        var czi = v * 12 + i;
        var cz = data.data[data.num][czi] + 128;
        omesh[v][i].scale.z = cz;
        omesh[v][i].position.z = cz / 2;
        omesh[v][i].material.setValues({
          color: rgbToHex(lrgb(cz, 'R'), lrgb(cz, 'G'), lrgb(cz, 'B'))
        });

      }
    }
  }

  function render() {
    renderer.clear();
    controls.update();
    renderer.render(scene, camera);
    requestAnimationFrame(render);
  }

  render();
}

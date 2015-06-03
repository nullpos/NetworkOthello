var data = {
	data: [-125, 116, -98, 121, -117, -33, 76, 42, -122, -123, -100, 109,
		117, 74, 42, 38, 2, -33, 60, -83, 23, 78, 76, -96,
		111, 16, 116, -68, 24, -7, -11, 81, -27, 61, 40, -114
	]
};

$(init);

function init() {

	var renderer,
		scene = [],
		geometry= [],
		material = [],
		mesh = [],
		camera = [],
		controls;

	initRenderer();
	initScene();
	initMesh();
	initCamera();

	function initRenderer() {
		renderer = new THREE.WebGLRenderer({ antialias: true });
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
		var light = new THREE.DirectionalLight(0xffffff);
		light.position.set(100, 200, 300);
		scene = new THREE.Scene();
		scene.add(light);

		var axis = new THREE.AxisHelper(10000);
		scene.add(axis);
	}

	function initMesh() {
		for (var v = 0; v < 3; v++) {
			for (var i = 0; i < 8; i++) {
				for (var j = 0; j < 8; j++) {

					/* cells */
					var px = i * 50 - 200 + (v-1) * 500 + 25;
					var py = j * 50 - 200 + 25;
					var zi = v * 12 + weight[i][j];
					var z = data.data[zi]+128;

					geometry[i] = new THREE.BoxGeometry(50, 50, z);
					material[i] = new THREE.MeshLambertMaterial({
							color: rgbToHex(lrgb(z, 'R'), lrgb(z, 'G'), lrgb(z, 'B'))
					});
					mesh[i] = new THREE.Mesh(geometry[i], material[i]);
					mesh[i].position.x = px;
					mesh[i].position.y = py;
					mesh[i].position.z = z/2;
					scene.add(mesh[i]);
				}
			}

			/* text */
			var tx = (v - 1) * 500 - 50;
			var ty = 300;
			var textgeo = new THREE.TextGeometry(v,
				{size: 100, font: "helvetiker", weight: "bold", style: "normal"});
			var textmate = new THREE.MeshLambertMaterial({color: 0x444444});
			var textmesh = new THREE.Mesh(textgeo, textmate);
			textmesh.position.x = tx;
			textmesh.position.y = ty;
			textmesh.position.z = 0;
			scene.add(textmesh);

			/* other */
			var geo = [],
				mat = [],
				mes = [];
			for (var i = 0; i < 2; i++) {
				var cx = (v - 1) * 500 - 100 + i * 200;
				var cy = -300;
				var czi = v * 12 + i;
				var cz = data.data[czi]+128;
				geo[i] = new THREE.BoxGeometry(200, 50, cz);
				mat[i] = new THREE.MeshLambertMaterial({
						color: rgbToHex(lrgb(cz, 'R'), lrgb(cz, 'G'), lrgb(cz, 'B'))
				});
				mes[i] = new THREE.Mesh(geo[i], mat[i]);
				mes[i].position.x = cx;
				mes[i].position.y = cy;
				mes[i].position.z = cz/2;
				scene.add(mes[i]);

				var text = (i == 0) ? "Score" : "Put" ;
				var div = (i == 0) ? -70 : -30;
				var ctextgeo = new THREE.TextGeometry(text,
					{size: 40, font: "helvetiker", weight: "bold", style: "normal"});
				var ctextmesh = new THREE.Mesh(ctextgeo, textmate);
				ctextmesh.position.x = cx + div;
				ctextmesh.position.y = cy - 100;
				ctextmesh.position.z = 0;
				scene.add(ctextmesh);
			}
		}
	}

	function initCamera() {
		camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 10000);
		camera.position.set(0, 0, 1500);
		camera.lookAt(new THREE.Vector3( 0, 0, 0 ));
		controls = new THREE.TrackballControls(camera, $("#view")[0]);
		scene.add(camera);
	}


	function render() {
		renderer.clear();
		controls.update();
		renderer.render(scene, camera);
		requestAnimationFrame(render);
	}

	render();
}

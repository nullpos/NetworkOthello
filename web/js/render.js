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
		camera = [];

	initRenderer();
	initScene();
	initMesh();
	initCamera();

	function initRenderer() {
		renderer = new THREE.WebGLRenderer({ antialias: true });
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
	}

	function initMesh() {
		for (var v = 0; v < 3; v++) {
			for (var i = 0; i < 8; i++) {
				for (var j = 0; j < 8; j++) {
					var px = i * 50 - 200 - v * 500;
					var py = j * 50 - 200;
					var zi = v * 12 + weight[i][j];
					var z = data.data[zi]+128;
					geometry[i] = new THREE.SphereGeometry(50, 50, z);
					material[i] = new THREE.MeshLambertMaterial({
							color: rgbToHex(lrgb(z, 'R'), lrgb(z, 'G'), lrgb(z, 'B'))
					});
					mesh[i] = new THREE.Mesh(geometry[i], material[i]);
					mesh[i].position.x = px;
					mesh[i].position.y = py;
					mesh[i].position.z = 0;
					scene.add(mesh[i]);
				}
			}
		}
	}

	function initCamera() {
		camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 10000);
		camera.position.set(300, 300, 300);
		camera.lookAt(new THREE.Vector3( 0, 0, 0 ));
		scene.add(camera);
	}


	function render() {
		renderer.clear();
		renderer.render(scene, camera);
		requestAnimationFrame(render);
	}

	render();
}

package pl.eqs.snake3d;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraController {

	private PerspectiveCamera cam;
	private Vector3 axis;
	private float angle;
	private int step;
	private int counter = 0;
	
	public CameraController(PerspectiveCamera cam, Vector3 axis, float angle, int step) {
		super();
		this.cam = cam;
		this.axis = axis;
		this.angle = angle;
		this.step = step;
	}
	
	public void update() {
		counter++;
		cam.rotate(axis, angle / step);
		cam.update();
	}

	public int getStep() {
		return step;
	}

	public int getCounter() {
		return counter;
	}
}

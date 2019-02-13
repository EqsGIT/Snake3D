package pl.eqs.snake3d;

import com.badlogic.gdx.math.Vector3;

public class Wall {

	private Vector3 position;
	private Vector3 dimension;
	
	public Wall(Vector3 position, Vector3 dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
	}

	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getDimension() {
		return dimension;
	}
	
}

package pl.eqs.snake3d;

import com.badlogic.gdx.math.Vector3;

public class MyMath {

	public static Vector3 vectorProduct(Vector3 a, Vector3 b) {
		return new Vector3(a.y * b.z - a.z * b.y,
						 -(a.x * b.z - a.z * b.x),
						   a.x * b.y - a.y * b.x);
	}
	
	public static boolean cubeCollision(Vector3 ap, Vector3 as, Vector3 bp, Vector3 bs) {

		if(Math.abs(ap.x - bp.x) < (as.x + bs.x) / 2) {
			if(Math.abs(ap.y - bp.y) < (as.y + bs.y) / 2) {
				if(Math.abs(ap.z - bp.z) < (as.z + bs.z) / 2) {
					return true;
				}
			}
		}
		return false;
	}
	
}

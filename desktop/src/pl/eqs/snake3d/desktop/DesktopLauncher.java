package pl.eqs.snake3d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import pl.eqs.snake3d.Engine;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Snake 3D";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Engine(), config);
	}
}

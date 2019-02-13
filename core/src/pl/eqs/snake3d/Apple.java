package pl.eqs.snake3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Apple {

	private Model model;
	private ModelInstance modelInstance;
	
	public Apple() {
		 ModelBuilder modelBuilder = new ModelBuilder();
		 model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
		 modelInstance = new ModelInstance(model);   
		 
		 int x = (int) (Math.random() * (4 - (-4) + 1)) + (-4);
		 int y = (int) (Math.random() * (4 - (-4) + 1)) + (-4);
		 int z = (int) (Math.random() * (4 - (-4) + 1)) + (-4);
		 modelInstance.transform.setTranslation(new Vector3(x,y,z).scl(5f));
	}
	
	public Model getModel() {
		return model;
	}

	public ModelInstance getModelInstance() {
		return modelInstance;
	}
	
}

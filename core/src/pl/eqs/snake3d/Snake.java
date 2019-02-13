package pl.eqs.snake3d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Snake {

	private List<Model> model = new ArrayList<Model>();
	private List<ModelInstance> modelInstance = new ArrayList<ModelInstance>();

	private Vector3 vecF;
	private Vector3 vecT;

	public Snake(Vector3 vecF, Vector3 vecT, int length) {
		super();
		this.vecF = vecF;
		this.vecT = vecT;

		ModelBuilder modelBuilder = new ModelBuilder();
		for(int i = 0; i < length; i++) {
			model.add(modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal));
			modelInstance.add(new ModelInstance(model.get(i)));
		}
	}

	public void update() {
		for(int i = modelInstance.size() - 1; i>0; i--) {
			modelInstance.get(i).transform.setTranslation(modelInstance.get(i-1).transform.getTranslation(new Vector3()));
		}
		modelInstance.get(0).transform.setTranslation(modelInstance.get(0).transform.getTranslation(new Vector3()).add(vecF) );
	}
	
	public void growUp() {
		ModelBuilder modelBuilder = new ModelBuilder();
		for(int i = 0; i<5; i++) {
			model.add(modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal));
	        modelInstance.add(new ModelInstance(model.get(model.size()-1)));
	        modelInstance.get(modelInstance.size()-1).transform.setTranslation( modelInstance.get(modelInstance.size()-2).transform.getTranslation(new Vector3()) );
		}
	}
	
	public void removeTail(int i) {
		if(modelInstance.size() >= i) {
			modelInstance.subList(i, modelInstance.size()).clear();			
		}
	}
	
	public void turnUp() {
		Vector3 of = new Vector3(vecF);
		vecF.set(new Vector3(vecT));
		vecT.set(of.scl(-1f));
	}
	
	public void turnDown() {
		Vector3 of = new Vector3(vecF);
		vecF.set(new Vector3(vecT).scl(-1f));
		vecT.set(of);
	}
	
	public void turnLeft() {
		Vector3 left = MyMath.vectorProduct(getVecT(), getVecF()).scl(.2f);
		vecF.set( left );
	}	
	
	public void turnRight() {
		Vector3 right = MyMath.vectorProduct(getVecF(), getVecT()).scl(.2f);
		System.out.print(right);
		vecF.set( right );
	}	

	public List<Model> getModel() {
		return model;
	}

	public List<ModelInstance> getModelInstance() {
		return modelInstance;
	}

	public Vector3 getVecF() {
		return new Vector3(vecF);
	}

	public Vector3 getVecT() {
		return new Vector3(vecT);
	}



}

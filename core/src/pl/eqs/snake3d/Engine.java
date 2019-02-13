package pl.eqs.snake3d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Engine extends ApplicationAdapter {

	private ModelBatch modelBatch;
    private Environment environment;  
    
    private PerspectiveCamera cam;
    private Vector3 camPosition;
    private CameraController camCtrl;
    
    private Wall[] wallProp;
    private List<Model> wall = new ArrayList<Model>();
    private List<ModelInstance> wallInstance = new ArrayList<ModelInstance>();
    
    private Snake snake;
    private Apple apple;
    
    private Long gameT; 
    private int gameC;
	
	@Override
	public void create () {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .5f, .5f, .5f, 1f));
        environment.add(new PointLight().set(1f, 1f, 1f, 0, 0, 0, 1000f));
        
        modelBatch = new ModelBatch();
        
        wallProp = new Wall[] {
        		new Wall(new Vector3(0, 0, -30f), new Vector3(45, 45, 15)),
        		new Wall(new Vector3(0, 0, 30f), new Vector3(45, 45, 15)),
        		new Wall(new Vector3(-30f, 0, 0), new Vector3(15, 45, 45)),
        		new Wall(new Vector3(30f, 0, 0), new Vector3(15f, 45f, 45f)),
        		new Wall(new Vector3(0, -30f, 0), new Vector3(45, 15, 45)),
        		new Wall(new Vector3(0, 30f, 0), new Vector3(45, 15, 45))
        };
        
        ModelBuilder modelBuilder = new ModelBuilder();
        for(Wall w : wallProp) {
        	wall.add(modelBuilder.createBox(w.getDimension().x, w.getDimension().y, w.getDimension().z,
        			new Material(TextureAttribute.createDiffuse(new Texture("room.png"))), Usage.Position | Usage.Normal | Usage.TextureCoordinates));
        	wallInstance.add(new ModelInstance(wall.get(wall.size() - 1), w.getPosition()));
        }
        
        snake = new Snake(new Vector3(5f, 0, 0), new Vector3(0,5f,0), 10);
        apple = new Apple();
	            
        camPosition = snake.getModelInstance().get(0).transform.getTranslation(new Vector3());
        
        cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.far = 150f;
        cam.position.set( camPosition );
		cam.lookAt( new Vector3( cam.position ).add( snake.getVecF() ));
		cam.update();	
		
		gameT = System.currentTimeMillis();
	}

	@Override
	public void render () {		
		this.update();		
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		modelBatch.begin(cam);
		for(ModelInstance w : wallInstance) {
        	modelBatch.render(w, environment);     
        }
		for(int i=1; i<snake.getModelInstance().size(); i++) {
            modelBatch.render(snake.getModelInstance().get(i), environment);        	
        }
		modelBatch.render(apple.getModelInstance(), environment);
        modelBatch.end();
	}
	
	public void update() {
		if(System.currentTimeMillis() - gameT > 30) {
			gameC++;
			
			if(camCtrl != null) {
				camPosition.set( snake.getModelInstance().get(0).transform.getTranslation( new Vector3() ));
				cam.position.set( camPosition );
				camCtrl.update();
				if(camCtrl.getCounter() >= camCtrl.getStep()) {
					camCtrl = null;
					gameC = 0;
				}
			} else {
				camPosition.add(new Vector3( snake.getVecF() ).scl(.1f));			
				cam.position.set(camPosition);
				cam.lookAt(new Vector3( cam.position ).add( snake.getVecF() ));
				cam.update();
			}
			
			if(gameC % 10 == 0 && camCtrl == null) {
				if(MyMath.cubeCollision(snake.getModelInstance().get(0).transform.getTranslation(new Vector3()), new Vector3(5f, 5f, 5f), apple.getModelInstance().transform.getTranslation(new Vector3()), new Vector3(5f, 5f, 5f))) {
			    	snake.growUp();
			    	apple = new Apple();
			    }
				
				for(int i = 1; i < snake.getModelInstance().size(); i++) {
					if(MyMath.cubeCollision(snake.getModelInstance().get(0).transform.getTranslation(new Vector3()).add( new Vector3(snake.getVecF()) ), new Vector3(5f, 5f, 5f),
							snake.getModelInstance().get(i).transform.getTranslation(new Vector3()), new Vector3(5f, 5f, 5f))) {
						snake.removeTail(i);
						break;
					}
				}
				
				for(Wall w : wallProp) {
					if(MyMath.cubeCollision(snake.getModelInstance().get(0).transform.getTranslation(new Vector3()).add( new Vector3(snake.getVecF()) ), new Vector3(5f, 5f, 5f),
							w.getPosition(), w.getDimension())) {
						
						boolean goTop = true;
						for(Wall w2 : wallProp) {
							if(MyMath.cubeCollision(snake.getModelInstance().get(0).transform.getTranslation(new Vector3()).add( new Vector3(snake.getVecT()) ), new Vector3(5f, 5f, 5f),
									w2.getPosition(), w2.getDimension())) {
								goTop = false;
								break;
							}
						}
						
						if(goTop) {
							Vector3 axis = MyMath.vectorProduct(snake.getVecT(), snake.getVecF());
							camCtrl = new CameraController(cam, axis, -90f, 10);
							snake.turnUp();							
						} else {
							Vector3 axis = MyMath.vectorProduct(snake.getVecT(), snake.getVecF());
							camCtrl = new CameraController(cam, axis, 90f, 10);
							snake.turnDown();		
						}
						
						break;
					}
				}
				
				snake.update();
				gameC = 0;
			}
			
			gameT = System.currentTimeMillis();
		}
		
		if(camCtrl == null) {
			keyBoardCtrl();
		}
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		for(Model w : wall) {
        	w.dispose();
        }
		for(Model s : snake.getModel()) {
            s.dispose();         	
        } 
		apple.getModel().dispose();
	}
	
	public void keyBoardCtrl() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			Vector3 axis = MyMath.vectorProduct(snake.getVecT(), snake.getVecF());
			camCtrl = new CameraController(cam, axis, -90f, 10);
			snake.turnUp();
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			Vector3 axis = MyMath.vectorProduct(snake.getVecT(), snake.getVecF());
			camCtrl = new CameraController(cam, axis, 90f, 10);
			snake.turnDown();
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			camCtrl = new CameraController(cam, snake.getVecT(), 90f, 10);
			snake.turnLeft();
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			camCtrl = new CameraController(cam, snake.getVecT(), -90f, 10);
			snake.turnRight();
		}
	}
}

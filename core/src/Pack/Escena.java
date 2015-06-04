package Pack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Escena implements Screen {
	OrthographicCamera cam;
	Pajaro red;
	TextureRegion back, sling, sling2;
	World world;
	AnBiLit game;
	
	Box2DDebugRenderer dr = new Box2DDebugRenderer();
	
	Sprite sFloor;
	Body bFloor;
	
	public Escena(AnBiLit game){
		this.game = game;
	}

	@Override
	public void show() {
		
		world = new World(new Vector2(0, -9.8f), true);
		back = new TextureRegion(new Texture("background.png"));
		sling = new TextureRegion(new Texture("slingshot.png"));
		sling2 = new TextureRegion(new Texture("slingshot2.png"));
		red = new Pajaro(world, "red.png");
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		sFloor = new Sprite(new Texture("ground.png"));
		
		//CUERPO ESTATICO (Ground)
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(sFloor.getX() + sFloor.getWidth()/2, sFloor.getY() + sFloor.getHeight()/2);
        bFloor = world.createBody(groundBodyDef);

	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sFloor.getWidth()/2, sFloor.getHeight()/2);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = shape;
	    fixtureDef.density = 1f;
	    bFloor.createFixture(fixtureDef);
	    
        shape.dispose();
	}

	@Override
	public void render(float delta) {
		world.step(1/60f, 6, 2);
		//ACTUALIZAR
		cam.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor((245/255f), (255/255f), (255/255f), 1); //RGB
		
		//MOVER 
        //---------------------------------------------------------------------------------------------------
        mover();
        //DIBUJAR
        //---------------------------------------------------------------------------------------------------
		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
			game.batch.draw(back, 0, 0, back.getRegionWidth(), back.getRegionHeight());
			sFloor.setPosition(0,0);
			sFloor.draw(game.batch);
			game.batch.draw(sling, 150, 75, sling.getRegionWidth(), sling.getRegionHeight());
			red.render(game.batch);
			game.batch.draw(sling2, 150, 75, sling.getRegionWidth(), sling.getRegionHeight());
		game.batch.end();
		
			if(Gdx.input.isKeyJustPressed(Input.Keys.A))
				game.setScreen(game.menu);
			/*
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
				red.body.applyForceToCenter(new Vector2(1000000000, 1000000000), true);	*/
		
		dr.setDrawBodies(true);
		dr.setDrawVelocities(true);
		dr.render(world, cam.combined);
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		//cam.position.set(50 + Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.batch.dispose();
		world.dispose();
		red.dispose();
	}
	

	public void mover(){
        float iX = Gdx.input.getX(), iY = Gdx.input.getY(), 
        		gH = Gdx.graphics.getHeight(), 
        		rbX = red.body.getPosition().x, rbY = red.body.getPosition().y, x = cam.position.x, 
        		dX = Gdx.input.getDeltaX()*10, scrollDx = 0.000006f*x;
        if(Gdx.input.isTouched()){
        	if(red.sprite.getBoundingRectangle().contains(iX + 50, gH - iY)){
        		System.out.println("Red Tocado");
        		//red.create(world);
        		return;
        	}
        	//---Movimiento en 'x'
        	if(cam.position.x+dX >= Gdx.graphics.getWidth()/2 && cam.position.x+dX <= 1024+Gdx.graphics.getWidth()/2){
        		cam.position.set(x+dX, cam.position.y, 0);
        		//---Zoom de cam
        		if(!(dX==0)) dX=(dX>0)? 1:-1;
    			cam.zoom += (dX > 0 && cam.zoom-scrollDx > 0.7)? -scrollDx : (dX < 0 && cam.zoom+scrollDx <= 1 )? scrollDx : 0;
    			
        	}
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
        	cam.position.set(x+10, cam.position.y, 0);
        	if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
        			&& !Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
        		cam.zoom += -scrollDx;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
        	cam.position.set(x-10, cam.position.y, 0);
        	if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
        			&& !Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
             	cam.zoom += scrollDx;
        }
	}
	/*
	public void lanzar(int x1, int y1, int x2, int y2){
		float t=1/Gdx.graphics.getDeltaTime();
		float hip = (float) Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
		float Vi = hip/t;
		float cos = Math.abs(x1-x2)/hip;
		float sen = Math.abs(y1-y2)/hip;
		float posX = Vi*cos*t + x1;
		float posY = -(world.getGravity().y*t*t)/2 + Vi*sen*t + y1;
		float velX = Vi*cos;
		float velY = -(world.getGravity().y*t) + Vi*((Math.abs(x1-x2))/(hip));
	
		red.body.setLinearVelocity(velX, velY);
		red.sprite.setPosition(posX, posY);
	*/

}


class Pajaro{
	Sprite sprite;
	Body body;
	public BodyDef bodyDef;
	Texture textura;
	
	Pajaro(World world, String texture){
		textura = new Texture(texture);
		sprite = new Sprite(textura);
		sprite.setPosition(170, 210);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		create(world);
	}
	public void create(World world) {
		
		if(body != null)
			world.destroyBody(body);
		
		bodyDef = new BodyDef();
	    bodyDef.type = BodyDef.BodyType.DynamicBody;
	    bodyDef.position.set(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
	    body = world.createBody(bodyDef);
	    
	    CircleShape shape = new CircleShape();
	    shape.setRadius(sprite.getHeight()/2);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.density = 1f;
	    fixtureDef.friction = 1f;
	    fixtureDef.restitution = .5f;
		fixtureDef.shape = shape;
	    
		body.setAngularDamping(5);
	    body.createFixture(fixtureDef);
	    
        shape.dispose();
	}
	public void dispose(){
		textura.dispose();
	}
	
	public void render(SpriteBatch sb){
		sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getWidth()/2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		//sb.begin();
			sprite.draw(sb);
		//sb.end();
	}
}

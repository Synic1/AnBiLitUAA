package Pack;
 
import static utiles.Constantes.PPM;
import utiles.Constantes;
import utiles.StaticBody;
import UI.CircleButton;
import UI.Puntos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import entidades.EntityAB;
import entidades.Sling;
import entidades.bloques.Bloque;
import entidades.bloques.Bloques;
import entidades.cerdos.CerdoBase;
import entidades.cerdos.Cerdos.CerdoC;
import entidades.pajaros.Pajaro;
import entidades.pajaros.PajaroAmarillo;
import entidades.pajaros.PajaroBlue;
import entidades.pajaros.PajaroRed;
import entidades.pajaros.PajaroRedGrande;
 
 public class Escena implements Screen, ContactListener {
 	AnBiLit game;
 	OrthographicCamera cam;
 	TextureRegion back;
 	StaticBody ground;
 	CircleButton menu, reset;
 	World world;
 	Sling sling;
 	Pajaro pajaro;
 	Sound level_start;
 	
 	Array<EntityAB> entidades = new Array<EntityAB>();
 	Array<Body> fixturesPorQuitar = new Array<Body>();
 	
 	static float sumaFuerzas;
 	public static int puntos = 0;
 	
 	Puntos puntaje;
 	
 	Box2DDebugRenderer dr = new Box2DDebugRenderer();
 	
 	public Escena(AnBiLit game){
 		this.game = game;
 	}
 
 	@Override
 	public void show() {
 		world = new World(new Vector2(0, -9.8f), true);
 		back = new TextureRegion(new Texture("background.png"));
 		ground = new StaticBody(world, "Imagenes/Escena/ground00.json", "ground", new Texture("Imagenes/Escena/ground00.png"));
 		puntaje = new Puntos();
 		menu = new CircleButton("Imagenes/Escena/menu.png");
 		reset = new CircleButton("Imagenes/Escena/reset.png");
 		pajaro = new PajaroAmarillo(world);
 		//Nivel Temporal//------------------------------------------------------------------------------
 		System.out.println("\n\n\n\n");
 		
 		entidades.clear();
 		entidades.add(new CerdoC(world, 1280f, 240f));
 		
 		entidades.add(new Bloques.PiedraG(world,1130f, 240f, (short)90));
		entidades.add(new Bloques.MaderaG(world,1130f, 350f, (short)90));
		entidades.add(new Bloques.VidrioG(world,1130f, 450f, (short)90));
		
		entidades.add(new Bloques.PiedraG(world,1220f, 240f, (short)90));
		entidades.add(new Bloques.MaderaG(world,1220f, 350f, (short)90));
		entidades.add(new Bloques.VidrioG(world,1220f, 450f, (short)90));
		
		entidades.add(new Bloques.PiedraG(world,1175f, 295f, (short)0));
		entidades.add(new Bloques.MaderaG(world,1175f, 400f, (short)0));
		entidades.add(new Bloques.VidrioG(world,1175f, 500f, (short)0));
 		//Nivel Temporal//------------------------------------------------------------------------------
 		
 		Constantes.seguirPajaro = false;
 		cam = new OrthographicCamera(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);
 		sling = new Sling("slingshot.png", "slingshot2.png", pajaro);
 	    
 	    dr.setDrawBodies(true);
 		dr.setDrawVelocities(true);
 		level_start = Gdx.audio.newSound(Gdx.files.internal("Audio/level_start.mp3"));
 		level_start.play();
 		puntos=0;
        world.setContactListener(this);
 	}
 	@Override
 	public void render(float delta) {
 		//MECANICA DE OPCIONES 
        //---------------------------------------------------------------------------------------------------
 		//TODO: esto debe ser un switch, creo..
 		if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
 			Constantes.seguirPajaro=(Constantes.seguirPajaro)?false:true;
 		if(Gdx.input.isKeyJustPressed(Input.Keys.F4))
 			Constantes.Configuracion.debugRender=(Constantes.Configuracion.debugRender)?false:true;
 		if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
 			cam.zoom+=cam.zoom*0.5f;
 		if(Gdx.input.isKeyJustPressed(Input.Keys.PLUS))
 			cam.zoom-=cam.zoom*0.1f;
 		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
 			cam.position.y-=0.5f;
 		if(Gdx.input.isKeyPressed(Input.Keys.UP))
 			cam.position.y+=0.5f;
 		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
 			cam.position.x-=0.5f;
 		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
 			cam.position.x+=0.5f;
 		//---------------------------------------------------------------------------------------------------
 		world.step(1/60f, 6, 2);
 		//ACTUALIZAR
 		camUpdate();
 		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 		Gdx.gl.glClearColor((245/255f), (255/255f), (255/255f), 1); //RGB
 		puntaje.actualizar(puntos);//TODO ALTIRO CON ESE 10
 		//MECANICA DE JUEGO// 
         //---------------------------------------------------------------------------------------------------
 		click();//click inicial principalmente. para poder lanzar
         mover();//Scroll en el juego
         if(Constantes.click && !(sling.estirando))//si click y no apuntando para lanzar
         	pajaro.comportamiento();
         removerRotos();//quitar entidades 'muertas'
         if(terminoNivel()){
        	 System.out.println("Nivel se acabo!!!!!!!!!!!!!!!!!!! "+"Linea 53 en escena +-");
        	 game.setScreen(game.niveles);
         }
         if(Gdx.input.isTouched()){
 			 Gdx.input.setCursorImage(new Pixmap(Gdx.files.internal("Imagenes/cursor0.png")), 0, 0);
        	 if(menu.selectedPPM(cam, 10, (Gdx.graphics.getHeight()+2000)/PPM-10, 64/PPM, 64/PPM))
        		 game.setScreen(game.niveles);
        	 if(reset.selectedPPM(cam, 74, (Gdx.graphics.getHeight()+2000)/PPM-10, 64/PPM, 64/PPM))
        		 game.setScreen(this);
         }else{
 			 Gdx.input.setCursorImage(new Pixmap(Gdx.files.internal("Imagenes/cursor1.png")), 0, 0);
         }
         //DIBUJAR//
         //---------------------------------------------------------------------------------------------------
 		game.batch.setProjectionMatrix(cam.combined);
 		game.batch.begin();
 			//fondo
 			game.batch.draw(back, 0, -170f/PPM, back.getRegionWidth()/PPM, back.getRegionHeight()/PPM);
 			//P�jaro
 			if(pajaro.lanzado) 
 				pajaro.render(game.batch);
 			sling.render(game.batch); 									//render el p�jaro (a veces), entre el sling y ligas.
 			for(EntityAB entidad: entidades)							//render elementos en el nivel
 				entidad.render(game.batch);
 			ground.draw(game.batch);
 			menu.render(game.batch, 10, (Gdx.graphics.getHeight()+2000)/PPM-10, 64/PPM, 64/PPM, cam);
 			reset.render(game.batch, 74, (Gdx.graphics.getHeight()+2000)/PPM-10, 64/PPM, 64/PPM, cam);
 			puntaje.render(game.batch, 50/PPM, (Gdx.graphics.getHeight()+2048)/PPM, cam); 					//puntaje
 		game.batch.end();
 		if(Constantes.Configuracion.debugRender)
 			dr.render(world, cam.combined);
 		//---------------------------------------------------------------------------------------------------
 	}
 	//METODOS T.EJECUCION
 	//------------------------------------------------------------------------------------------------------------------
 	@Override
 	public void resize(int width, int height) {
 		cam.setToOrtho(false, width/PPM, height/PPM);
 	}
 	@Override
 	public void pause() {}
 	@Override
 	public void resume() {}
 	@Override
 	public void hide() {}
 	@Override
 	public void dispose() {
 		game.batch.dispose();
 		world.dispose();
 		pajaro.dispose();
 		ground.dispose();
 	 	menu.dispose();
 	 	reset.dispose();
 	 	for(EntityAB entidad: entidades)
 	 		entidad.dispose();
 	}
 	//MECANICA GAMEPLAY
 	//------------------------------------------------------------------------------------------------------------------
 	private void click() {//Entrada usuario
 		if(!Constantes.click)
 			Constantes.vecClickInicial = new Vector2(cam.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).x, cam.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).y);	
 			
 		if(sling.estirar(cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x, cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y, cam))
 			return;
 		
 		//System.out.println(Constantes.vecClickInicial.toString());
 		Constantes.click = Gdx.input.isTouched();
 	}	
 	private void mover() {//Scroll de pantalla
 		if(!Gdx.input.isTouched())
 			return;
         float iX = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x,
         	  gW = Gdx.graphics.getWidth(), x = cam.position.x, 
         	  dX = Gdx.input.getDeltaX(), scrollDx = 0.000003f*x;
         
     	//---Movimiento en 'x' de la c�mara
     	if(dX!=0) dX=(dX>0)? -10 : 10;
     	if(x+dX/PPM > (gW/2)/PPM && x+dX < (back.getRegionWidth()-gW/2)/PPM && !pajaro.tocado) {
     		if(iX > (150+sling.getTextura().getWidth())/PPM){//Despu�s de la resortera
     			Constantes.seguirPajaro = false;
     			if(pajaro.lanzado && pajaro.comportamientoRealizado)
     				Constantes.seguirPajaro = false;
     			cam.position.x += dX/PPM;
 				cam.zoom += (dX > 0 && cam.zoom-scrollDx > 0.7)? -scrollDx*PPM : (dX < 0 && cam.zoom+scrollDx <= 1 )? scrollDx*PPM : 0;
     		}
     	}
 	}
 	private void camUpdate() {//Actualizar/Refrescar Camera
 		if(Constantes.seguirPajaro){
 			Vector2 pajPos = pajaro.posision();
 			if(pajPos.x > Gdx.graphics.getWidth()/2/PPM && pajPos.x < (2048/PPM)-Gdx.graphics.getWidth()/2/PPM)
 				cam.position.x = pajPos.x;
 			if(pajPos.y > (Gdx.graphics.getHeight())*0.75f/PPM)
 				cam.position.y = pajPos.y - (Gdx.graphics.getHeight()+PPM)*0.25f/PPM;
 		}
 		cam.update();
 	}
 	private void removerRotos(){
 		for(Body b: fixturesPorQuitar){
 			for(EntityAB entidad: entidades){
 				if(entidad.getBody()==b && entidad.vida<0){
 					world.destroyBody(b);
 					fixturesPorQuitar.removeValue(b, true);
 					entidades.removeValue(entidad, true);
 					break;
 				}
 			}
 		}
 	}
 	private boolean terminoNivel(){
 		for(EntityAB entidad: entidades)
 			if(entidad instanceof CerdoBase)
 				return false;//primer cerdo encontrado.. No termino
 		return true;//no cerdo.. nivel terminado
 	}
 	//CONTACT LISTENER
 	//------------------------------------------------------------------------------------------------------------------
 	public void postSolve(Contact contact, ContactImpulse impulse) {
 		Fixture golpeado = contact.getFixtureA(), golpeador = contact.getFixtureB();
 		//--------------------------NO  BORRAR--------------------------
 		//							[version0]creo que esta es mas "simple"
 		
 		if(golpeado.getBody()!=ground.body && golpeador.getBody()!=ground.body)//el piso no extiende de entity; se arroja excepci�n
 		try{
 			if(checarDanio(golpeado, golpeador, impulse)){
 				((Bloque)golpeado.getBody().getUserData()).actualizar();
 				if(((Bloque)golpeado.getBody().getUserData()).daniar((EntityAB)golpeador.getBody().getUserData()))
 					fixturesPorQuitar.add(golpeado.getBody());//se agrega si la vida es cero o menor
 			}
 			if(golpeador.getBody().getUserData() instanceof Pajaro)
 				((Pajaro)golpeador.getBody().getUserData()).bloqueo();//ya no dibujar trayectoria ni comportaminto
 		}catch(Exception e){
 			//e.printStackTrace();
 			//-------------------------cerdos
 			if(golpeador.getBody().getUserData() instanceof CerdoBase){
 				if(((CerdoBase)golpeador.getBody().getUserData()).daniar((EntityAB)golpeador.getBody().getUserData()))
 					fixturesPorQuitar.add(golpeador.getBody());//se agrega si la vida es cero o menor
 				if(checarDanio(golpeado, golpeador, impulse))
 					if(((CerdoBase)golpeador.getBody().getUserData()).daniar((EntityAB)golpeador.getBody().getUserData()))
 						fixturesPorQuitar.add(golpeador.getBody());//se agrega si la vida es cero o menor
 				
 					((CerdoC)golpeador.getBody().getUserData()).daniarme(1);
 					
 					if(golpeado.getBody().getUserData() instanceof Pajaro)
 						((Pajaro)golpeado.getBody().getUserData()).bloqueo();//ya no dibujar trayectoria ni comportaminto
 			}
 			else {
				System.out.println("algo pasa");
			}
 		}
 		
 		/*//otro metodo que no sirve del todo bien
 		 if(!(golpeado.getBody().getUserData() instanceof EntityAB))
 			return;
 		if(((EntityAB)golpeado.getBody().getUserData()).normalMax < sum(impulse.getNormalImpulses()) ||
 				((EntityAB)golpeado.getBody().getUserData()).tangentMax < sum(impulse.getTangentImpulses()))
 			fixturesPorQuitar.add(golpeado.getBody());
 		if(!(golpeador.getBody().getUserData() instanceof EntityAB))
 			return;
 		if(((EntityAB)golpeador.getBody().getUserData()).normalMax < sum(impulse.getNormalImpulses()) ||
 				((EntityAB)golpeador.getBody().getUserData()).tangentMax < sum(impulse.getTangentImpulses()))
 			fixturesPorQuitar.add(golpeador.getBody());
 		 * */
 	}
 	public void beginContact(Contact contact) {}
 	public void endContact(Contact contact) {}
 	public void preSolve(Contact contact, Manifold oldManifold) {}
 	//COMPLEMENTOS A CONTACT LISTENER
 	//------------------------------------------------------------------------------------------------------------------
 	private boolean checarDanio(Fixture golpeado, Fixture golpeador, ContactImpulse impulse){
 		// impacto
 		if(((EntityAB)golpeado.getBody().getUserData()).normalMax < sum(impulse.getNormalImpulses()))
 			return true;
 		// Fricci�n
 		if(((EntityAB)golpeado.getBody().getUserData()).tangentMax < sum(impulse.getTangentImpulses()))
 			return true;
 		return false;
 	}	
 	private static float sum(float[] a){//sumatoria -.-
 		sumaFuerzas = 0;
 		for(float f: a)
 			sumaFuerzas+=f;
 		return sumaFuerzas;
 	}
 	//------------------------------------------------------------------------------------------------------------------
 }
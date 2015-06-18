package entidades;

import static utiles.Constantes.PPM;
import utiles.Constantes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import entidades.pajaros.Pajaro;

public class Sling extends EntityAB{
	
	Texture textureAlt;//segunda parte del sling//frontal (creo)
	private TextureRegion textureLiga;
	
	private int fuerzaElastico;//fuerza de lanzamiento Nota::incremental
	public Vector2 pivote1, pivote2;//donde esta amarrado el elastico: para poseriormente dibujarse
	public float dstMax = 100f/PPM;//distancia de "estiramiento" maximo de la liga
	public boolean estiramiento;
	
	Pajaro pajaro;//pajaro cargado en la resortera
	
	public Sling(World world, String rutaSprite, String rutaSprite2, Pajaro pajaro) {
		super(world, rutaSprite);
		this.pajaro = pajaro;
		textureAlt = new Texture(rutaSprite2);
		textureLiga = new TextureRegion(new Texture(Constantes.Graficas.strNegroPxl));
		pivote1 = new Vector2(-140/PPM, -20/PPM);
		pivote2 = new Vector2(-170/PPM, -17/PPM);
	}

	@Override
	public void render(SpriteBatch sb) {
		//liga derecha//
        dibujarLigaD(pivote1.x, pivote1.y, pajaro.posision().x-320/PPM, pajaro.posision().y-240/PPM, 6.4f/PPM, sb);
		sb.draw(textura, (2048*0.07f)/PPM, 64/PPM, textura.getWidth()/PPM, textura.getHeight()/PPM);//sling
		pajaro.render(sb);
		//liga izquierda
		dibujarLigaI(pivote2.x, pivote2.y, pajaro.posision().x-320/PPM, pajaro.posision().y-240/PPM, 6.4f/PPM, sb);
		sb.draw(textureAlt, (2048*0.07f)/PPM, 64/PPM, textureAlt.getWidth()/PPM, textureAlt.getHeight()/PPM);//sling
	}
	
	public void estirar(float x, float y) {
		
		int xx = (x>208/PPM)?0:(x>80/PPM)?1:0;
		int yy = (y>400/PPM)?0:(y>100/PPM)?1:0;
		
		if(pajaro.tocado && Constantes.click){
			pajaro.mover((xx==1)?x:pajaro.posision().x, (yy==1)?y:pajaro.posision().y);
			return;
		}
		else if(pajaro.tocado)
		System.out.println("Lanzar!!");
		
	}
	
	public void lanzar(){

	}
	
	@Override
	public void dispose() {
		super.dispose();
		textureAlt.dispose();
	}
	
	public void setPajaro(Pajaro pajaro) {
		this.pajaro = pajaro;
	}
	
	public Texture getTextura() {
		return textura;
	}
	
	public Texture getTexturaAlt() {
		return textureAlt;
	}
	
	public int getFuerzaElastico() {
		//TODO rango de estiramiento devuelve diferente valor
		// a mayor distancia mayor fuerza;
		return fuerzaElastico;
	}
	
	private void dibujarLigaD(float x1, float y1, float x2, float y2, float grosor, SpriteBatch sb) {
		if(pajaro.posision().x > ((2048*0.07f)+32)/PPM && pajaro.lanzado)
			return;
		//puntoB - puntoA
	    float dx = (x2-x1);
	    float dy = (y2-y1);
	    float largoLinea = (float)(Math.sqrt(dx*dx + dy*dy)+(pajaro.sprite.getWidth()/2));//distancia entre puntos
	    float anguloRadianes = (float)Math.atan2(dy, dx);//angulo entre puntos
	    
	    sb.draw(textureLiga, 192/PPM, 7, 0, 0, largoLinea, grosor, 1, 1, (float) Math.toDegrees(anguloRadianes));
	    estiramiento = largoLinea < dstMax;
	    //sb.draw(textura, x1, y1, dist, thickness, 0, 0, rad);
	}
	
	private void dibujarLigaI(float x1, float y1, float x2, float y2, float grosor, SpriteBatch sb) {
		if(pajaro.posision().x > ((2048*0.07f)+32)/PPM && pajaro.lanzado)
			return;
		//puntoB - puntoA
	    float dx = (x2-x1);
	    float dy = (y2-y1);
	    float largoLinea = (float)(Math.sqrt(dx*dx + dy*dy)+pajaro.sprite.getWidth()/2);//distancia entre puntos
	    float anguloRadianes = (float)Math.atan2(dy, dx);//angulo entre puntos
	    sb.draw(textureLiga, 160/PPM, 7, 0, 0, largoLinea, grosor, 1, 1, (float) Math.toDegrees(anguloRadianes));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

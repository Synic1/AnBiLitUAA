package entidades;

import static utiles.Constantes.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public abstract class EntityAB {
	
	protected Texture textura;
	protected Sprite sprite;
	protected Body body;
	protected BodyDef bodyDef;
	
	public int vida = 100;
	
	public EntityAB(String rutaSprite) {
		textura = new Texture(rutaSprite);
		sprite = new Sprite(textura);
		sprite.setOrigin((sprite.getWidth()/2)/PPM, (sprite.getHeight()/2)/PPM);
		sprite.setSize(sprite.getWidth()/PPM, sprite.getHeight()/PPM);
	}
	
	public abstract void render(SpriteBatch sb);
	public abstract void actualizar();
	public abstract boolean daniar(Object daniador);
	
	public void dispose() {
		textura.dispose();
	}
	
	public Body getBody() {
		return body;
	}
	
}
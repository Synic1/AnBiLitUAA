package entidades.cerdos;

import utiles.Constantes;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Cerdos{

	public static class CerdoC extends CerdoBase{
		public CerdoC(World world, float x, float y) {
			super(world, Constantes.Graficas.strTexPig);
			body.setTransform(new Vector2(x, y), body.getAngle());
		}
	}
}	

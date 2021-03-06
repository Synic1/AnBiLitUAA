package entidades.pajaros;

import utiles.Constantes;

import com.badlogic.gdx.physics.box2d.World;

public final class PajaroRedGrande extends Pajaro implements ComportamientoPajaro {

	public PajaroRedGrande(World world) {
		super(world, Constantes.Graficas.strTexRedG);
		tipo = "rojoGrande";
		fuerzaLanzamiento *= 3;
	}
	
	@Override
	public boolean comportamiento() {
		if(comportamientoRealizado)
			return false;
		super.comportamiento();
		return comportamientoRealizado;
	}
	
	@Override
	public boolean isComportamientoRealizado() {
		return comportamientoRealizado;
	}
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		
	}
}

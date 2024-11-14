package br.com.erick.antsim.model;

import br.com.erick.antsim.model.Ant.Modes;
import br.com.erick.antsim.utilitaries.Directions;

public class Pheromone extends UniverseObject{

	private final int lifeRange;
	private int life = 0;
	private boolean isColonyRadius = false;
	private boolean ps = false;
	private boolean pb = false;
	
	private Directions arrow;

	public Directions getArrow() {
		return arrow;
	}

	public void refreshLife(Ant a) {
		if(a.getActualMode() == Modes.BACKING_COLONY) {
			ps = true;
		}else {
			pb = true;
		}
		if(pb && ps) {
			life = lifeRange;
			pb = false;
			ps = false;
		}
	}
	
	public int getLife() {
		return life;
	}

	public boolean isColonyRadius() {
		return isColonyRadius;
	}

	public Pheromone(Directions arrow, boolean isColonyRadius, int lifeRange) {
		this.lifeRange = lifeRange;
		life = lifeRange;
		this.arrow = arrow;
		this.isColonyRadius = isColonyRadius;
	}
	
	public int decay() {
		if(!isColonyRadius) {
			life--;
			return life;			
		}
		return 1;
	}
}

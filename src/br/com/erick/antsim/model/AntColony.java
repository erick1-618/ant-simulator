package br.com.erick.antsim.model;

import java.util.Random;

import br.com.erick.antsim.utilitaries.Directions;

public class AntColony extends UniverseObject implements Alive{

	private int lifeRange;
	private boolean initializated = false;
	private int ants;
	private int coolDown = 0;
	private int foodCount = 0;
	
	public AntColony(int maximumAnts, int lifeRange) {
		this.ants = maximumAnts;
		this.lifeRange = lifeRange;
	}

	public synchronized void addFood() {
		this.foodCount++;
		notifyController(this.foodCount);
	}
	
	private void notifyController(int foodCount) {
		this.getField().getSimController().setFoodCounter(foodCount);
	}
	
	private void setColonyRadius() {
		Field f; 
		for(int i = 0; i < Directions.values().length; i++) {
			Directions d = Directions.values()[i];
			f = this.getField().getDeslocatedField(d);
			f.setPheromone(d, true, 1);
		}
	}

	@Override
	public void act() {
		if(initializated) {			
			if(coolDown == 0 && (ants > 0 || ants < 0)) {
				Random r = new Random();
				Directions d = Directions.values()[r.nextInt(8)];
				Field f = this.getField().getDeslocatedField(d);
				if(f.getObj() != null) return;
				f.setObj(new Ant(d, lifeRange));
				coolDown = 20;
				ants--;
			}
			else {
				coolDown--;
			}
		}else {
			setColonyRadius();
			initializated = true;
		}
	}
}

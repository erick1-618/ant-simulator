package br.com.erick.antsim.model;

public class Pheromone extends UniverseObject{
	
	private int level;

	public Pheromone() {
	}

	public int getLevel() {
		return level;
	}
	
	public void addLevel(int level) {
		this.level += level;
	}

	public void decay() {
		if(level > 0) level--;
	}

}

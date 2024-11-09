package br.com.erick.antsim.model;

import br.com.erick.antsim.application.SimulationController;
import br.com.erick.antsim.utilitaries.Directions;

public class Field {
	
	private int[] xyPosition = new int[2];
	private Pheromone pheromone = null;
	private UniverseObject obj = null;
	private SimulationController simController;
	private Directions arrow;
	private boolean isColonyRaius;

	public boolean isColonyRaius() {
		return isColonyRaius;
	}

	public void setColonyRaius(boolean isColonyRaius) {
		this.isColonyRaius = isColonyRaius;
	}

	public Directions getArrow() {
		return arrow;
	}

	public void setArrow(Directions arrow) {
		this.arrow = arrow;
	}

	public SimulationController getSimController() {
		return simController;
	}

	public Pheromone getPheromone() {
		return pheromone;
	}

	public void addPheromone(int level) {		
		if(this.pheromone == null) this.pheromone = new Pheromone();
		this.pheromone.addLevel(level);
	}

	public Field(UniverseObject obj, SimulationController simulationController, int xPosition, int yPosition) {
		this.obj = obj;
		if(obj != null) this.obj.setField(this);
		this.simController = simulationController;
		this.xyPosition[0] = xPosition;
		this.xyPosition[1] = yPosition;
	}
	
	public Field getDeslocatedField(Directions deslocation) {
		try {
			switch (deslocation) {
			case D:
				return simController.getMatrix()[xyPosition[0] + 1][xyPosition[1]];
			case L:
				return simController.getMatrix()[xyPosition[0]][xyPosition[1] - 1];
			case LD:
				return simController.getMatrix()[xyPosition[0] + 1][xyPosition[1] - 1];
			case LU:
				return simController.getMatrix()[xyPosition[0] - 1][xyPosition[1] - 1];
			case R:
				return simController.getMatrix()[xyPosition[0]][xyPosition[1] + 1];
			case RD:
				return simController.getMatrix()[xyPosition[0] + 1][xyPosition[1] + 1];
			case RU:
				return simController.getMatrix()[xyPosition[0] - 1][xyPosition[1] + 1];
			case U:
				return simController.getMatrix()[xyPosition[0] - 1][xyPosition[1]];
			default:
				return null;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public UniverseObject getObj() {
		return obj;
	}

	public void setObj(UniverseObject obj) {
		this.obj = obj;
		if(obj != null) obj.setField(this);
	}

	public boolean isTransient() {
		return pheromone != null || obj instanceof Alive;
	}
	
	public String toString() {
		if(obj != null) {
			if(obj instanceof Ant) {
				return "A";
			}
			if(obj instanceof AntColony) {
				return "C";
			}
			if(obj instanceof Food) {
				return "F";
			}
		}
		if(pheromone != null) return "" + pheromone.getLevel();
		return "▢";
	}

	public void setPheromoneNull() {
		this.pheromone = null;
	}

	public void transitate() {
		if(pheromone != null) pheromone.decay();
		if(obj != null) ((Alive) obj).act();
	}

	public void resetMovedFlag() {
		if(this.getObj() instanceof Ant) {
			((Ant)this.getObj()).resetMovedFlag();
		}
	}
}

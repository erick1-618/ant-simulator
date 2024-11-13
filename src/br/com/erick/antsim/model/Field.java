package br.com.erick.antsim.model;

import java.util.ArrayList;
import java.util.List;

import br.com.erick.antsim.application.SimulationController;
import br.com.erick.antsim.utilitaries.Directions;

public class Field {
	
	private int[] xyPosition = new int[2];
	private Pheromone pheromone = null;
	private UniverseObject obj = null;
	private SimulationController simController;
	
	public Pheromone getPheromone() {
		return this.pheromone;
	}
	
	public void setPheromone(Directions d, boolean cRadius) {
		if(this.pheromone == null) {
			this.pheromone = new Pheromone(d, cRadius);
		}
	}

	public SimulationController getSimController() {
		return simController;
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
		if(obj instanceof Ant && !this.isColonyRadius() && this.pheromone != null) {
			Ant a = (Ant) obj;
			pheromone.refreshLife(a);
		}
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
		if(pheromone != null) return "" + pheromone.getLife();
		return "▢";
	}

	public void transitate() {
		if(pheromone != null) {
			if(pheromone.decay() == 0) this.pheromone = null;
		}
		if(obj != null && obj instanceof Alive) ((Alive) obj).act();
	}

	public void resetMovedFlag() {
		if(this.getObj() instanceof Ant) {
			((Ant)this.getObj()).resetMovedFlag();
		}
	}
	
	public boolean isColonyRadius() {
		return this.getPheromone() != null ? this.getPheromone().isColonyRadius() : false;
	}
	
	public Directions getArrow() {
		return this.pheromone != null ? this.pheromone.getArrow() : null;
	}
	
	public List<Field> getThisPointers() {
		List<Field> fields = new ArrayList<Field>();
		Field deslocated;
		for(int i = 0; i < Directions.values().length; i++) {
			deslocated = this.getDeslocatedField(Directions.values()[i]);
			if(deslocated != null && deslocated.getArrow() != null &&
					deslocated.getDeslocatedField(deslocated.getArrow()) == this) {
				fields.add(deslocated);
			}
		}
		return fields.isEmpty() ? null : fields;
	}
}

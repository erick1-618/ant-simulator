package br.com.erick.antsim.model;

import java.util.List;
import java.util.Random;

import br.com.erick.antsim.utilitaries.Directions;

public class Ant extends UniverseObject implements Alive {

	private int lifeRange;
	private boolean hasMovedFlag;
	private Field previousField = null;

	public static enum Modes {
		SEARCHING_FOOD, BACKING_COLONY
	}

	public Ant(Directions d, int lifeRange) {
		this.lifeRange = lifeRange;
		this.food = null;
		this.actualMode = Modes.SEARCHING_FOOD;
		this.searchDir = Directions.getArc(d);
		this.backDir = Directions.getArc(Directions.getOposite(d));
	}

	private Modes actualMode;
	private Food food;
	private Directions[] searchDir;
	private Directions[] backDir;
	private Directions arrowGot;

	@Override
	public void act() {
		Field f;
		boolean earlyModeChange = false;
		f = getBestField(this.actualMode);
		if (f == null)
			return;

		if (f.getObj() instanceof AntColony) {
			((AntColony) f.getObj()).addFood();
			this.food = null;
			this.actualMode = Modes.SEARCHING_FOOD;
		} else {
			if (f.getObj() instanceof Food) {
				this.actualMode = Modes.BACKING_COLONY;
				earlyModeChange = true;
				this.food = (Food) f.getObj();
				this.getField().setPheromone(arrowGot, false, this.lifeRange);
				f.setObj(null);
			}
			move(f, earlyModeChange);
		}
	}

	private Field getBestField(Modes m) {

		Random r = new Random();
		Field f = null;
		Field deslocated = null;

		for (int i = 0; i < Directions.values().length; i++) {
			deslocated = this.getField().getDeslocatedField(Directions.values()[i]);
			if (deslocated != null) {
				if (deslocated.getObj() instanceof Food && this.actualMode == Modes.SEARCHING_FOOD) {
					arrowGot = Directions.values()[i];
					return deslocated;
				}
				if (deslocated.getObj() instanceof AntColony && this.actualMode == Modes.BACKING_COLONY) {
					return deslocated;
				}
				if (this.actualMode == Modes.BACKING_COLONY && deslocated.isColonyRadius()) {
					f = deslocated;
				}
			}
		}

		if (f != null)
			return f.getObj() == null ? f : null;

		if (this.getField().getArrow() != null) {
			if (this.actualMode == Modes.SEARCHING_FOOD) {
				deslocated = this.getField().getDeslocatedField(this.getField().getArrow());
			} else {
				List<Field> fields = this.getField().getThisPointers();
				if (fields != null) {
					deslocated = fields.get(r.nextInt(fields.size()));
				}
			}
			if (deslocated != null) {
				if (deslocated.getObj() == null
						|| (deslocated.getObj() instanceof AntColony && this.actualMode == Modes.BACKING_COLONY)
						|| (deslocated.getObj() instanceof Food && this.actualMode == Modes.SEARCHING_FOOD)) {
					arrowGot = this.getField().getArrow();
					return deslocated;
				}
			}
		}

		Directions d;
		d = this.actualMode == Modes.SEARCHING_FOOD ? this.searchDir[r.nextInt(3)] : this.backDir[r.nextInt(3)];
		int counter = 3;
		while (counter >= 0) {
			deslocated = this.getField().getDeslocatedField(d);
			if (deslocated == previousField) {
				counter--;
				continue;
			}
			if (deslocated != null && deslocated.getObj() == null) {
				f = deslocated;
				break;
			} else {
				if (this.actualMode == Modes.SEARCHING_FOOD) {
					this.searchDir = Directions.getArc(Directions.rotateRight(d));
					this.backDir = Directions.getArc(Directions.rotateRight(Directions.getOposite(d)));
				} else {
					this.backDir = Directions.getArc(Directions.rotateRight(d));
					this.searchDir = Directions.getArc(Directions.rotateRight(Directions.getOposite(d)));
				}
			}
			d = this.actualMode == Modes.SEARCHING_FOOD ? this.searchDir[r.nextInt(3)] : this.backDir[r.nextInt(3)];
			counter--;
		}
		if (f != null && actualMode == Modes.SEARCHING_FOOD) {
			arrowGot = d;
		}
		return f;
	}

	public Modes getActualMode() {
		return actualMode;
	}

	public void move(Field f, boolean isEarlyModeChange) {
		if (hasMovedFlag)
			return;
		hasMovedFlag = true;
		this.previousField = this.getField();
		this.getField().setObj(null);
		if ((actualMode == Modes.SEARCHING_FOOD || isEarlyModeChange) && this.getField().getArrow() == null) {
			this.getField().setPheromone(arrowGot, false, this.lifeRange);
			arrowGot = null;
		}
		this.setField(f);
		if (isEarlyModeChange)
			this.getField().setPheromone(arrowGot, false, this.lifeRange);
		f.setObj(this);
	}

	public void resetMovedFlag() {
		this.hasMovedFlag = false;
	}
}

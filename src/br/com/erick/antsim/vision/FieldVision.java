package br.com.erick.antsim.vision;

import java.awt.Color;

import javax.swing.JPanel;

import br.com.erick.antsim.model.Ant;
import br.com.erick.antsim.model.AntColony;
import br.com.erick.antsim.model.Field;
import br.com.erick.antsim.model.Food;

public class FieldVision extends JPanel {

	private static final Color ANT_COLOR = Color.BLACK;
	private static final Color ANT_COLONY_COLOR = Color.BLUE;
	private static final Color FOOD_COLOR = Color.GREEN;
	private static final Color FOOD_FOUND_COLOR = Color.RED;
	private static final Color COLONY_RADIUS_COLOR = Color.PINK;

	public FieldVision(Field f) {
		this.field = f;
		this.refreshColor();
	}

	private Field field;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void refreshColor() {
		if (this.field.getObj() != null) {
			if (this.field.getObj() instanceof Ant) {
				setBackground(ANT_COLOR);
			} else if (this.field.getObj() instanceof AntColony) {
				setBackground(ANT_COLONY_COLOR);
			} else if (this.field.getObj() instanceof Food) {
				setBackground(FOOD_COLOR);
			}
		} else if (this.field.getPheromone() != null && this.field.getPheromone().getLevel() > 0) {
			setBackground(FOOD_FOUND_COLOR);
		} else if (this.field.getArrow() != null) {
			setBackground(COLONY_RADIUS_COLOR);
		} else {			
			this.setBackground(Color.WHITE);
		}
	}
}

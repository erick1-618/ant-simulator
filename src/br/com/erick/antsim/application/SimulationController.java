package br.com.erick.antsim.application;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.erick.antsim.model.Ant;
import br.com.erick.antsim.model.AntColony;
import br.com.erick.antsim.model.Field;
import br.com.erick.antsim.model.Food;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory.WorldLayouts;
import br.com.erick.antsim.vision.Window;

public class SimulationController {
	
	private Window window;
	private Field[][] matrix;
	private Thread exeSimulationThread;
	private Set<Field> fieldsSet = new HashSet<>();
	private boolean isActive;
	private int foodCounter;
	private int worldFood = 0;
	private int speed;
	private int maximumAnts;
	private int lifeRange;
	private long simIterations = 0;

	public boolean isActive() {
		return isActive;
	}
	
	public int getWorldFood() {
		return worldFood;
	}

	public void setFoodCounter(int foodCounter) {
		this.foodCounter = foodCounter;
		if(window != null) window.refreshLabel();
	}

	public int getFoodCounter() {
		return foodCounter;
	}
	
	public Thread getSimulationThread() {
	    return exeSimulationThread;
	}
	
	public int getAntCounter() {
		int c = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				if(matrix[i][j].getObj() instanceof Ant) c++;
			}
		}
		return c;
	}

	public SimulationController(int size, WorldLayouts layout, Window window, int selectedSpeed, int maxAnts, int lifeRange){
		this.lifeRange = lifeRange;
		this.window = window;
		this.speed = selectedSpeed;
		this.maximumAnts =  maxAnts <= 0 ? -1 : maxAnts;
		this.matrix = getFieldMatrix(size, layout);
	}
	
	public void startSimulation() {
		isActive = true;
		exeSimulationThread = new Thread(() -> {
				
			while(true) {
				simIterations++;
				Set<Field> transientFields = fieldsSet.stream().filter(f -> f.isTransient()).collect(Collectors.toSet());
				transientFields.forEach(f -> f.resetMovedFlag());
				transientFields.forEach(f -> f.transitate());
				if(window != null) window.refreshFields();
				try {
					Thread.sleep(this.speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(this.foodCounter ==  worldFood) {
					break;
				}
			}
			this.isActive = false;
		});
		exeSimulationThread.start();
	}
	
	public long getTotalIterations() throws InterruptedException {
		this.exeSimulationThread.join();
		return this.simIterations;
	}

	public Field[][] getMatrix() {
		return matrix;
	}

	private Field[][] getFieldMatrix(int size, WorldLayouts layout) {
		Field[][] fieldMatrix = new Field[size][size];
		char[][] charMatrix = WorldLayoutFactory.getCharMatrix(layout, size);
		Field aux = null;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				switch(charMatrix[i][j]) {
				case 'F': 
					aux = new Field(new Food(), this, i, j);
					worldFood++;
					break;
				case 'G': aux = new Field(new AntColony(maximumAnts, lifeRange), this, i, j); break;
				case '▢': aux = new Field(null, this, i, j); break;
				}
				fieldsSet.add(aux);
				fieldMatrix[i][j] = aux;
			}
		}
		return fieldMatrix;
	}
}

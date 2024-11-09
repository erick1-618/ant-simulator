package br.com.erick.antsim.vision;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.erick.antsim.application.SimulationController;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory.WorldLayouts;

public class Window extends JFrame{

	private JPanel panel = new JPanel();
	private SimulationController simController;
	private FieldVision fields[][];
	private JLabel label;
	
	public Window(int size, WorldLayoutFactory.WorldLayouts layout) {
		this.simController = new SimulationController(size, layout, this);
		this.panel.setLayout(new GridLayout(size, size));
		fields = new FieldVision[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				fields[i][j] = new FieldVision(simController.getMatrix()[i][j]);
				panel.add(fields[i][j]);
			}
		}
		
		label = new JLabel();
		label.setText("Total food: " + simController.getWorldFood() + " Food collected: " + simController.getFoodCounter());
		
		setTitle("Ant Simulator");
		setLayout(new BorderLayout());
		add(label, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		setResizable(false);
		setSize(new Dimension(600, 600));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		simController.startSimulation();
	}
	
	public static void main(String[] args) {
		new Window(100, WorldLayouts.FOUR_CORNERS);
	}
	
	public void refreshFields() {
		for(int i = 0; i < fields.length; i++) {
			for(int j = 0; j < fields.length; j++) {
				fields[i][j].refreshColor();
			}
		}
	}

	public void refreshLabel() {
		label.setText("Total food: " + simController.getWorldFood() + " Food collected: " + simController.getFoodCounter());
	}
}

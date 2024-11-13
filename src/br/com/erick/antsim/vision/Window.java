package br.com.erick.antsim.vision;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.erick.antsim.application.SimulationController;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory;
import br.com.erick.antsim.utilitaries.WorldLayoutFactory.WorldLayouts;

public class Window extends JFrame{

	private JPanel panel = new JPanel();
	private SimulationController simController;
	private FieldVision fields[][];
	private JLabel label;
	
	public Window(int size) {

        // ComboBox para seleção de opções
		String[] aOptions = {"000 [no limit]", "200", "400", "600", "800"};
		String[] pOptions = {"Fast", "Normal", "Slow"};
        String[] wOptions = {"Two Squares", "Up Square", "Down Square", "Four Corners", "Random"};
        JComboBox<String> pausesSelectionBox = new JComboBox<>(pOptions);
        JComboBox<String> worldsSelectionBox = new JComboBox<>(wOptions);
        JComboBox<String> antsSelectionBox = new JComboBox<>(aOptions);

        // Painel para adicionar os componentes
        JPanel optionPanel = new JPanel();
        optionPanel.add(new JLabel("Maximum ants:"));
        optionPanel.add(antsSelectionBox);
//        panel.add(Box.createHorizontalStrut(15)); // Espaço entre os elementos
        optionPanel.add(new JLabel("World Layout:"));
        optionPanel.add(worldsSelectionBox);
        optionPanel.add(new JLabel("Simulation speed:"));
        optionPanel.add(pausesSelectionBox);

        // Exibir o OptionPane com o painel personalizado
        int result = JOptionPane.showConfirmDialog(null, optionPanel, "Escolha um dígito e uma opção", JOptionPane.OK_CANCEL_OPTION);

        // Verificar se o usuário clicou em OK
        if (result == JOptionPane.OK_OPTION) {
            // Obter o valor digitado e a opção selecionada
            int selectedNumber = Integer.parseInt(((String) antsSelectionBox.getSelectedItem()).substring(0, 3)); 
            char selectedWorld = ((String) worldsSelectionBox.getSelectedItem()).charAt(0);
            int selectedSpeed = pausesSelectionBox.getSelectedIndex();
            int sSpeed = 100;
            switch(selectedSpeed) {
            case 0: sSpeed = 0; break;
            case 1: sSpeed = 100; break;
            case 2: sSpeed = 500; break;
            }

            switch(selectedWorld) {
            case 'T': this.simController = new SimulationController(size, WorldLayouts.TWO_SQUARES, this, sSpeed, selectedNumber); break;
            case 'U': this.simController = new SimulationController(size, WorldLayouts.UP_SQUARE, this, sSpeed, selectedNumber); break;
            case 'D': this.simController = new SimulationController(size, WorldLayouts.DOWN_SQUARE, this, sSpeed, selectedNumber); break;
            case 'F': this.simController = new SimulationController(size, WorldLayouts.FOUR_CORNERS, this, sSpeed, selectedNumber); break;
            case 'R': this.simController = new SimulationController(size, WorldLayouts.RANDOM, this, sSpeed, selectedNumber); break;
            }
            
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
	}
	
	public static void main(String[] args) {
		new Window(100);
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

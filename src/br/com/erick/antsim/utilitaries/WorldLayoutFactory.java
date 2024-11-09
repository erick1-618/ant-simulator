package br.com.erick.antsim.utilitaries;

import java.util.Random;

public class WorldLayoutFactory {
	
	public static enum WorldLayouts {
		TWO_SQUARES, UP_SQUARE, DOWN_SQUARE, FOUR_CORNERS, RANDOM
	}
	
	public static char[][] getCharMatrix(WorldLayouts layout, int size) {
		int center = Math.floorDiv(size, 2);
		int ratio = Math.floorDiv(center + 1, 2);
		switch(layout) {
		case FOUR_CORNERS:
			return fourCornersLayout(size, center, ratio);
		case DOWN_SQUARE:
			return downSquareLayout(size, center, ratio);
		case RANDOM:
			return randomLayout(size, center);
		case UP_SQUARE:
			return upSquareLayout(size, center, ratio);
		case TWO_SQUARES:
			return twoSquaresLayout(size, center, ratio);
		}
		return null;
	}
	
	private static char[][] randomLayout(int size, int center){
		char[][] matrix = getStandartCharMatrix(size, center);
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(matrix[i][j] == 'G') continue;
				if(random.nextBoolean()) matrix[i][j] = 'F';
			}
		}
		return matrix;
	}
	
	private static char[][] fourCornersLayout(int size, int center, int ratio){
		char[][] matrix = getStandartCharMatrix(size, center);
		for(int i = 0; i < ratio; i++) {
			for(int j = 0; j < ratio; j++) {				
				matrix[i][j] = 'F';
				matrix[(size - 1) - i][j] = 'F';
				matrix[i][(size - 1) - j] = 'F';
				matrix[(size - 1) -i][(size - 1) - j] = 'F';
			}	
		}
		return matrix;
	}

	private static char[][] twoSquaresLayout(int size, int center, int ratio) {
		char[][] matrix = getStandartCharMatrix(size, center);
		for(int i = 0; i < ratio; i++) {
			for(int j = 0; j < ratio; j++) {
				matrix[i][center + j] = 'F';
				matrix[i][center - j] = 'F';
				matrix[(size - 1) - i][center + j] = 'F';
				matrix[(size - 1) - i][center - j] = 'F';
			}
		}
		return matrix;
	}
	
	private static char[][] upSquareLayout(int size, int center, int ratio) {
		char[][] matrix = getStandartCharMatrix(size, center);
		for(int i = 0; i < ratio; i++) {
			for(int j = 0; j < ratio; j++) {
				matrix[i][center + j] = 'F';
				matrix[i][center - j] = 'F';
			}
		}
		return matrix;
	}
	
	private static char[][] downSquareLayout(int size, int center, int ratio) {
		char[][] matrix = getStandartCharMatrix(size, center);
		for(int i = 0; i < ratio; i++) {
			for(int j = 0; j < ratio; j++) {
				matrix[(size - 1) - i][center + j] = 'F';
				matrix[(size - 1) - i][center - j] = 'F';
			}
		}
		return matrix;
	}
	
	private static char[][] getStandartCharMatrix(int size, int center){
		char[][] matrix = new char[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(i == j && j == center) {
					matrix[i][j] = 'G';
					continue;
				}
				matrix[i][j] = '▢';
			}
		}
		return matrix;
	}
}

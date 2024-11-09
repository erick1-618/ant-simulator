package br.com.erick.antsim.utilitaries;

import java.util.ArrayList;
import java.util.List;

public enum Directions {
	U, D, L, R, RU, RD, LU, LD;
	
	public static Directions rotateRight(Directions d) {
		switch(d) {
		case D:
			return LD;
		case L:
			return LU;
		case LD:
			return L;
		case LU:
			return U;
		case R:
			return RD;
		case RD:
			return D;
		case RU:
			return R;
		case U:
			return RU;
		default:
			return null;
		}
	}
	
	public static Directions rotateLeft(Directions d) {
		switch(d) {
		case D:
			return RD;
		case L:
			return LD;
		case LD:
			return D;
		case LU:
			return L;
		case R:
			return RU;
		case RD:
			return R;
		case RU:
			return U;
		case U:
			return LU;
		default:
			return null;
		}
	}
	
	public static Directions[] getArc(Directions d){
		Directions[] arc = new Directions[3];
		arc[0] = d;
		arc[1] = Directions.rotateLeft(d);
		arc[2] = Directions.rotateRight(d);
		return arc;
	}
	
	public static Directions getOposite(Directions d) {
		for(int i = 0; i < 4; i++) {
			d = rotateRight(d);
		}
		return d;
	}
}

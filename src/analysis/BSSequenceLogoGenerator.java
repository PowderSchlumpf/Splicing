package analysis;

import java.util.ArrayList;

import dto.BSDTOSequenceFragment;


public class BSSequenceLogoGenerator {
	public static final int MODE_FIVE_PRIME = 0;
	public static final int MODE_THREE_PRIME = 1;
	public static final int MODE_FIVE_AND_THREE_PRIME = 2;
	
	public static final int A = 0;
	public static final int C = 1;
	public static final int G = 2;
	public static final int T = 3;
	
	public static int[][][] generateSeqLogo(ArrayList<BSDTOSequenceFragment> fragmentList, int mode, int logoLength, int positionsBefore) {
		int[][][] logos = new int[2][4][logoLength+positionsBefore];
		if (mode == MODE_FIVE_PRIME || mode == MODE_FIVE_AND_THREE_PRIME) {
			logos[0] = getFivePrimeLogo(fragmentList, logoLength, positionsBefore);
		}
		if (mode == MODE_THREE_PRIME || mode == MODE_FIVE_AND_THREE_PRIME) {
			logos[1] = getThreePrimeLogo(fragmentList, logoLength, positionsBefore);
		}
		return logos;
	}
	
	private static int[][] getFivePrimeLogo(ArrayList<BSDTOSequenceFragment> fragmentList, int logoLength, int positionsBefore) {
		int[][] fivePrimeLogo = new int[4][logoLength+positionsBefore];
		for (BSDTOSequenceFragment fragment : fragmentList) {
			for (int i = 0; i < logoLength+positionsBefore; i++) {
				if (i > fragment.getFiveprimeFragment().length()-1) {
					break;
				}
				switch (fragment.getFiveprimeFragment().charAt(i)) {
				case 'A':
					fivePrimeLogo[A][i]++;
					break;
				case 'C':
					fivePrimeLogo[C][i]++;
					break;
				case 'G':
					fivePrimeLogo[G][i]++;
					break;
				case 'T':
					fivePrimeLogo[T][i]++;
					break;
				default:
					break;
				}
			}
		}
		return fivePrimeLogo;
	}
	
	private static int[][] getThreePrimeLogo(ArrayList<BSDTOSequenceFragment> fragmentList, int logoLength, int positionsBefore) {
		int[][] threePrimeLogo = new int[4][logoLength+positionsBefore];
		int actualPos = -1;
		for (BSDTOSequenceFragment fragment : fragmentList) {
			for (int i = (logoLength+positionsBefore)-1; i >= 0; i--) {
				if (i > fragment.getThreeprimeFragment().length()-1) {
					break;
				}
				actualPos = (fragment.getThreeprimeFragment().length()-1)-i;
				switch (fragment.getThreeprimeFragment().charAt(actualPos)) {
				case 'A':
					threePrimeLogo[A][((logoLength+positionsBefore)-1)-i]++;
					break;
				case 'C':
					threePrimeLogo[C][((logoLength+positionsBefore)-1)-i]++;
					break;
				case 'G':
					threePrimeLogo[G][((logoLength+positionsBefore)-1)-i]++;
					break;
				case 'T':
					threePrimeLogo[T][((logoLength+positionsBefore)-1)-i]++;
					break;
				default:
					break;
				}
			}
		}
		return threePrimeLogo;
	}
	
	

}

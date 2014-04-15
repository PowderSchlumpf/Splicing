package execute;

import gui.BSMainWindow;

import java.util.ArrayList;

import javax.swing.JFrame;

import analysis.BSSequenceLogoGenerator;


public class BSMain {
	final static int ARG_SPECIES = 0;
	final static int ARG_GFFPATH = 1;
	final static int ARG_FASTAPATH = 2;
	final static int ARG_RAWLOGO = 3;
	final static int ARG_PERCLOGO = 4;
	final static int ARG_FASTADEBUGPATH = 5;
	final static int ARG_GFFDEBUGPATH = 6;
	final static int ARG_LENGTHOUTPATH = 7;
	final static int ARG_CLUSTEROUTPATH = 8;
	
	final static int positionsBefore = 10;
	final static int logoLength = 10;
	
	public static void main(String[] args) {
		BSMainWindow mainFrame = new BSMainWindow();
		mainFrame.setVisible(true);
		
//		BSController controller = new BSController(args[ARG_SPECIES], true, false);
//		controller.readInput(args[ARG_GFFPATH], null, args[ARG_FASTAPATH], null, positionsBefore, logoLength);
////		controller.analyseLogos(BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME, logoLength, args[ARG_RAWLOGO], args[ARG_PERCLOGO], true);
//		
////		############## S.cerevisiae: ###############
//		if (args[ARG_SPECIES].equals("yeast")) {		
//		
////			controller.analyseClusteredSpliceSitesFivePrime(5, 4, args[ARG_CLUSTEROUTPATH]);
////			ArrayList<Integer> positionsFP = new ArrayList<>();
////			positionsFP.add(-3);
////			positionsFP.add(1);
////			positionsFP.add(2);
////			positionsFP.add(3);
////			positionsFP.add(4);
//////			positionsFP.add(5);
//////			positionsFP.add(6);
////			controller.analyseClusteredSpliceSitesFivePrimeSpecializedPositions(positionsFP, 3, 4, args[ARG_CLUSTEROUTPATH]);
//			ArrayList<Integer> positionsTP = new ArrayList<>();
//			positionsTP.add(-3);
//			positionsTP.add(-9);
////			positionsTP.add(1);
////			positionsTP.add(2);
////			positionsTP.add(3);
////			positionsTP.add(4);
////			positionsTP.add(5);
//			controller.analyseClusteredSpliceSitesThreePrimeSpecializedPositions(positionsTP, 10, 0, args[ARG_CLUSTEROUTPATH]);
////			controller.analyseClusteredSpliceSitesThreePrime(10, 5, args[ARG_CLUSTEROUTPATH]);
////			controller.analyseInputLengths(args[ARG_LENGTHOUTPATH]);
////			controller.analyseLogos(BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME, logoLength, args[ARG_RAWLOGO], args[ARG_PERCLOGO], true);
////			controller.analysePhase();
////			controller.analyseSeqLogoForPhases(args[ARG_GFFPATH], args[ARG_FASTAPATH], logoLength, args[ARG_RAWLOGO]+"_phase", args[ARG_PERCLOGO]+"_phase", true);
//		}
	}
}

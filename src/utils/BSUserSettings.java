package utils;

import java.util.ArrayList;

public class BSUserSettings {
	
	private static String gffPath;
	private static String gffQuery;
	private static String fastaPath;
	private static String outputpath;
	private static int positionsBeforeFivePrimeSS;
	private static int logoLengthFivePrime;
	private static int logoLengthThreePrime;
	private static int positionsAfterThreePrimeSS;
	private static int analyzationType;
	private static int analysisSequenceSpliceSite;
	private static ArrayList<Integer> spliceSitePositionList;
	
	
	public static String getGffPath() {
		return gffPath;
	}
	public static void setGffPath(String gffPath) {
		BSUserSettings.gffPath = gffPath;
	}
	public static String getGffQuery() {
		return gffQuery;
	}
	public static void setGffQuery(String gffQuery) {
		BSUserSettings.gffQuery = gffQuery;
	}
	public static String getFastaPath() {
		return fastaPath;
	}
	public static void setFastaPath(String fastaPath) {
		BSUserSettings.fastaPath = fastaPath;
	}
	public static String getOutputpath() {
		return outputpath;
	}
	public static void setOutputpath(String outputpath) {
		BSUserSettings.outputpath = outputpath;
	}
	public static int getPositionsBeforeFivePrimeSS() {
		return positionsBeforeFivePrimeSS;
	}
	public static void setPositionsBeforeFivePrimeSS(int positionsBeforeFivePrimeSS) {
		BSUserSettings.positionsBeforeFivePrimeSS = positionsBeforeFivePrimeSS;
	}
	public static int getLogoLengthFivePrime() {
		return logoLengthFivePrime;
	}
	public static void setLogoLengthFivePrime(int logoLengthFivePrime) {
		BSUserSettings.logoLengthFivePrime = logoLengthFivePrime;
	}
	public static int getLogoLengthThreePrime() {
		return logoLengthThreePrime;
	}
	public static void setLogoLengthThreePrime(int logoLengthThreePrime) {
		BSUserSettings.logoLengthThreePrime = logoLengthThreePrime;
	}
	public static int getPositionsAfterThreePrimeSS() {
		return positionsAfterThreePrimeSS;
	}
	public static void setPositionsAfterThreePrimeSS(int positionsAfterThreePrimeSS) {
		BSUserSettings.positionsAfterThreePrimeSS = positionsAfterThreePrimeSS;
	}
	public static int getAnalyzationType() {
		return analyzationType;
	}
	public static void setAnalyzationType(int analyzationType) {
		BSUserSettings.analyzationType = analyzationType;
	}
	public static int getAnalysisSequenceSpliceSite() {
		return analysisSequenceSpliceSite;
	}
	public static void setAnalysisSequenceSpliceSite(int analysisSequenceSpliceSite) {
		BSUserSettings.analysisSequenceSpliceSite = analysisSequenceSpliceSite;
	}
	public static ArrayList<Integer> getSpliceSitePositionList() {
		return spliceSitePositionList;
	}
	public static void setSpliceSitePositionList(
			ArrayList<Integer> spliceSitePositionList) {
		BSUserSettings.spliceSitePositionList = spliceSitePositionList;
	}

	
}

package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class BSUserSettings {
	
	private static String gffPath;
	private static String gffQuery;
	private static String[] gffBiotypes;
	private static String fastaPath;
	private static String outputpath;
	private static int positionsBeforeFivePrimeSS;
	private static int logoLengthFivePrime;
	private static int logoLengthThreePrime;
	private static int positionsAfterThreePrimeSS;
	private static int analyzationType;
	private static int analysisSequenceSpliceSite;
	private static ArrayList<Integer> spliceSitePositionList;
	
	private static HashMap<Integer, Integer> transcriptsPerGene;
	private static HashMap<Integer, Integer> intronsPerGene;
	
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
	public static String[] getGffBiotypes() {
		return gffBiotypes;
	}
	public static void setGffBiotypes(String[] gffBiotypes) {
		BSUserSettings.gffBiotypes = gffBiotypes;
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
	public static HashMap<Integer, Integer> getTranscriptsPerGene() {
		return transcriptsPerGene;
	}
	public static void setTranscriptsPerGene(
			HashMap<Integer, Integer> transcriptsPerGene) {
		BSUserSettings.transcriptsPerGene = transcriptsPerGene;
	}
	public static HashMap<Integer, Integer> getIntronsPerGene() {
		return intronsPerGene;
	}
	public static void setIntronsPerGene(HashMap<Integer, Integer> intronsPerGene) {
		BSUserSettings.intronsPerGene = intronsPerGene;
	}

	
}

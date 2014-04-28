package execute;

import inputreading.fasta.BSFastaFileReader;
import inputreading.gff.BSGFFTReader;
import inputreading.gff.utils.BSGFFFields;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import output.BSFileWriter;
import utils.BSUtils;
import analysis.BSIntronAnalysis;
import analysis.BSSequenceLogoGenerator;
import dto.BSDTOGFFEntry;
import dto.BSDTOSequenceFragment;

public class BSController {
	private int species;
	public final int species_yeast = 0;
	public final int species_human = 1;
	
	private HashMap<BSGFFFields, ArrayList<String>> fields;
	private ArrayList<BSDTOGFFEntry> gffEntries;
	private ArrayList<BSDTOSequenceFragment> seqList;
	
	private boolean onlyGenes;
	private boolean all;
	
	private int positionsBefore;
	private int logoLength;
	
	private String pathEnding;
	
	public BSController(String species, boolean onlyGenes, boolean all) {
		if (species != null && species.equals("yeast")) {
			this.species = species_yeast;
		} else {
			this.species = species_human;
		}
		this.onlyGenes = onlyGenes;
		this.all = all;
		fields = setFields();
		pathEnding = (fields.containsKey(BSGFFFields.SEQUENCE) ? "_"+fields.get(BSGFFFields.SEQUENCE) : "_all")+"_onlyGenes="+onlyGenes+".txt";
	}
	
	public void setSpecies(int species) {
		this.species = species;
	}

	private HashMap<BSGFFFields, ArrayList<String>> setFields() {
		HashMap<BSGFFFields, ArrayList<String>> fieldMap = new HashMap<>();
		ArrayList<String> seq = new ArrayList<>();
		if (species == species_yeast) {
			seq.add("not_chrmt");
		} else if (species == species_human) {
			seq.add("1");
			seq.add("2");
			seq.add("3");
			seq.add("4");
			seq.add("5");
			seq.add("6");
			seq.add("7");
			seq.add("8");
			seq.add("9");
			seq.add("10");
			seq.add("11");
			seq.add("12");
			seq.add("13");
			seq.add("14");
			seq.add("15");
			seq.add("16");
			seq.add("17");
			seq.add("18");
			seq.add("19");
			seq.add("20");
			seq.add("21");
			seq.add("22");
			seq.add("X");
			seq.add("Y");
		}
		fieldMap.put(BSGFFFields.SEQUENCE, seq);
		ArrayList<String> feat = new ArrayList<>();
		if (species == species_yeast) {
			feat.add("intron");
			feat.add("five_prime_UTR_intron");	
		} else if (species == species_human) {
			feat.add("exon");
			feat.add("gene");
		}
		fieldMap.put(BSGFFFields.FEATURE, feat);
		return fieldMap;
	}
	
	public boolean readInput(String gffPath, String gffDebugPath, String fastaPath, String fastaDebugPath, int positionsbefore, int logoLength) {
		this.positionsBefore = positionsbefore;
		this.logoLength = logoLength;
		boolean successful = false;
		successful = readGFFInput(gffPath, gffDebugPath);
		successful = successful && readFastaInput(fastaPath, fastaDebugPath, null);
		return successful;
	}
	
	private boolean readGFFInput(String gffPath, String gffDebugPath) {
		boolean successful = false;
		BSGFFTReader gffr = new BSGFFTReader(fields);
		if (species == species_human) {
			gffEntries = gffr.readGFFWithExons(gffPath, onlyGenes, all);
		} else {
			gffEntries = gffr.readGFF(gffPath, onlyGenes, all);
		}
		successful = true;
		
		if (gffDebugPath != null) {
			// write gffEntries to debug:
			FileWriter fw = null;
			try {
				fw = new FileWriter(new File(gffDebugPath+pathEnding));
				for (BSDTOGFFEntry gffEntry : gffEntries) {
					fw.append(gffEntry.toString()+"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return successful;
	}
	
	private boolean readFastaInput(String fastaPath, String fastaDebugPath, ArrayList<BSDTOGFFEntry> gffEntries) {
		boolean successful = false;
		BSFastaFileReader ffr = new BSFastaFileReader();
		seqList = ffr.readFastaFileFragments(fastaPath, (gffEntries == null ? BSUtils.mapGFFEntries(this.gffEntries) : BSUtils.mapGFFEntries(gffEntries)), positionsBefore, logoLength);
		successful = true;
		if (fastaDebugPath != null) {
			// write seqList to debug:
			FileWriter fw = null;
			try {
				fw = new FileWriter(new File(fastaDebugPath+pathEnding));
				for (BSDTOSequenceFragment frag : seqList) {
					fw.append(frag.toString()+"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return successful;
	}
	
	public boolean analyseIntronLengths(String outPath) {
		return BSFileWriter.printCountOfIntronLengths(BSIntronAnalysis.getCountOfIntronLengths(gffEntries), outPath+"counts"+pathEnding);
//		return BSFileWriter.printIntronLengths(gffEntries, outPath+pathEnding);
	}

	public void analyseLogos(int logoMode, int logolength, String outRawLogoPath, String outPercLogoPath, boolean consoleLog) {
		this.logoLength = logolength;
		int[][][] logos = BSSequenceLogoGenerator.generateSeqLogo(seqList, BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME, logoLength, positionsBefore);
		if (BSFileWriter.printSequenceLogoToText(outRawLogoPath+"_fivePrime_"+pathEnding, logos, BSFileWriter.PRINT_FIVE_PRIME, logoLength, positionsBefore)) {
			System.err.println("writing raw count logo successful =)");
		}
		if (BSFileWriter.printSequenceLogoToText(outRawLogoPath+"_threePrime_"+pathEnding, logos, BSFileWriter.PRINT_THREE_PRIME, logoLength, positionsBefore)) {
			System.err.println("writing raw count logo successful =)");
		}
		if (logos != null && consoleLog) {
			for (int logo = 0; logo < 2; logo++) {
				int[][] xpLogo = logos[logo];
				if (xpLogo != null) {
					System.out.println((logo == 0 ? "Five" : "Three") + "PrimeLogo\t(A=0; T=1; G=2; C=3)");
					if (logo == 0) {
						System.out.print("Pos:");
						for (int i = positionsBefore; i > 0; i--) {
							System.out.print("\t-"+i);
						}
						for (int i = 1; i <= logoLength; i++) {
							System.out.print("\t"+i);
						}
						System.out.print("\n");
					} else {
						System.out.print("Pos:");
						for (int i = logoLength+positionsBefore-1; i >= 0; i--) {
							System.out.print("\tl-"+i);
						}
						System.out.print("\n");
					}
					for (int i = 0; i < xpLogo.length; i++) {
						System.out.print(i + ":");
						for (int j = 0; j < xpLogo[i].length; j++) {
							System.out.print("\t"+xpLogo[i][j]);
						}
						System.out.print("\n");
					}
				} else {
					System.err.println("Something went wrong by generating " + (logo == 0 ? "five" : "three") + " prime logo :(");
				}
				System.out.print("\n");
			}
		}
		
		if (outPercLogoPath != null) {
			double[][][] logosInPercent = BSUtils.getLogosInPercent(logos, seqList.size());
			if (BSFileWriter.printSequenceLogoToText(outPercLogoPath+pathEnding, logosInPercent, logoLength, positionsBefore)) {
				System.err.println("writing percent logo successful =)");
			}
			if (logosInPercent != null && consoleLog) {
				for (int logo = 0; logo < 2; logo++) {
					double[][] xpLogo = logosInPercent[logo];
					if (xpLogo != null) {
						System.out.println((logo == 0 ? "Five" : "Three") + "PrimeLogo\t(A=0; T=1; G=2; C=3)");
						if (logo == 0) {
							System.out.print("Pos:");
							for (int i = positionsBefore; i > 0; i--) {
								System.out.print("\t-"+i);
							}
							for (int i = 1; i <= logoLength; i++) {
								System.out.print("\t"+i);
							}
							System.out.print("\n");
						} else {
							System.out.print("Pos:");
							for (int i = logoLength+positionsBefore-1; i >= 0; i--) {
								System.out.print("\tl-"+i);
							}
							System.out.print("\n");
						}						
						for (int i = 0; i < xpLogo.length; i++) {
							System.out.print(i + ":");
							for (int j = 0; j < xpLogo[i].length; j++) {
								System.out.print("\t"+xpLogo[i][j]);
							}
							System.out.print("\n");
						}
					} else {
						System.err.println("Something went wrong by generating " + (logo == 0 ? "five" : "three") + " prime logo :(");
					}
					System.out.print("\n");
				}
			}
		}
	}

	public void analysePhase() {
		if (gffEntries != null) {
			System.out.println("Frame Statistics:");
			int[] frameStat = BSIntronAnalysis.getFrameStatistics(gffEntries);
			System.out.println("\t0\t1\t2");
			double count = 0;
			for (int i = 0; i < frameStat.length; i++) {
				count += frameStat[i];
				System.out.print("\t"+frameStat[i]);
			}
			System.out.print("\n");
			for (int i = 0; i < frameStat.length; i++) {
				double perc = (int)((frameStat[i]/count)*10000)/100.0;
				System.out.print("\t"+perc);
			}
		}
	}
	
	public void analyseSeqLogoForPhases(String gffPath, String fastaPath, int logolength, String outRawLogoPath, String outPercLogoPath, boolean consoleLog) {
		if (gffEntries == null) {
			readGFFInput(gffPath, null);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<BSDTOGFFEntry> entriesForFrame = BSIntronAnalysis.getGffEntriesForFrame(gffEntries, i);
			readFastaInput(fastaPath, null, entriesForFrame);
			analyseLogos(BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME, logolength, outRawLogoPath+i, outPercLogoPath+i, consoleLog);
		}
	}
	
	public void analyseClusteredSpliceSitesFivePrime(int posBefore, int posAfter, String outPath) {
		HashMap<String, Integer> clusterFivePrime = new HashMap<>();
		ArrayList<String> ssList = new ArrayList<>();
		int startPos = -1;
		int endPos = -1;
		if (posBefore <= positionsBefore) {
			startPos = positionsBefore-posBefore;
		} else {
			startPos = 0;
		}
		if (posAfter <= logoLength) {
			endPos = positionsBefore+posAfter;
		} else {
			endPos = positionsBefore+logoLength;
		}
		for (BSDTOSequenceFragment frag : seqList) {
			String fpss = frag.getFiveprimeFragment().substring(startPos, endPos);
			ssList.add(fpss);
			if (clusterFivePrime.containsKey(fpss)) {
				clusterFivePrime.put(fpss, (clusterFivePrime.get(fpss)+1));
			} else {
				clusterFivePrime.put(fpss, 1);
			}
		}
		BSFileWriter.printSS(ssList, outPath+"_fp_seqs_"+pathEnding, BSFileWriter.PRINT_FIVE_PRIME);
		if (BSFileWriter.printSpliceSiteClustering(clusterFivePrime, outPath+"_fp_"+pathEnding, BSFileWriter.PRINT_FIVE_PRIME)){
			System.err.println("successful printing cluster for five prime");
		} else {
			System.err.println("oops. something went wrong printing cluster for five prime");
		}
	}
	
	public void analyseClusteredSpliceSitesThreePrime(int posBefore, int posAfter, String outPath) {
		HashMap<String, Integer> clusterThreePrime = new HashMap<>();
		int startPos = -1;
		int endPos = -1;
		if (posBefore <= positionsBefore) {
			startPos = positionsBefore-posBefore;
		} else {
			startPos = 0;
		}
		if (posAfter <= logoLength) {
			endPos = positionsBefore+posAfter;
		} else {
			endPos = positionsBefore+logoLength;
		}
		int observedPosition = -1;
		for (BSDTOSequenceFragment frag : seqList) {
			observedPosition = frag.getThreeprimeFragment().length()-logoLength-positionsBefore;
			String tpss = frag.getThreeprimeFragment().substring(observedPosition+startPos, observedPosition+endPos);
			if (clusterThreePrime.containsKey(tpss)) {
				clusterThreePrime.put(tpss, (clusterThreePrime.get(tpss)+1));
			} else {
				clusterThreePrime.put(tpss, 1);
			}
		}
		if (BSFileWriter.printSpliceSiteClustering(clusterThreePrime, outPath+"_tp_"+pathEnding, BSFileWriter.PRINT_THREE_PRIME)) {
			System.err.println("successful printing cluster for three prime");
		} else {
			System.err.println("oops. something went wrong printing cluster for three prime");
		}
	}
	
	public void analyseClusteredSpliceSitesFivePrimeSpecializedPositions(ArrayList<Integer> positions, int posBefore, int posAfter, String outPath) {
		HashMap<String, Integer> clusterFivePrime = new HashMap<>();
		ArrayList<Integer> positionsOfInterest = BSUtils.sortAndPreparePositions(positions, positionsBefore, logoLength);
		ArrayList<String> ssList = new ArrayList<>();
		int startPos = -1;
		int endPos = -1;
		if (posBefore <= positionsBefore) {
			startPos = positionsBefore-posBefore;
		} else {
			startPos = 0;
		}
		if (posAfter <= logoLength) {
			endPos = positionsBefore+posAfter;
		} else {
			endPos = positionsBefore+logoLength;
		}
		for (BSDTOSequenceFragment frag : seqList) {
			StringBuffer sb = new StringBuffer();
			sb.append(frag.getFiveprimeFragment().substring(0, positionsBefore+logoLength));
			for (int i = 0; i < sb.length(); i++) {
				if (!positionsOfInterest.contains(i)) {
					sb.replace(i, i+1, ".");
				}
			}
			String fpss = sb.substring(startPos, endPos);
			ssList.add(fpss);
			if (clusterFivePrime.containsKey(fpss)) {
				clusterFivePrime.put(fpss, (clusterFivePrime.get(fpss)+1));
			} else {
				clusterFivePrime.put(fpss, 1);
			}
		}
		BSFileWriter.printSS(ssList, outPath+"_fp_spec_seqs_"+pathEnding, BSFileWriter.PRINT_FIVE_PRIME);
		if (BSFileWriter.printSpliceSiteClustering(clusterFivePrime, outPath+"_fp_specialized_"+pathEnding, BSFileWriter.PRINT_FIVE_PRIME)){
			System.err.println("successful printing cluster for five prime");
		} else {
			System.err.println("oops. something went wrong printing cluster for five prime");
		}
	}
	
	public void analyseClusteredSpliceSitesThreePrimeSpecializedPositions(ArrayList<Integer> positions, int posBefore, int posAfter, String outPath) {
		HashMap<String, Integer> clusterThreePrime = new HashMap<>();
		ArrayList<Integer> positionsOfInterest = BSUtils.sortAndPreparePositions((positions != null ? positions : new ArrayList<Integer>()), positionsBefore, logoLength);
		ArrayList<String> ssList = new ArrayList<>();
		int startPos = -1;
		int endPos = -1;
		if (posBefore <= positionsBefore) {
			startPos = positionsBefore-posBefore;
		} else {
			startPos = 0;
		}
		if (posAfter <= logoLength) {
			endPos = positionsBefore+posAfter;
		} else {
			endPos = positionsBefore+logoLength;
		}
		int observedPosition = -1;
		for (BSDTOSequenceFragment frag : seqList) {
			observedPosition = frag.getThreeprimeFragment().length()-logoLength-positionsBefore;
			StringBuffer sb = new StringBuffer();
			sb.append(frag.getThreeprimeFragment().substring(observedPosition, frag.getThreeprimeFragment().length()));
			for (int i = 0; i < sb.length(); i++) {
				if (!positionsOfInterest.contains(i)) {
					sb.replace(i, i+1, ".");
				}
			}
			String tpss = sb.substring(startPos, endPos);
			ssList.add(tpss);
			if (clusterThreePrime.containsKey(tpss)) {
				clusterThreePrime.put(tpss, (clusterThreePrime.get(tpss)+1));
			} else {
				clusterThreePrime.put(tpss, 1);
			}
		}
		BSFileWriter.printSS(ssList, outPath+"_tp_spec_seqs_"+pathEnding, BSFileWriter.PRINT_THREE_PRIME);
		if (BSFileWriter.printSpliceSiteClustering(clusterThreePrime, outPath+"_tp_specialized_"+pathEnding, BSFileWriter.PRINT_THREE_PRIME)) {
			System.err.println("successful printing cluster for three prime");
		} else {
			System.err.println("oops. something went wrong printing cluster for three prime");
		}
	}
}

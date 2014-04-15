package analysis;

import java.util.ArrayList;
import java.util.HashMap;

import dto.BSDTOGFFEntry;
import dto.BSDTOGFFEntryIntron;

public class BSIntronAnalysis {
	
	public static int[] getFrameStatistics(ArrayList<BSDTOGFFEntry> gffEntries) {
		int[] frameStatistic = new int[3];
		for (BSDTOGFFEntry intron : gffEntries) {
			if (intron instanceof BSDTOGFFEntryIntron) {
				int geneStart = 0;
				int intronStart = 0;
				int posToRej = 0;
				int framePos = 0;
				if (intron.getStrand().equals("-")) {
					geneStart = ((BSDTOGFFEntryIntron) intron).getGeneEnd();
					intronStart = intron.getEnd();
					posToRej = rejectedIntronPositions(gffEntries, geneStart, intronStart, intron.getSeqName(), ((BSDTOGFFEntryIntron) intron).getParentID(), intron.getStrand());
					framePos = geneStart - intronStart - posToRej;
					frameStatistic[framePos%3]++;
				} else {
					geneStart = ((BSDTOGFFEntryIntron) intron).getGeneStart();
					intronStart = intron.getStart();
					posToRej = rejectedIntronPositions(gffEntries, geneStart, intronStart, intron.getSeqName(), ((BSDTOGFFEntryIntron) intron).getParentID(), intron.getStrand());
					framePos = (intronStart - geneStart - posToRej);
					frameStatistic[framePos%3]++;
				}
			}
		}
		return frameStatistic;
	}
	
	public static ArrayList<BSDTOGFFEntry> getGffEntriesForFrame(ArrayList<BSDTOGFFEntry> gffEntries, int phase) {
		ArrayList<BSDTOGFFEntry> entriesForFrame = new ArrayList<>();
		for (BSDTOGFFEntry intron : gffEntries) {
			if (intron instanceof BSDTOGFFEntryIntron) {
				int geneStart = 0;
				int intronStart = 0;
				int posToRej = 0;
				int framePos = 0;
				if (intron.getStrand().equals("-")) {
					geneStart = ((BSDTOGFFEntryIntron) intron).getGeneEnd();
					intronStart = intron.getEnd();
					posToRej = rejectedIntronPositions(gffEntries, geneStart, intronStart, intron.getSeqName(), ((BSDTOGFFEntryIntron) intron).getParentID(), intron.getStrand());
					framePos = geneStart - intronStart - posToRej;
				} else {
					geneStart = ((BSDTOGFFEntryIntron) intron).getGeneStart();
					intronStart = intron.getStart();
					posToRej = rejectedIntronPositions(gffEntries, geneStart, intronStart, intron.getSeqName(), ((BSDTOGFFEntryIntron) intron).getParentID(), intron.getStrand());
					framePos = (intronStart - geneStart - posToRej);
				}
				if (framePos%3 == phase) {
					entriesForFrame.add(intron);
				}
			}
		}
		return entriesForFrame;
	}
	
	private static int rejectedIntronPositions(ArrayList<BSDTOGFFEntry> gffEntries, int geneStart, int intronStart, String seq, String parentId, String strand) {
		int positionsToReject = 0;
		ArrayList<String> rejectedPositions = new ArrayList<>();
		for (BSDTOGFFEntry intron : gffEntries) {
			if (intron.getSeqName().equals(seq) && ((BSDTOGFFEntryIntron) intron).getParentID().equals(parentId)) {
				if (strand.equals("-") && intron.getStrand().equals(strand)) {
					if (((BSDTOGFFEntryIntron) intron).getGeneEnd() == geneStart && intron.getEnd() > intronStart && intron.getStart() > intronStart) {
						boolean positionAdded = false;
						for (String pos : rejectedPositions) {
							if (intron.getStart() >= Integer.valueOf(pos.split(";")[0]) && intron.getStart() <= Integer.valueOf(pos.split(";")[1])) {
								if (intron.getEnd() >= Integer.valueOf(pos.split(";")[0]) && intron.getEnd() <= Integer.valueOf(pos.split(";")[1])) {
									// nothing to do
								} else {
									pos = pos.split(";")[0]+";"+intron.getEnd();
									positionAdded = true;
									break;
								}
							} else if (intron.getEnd() >= Integer.valueOf(pos.split(";")[0]) && intron.getEnd() <= Integer.valueOf(pos.split(";")[1])) {
								pos = intron.getStart()+";"+pos.split(";")[1];
								positionAdded = true;
								break;
							}
						}
						if(!positionAdded) {
							rejectedPositions.add(intron.getStart()+";"+intron.getEnd());
						}
					}
				} else if (!strand.equals("-") && !intron.getStrand().equals("-")) {
					if (((BSDTOGFFEntryIntron) intron).getGeneStart() == geneStart && intron.getStart() < intronStart && intron.getEnd() < intronStart) {
						boolean positionAdded = false;
						for (String pos : rejectedPositions) {
							positionAdded = false;
							if (intron.getStart() >= Integer.valueOf(pos.split(";")[0]) && intron.getStart() <= Integer.valueOf(pos.split(";")[1])) {
								if (intron.getEnd() >= Integer.valueOf(pos.split(";")[0]) && intron.getEnd() <= Integer.valueOf(pos.split(";")[1])) {
									// nothing to do
								} else {
									pos = pos.split(";")[0]+";"+intron.getEnd();
									positionAdded = true;
									break;
								}
							} else if (intron.getEnd() >= Integer.valueOf(pos.split(";")[0]) && intron.getEnd() <= Integer.valueOf(pos.split(";")[1])) {
								pos = intron.getStart()+";"+pos.split(";")[1];
								positionAdded = true;
								break;
							}
						}
						if(!positionAdded) {
							rejectedPositions.add(intron.getStart()+";"+intron.getEnd());
						}
					}
				}
			}
		}
		for (String pos : rejectedPositions) {
			positionsToReject += (Integer.valueOf(pos.split(";")[1])-1) - Integer.valueOf(pos.split(";")[0]);
		}
		return positionsToReject;
	}
	
	public static HashMap<Integer, Integer> getCountOfIntronLengths(ArrayList<BSDTOGFFEntry> gffEntries) {
		HashMap<Integer, Integer> lengthMap = new HashMap<>();
		for (BSDTOGFFEntry entry : gffEntries) {
			int length = entry.getEnd()-entry.getStart();
			if (lengthMap.containsKey(length)) {
				lengthMap.put(length, (lengthMap.get(length)+1));
			} else {
				lengthMap.put(length, 1);
			}
		}
		return lengthMap;
		
	}
}

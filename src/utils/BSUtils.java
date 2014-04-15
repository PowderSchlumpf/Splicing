package utils;

import java.util.ArrayList;
import java.util.HashMap;

import dto.BSDTOGFFEntry;

public class BSUtils {

	public static HashMap<String, ArrayList<BSDTOGFFEntry>> mapGFFEntries(ArrayList<BSDTOGFFEntry> gffEntries) {
		HashMap<String, ArrayList<BSDTOGFFEntry>> entryMap = new HashMap<>();
		for (BSDTOGFFEntry entry : gffEntries) {
			if (entryMap.containsKey(entry.getSeqName())) {
				entryMap.get(entry.getSeqName()).add(entry);
			} else {
				ArrayList<BSDTOGFFEntry> entrylist = new ArrayList<>();
				entrylist.add(entry);
				entryMap.put(entry.getSeqName(), entrylist);
			}
		}
		return entryMap;
	}
	
	public static String reverseStrandSequence(StringBuffer reverseSeqBuffer, String string) {
		if (reverseSeqBuffer == null && string != null) {
			reverseSeqBuffer = new StringBuffer(string);
		}
		reverseSeqBuffer = reverseSeqBuffer.reverse();
		for (int i = 0; i < reverseSeqBuffer.length(); i++) {
			switch (reverseSeqBuffer.charAt(i)) {
			case 'A':
				reverseSeqBuffer.setCharAt(i, 'T');
				break;
			case 'T':
				reverseSeqBuffer.setCharAt(i, 'A');
				break;
			case 'C':
				reverseSeqBuffer.setCharAt(i, 'G');
				break;
			case 'G':
				reverseSeqBuffer.setCharAt(i, 'C');
				break;
			default:
				break;
			}
		}
		String rev = "";
		try {
			rev = reverseSeqBuffer.toString();
		} catch (Exception e) {
			System.out.println(reverseSeqBuffer);
		}
		return rev;
	}
	
	public static double[][][] getLogosInPercent(int[][][] logos, double seqNumb) {
		int logoLength = 0;
		if (logos[0] != null) {
			logoLength = logos[0][0].length;
		} else {
			logoLength = logos[1][0].length;
		}
		double[][][] logosInPercent = new double[2][4][logoLength];
		for (int type = 0; type < logos.length; type++) {
			if (logos[type] == null) {
				continue;
			}
			for (int nucleotides = 0; nucleotides < logos[type].length; nucleotides++) {
				for (int pos = 0; pos < logos[type][nucleotides].length; pos++) {
					double value = logos[type][nucleotides][pos] / seqNumb * 100;
					value = ((int) (value * 1000.0))/1000.0;
					logosInPercent[type][nucleotides][pos] = value;
				}
			}
		}
		return logosInPercent;
	}

	public static ArrayList<Integer> sortAndPreparePositions(ArrayList<Integer> positions, int posBefore, int logoLength) {
		ArrayList<Integer> sorted = new ArrayList<>();
		for (Integer i : positions) {
			int pos = i;
			if (i < 0) {
				pos = posBefore+i;
			} else {
				pos = posBefore+i-1; 
			}
			if (sorted.size() == 0) {
				sorted.add(pos);
			} else {
				for (int j = 0; j < sorted.size(); j++) {
					if (pos < sorted.get(j)) {
						sorted.add(j, pos);
						break;
					}
				}
				if (!sorted.contains(pos)) {
					sorted.add(pos);
				}
			}
		}
		return sorted;
	}
}

package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dto.BSDTOGFFEntry;

public class BSFileWriter {
	
	public static final int PRINT_FIVE_PRIME = 0;
	public static final int PRINT_THREE_PRIME = 1;
	
	public static boolean printSequenceLogoToText(String path, int[][][] seqLogo, int logoToPrint, int logoLength, int positionsBefore) {
		boolean success = false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			if (seqLogo != null) {
				for (int logo = 0; logo < 2; logo++) {
					if (logo == logoToPrint) {
						int[][] xpLogo = seqLogo[logo];
						if (xpLogo != null) {
							fw.write("#"+(logo == 0 ? "Five" : "Three") + "PrimeLogo raw counts\t(A=0; C=1; G=2; T=3)\n");
							fw.append("#");
							if (logo == 0) {
								for (int i = positionsBefore; i > 0; i--) {
									fw.append("\t-"+i);
								}
								for (int i = 1; i <= logoLength; i++) {
									fw.append("\t"+i);
								}
								fw.append("\n");
							} else {
								System.out.print("Pos:");
								for (int i = logoLength+positionsBefore-1; i >= 0; i--) {
									fw.append("\tl-"+i);
								}
								fw.append("\n");
							}
							for (int i = 0; i < xpLogo.length; i++) {
								fw.append(i + ":");
								for (int j = 0; j < xpLogo[i].length; j++) {
									fw.append("\t"+xpLogo[i][j]);
								}
								fw.append("\n");
							}
						} else {
							fw.write("Something went wrong by generating " + (logo == 0 ? "five" : "three") + " prime logo :(\n");
						}
						fw.append("\n");
					}
				}
			}
			success = true;
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
		return success;
	}
	
	public static boolean printSequenceLogoToText(String path, double[][][] seqLogo, int logoLength, int positionsBefore) {
		boolean success = false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			if (seqLogo != null) {
				for (int logo = 0; logo < 2; logo++) {
					double[][] xpLogo = seqLogo[logo];
					if (xpLogo != null) {
						fw.write((logo == 0 ? "Five" : "Three") + "PrimeLogo raw counts\t(A=0; C=1; G=2; T=3)\n");
						fw.append("Pos:");
						if (logo == 0) {
							for (int i = positionsBefore; i > 0; i--) {
								fw.append("\t-"+i);
							}
							for (int i = 1; i <= logoLength; i++) {
								fw.append("\t"+i);
							}
							fw.append("\n");
						} else {
							System.out.print("Pos:");
							for (int i = logoLength+positionsBefore-1; i >= 0; i--) {
								fw.append("\tl-"+i);
							}
							fw.append("\n");
						}
						for (int i = 0; i < xpLogo.length; i++) {
							fw.append(i + ":");
							for (int j = 0; j < xpLogo[i].length; j++) {
								fw.append("\t"+xpLogo[i][j]);
							}
							fw.append("\n");
						}
					} else {
						fw.write("Something went wrong by generating " + (logo == 0 ? "five" : "three") + " prime logo :(\n");
					}
					fw.append("\n");
				}
			}
			success = true;
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
		return success;
	}
	
	public static boolean printIntronLengths(ArrayList<BSDTOGFFEntry> gffEntries, String path) {
		boolean success = false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			for (int i = 0; i < gffEntries.size(); i++) {
				if (i == 0) {
					fw.append(""+(gffEntries.get(i).getEnd()-gffEntries.get(i).getStart()));
				} else {
					fw.append(";"+(gffEntries.get(i).getEnd()-gffEntries.get(i).getStart()));
				}
				if (i == gffEntries.size()-1) {
					fw.append("\n");
				}
			}
			for (BSDTOGFFEntry intron : gffEntries) {
				fw.append((intron.getEnd()-intron.getStart())+";");
			}
			success = true;
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
		return success;
	}
	
	public static boolean printSpliceSiteClustering(HashMap<String, Integer> cluster, String path, int type) {
		boolean success = false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.append("## "+ (type == PRINT_FIVE_PRIME ? "FivePrime" : "ThreePrime") + " SpliceSite Clustering\n");
			for (String ss : cluster.keySet()) {
				fw.append(ss+"\t"+cluster.get(ss)+"\n");
			}
			success = true;
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
		return success;
	}

	public static boolean printSS(ArrayList<String> ssList, String path, int type) {
		boolean success = false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.append("## "+ (type == PRINT_FIVE_PRIME ? "FivePrime" : "ThreePrime") + " SpliceSite Clustering\n");
			for (String ss : ssList) {
				fw.append(ss+"\n");
			}
			success = true;
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
		return success;
	}

}

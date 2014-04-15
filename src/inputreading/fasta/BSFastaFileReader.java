package inputreading.fasta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.BSUtils;
import dto.BSDTOGFFEntry;
import dto.BSDTOSequenceFragment;
import dto.BSDTOSequenceWhole;

public class BSFastaFileReader {
	
	/**
	 * This method reads a fasta file and stores all the sequences within this file in whole length.
	 * @param path
	 * @return ArrayList of whole sequences
	 */
	public ArrayList<BSDTOSequenceWhole> readFastaFile(String path) {
		ArrayList<BSDTOSequenceWhole> sequences = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String line = "";
			BSDTOSequenceWhole tempSeq = null;
			StringBuffer seqBuilder = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">")) {
					if (tempSeq != null && seqBuilder != null) {
						if (sequences == null) {
							sequences = new ArrayList<>();
						}
						tempSeq.setSequence(seqBuilder.toString());
						sequences.add(tempSeq);
					}
					tempSeq = new BSDTOSequenceWhole(line.substring(1));
					seqBuilder = null;
				} else if (!Character.isLetter(line.charAt(0))) {
					continue;
				} else {
					if (seqBuilder == null) {
						seqBuilder = new StringBuffer(line);
					} else {
						seqBuilder.append(line);
					}
				}
			}
			if (tempSeq != null && seqBuilder != null) {
				if (sequences == null) {
					sequences = new ArrayList<>();
				}
				tempSeq.setSequence(seqBuilder.toString());
				sequences.add(tempSeq);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sequences;
	}
	
	public ArrayList<BSDTOSequenceFragment> readFastaFileFragments(String path, HashMap<String, ArrayList<BSDTOGFFEntry>>  entrySet, 
			int positionsBefore, int logolength) {
		ArrayList<BSDTOSequenceFragment> fragments = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String line = "";
			BSDTOSequenceFragment tempFragment = null;
			BSDTOSequenceWhole tempSequenceWhole = null;
			BSDTOGFFEntry tempEntry = null;
			StringBuffer seqBuilder = null;
			boolean doonthefly = false;
			int positionStartCounter = -1;
			int positionEndCounter = -1;
			boolean contReading = false;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">")) {
					if (!doonthefly && tempSequenceWhole != null && seqBuilder != null) {
						tempSequenceWhole.setSequence(seqBuilder.toString());
						for (String seqName : entrySet.keySet()) {
							if (tempSequenceWhole.getId().equals(seqName)) {
								if (fragments == null) {
									fragments = new ArrayList<>();
								}
								fragments = addAllFragmentsToFragmentList(tempSequenceWhole, entrySet.get(seqName), fragments, positionsBefore, logolength);
								break;
							}
						}
					}
					seqBuilder = null;
					tempSequenceWhole = null;
					for (String seqName : entrySet.keySet()) {
						String currName = line.contains(" ") ? line.split(" ")[0].substring(1) : line.substring(1);
						if (currName.equals(seqName)) {
							if (entrySet.get(seqName).size() > 1) {
								doonthefly = false;
								tempSequenceWhole = new BSDTOSequenceWhole(currName);
							} else {
								doonthefly = true;
								tempFragment = new BSDTOSequenceFragment(currName);
								tempEntry = entrySet.get(seqName).get(0);
							}
							break;
						}
					}
				} else if (!Character.isLetter(line.charAt(0))) {
					continue;
				} else {
					if (doonthefly) {
						if (tempFragment == null) {
							continue;
						} else {
							if (seqBuilder == null) {
								seqBuilder = new StringBuffer();
							}
							positionStartCounter = positionEndCounter+1;
							positionEndCounter += line.length();
							// start reading
							if (positionEndCounter >= tempEntry.getStart() && positionStartCounter <= tempEntry.getStart()) {
								// have to read more than one line
								if (positionEndCounter < tempEntry.getEnd()) {
									seqBuilder.append(line.substring((tempEntry.getStart()) % line.length()));
									contReading = true;
								} else {
									seqBuilder.append(line.subSequence((tempEntry.getStart()) % line.length(), (tempEntry.getEnd()) % line.length()));
									contReading = false;
								}
								
							// continue reading
							} else if (contReading) {
								// have to read more than one line
								if (positionEndCounter < tempEntry.getEnd()) {
									seqBuilder.append(line);
									contReading = true;
								} else {
									seqBuilder.append(line.subSequence(0, (tempEntry.getEnd()) % line.length()));
									contReading = false;
								}
							}
						}
						if (!contReading && seqBuilder.length() > 0) {
							if (tempEntry.getStrand().equals("-")) {
								tempFragment.setSeqFragment(BSUtils.reverseStrandSequence(seqBuilder, null));
							} else {
								tempFragment.setSeqFragment(seqBuilder.toString());
							}
							if (fragments == null) {
								fragments = new ArrayList<>();
							}
							fragments.add(tempFragment);
							tempFragment = null;
							seqBuilder = null;
							positionStartCounter = -1;
							positionEndCounter = -1;
						}
					} else {
						if (seqBuilder == null) {
							seqBuilder = new StringBuffer(line);
						} else {
							seqBuilder.append(line);
						}
					}
				}
			}
			if (!doonthefly && tempSequenceWhole != null && seqBuilder != null) {
				tempSequenceWhole.setSequence(seqBuilder.toString());
				for (String seqName : entrySet.keySet()) {
					if (tempSequenceWhole.getId().equals(seqName)) {
						if (fragments == null) {
							fragments = new ArrayList<>();
						}
						fragments = addAllFragmentsToFragmentList(tempSequenceWhole, entrySet.get(seqName), fragments, positionsBefore, logolength);
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fragments;
	}

	private ArrayList<BSDTOSequenceFragment> addAllFragmentsToFragmentList(BSDTOSequenceWhole tempSequenceWhole, ArrayList<BSDTOGFFEntry> entryList,
			ArrayList<BSDTOSequenceFragment> fragments, int positionsBefore, int logolength) {
		for (BSDTOGFFEntry entry : entryList) {
			BSDTOSequenceFragment fragm = new BSDTOSequenceFragment(tempSequenceWhole.getId());
			if (entry.getStrand().equals("-")) {
				fragm.setFiveprimeFragment(BSUtils.reverseStrandSequence(null, tempSequenceWhole.getSequence().substring((entry.getEnd()-logolength), (entry.getEnd()+positionsBefore))));
				fragm.setThreeprimeFragment(BSUtils.reverseStrandSequence(null, tempSequenceWhole.getSequence().substring((entry.getStart()-logolength), (entry.getStart()+positionsBefore))));
//				fragm.setSeqFragment(BSUtils.reverseStrandSequence(null, (tempSequenceWhole.getSequence().substring((entry.getStart()-positionsBefore), (entry.getEnd()+positionsBefore)))));
			} else {
				fragm.setFiveprimeFragment(tempSequenceWhole.getSequence().substring((entry.getStart()-positionsBefore), (entry.getStart()+logolength)));
				fragm.setThreeprimeFragment(tempSequenceWhole.getSequence().substring((entry.getEnd()-positionsBefore), (entry.getEnd()+logolength)));
//				fragm.setSeqFragment(tempSequenceWhole.getSequence().substring((entry.getStart()-positionsBefore), (entry.getEnd()+positionsBefore)));
			}
			fragments.add(fragm);
		}
		return fragments;
	}
}

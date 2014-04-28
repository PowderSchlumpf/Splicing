package inputreading.gff;

import inputreading.gff.utils.BSGFFConstants;
import inputreading.gff.utils.BSGFFFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dto.BSDTOGFFEntry;
import dto.BSDTOGFFEntryExon;
import dto.BSDTOGFFEntryIntron;

public class BSGFFTReader {
	private HashMap<BSGFFFields, ArrayList<String>> query;
	private HashMap<String, ArrayList<String>> attributes;
	
	public BSGFFTReader(HashMap<BSGFFFields, ArrayList<String>> fields) {
		this.query = fields;
		if (fields != null && fields.containsKey(BSGFFFields.ATTRIBUTE)) {
			setAttributes(fields.get(BSGFFFields.ATTRIBUTE));
		}
	}
	
	public ArrayList<BSDTOGFFEntry> readGFFWithExons(String path, boolean onlyGenes, boolean all) {
		ArrayList<BSDTOGFFEntry> gffList = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String line = "";
			String currentGeneid = "";
			String exonGeneid = "";
			int geneStart = -1;
			int geneEnd = -1;
			HashMap<Integer, BSDTOGFFEntry> exons = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] fields = line.split("\t");
				if (isToSkipLine(fields)) {
					continue;
				}
				if (fields[BSGFFConstants.FEATURE].equals("gene") && fields.length > BSGFFConstants.ATTRIBUTE) {
					if (exons != null && !currentGeneid.equals("")) {
						if (gffList == null) {
							gffList = new ArrayList<>();
						}
						gffList.addAll(getIntronsFromExonList(currentGeneid, geneStart, geneEnd, exons));
					}
					currentGeneid = getGeneId(fields[BSGFFConstants.ATTRIBUTE]);
					geneStart = Integer.valueOf(fields[BSGFFConstants.START]);
					geneEnd = Integer.valueOf(fields[BSGFFConstants.END]);
					exons = new HashMap<>();
				}
				
				else if (fields[BSGFFConstants.FEATURE].equals("exon") && fields.length > BSGFFConstants.ATTRIBUTE) {
					exonGeneid = getGeneId(fields[BSGFFConstants.ATTRIBUTE]);
					if (exonGeneid.equals(currentGeneid)) {
						BSDTOGFFEntry exon = createNewEntry(fields, null, "exon");
						((BSDTOGFFEntryExon) exon).setExonNumber(getExonNumber(fields[BSGFFConstants.ATTRIBUTE]));
						if (exons != null && exons.containsKey(((BSDTOGFFEntryExon) exon).getExonNumber())) {
							if (gffList == null) {
								gffList = new ArrayList<>();
							}
							gffList.addAll(getIntronsFromExonList(currentGeneid, geneStart, geneEnd, exons));
							exons = new HashMap<>();
						}
						exons.put(((BSDTOGFFEntryExon) exon).getExonNumber(), exon);
					}
				}
			}
			if (exons != null && !currentGeneid.equals("")) {
				if (gffList == null) {
					gffList = new ArrayList<>();
				}
				gffList.addAll(getIntronsFromExonList(currentGeneid, geneStart, geneEnd, exons));
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
		return gffList;
	}

	public ArrayList<BSDTOGFFEntry> readGFF(String path, boolean onlyGenes, boolean all) {
		ArrayList<BSDTOGFFEntry> gffEntryList = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String line = "";
			// map of structure: key-ID, value-feature;start;end
			HashMap<String, String> tempParentEntryList = new HashMap<>();
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")) {
					String[] fields = line.split("\t");
					if (fields.length > BSGFFConstants.ATTRIBUTE) {
						if (fields[BSGFFConstants.ATTRIBUTE].contains("ID=")) {
							tempParentEntryList.put(getAttributeId(fields[BSGFFConstants.ATTRIBUTE]), fields[BSGFFConstants.FEATURE]+";"+fields[BSGFFConstants.START]+";"+fields[BSGFFConstants.END]);
						}
					}
					
					if (isToSkipLine(fields)) {
						continue;
					}
					
					if (gffEntryList == null) {
						gffEntryList = new ArrayList<>();
					}
					if (onlyGenes && isGeneIntron(tempParentEntryList, fields)) {
						gffEntryList.add(createNewEntry(fields, tempParentEntryList, 
								query.containsKey(BSGFFFields.FEATURE) && (query.get(BSGFFFields.FEATURE).contains("intron") ||
								query.get(BSGFFFields.FEATURE).contains("five_prime_UTR_intron"))? "intron" : null));
					} else if (!onlyGenes && !isGeneIntron(tempParentEntryList, fields)) {
						gffEntryList.add(createNewEntry(fields, tempParentEntryList, query.containsKey(BSGFFFields.FEATURE) && (query.get(BSGFFFields.FEATURE).contains("intron") ||
								query.get(BSGFFFields.FEATURE).contains("five_prime_UTR_intron"))? "intron" : null));
					} else if (all) {
						gffEntryList.add(createNewEntry(fields, tempParentEntryList, query.containsKey(BSGFFFields.FEATURE) && (query.get(BSGFFFields.FEATURE).contains("intron") ||
								query.get(BSGFFFields.FEATURE).contains("five_prime_UTR_intron"))? "intron" : null));
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
		return gffEntryList;
	}
	
	/**
	 * This method checks an input line to contain query features. 
	 * @param fields - The String array of the input line
	 * @return true if the line has to be skipped, false if the line has to be kept
	 */
	private boolean isToSkipLine(String[] fields) {
		boolean goon = false;
		if (query != null) {
			if (query.containsKey(BSGFFFields.SEQUENCE) && query.get(BSGFFFields.SEQUENCE).size() > 0) {
				for (String seq : query.get(BSGFFFields.SEQUENCE)) {
					if (seq.startsWith("not_")) {
						if (fields[BSGFFConstants.SEQUENCE].equals(seq.substring(4))) {
							// if it's the query sequence which should NOT be kept:
							return true;
						} else {
							goon = true;
						}
					}
					else if (fields[BSGFFConstants.SEQUENCE].equals(seq)) {
						// if it's not the query sequence:
						goon = true;
						break;
					}
				}
				if (!goon) {
					return true;
				}
			}
			
			goon = false;
			if (query.containsKey(BSGFFFields.FEATURE) && query.get(BSGFFFields.FEATURE).size() > 0) {
				for (String feat : query.get(BSGFFFields.FEATURE)) {
					if (fields[BSGFFConstants.FEATURE].equals(feat)) {
						// if it's the query feature:
						goon = true;
						break;
					}
				}
				// if not the query feature:
				if (!goon) {
					return true;
				}
			}
			
			if (query.containsKey(BSGFFFields.ATTRIBUTE) && query.get(BSGFFFields.ATTRIBUTE).size() > 0 && fields.length > 8) {
				if (!containsAttribute(fields[BSGFFConstants.ATTRIBUTE])) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void setAttributes(ArrayList<String> attList) {
		attributes = new HashMap<>();
		for (String att : attList) {
			if (att.contains(";")) {
				String[] splittedAtts = att.split(";");
				for (int i = 0; i < splittedAtts.length; i++) {
					addAttribute(splittedAtts[i].split("=")[0], splittedAtts[i].split("=")[1]);
				}
			} else {
				addAttribute(att.split("=")[0], att.split("=")[1]);
			}
		}
	}
	
	private void addAttribute(String key, String value) {
		if (attributes.containsKey(key)) {
			ArrayList<String> existingValues = attributes.get(key);
			existingValues.add(value);
			attributes.put(key, existingValues);
		} else {
			ArrayList<String> newValues = new ArrayList<>();
			newValues.add(value);
			attributes.put(key, newValues);
		}
	}
	
	private boolean containsAttribute(String attributes) {
		boolean containsAtt = false;
		if (attributes.contains(";")) {
			String[] attr = attributes.split(";");
			for (int i = 0; i < attr.length; i++) {
				String key = attr[i].split("=")[0];
				String value = attr[i].split("=")[1];
				if (this.attributes.containsKey(key)) {
					for (String att : this.attributes.get(key)) {
						if (att.equalsIgnoreCase(value)) {
							containsAtt = true;
							break;
						}
					}
					if (containsAtt) {
						break;
					}
				}
			}
		}
		return containsAtt;
	}
	
	private ArrayList<BSDTOGFFEntry> getIntronsFromExonList(String geneid, int geneStart, int geneEnd, HashMap<Integer, BSDTOGFFEntry> exonList) {
		ArrayList<BSDTOGFFEntry> intronList = new ArrayList<>();
		for (int i = 2; i < exonList.size(); i++) {
			if (exonList.get(i-1).getEnd() > exonList.get(i).getStart()) {
				continue;
			}
			BSDTOGFFEntry intron = calculateIntron(geneid, geneStart, geneEnd, exonList.get(i-1), exonList.get(i));
			if (intron.getStart() >= intron.getEnd()) {
				continue;
			} else {
				intronList.add(intron);
			}
		}
		return intronList;
	}

	private BSDTOGFFEntry calculateIntron(String geneid, int geneStart, int geneEnd, BSDTOGFFEntry exon1, BSDTOGFFEntry exon2) {
		String[] intronFields = new String[BSGFFConstants.ATTRIBUTE+1];
		intronFields[BSGFFConstants.SEQUENCE] = exon1.getSeqName();
		intronFields[BSGFFConstants.FEATURE] = "intron";
		intronFields[BSGFFConstants.START] = String.valueOf(exon1.getEnd()+1);
		intronFields[BSGFFConstants.END] = String.valueOf(exon2.getStart());
		intronFields[BSGFFConstants.STRAND] = exon1.getStrand();
		intronFields[BSGFFConstants.FRAME] = ".";
		intronFields[BSGFFConstants.ATTRIBUTE] = geneid;
		BSDTOGFFEntry intron = createNewEntry(intronFields, null, "intron");
		if (intron instanceof BSDTOGFFEntryIntron) {
			((BSDTOGFFEntryIntron) intron).setGeneStart(geneStart-1);
			((BSDTOGFFEntryIntron) intron).setGeneEnd(geneEnd);
		}
		return intron;
	}

	private String getGeneId(String attributes) {
		String geneid = null;
		if (attributes.contains(";")) {
			geneid = attributes.split(";")[0];
		} else {
			geneid = attributes;
		}
		try {
			geneid = geneid.substring(9, geneid.length()-1);
		} catch (StringIndexOutOfBoundsException e) {
			// bad luck for the user
		}
		return geneid;
	}
	
	private BSDTOGFFEntry createNewEntry(String[] fields, HashMap<String,String> tempParentEntryList, String feature) {
		BSDTOGFFEntry gffEntry = feature != null && feature.equals("intron") ? new BSDTOGFFEntryIntron() : 
			(feature != null && feature.equals("exon") ? new BSDTOGFFEntryExon() : new BSDTOGFFEntry());
		for (int i = 0; i < fields.length; i++) {
			if (i == BSGFFConstants.SEQUENCE) {
				gffEntry.setSeqName(fields[i]);
			} else if (i == BSGFFConstants.FEATURE) {
				gffEntry.setFeature(fields[i]);
			} else if (i == BSGFFConstants.START) {
				gffEntry.setStart(Integer.valueOf(fields[i])-1);
			} else if (i == BSGFFConstants.END) {
				gffEntry.setEnd(Integer.valueOf(fields[i]));
			} else if (i == BSGFFConstants.SCORE) {
				if (fields[i] == null || fields[i].equals(".")) {
					gffEntry.setScore(Double.MIN_VALUE);
				} else {
					gffEntry.setScore(Double.valueOf(fields[i]));
				}
			} else if (i == BSGFFConstants.STRAND) {
				gffEntry.setStrand(fields[i]);
			} else if (i == BSGFFConstants.FRAME) {
				if (fields[i].equals(".")) {
					gffEntry.setFrame(-1);
				} else {
					gffEntry.setFrame(Integer.valueOf(fields[i]));
				}
			} else if (i == BSGFFConstants.ATTRIBUTE) {
				gffEntry.setAttributeID(getAttributeId(fields[i]));
			}
		}
		if (gffEntry instanceof BSDTOGFFEntryIntron) {
			if (fields.length > BSGFFConstants.ATTRIBUTE) {
				((BSDTOGFFEntryIntron) gffEntry).setParentID(getParentId(fields[BSGFFConstants.ATTRIBUTE]));
				String parentIdPrefix = "";
				if (((BSDTOGFFEntryIntron) gffEntry).getParentID().contains("_")) {
					parentIdPrefix = ((BSDTOGFFEntryIntron) gffEntry).getParentID().substring(0, ((BSDTOGFFEntryIntron) gffEntry).getParentID().indexOf("_"));
				}
				if (tempParentEntryList != null) {
					if (tempParentEntryList.containsKey(((BSDTOGFFEntryIntron) gffEntry).getParentID())) {
						String parentStartEnd = tempParentEntryList.get(((BSDTOGFFEntryIntron) gffEntry).getParentID());
						((BSDTOGFFEntryIntron) gffEntry).setGeneStart(Integer.valueOf(parentStartEnd.split(";")[1])-1);
						((BSDTOGFFEntryIntron) gffEntry).setGeneEnd(Integer.valueOf(parentStartEnd.split(";")[2]));
					} else if (tempParentEntryList.containsKey(parentIdPrefix)) {
						String parentStartEnd = tempParentEntryList.get(parentIdPrefix);
						((BSDTOGFFEntryIntron) gffEntry).setGeneStart(Integer.valueOf(parentStartEnd.split(";")[1])-1);
						((BSDTOGFFEntryIntron) gffEntry).setGeneEnd(Integer.valueOf(parentStartEnd.split(";")[2]));
					}
				}
			}
		}
		return gffEntry;
	}
	
	private String getAttributeId(String attributes) {
		String attributeID = null;
		if (attributes.contains(";")) {
			String[] fields = attributes.split(";");
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].startsWith("ID=")) {
					attributeID = fields[i].substring(3);
					break;
				}
			}
		} else {
			if (attributes.startsWith("ID=")) {
				attributeID = attributes.substring(3);
			} else {
				attributeID = attributes;
			}
		}
		return attributeID;
	}
	
	private String getParentId(String attributes) {
		String parentId = null;
		if (attributes.contains(";")) {
			String[] fields = attributes.split(";");
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].startsWith("Parent=")) {
					parentId = fields[i].substring(7);
					break;
				}
			}
		} else {
			if (attributes.startsWith("Parent=")) {
				parentId = attributes.substring(7);
			} else {
				parentId = attributes;
			}
		}
		return parentId;
	}
	
	private int getExonNumber(String attributes) {
		String exonNumber = null;
		if (attributes.contains(";")) {
			String[] fields = attributes.split(";");
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i].trim();
				if (field.startsWith("exon_number")) {
					exonNumber = field.substring(13, field.length()-1);
					break;
				}
			}
		} else {
			if (attributes.startsWith("exon_number")) {
				exonNumber = attributes.substring(13, attributes.length()-1);
			}
		}
		int exonNumb = -1;
		try {
			exonNumb = Integer.valueOf(exonNumber);
		} catch (Exception e) {
			
		}
		return exonNumb;
	}
	
	private boolean isGeneIntron(HashMap<String,String> tempParentEntryList, String[] fields) {
		boolean isGeneIntron = false;
		if (fields[BSGFFConstants.FEATURE].equals("intron") || fields[BSGFFConstants.FEATURE].equals("five_prime_UTR_intron")) {
			if (fields.length > BSGFFConstants.ATTRIBUTE) {
				String parentID = getParentId(fields[BSGFFConstants.ATTRIBUTE]);
				String parentIDPrefix = "";
				if (parentID.contains("_")) {
					parentIDPrefix = parentID.substring(0, parentID.indexOf("_"));
				}
				if (tempParentEntryList.containsKey(parentID)) {
					isGeneIntron = tempParentEntryList.get(parentID).split(";")[0].equals("gene");
				} else if (tempParentEntryList.containsKey(parentIDPrefix)) {
					isGeneIntron = tempParentEntryList.get(parentIDPrefix).split(";")[0].equals("gene");
				}
			}
		}
		return isGeneIntron;
	}

}

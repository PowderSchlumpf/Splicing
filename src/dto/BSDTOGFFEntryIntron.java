package dto;

public class BSDTOGFFEntryIntron extends BSDTOGFFEntry {
	
	private int geneStart;
	private int geneEnd;
	private String parentID;
	
	public int getGeneStart() {
		return geneStart;
	}
	public void setGeneStart(int geneStart) {
		this.geneStart = geneStart;
	}
	public int getGeneEnd() {
		return geneEnd;
	}
	public void setGeneEnd(int geneEnd) {
		this.geneEnd = geneEnd;
	}
	
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public String toString() {
		return "seqName=" + seqName + ";feature=" + feature + ";start=" + start + ";end=" + end + ";score=" + score
				+ ";strand=" + strand + ";frame=" + frame + ";attributeID=" + attributeID + ";parentID=" + parentID + ";geneStart=" + geneStart + ";geneEnd="+ geneEnd;
	}

}

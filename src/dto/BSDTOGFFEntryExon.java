package dto;

public class BSDTOGFFEntryExon extends BSDTOGFFEntry {
	
	private int exonNumber;

	public int getExonNumber() {
		return exonNumber;
	}

	public void setExonNumber(int exonNumber) {
		this.exonNumber = exonNumber;
	}

	public String toString() {
		return "seqName=" + seqName + ";feature=" + feature + ";start=" + start + ";end=" + end + ";score=" + score
				+ ";strand=" + strand + ";frame=" + frame + ";attributeID=" + attributeID + ";exonNumber=" + exonNumber
				+ ";transcriptID=" + transcriptID + ";biotype=" + biotype;
	}
}

package dto;

public class BSDTOGFFEntry {
	
	protected String seqName;
	protected String feature;
	protected int start;
	protected int end;
	protected double score;
	protected String strand;
	protected int frame;
	protected String attributeID;
	protected String transcriptID;
	protected String biotype;
	
	
	public String getSeqName() {
		return seqName;
	}
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}
	public String getAttributeID() {
		return attributeID;
	}
	public void setAttributeID(String attributeID) {
		this.attributeID = attributeID;
	}
	public String getTranscriptID() {
		return transcriptID;
	}
	public void setTranscriptID(String transcriptID) {
		this.transcriptID = transcriptID;
	}
	public String getBiotype() {
		return biotype;
	}
	public void setBiotype(String biotype) {
		this.biotype = biotype;
	}
	public String toString() {
		return "seqName=" + seqName + ";feature=" + feature + ";start=" + start + ";end=" + end + ";score=" + score
				+ ";strand=" + strand + ";frame=" + frame + ";attributeID=" + attributeID + ";transcriptID=" + transcriptID
				+ ";biotype=" + biotype;
	}

}

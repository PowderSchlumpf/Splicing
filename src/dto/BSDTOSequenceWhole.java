package dto;

public class BSDTOSequenceWhole extends BSDTOSequence {

	private String sequence;
	
	public BSDTOSequenceWhole(String idAndDescription) {
		super(idAndDescription);
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public String toString() {
		return "idAndDescription=" + idAndDescription + ";sequence=" + sequence;
	}

}

package dto;

public class BSDTOSequenceFragment extends BSDTOSequence {

	private String seqFragment;
	private String fiveprimeFragment;
	private String threeprimeFragment;
	
	public BSDTOSequenceFragment(String idAndDescription) {
		super(idAndDescription);
	}

//	public String getSeqFragment() {
//		return seqFragment;
//	}

	public void setSeqFragment(String seqFragment) {
		this.seqFragment = seqFragment;
	}

	public String getFiveprimeFragment() {
		return fiveprimeFragment;
	}

	public void setFiveprimeFragment(String fiveprimeFragment) {
		this.fiveprimeFragment = fiveprimeFragment;
	}

	public String getThreeprimeFragment() {
		return threeprimeFragment;
	}

	public void setThreeprimeFragment(String threeprimeFragment) {
		this.threeprimeFragment = threeprimeFragment;
	}
	
	public String toString() {
		return "idAndDescription=" + idAndDescription + ";seqFragment=" + seqFragment + ";fiveprimeFragment=" + fiveprimeFragment + 
				";threeprimeFragment=" + threeprimeFragment;
	}

}

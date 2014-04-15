package dto;

public abstract class BSDTOSequence {
	
	protected String idAndDescription;
	
	public BSDTOSequence(String idAndDescription) {
		this.idAndDescription = idAndDescription;
	}

	public String getId() {
		return idAndDescription;
	}

	public void setId(String id) {
		this.idAndDescription = id;
	}

}

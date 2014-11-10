package fpt.qa.additionalinformation.name_mapper;
/**
 * @author Thien BUI-DUC, hus.ict@gmail.com
 * <p>
 * 3 Sep 2014, 22:42:23
 * <p>
 */
public class NamedEntity {

	private String type;
	private String finalName;

	public NamedEntity() {
	}

	public NamedEntity(String type, String finalName) {
		super();
		this.type = type;
		this.finalName = finalName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFinalName() {
		return finalName;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}



	@Override
	public String toString() {
		return "NamedEntity [type=" + type + ", finalName=" + finalName + "]";
	}

}

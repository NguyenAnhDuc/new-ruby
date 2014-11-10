package fpt.qa.vnTime.vntime;

public class VnTimeObject {
	private Integer id;
	private String temporalExpression;
	private Integer beginCharoffset;
	private Integer endCharOffset;
	private String normalizedExpression;

	public VnTimeObject(Integer id, String temporalExpression,
			Integer beginCharoffset, Integer endCharOffset,
			String normalizedExpression) {
		this.id = id;
		this.temporalExpression = temporalExpression;
		this.beginCharoffset = beginCharoffset;
		this.endCharOffset = endCharOffset;
		this.normalizedExpression = normalizedExpression;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTemporalExpression() {
		return this.temporalExpression;
	}

	public void setTemporalExpression(String temporalExpression) {
		this.temporalExpression = temporalExpression;
	}

	public Integer getBeginCharoffset() {
		return this.beginCharoffset;
	}

	public void setBeginCharoffset(Integer beginCharoffset) {
		this.beginCharoffset = beginCharoffset;
	}

	public Integer getEndCharOffset() {
		return this.endCharOffset;
	}

	public void setEndCharOffset(Integer endCharOffset) {
		this.endCharOffset = endCharOffset;
	}

	public String getNormalizedExpression() {
		return this.normalizedExpression;
	}

	public void setNormalizedExpression(String normalizedExpression) {
		this.normalizedExpression = normalizedExpression;
	}

	public String toString() {
		return this.id + "|" + this.temporalExpression + "|"
				+ this.beginCharoffset + "|" + this.endCharOffset + "|"
				+ this.normalizedExpression;
	}
}

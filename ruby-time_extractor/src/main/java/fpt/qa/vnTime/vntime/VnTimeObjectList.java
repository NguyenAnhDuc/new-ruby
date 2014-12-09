package fpt.qa.vnTime.vntime;

import java.util.ArrayList;

public class VnTimeObjectList extends ArrayList<VnTimeObject> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toString() {
		StringBuilder s = new StringBuilder("");
		for (VnTimeObject su : this) {
			s.append(su.toString()).append("\n");
		}
		return s.toString();
	}
}

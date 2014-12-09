package fpt.qa.genreclassifier;

import fpt.qa.rubyweb.AppController;

public interface IConstands {
	public static final ConjGenre CONJ_TYPE = new ConjGenre((new AppController()).getClass()
			.getClassLoader().getResource("").getPath());
}

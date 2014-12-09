package fpt.qa.typeclassifier;

import fpt.qa.rubyweb.AppController;

public interface IConstands {
	public static final ConjType CONJ_TYPE = new ConjType((new AppController()).getClass()
			.getClassLoader().getResource("").getPath());
}

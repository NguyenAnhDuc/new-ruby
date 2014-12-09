package fpt.qa.langclassifier;


import fpt.qa.rubyweb.AppController;

public interface IConstands {
	public static final ConjLang CONJ_TYPE = new ConjLang((new AppController()).getClass()
			.getClassLoader().getResource("").getPath());
}

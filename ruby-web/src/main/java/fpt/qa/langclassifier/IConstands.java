package fpt.qa.langclassifier;

import com.fpt.ruby.business.helper.RedisHelper;

public interface IConstands {
	public static final ConjLang CONJ_TYPE = new ConjLang((new RedisHelper()).getClass()
			.getClassLoader().getResource("").getPath());
}

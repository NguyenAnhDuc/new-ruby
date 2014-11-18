package fpt.qa.typeclassifier;

import com.fpt.ruby.business.helper.RedisHelper;

public interface IConstands {
	public static final ConjType CONJ_TYPE = new ConjType((new RedisHelper()).getClass()
			.getClassLoader().getResource("").getPath());
}

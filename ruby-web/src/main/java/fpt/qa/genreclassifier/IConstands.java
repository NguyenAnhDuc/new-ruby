package fpt.qa.genreclassifier;

import com.fpt.ruby.business.helper.RedisHelper;

public interface IConstands {
	public static final ConjGenre CONJ_TYPE = new ConjGenre((new RedisHelper()).getClass()
			.getClassLoader().getResource("").getPath());
}

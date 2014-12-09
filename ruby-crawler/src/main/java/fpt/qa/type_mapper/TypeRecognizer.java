package fpt.qa.type_mapper;

import com.fpt.ruby.commons.constants.ProgramType;
import com.fpt.ruby.commons.helper.TypeMapperHelper;

import java.util.HashSet;
import java.util.Set;


public class TypeRecognizer {
	private ProgramType type;
	private Set<String> dedicatedChannels; // all program in this channel
	private Set<String> typeKeywords;

	public void loadConfig(String[] chns, String[] kws) {
		dedicatedChannels = new HashSet<String>();
		typeKeywords = new HashSet<String>();

		for (String chn : chns) {
			dedicatedChannels.add(TypeMapperHelper.normalize(chn));
		}

		for (String kw : kws) {
			typeKeywords.add(TypeMapperHelper.normalize(kw));
		}
	}
	
	public void show() {
		System.out.println("TypeRecognizer = " + type.toString());
		System.out.println("DEDICATED CHANNELS: ");
		for (String str : dedicatedChannels) {
			System.out.print(str + " ");
		}

		System.out.println();
		System.out.println("TYPE KW: ");
		for (String str : typeKeywords) {
			System.out.print(str + " ");
		}

	}

	public ProgramType getType() {
		return type;
	}

	public void SetType(ProgramType type) {
		this.type = type;
	}

	public Set<String> getTypeKeywords() {
		return typeKeywords;
	}

	public Set<String> getDedicatedChannels() {
		return dedicatedChannels;
	}

	public Boolean contains(String channel, String prog) {
		String nCh = TypeMapperHelper.normalize(channel);
		String nProg = TypeMapperHelper.normalize(prog);
		for (String c: dedicatedChannels) {
			if (c.equals(nCh)) {
				return true;
			}
		}
		
		for (String t: typeKeywords) {
			if (t.contains(prog) || prog.contains(t))
				return true;
		}
		return customCode(nCh, nProg);
	}

	protected Boolean customCode(String channel, String prog) {
		return false;
	}
}

package fpt.qa.type_mapper;

import java.util.HashSet;
import java.util.Set;

import com.fpt.ruby.business.constants.ProgramType;

public class TypeRecognizer {
	private ProgramType type;
	private Set<String> dedicatedChannels; // all program in this channel
	private Set<String> typeKeywords;

	public void loadConfig(String[] chns, String[] kws) {
		dedicatedChannels = new HashSet<String>();
		typeKeywords = new HashSet<String>();

		for (String chn : chns) {
			dedicatedChannels.add(TypeMapperUtil.normalize(chn));
		}

		for (String kw : kws) {
			typeKeywords.add(TypeMapperUtil.normalize(kw));
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
		String nCh = TypeMapperUtil.normalize(channel);
		String nProg = TypeMapperUtil.normalize(prog);
		for (String c: dedicatedChannels) {
			if (c.contains(nCh) || nCh.contains(c)) {
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

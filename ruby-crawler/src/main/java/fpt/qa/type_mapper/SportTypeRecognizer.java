package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class SportTypeRecognizer extends TypeRecognizer {
    public SportTypeRecognizer() {
        SetType(ProgramType.SPORT);

        String[] dedicatedChannels = new String[]{"fox sport", "vtvcab3", "vtvcab16", "fox sports"};
        String[] typeKeywords = new String[]{"sport", "bong da", "foot ball",
                "bong chuyen", "soccer", "the thao", "league", "world cup", "seagame",
                "asiad", "laliga", "hang nhat", "cup quoc gia", "dua xe", "cau long",
                "racing", "vdqg", "volleyball", "basketball", "bong chuyen", "bong nem", "bong ro"};

        super.loadConfig(dedicatedChannels, typeKeywords);
    }
}

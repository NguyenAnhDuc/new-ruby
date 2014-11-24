package fpt.qa.type_mapper;
import com.fpt.ruby.business.constants.ProgramType;

public class RacingTypeRecognizer extends TypeRecognizer {
    public RacingTypeRecognizer() {
        SetType(ProgramType.RACING);

        String[] dedicatedChannels = new String[] {};
        String[] typeKeywords = new String[] { "motor racing" , "đua xe", "bike racing", "car racing"};
        super.loadConfig(dedicatedChannels, typeKeywords);
    }
}

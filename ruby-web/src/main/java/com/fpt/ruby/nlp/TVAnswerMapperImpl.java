package com.fpt.ruby.nlp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.Log;
import com.fpt.ruby.business.model.QueryParamater;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.intent.detection.TVIntentDetect;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;

import fpt.qa.mdnlib.util.string.DiacriticConverter;
import fpt.qa.mdnlib.util.string.StrUtil;
import fpt.qa.typeclassifier.ProgramTypeExtractor;

public class TVAnswerMapperImpl implements TVAnswerMapper {
	public static final String DEF_ANS = "Xin lỗi, chúng tôi không có thông tin cho câu trả lời của bạn";
	public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
	private TVIntentDetect intentDetector = new TVIntentDetect();
	private TVIntentDetect nonDiacritic = new TVIntentDetect();
	private TVProgramService tps = new TVProgramService();
	private final int limitSizeAnswer = 10;
	public void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
//		String dir = "D:/Workspace/Code/FTI/rubyweb/src/main/resources";
		
		intentDetector.init( dir + "/qc/tv", dir + "/dicts");
		nonDiacritic.init( dir + "/qc/tv/non-diacritic", dir + "/dicts/non-diacritic");
	}
	
	public RubyAnswer getAnswer ( String question, LogService logService, ConjunctionHelper conjunctionHelper ) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		String tmp = "\t" + question + "\n";
		
		String intent = intentDetector.getIntent( question );
		tmp += "\t" + "TV Intent: " + intent + "\n";
		
		String intent2 = nonDiacritic.getIntent( question );
		tmp += "\t" + "Non-diacritic TV Intent: " + intent2 + "\n";
		
		if (!DiacriticConverter.hasDiacriticAccents(question)){
			intent = intent2;
		}
		
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(intent);
		rubyAnswer.setAnswer(DEF_ANS );
		
		if (intent.equalsIgnoreCase(IntentConstants.TV_UDF)) 
			return rubyAnswer;
		
		TVModifiers mod = TVModifiersHelper.getModifiers( question, conjunctionHelper );
		tmp += "\t" + question.replaceAll( "\\s+", " " ) + "\n";
		tmp += "\t" + mod + "\n";
		
		rubyAnswer.setQuestionType(mod.getChannel());
		
		// get Time condition
		TimeExtract timeExtract = NlpHelper.getTimeCondition( question.replaceAll( "(\\d+)(h)", "$1 giờ" ) );
		Date start = timeExtract.getBeforeDate();
		Date end = timeExtract.getAfterDate();
		
		
		if (question.contains( "đang" ) && !question.contains( "đang làm gì" ) ||
				question.contains( "bây giờ" ) || question.contains( "hiện tại" )){
			start = new Date();
			end = start;
		}
		
		if (question.contains( "dang" ) && !question.contains( "dang lam gi" ) ||
				question.contains( "bay gio" ) || question.contains( "hien tai" )
					|| question.contains( "sap" ) || question.contains( "tiep theo" )){
			start = new Date();
			end = start;
		}
		mod.setStart(start);
		mod.setEnd(end);
		
		QueryParamater queryParamater = new QueryParamater();
		
		rubyAnswer.setBeginTime(mod.getStart());
		rubyAnswer.setEndTime(mod.getEnd());
		// end time processing
		System.out.println("Find list TV Program");
		List< TVProgram > progs = tps.getList( mod, question );
		System.out.println("List TVProgram Size: " + progs.size());
		// Log
		System.out.println("[TVANSWERMAPPERIMPL]: WRITE LOG" );
		Log log = new Log();
		queryParamater = new QueryParamater();
		queryParamater.setBeginTime( mod.getStart() );
		queryParamater.setEndTime( mod.getEnd() );
		queryParamater.setTvProTitle(mod.getProg_title());
		queryParamater.setTvChannel(mod.getChannel());
		queryParamater.setTypes(new ProgramTypeExtractor().typeString(question));
		rubyAnswer.setQueryParamater(queryParamater);
		
		// Now extract the needed information from the list of returned item
		// to generate the answer
		if (mod.getProg_title() == null && mod.getType() != null && !mod.getType().isEmpty()){
			mod.setProg_title(StrUtil.join(mod.getType(), ","));
		}
		
		if (mod.getChannel() == null && mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Channel null and program null");
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( UDF_ANS  );
				return rubyAnswer;
			}
			
			if (mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getChannelAndProgram( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs )  );
			return rubyAnswer;
		}
		if (mod.getChannel() == null){
			System.err.println("[TVAnserMapper]: Channel null");
			if ( intent.equals( IntentConstants.TV_POL ) && progs.isEmpty()){
				rubyAnswer.setAnswer( "Không!"  );
				return rubyAnswer;
			}
			if (intent.equals( IntentConstants.TV_DAT )){
				rubyAnswer.setAnswer( getChannelAndTime( progs )  );
				return rubyAnswer;
			}
			if (intent.equals( IntentConstants.TV_CHN )){
				rubyAnswer.setAnswer( getChannel( progs )  );
				return rubyAnswer;
			}
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( DEF_ANS  );
				return rubyAnswer;
			}
			if (mod.getStart().equals( mod.getEnd() )){
				if (intent.equals( IntentConstants.TV_CHN )){
					rubyAnswer.setAnswer( getChannel( progs )  );
					return rubyAnswer;
				}
				rubyAnswer.setAnswer( getChannelAndProgram( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs )  );
			return rubyAnswer;
		}
		
		if (mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Program null");
			if (mod.getStart() != null && mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getTitle( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitleAndTime( progs )  );
			return rubyAnswer;
		}
		if (intent.equals( IntentConstants.TV_DAT )){
			if (progs.size() > 0){
				rubyAnswer.setAnswer( getTime( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer(mod.getChannel() + " không chiếu " + mod.getProg_title());
			return rubyAnswer;
		}
		
		if (intent.equals( IntentConstants.TV_POL )){
			if (progs.size() > 0){
				rubyAnswer.setAnswer( "Có"  );
				rubyAnswer.setAnswer(getTitleAndTime(progs));
				return rubyAnswer;
			}
			rubyAnswer.setAnswer(mod.getChannel() + " không chiếu " + mod.getProg_title());
			return rubyAnswer;
		}
		
		if (mod.getStart() == null){
			rubyAnswer.setAnswer( DEF_ANS  );
			return rubyAnswer;
		}
		
		if (mod.getStart().equals( mod.getEnd() )){
			if (progs.isEmpty()){
				rubyAnswer.setAnswer( "Không có " + mod.getProg_title() + " nào trên kênh " + mod.getChannel() + " vào lúc đó!" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitle( progs )  );
			return rubyAnswer;
		}
		
		if (progs.isEmpty()){
			rubyAnswer.setAnswer( "Không có chương trình " + mod.getProg_title() +
					" nào trên " + mod.getChannel() + " vào lúc đó" );
			return rubyAnswer;
		}
		rubyAnswer.setAnswer( getTitleAndTime( progs )  );
		return rubyAnswer;
	}

	public String  getTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
		String time = "";
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			time += sdf.format(progs.get( i ).getStart_date()) + "</br>";
		}
		
		if (limitSizeAnswer < progs.size()){
			time += ". . . ";
		}
		return time;
	}

	public String  getTitle ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String title = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			if (!title.contains(progs.get( i ).getTitle() + "</br>")){
				title += progs.get( i ).getTitle() + "</br>";
			}
		}
		if (limitSizeAnswer < progs.size()){
			title += ". . . ";
		}
		
		return title;
	}

	public String  getChannel ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String channel = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			if (!channel.contains(progs.get( i ).getChannel())){
				channel += progs.get( i ).getChannel() + "</br>";
			}
		}
		
		return channel;
	}

	public String  getTitleAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
		String title = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			title += sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
		}
		
		return title;
	}

	public String  getChannelAndProgram ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String info = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + tv.getTitle() + "</br>";
		}
		
		return info;
	}

	public String  getChannelAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
		String info = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + "</br>";
		}
		
		return info;
	}

	public String  getChannelProgAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
		String info = "";
		
		int limit = limitSizeAnswer;
		if (progs.size() < limitSizeAnswer) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
		}
		
		
		return info;
	}

	public String  getEndDate ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		return progs.get( 0 ).getEnd_date().toString();
	}
	
	
	/*public void studyFile(String fileIn, String fileOut){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( fileIn ) );
			BufferedWriter writer = new BufferedWriter( new FileWriter( fileOut ) );
			
			String line;
			while ((line = reader.readLine()) != null){
				writer.write( getAnswer( line  ) + "\n");
			}
			
			writer.close();
			reader.close();
		}catch ( IOException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		TVAnswerMapperImpl tam = new TVAnswerMapperImpl();
		tam.init();
		tam.studyFile( "D:\\Workspace\\Code\\FTI\\rubyweb\\AIML_tvd_questions.txt",
				"D:\\Workspace\\Code\\FTI\\rubyweb\\AIML_tvd_questions.out" );
	}*/

}
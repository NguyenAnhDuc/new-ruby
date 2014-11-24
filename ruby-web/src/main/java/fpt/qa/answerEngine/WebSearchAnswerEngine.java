package fpt.qa.answerEngine;


import com.fpt.ruby.business.helper.DisplayAnswerHelper;
import com.fpt.ruby.business.service.BingSearchService;
import com.fpt.ruby.model.RubyAnswer;

public class WebSearchAnswerEngine extends AnswerEngine {
    private static int LIMIT;
    private static BingSearchService bingSearchService = new BingSearchService();

    WebSearchAnswerEngine(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void doRun() {
        long start = System.currentTimeMillis();
        setAnswer(null);
        RubyAnswer answer = new RubyAnswer();
        answer.setDomain("websearch");
        answer.setQuestion(getQuestion());
        answer.setIntent("udf");
        answer.setAnswer(DisplayAnswerHelper.display(bingSearchService.getDocuments(getQuestion(), LIMIT)));
        setAnswer(answer);
        System.err.println("WebSearch Answer Time: " + (System.currentTimeMillis() - start));
    }

    public static void config(Object... params) {
        LIMIT = (Integer) params[0];
    }
}

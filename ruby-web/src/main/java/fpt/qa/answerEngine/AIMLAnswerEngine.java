package fpt.qa.answerEngine;

import com.fpt.ruby.template.RubyAnswer;

public class AIMLAnswerEngine extends AnswerEngine {
    private static String botID;
    private static String token;

    AIMLAnswerEngine(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void doRun() {
        long start = System.currentTimeMillis();
        this.setAnswer(null);
        RubyAnswer answer = new RubyAnswer();
        answer.setQuestion(getQuestion());
        answer.setDomain("aiml");
        answer.setIntent("aiml");
        answer.setAnswer("tôi không biết");
        //answer.setAnswer(ProcessHelper.getAIMLAnswer(getQuestion(), botID, token));
        this.setAnswer(answer);
        System.err.println("AIML Answer Time: " + (System.currentTimeMillis() - start));
    }

    public static void config(Object... params) {
        botID = (String) params[0];
        token = (String) params[1];
    }
}

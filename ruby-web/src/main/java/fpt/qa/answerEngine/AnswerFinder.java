package fpt.qa.answerEngine;

import com.fpt.ruby.model.RubyAnswer;

import java.util.Date;

public class AnswerFinder implements ThreadCompleteListener {
    final static int NUM_WORKER = 3;
    final static String UDF = "#^#";

    ThreadGroup group = new ThreadGroup("answer");
    AnswerEngine[] threads;
    RubyAnswer[] workerAnswers;
    RubyAnswer answer;

    public AnswerFinder(AIMLInfoWrapper aimlInfo, NLPInfoWrapper nlpInfo) {
        AIMLAnswerEngine.config(aimlInfo.getBotID(), aimlInfo.getToken());
        WebSearchAnswerEngine.config(5);
        NLPAnswerEngine.config(nlpInfo);

        threads = new AnswerEngine[NUM_WORKER];
        workerAnswers = new RubyAnswer[NUM_WORKER];
    }

    public RubyAnswer getAnswer(String question) {
        System.out.println("[QUESTION] " + question);

        threads[0] = new AIMLAnswerEngine(group, "aiml");
        threads[1] = new NLPAnswerEngine(group, "nlp");
        threads[2] = new WebSearchAnswerEngine(group, "web");

        for (AnswerEngine t: threads) {
            t.addListener(this);
            t.setQuestion(question);
        }

        answer = null;
        workerAnswers = new RubyAnswer[NUM_WORKER];
        for (int i = 0; i < NUM_WORKER; ++i) {
            workerAnswers[i] = null;
            threads[i].start();
        }

        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return answer;
    }

    @Override
    public void notifyOfThreadComplete(Runnable thread) {
        RubyAnswer answer = ((AnswerEngine) thread).getAnswer();
        if (answer != null && this.answer == null) { // Answer not set yet!
            String ans = answer.getAnswer();
            ans = ((ans == null || ans == "null") ? "" : ans.toLowerCase());

            if (thread instanceof AIMLAnswerEngine) {
                if (ans.equals("") || ans.equals("null") || ans.contains("tôi không biết"))
                    answer.setAnswer(UDF);
                System.out.println("ans from aiml: " + answer.getAnswer());
                workerAnswers[0] = answer;
            }

            if (thread instanceof NLPAnswerEngine) {
                if (ans.isEmpty() || ans.contains("xin lỗi,"))
                    answer.setAnswer(UDF);
                System.out.println("ans from nlp: " + answer.getAnswer());
                workerAnswers[1] = answer;
            }

            if (thread instanceof WebSearchAnswerEngine) {
                System.out.println("ans from web: " + answer.getAnswer());
                workerAnswers[2] = answer;

            }
            checkInterrupt();
        }
    }

    private boolean checkInterrupt() {
        for (int i = 0; i < NUM_WORKER; ++i) {
            if (workerAnswers[i] == null) return false;
            if (!workerAnswers[i].getAnswer().equals(UDF)) {
                this.answer = workerAnswers[i];
                System.out.println(i + " threads interrupt other! " + (new Date()).getTime());
                group.stop();
//                group.interrupt();
//                group.destroy();
                return true;
            }
        }
        return false;
    }
}
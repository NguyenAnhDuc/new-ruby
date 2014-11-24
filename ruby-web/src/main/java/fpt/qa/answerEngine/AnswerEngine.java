package fpt.qa.answerEngine;

import com.fpt.ruby.model.RubyAnswer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AnswerEngine extends Thread {
    private String question;
    private RubyAnswer answer;

    AnswerEngine(ThreadGroup group, String name) {
        super(group, name);
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        answer = null;
        this.question = question;
    }

    private final Set<ThreadCompleteListener> listeners
            = new CopyOnWriteArraySet<ThreadCompleteListener>();

    final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {
        if (listeners == null || listeners.size() == 0) return;
        for (ThreadCompleteListener listener: listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }

    public RubyAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(RubyAnswer answer) {
        this.answer = answer;
    }

    @Override
    public void run() {
            setAnswer(null);
            doRun();
            notifyListeners();
    }

//    public stavoid config(Object... params);

    public abstract void doRun();
}

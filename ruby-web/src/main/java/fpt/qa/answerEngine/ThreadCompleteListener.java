package fpt.qa.answerEngine;

public interface ThreadCompleteListener {
    void notifyOfThreadComplete(final Runnable thread);
}

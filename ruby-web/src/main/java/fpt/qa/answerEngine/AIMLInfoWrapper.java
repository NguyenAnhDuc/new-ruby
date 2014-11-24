package fpt.qa.answerEngine;

public class AIMLInfoWrapper {
    String botID, token;

    public AIMLInfoWrapper(String botID, String token) {
        this.botID = botID;
        this.token = token;
    }

    public String getBotID() {
        return botID;
    }

    public void setBotID(String botID) {
        this.botID = botID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

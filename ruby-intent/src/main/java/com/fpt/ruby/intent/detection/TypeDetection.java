package com.fpt.ruby.intent.detection;

public abstract class TypeDetection {
    static final String NOT_IN_HOT_FIX = "NIHF";

    public static String getIntent(String question) {
        String hotfixintent = getHotfix(question);
        if (!hotfixintent.equals(NOT_IN_HOT_FIX))
            return hotfixintent;
        return getIntent2(question);
    }

    public static String getHotfix(String question) {
        return NOT_IN_HOT_FIX;
    }

    public static String getIntent2(String question) {
        return null;
    }
}
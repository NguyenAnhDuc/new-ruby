package com.fpt.ruby.intent.detection;

import com.fpt.ruby.business.constants.IntentConstants;

public abstract class TypeDetection {
    public static String getIntent(String question) {
        return IntentConstants.UNDEF;
    }
}

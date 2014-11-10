package com.fpt.ruby.business.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fpt.ruby.business.model.QuestionStructure;

public class RedisHelper {
	private static final String HEAD_KEY = "head";
	private static final String MODIFIERS_KEY = "modifiers";
	
	
	public static String toRedisString(QuestionStructure questionStructure){
		JSONObject jsonQuestion = new JSONObject();
		jsonQuestion.put(HEAD_KEY, questionStructure.getHead());
		JSONArray modifiers = new JSONArray();
		for (String modifier : questionStructure.getModifiers()){
			modifiers.put(modifier);
		}
		jsonQuestion.put(MODIFIERS_KEY, modifiers);
		return jsonQuestion.toString();
	}
	
	public static QuestionStructure toQuestionStructure(String key,String  jsonString){
		QuestionStructure questionStructure = new QuestionStructure();
		JSONObject json = new JSONObject(jsonString);
		System.out.println("json string: " + jsonString);
		questionStructure.setHead(json.getString(HEAD_KEY));
		JSONArray modifierArray = json.getJSONArray(MODIFIERS_KEY);
		List<String> modifiers = new ArrayList<String>();
		for (int i=0;i<modifierArray.length();i++){
			modifiers.add(modifierArray.getString(i));
		}
		questionStructure.setModifiers(modifiers);
		questionStructure.setKey(key);
		return questionStructure;
	}
	
	public static void main(String[] args) {
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setHead("head");
		List<String> modifiers = new ArrayList<String>();
		modifiers.add("modifier 1");
		modifiers.add("modifier 2");
		questionStructure.setModifiers(modifiers);
		System.out.println(toRedisString(questionStructure));
		
	}
}

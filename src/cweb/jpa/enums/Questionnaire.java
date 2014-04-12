package cweb.jpa.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public enum Questionnaire {
	Questionnaire1("ACTIVITY", Question.Question1, Question.Question2, Question.Question3, Question.Question4 ), 
	Questionnaire2("WEIGHT", Question.Question5, Question.Question6, Question.Question7), 
	Questionnaire3("TEMPARATURE", Question.Question8 ), 
	Questionnaire4("OVERALL REACTION TO THE WEB PAGE COLORING", Question.Question9, Question.Question10, Question.Question11, Question.Question12, Question.Question13, Question.Question14), 
	Questionnaire5("PERCEPTION OF FOREGROUND AND BACKGROUND COLORS IS", Question.Question15, Question.Question16, Question.Question17),
	Questionnaire6("OVERALL REACTION TO AUTOMATIC WEB COLOR ADAPTATION", Question.Question18, Question.Question19, Question.Question20, Question.Question21);
	
	public static final int length = Questionnaire.values().length;
	public static final List<Questionnaire> list = new ArrayList<Questionnaire>(Arrays.asList(Questionnaire.values()));
	
	String label;
	List<Question> questionList;
	private Questionnaire(String label, Question...questions ) {
		this.label = label;
		this.questionList = new ArrayList<Question>();
		for (int i = 0; i < questions.length; i++){
			questionList.add(questions[i]);
		}
	}
	
	public List<Question> getQuestionList(){
		return questionList;
	}
	
	public Hashtable<String, Object> getHashtable(){
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		List<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();
		for(Question q: questionList){
			// System.out.println(q.text + " : " + q.minLabel + " : " + q.maxLabel);
			list.add(q.getHashtable());
		}
		ht.put("label", label);
		ht.put("questions", list);
		return ht;
	}
	
}

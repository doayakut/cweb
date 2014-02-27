package cweb.jpa.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public enum Question {
	Question1("1-1","","Active", "Passive"), 
	Question2("1-2","","Fresh","Stale"), 
	Question3("1-3","","Clean","Dirty"), 
	Question4("1-4","","Modern","Classical"), 
	Question5("2-1","","Hard","Soft"), 
	Question6("2-2","","Masculine","Feminine"), 
	Question7("2-3","","Heavy","Light"), 
	Question8("3-1","","Warm","Cool"), 
	Question9("4-1","","Terrible","Wonderful"), 
	Question10("4-2","","Difficult to perceive","Easy to perceive"), 
	Question11("4-3","","Frustrating","Satisfying"), 
	Question12("4-4","","Dull","Stimulating"), 
	Question13("4-5","","Unpleasant","Pleasant"), 
	Question14("4-6","","Visually Unacceptable","Visually Exceptable"), 
	Question15("5-1","","Hardly differentiable","Easily differentiable"), 
	Question16("5-2","","Confusing","Clear"),
	Question17("5-3","","Has poor contrast", "Has excellent contrast"),
	Question18("6-1","I felt comfortable using the color adapted web pages.","Strongly Disagree", "Strongly Agree"),
	Question19("6-2","The automatic color adaptation makes web pages appear erratic.","Strongly Disagree", "Strongly Agree"),
	Question20("6-3","The complete transparency of color adaptation of web pages is desirable.","Strongly Disagree", "Strongly Agree"),
	Question21("6-4","The ubiquitous access to web pages adapted to my color vision needs or preferences without extra installation is attractive.","Strongly Disagree", "Strongly Agree");
	
	public static final int length = Question.values().length;
	public static final List<Question> list = new ArrayList<Question>(Arrays.asList(Question.values()));
	public static int levels = 11;

	String id;
	String text;
	String minLabel;
	String maxLabel;
	
	private Question(String id, String text, String minLabel, String maxLabel) {
		this.id = id;
		this.text = text;
		this.minLabel = minLabel;
		this.maxLabel = maxLabel;
	}

	public Hashtable<String, Object> getHashtable() {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("id", id);
		ht.put("text", text);
		ht.put("minLabel", minLabel);
		ht.put("maxLabel", maxLabel);
		return ht;
	}

	public static Question getFromId(String key) {
		for(Question q: Question.list){
			if(q.id.compareToIgnoreCase(key) == 0)
				return q;
		}
		return null;
	}
	
	
}

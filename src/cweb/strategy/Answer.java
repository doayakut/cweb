package cweb.strategy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.jpa.enums.Question;

public class Answer implements BaseStgy{
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public Answer(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		Map<String, String[]> map = servlet.getRequest().getParameterMap();
		User u = servlet.getUser();
		if(u.getState() == User.State.TRAINING){

			if(u.getCurr() == 2){
				servlet.getUserservice().setUserState(u, User.State.TEST);
				servlet.getUserservice().setUserCurr(u, 0);
			}
			else{
				System.out.println("Curr Updated : " + u.getCurr() + 1);
				servlet.getUserservice().setUserCurr(u, u.getCurr() + 1);
			}
			
		}
		else{
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
			    String key = entry.getKey();
			    String[] values = entry.getValue();
			    // System.out.println(key + " : " + values[0]);
			    Question q = Question.getFromId(key);
			    if(q != null){
				    cweb.jpa.Answer a = servlet.getAnsservice().createAnswer();
				    a.setQuestion(Question.getFromId(key));
				    a.setValue(Float.parseFloat(values[0]));
				    a.setUser(u);
			    }
			}
			
	
		    System.out.println("Curr Updated : " + u.getCurr() + 1);
			servlet.getUserservice().setUserCurr(u, u.getCurr() + 1);
	}
		servlet.getEntityManager().merge(u);
		servlet.getEntityManager().flush();
		servlet.redirect("/home?action=eval");
	}
}

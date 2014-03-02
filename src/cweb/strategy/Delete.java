package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cweb.BaseServlet;
import cweb.BaseServlet.MessageType;
import cweb.jpa.Answer;
import cweb.jpa.User;
import cweb.jpa.service.AnswerService;
import cweb.jpa.service.UserService;

public class Delete implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;
	
	private User user = null;
	private Query queryAnswers;

	public Delete(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {

		String id = servlet.getRequest().getParameter("id").toLowerCase();
		
		user = getUserFromDBbyID(Long.valueOf(id));
		UserService us = servlet.getUserservice();
		AnswerService as = servlet.getAnsservice();
		
		if(user != null) {
			us.deleteUser(user);
			
			List<Answer> answers = getAnswersFromDB(user);

			for (Answer a : answers) {
				as.deleteAnswer(a);
			}
		}else{
			servlet.redirect("/home?type=admin", "Can not find user: " + id, MessageType.ERROR);
			return;
		}
		
		
		
		servlet.redirect("/home?type=admin");
		
	}
	@SuppressWarnings("unchecked")
	private List<Answer> getAnswersFromDB(User u){
		EntityManager em = servlet.getEntityManager();
		queryAnswers = em
				.createQuery("SELECT a FROM Answer a where a.user=:user");
		queryAnswers.setParameter("user", user);
		return (List<Answer>) queryAnswers.getResultList();
	}
	
	private User getUserFromDBbyID(long id) {
		
		
		String jpql = "select u from User u where u.id = :id";
		Query q= servlet.getEntityManager().createQuery(jpql);
		
		
		q.setParameter("id", id);
		
		@SuppressWarnings("unchecked")
		List<User> users =  (List<User>) q.getResultList();
		
		if(!users.isEmpty())		
			return users.get(0);
		else 
			return null;
	}

}

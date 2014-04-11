package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import cweb.BaseServlet;
import cweb.BaseServlet.MessageType;
import cweb.jpa.User;
import cweb.jpa.service.UserService;

public class Edit implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;
	
	private User user = null;

	public Edit(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		// TODO Auto-generated method stub

		String id = servlet.getRequest().getParameter("id").toLowerCase();
		String name = servlet.getRequest().getParameter("newname").toLowerCase();
		String gender = servlet.getRequest().getParameter("newgender").toLowerCase();
		String cvtype = servlet.getRequest().getParameter("newcvtype").toLowerCase();
		user = getUserFromDBbyID(Long.valueOf(id));
		if(user != null) {
			UserService userservice = servlet.getUserservice();
			userservice.setUserName(user, name);
			userservice.setUserGender(user, gender);
			userservice.setUserCvtype(user, cvtype);
			
			servlet.getEntityManager().merge(user);
		}else{
			servlet.redirect("/home?type=admin", "Can not find user: " + id, MessageType.ERROR);
			return;
		}
		

		servlet.redirect("/home?type=admin");
		
	}

	@SuppressWarnings("unused")
	private User getUserFromDB(String name) {
		
		
		String jpql = "select u from User u where u.name = :name";
		Query q= servlet.getEntityManager().createQuery(jpql);
		
		
		q.setParameter("name", name);
		
		@SuppressWarnings("unchecked")
		List<User> users =  (List<User>) q.getResultList();
		
		if(!users.isEmpty())		
			return users.get(0);
		else 
			return null;
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

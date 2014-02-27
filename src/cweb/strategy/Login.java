package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cweb.BaseServlet;
import cweb.BaseServlet.MessageType;
import cweb.jpa.User;

public class Login implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;
	
	private User user = null;

	public Login(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		
		String name = servlet.getRequest().getParameter("name").toLowerCase();
		user = getUserFromDB(name);
		
		if(user == null) { // User does not exist
			
			servlet.redirect("/home", "Can not find user", MessageType.ERROR);
			return;
			
		}
		
		
		HttpSession session = servlet.getRequest().getSession();
		
		if (session.getAttribute("user") != null)
			session.removeAttribute("user");

		session.setAttribute("user", user);

		servlet.redirect("/home?action=choice");

	}
	
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


}

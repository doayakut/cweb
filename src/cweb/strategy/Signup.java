package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cweb.BaseServlet.MessageType;
import cweb.jpa.service.UserService;
import cweb.BaseServlet;
import cweb.jpa.User;

public class Signup implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;
	
	private User user = null;

	public Signup(BaseServlet s) {
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

		String name = servlet.getRequest().getParameter("newname").toLowerCase();
		String gender = servlet.getRequest().getParameter("newgender").toLowerCase();
		String cvtype = servlet.getRequest().getParameter("newcvtype").toLowerCase();

		user = getUserFromDB(name);
		
		if(user == null) { // Create a new user
			user = servlet.getUserservice().createUser(servlet.getExperiment().getOrder());
			servlet.getExpservice().updateOrders(servlet.getExperiment());
			
			user.setCurr(0);
			if(user != null) {
				UserService userservice = servlet.getUserservice();
				userservice.setUserName(user, name);
				userservice.setUserGender(user, gender);
				userservice.setUserCvtype(user, cvtype);
			}
			else{
				servlet.redirect("/home?type=admin", "Can not create user", MessageType.ERROR);
				return;
			}
			
		}
		else{

			servlet.redirect("/home?type=admin", "User with " + name + "already exists", MessageType.ERROR);
			return;
		}

		servlet.redirect("/home?type=admin");
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

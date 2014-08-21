package cweb.strategy.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.strategy.BaseStgy;

public class HomePage implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public HomePage(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		Template t = new Template("vmfiles/Home.vm", servlet);
		
		User user = (User)servlet.getUser();

		if(user != null && user.isCVD())
			t.put("max", 54);
		else
			t.put("max", 18);
		
		/* fetching person hashtables */
		Hashtable<String, Object> ht_u = User.getHashtable(user);

		
		t.put("user", ht_u);
		t.put("usertype", servlet.getRequest().getParameter("type"));
		
		
		List<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();
		try {

			List<User> results = servlet.getUserservice()
					.findAllUsers();

			if (!results.isEmpty()) {
				for (User u : results) {
					Hashtable<String, Object> ht_p = User
							.getHashtable(u);
					list.add(ht_p);
				}
			}
		}
		 catch (Exception e) {
			servlet.printStack(e);
			return;
		}

		t.put("participants", list);
		servlet.print(t.render());
		
	}

}

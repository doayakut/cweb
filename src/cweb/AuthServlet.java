package cweb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cweb.strategy.Login;
import cweb.strategy.Logout;

@SuppressWarnings("serial")
public class AuthServlet extends BaseServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
		return;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
		return;
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!initBase(req, resp))
			return;

		
		if(action.equalsIgnoreCase("login")){	
			
			Login strategy = new Login(this);
			strategy.execute();
		}
		if(action.equalsIgnoreCase("logout")){
			if(this.getUser() == null){
				redirect("/auth");
				return;
			}
			
			Logout strategy = new Logout(this);
			strategy.execute();
		}

	}
}

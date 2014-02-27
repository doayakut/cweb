package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;

import cweb.BaseServlet;
import cweb.jpa.User;

public class Checkin implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public Checkin(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		//content type must be set to text/event-stream
		User u = servlet.getUser();
		System.out.println(u.getCurr());
        servlet.print(String.valueOf(u.getCurr()));
	}

}

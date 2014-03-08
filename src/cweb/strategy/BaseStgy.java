package cweb.strategy;

import java.io.IOException;
import java.util.ArrayList;

import cweb.BaseServlet;
import cweb.jpa.User;

public interface BaseStgy {
	
	public ArrayList<Class<User>> auths = null;
	public BaseServlet root = null; 

	public boolean authenticate();
	public void execute() throws IOException, IllegalArgumentException, IllegalAccessException;
	
}

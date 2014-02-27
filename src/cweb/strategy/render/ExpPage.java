package cweb.strategy.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.jpa.enums.Method;
import cweb.jpa.enums.PageType;
import cweb.strategy.BaseStgy;

public class ExpPage implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public ExpPage(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		if (servlet.getUser() == null) 
			return false;
		else return true;
	}

	@Override
	public void execute() throws IOException {
		if (servlet.getUser().isCompleted()) {
			servlet.redirect("/home");
			return;
		}
		if (servlet.getUser().isDonePages()) {
			servlet.redirect("/home");
			return;
		}
		// Get current index
		/* fetching person hashtables */
		Template t = null;
		User u = servlet.getUser();
		
		if(u.getState() == User.State.TRAINING){

			String folderpath = "/training/" + (u.getCurr() + 1);
			t = new Template("vmfiles/ExpPage.vm", servlet);
			Hashtable<String, Object> ht = new Hashtable<String, Object>();
			ht.put("html", folderpath + ".html");
			ht.put("css", folderpath + ".css");
			t.put("url", ht);
	
			Hashtable<String, Object> ht_u = User.getHashtable(u);
			t.put("user", ht_u);
			servlet.print(t.render());
		}
		else {
		
			String cvtype = u.getCvtype();
			String folderpath = "";
	
			int pageindex = servlet.getUserservice().getCurrPageIndex(u);
			Method m = servlet.getUserservice().getCurrMethod(u);
			PageType pt = servlet.getUserservice().getCurrPageType(u);
	
			folderpath = "/" + cvtype + "/" + m.name().toLowerCase() + "/"
					+ pt.name().toLowerCase() + "/" + (pageindex + 1);
	
			t = new Template("vmfiles/ExpPage.vm", servlet);
			Hashtable<String, Object> ht = new Hashtable<String, Object>();
			ht.put("html", folderpath + ".html");
			ht.put("css", folderpath + ".css");
			t.put("url", ht);
	
			Hashtable<String, Object> ht_u = User.getHashtable(u);
			t.put("user", ht_u);
			servlet.print(t.render());
		}
	}
}

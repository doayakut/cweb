package cweb.strategy.render;

import java.io.IOException;
import java.util.ArrayList;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.strategy.BaseStgy;

public class Choice implements BaseStgy {

	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public Choice(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		if (servlet.getUser().isCompleted()) {
			servlet.redirect("/home");
			return;
		}
		// Get current index
		/* fetching person hashtables */
		Template t = null;
		t = new Template("vmfiles/Choice.vm", servlet);

		servlet.print(t.render());
	}
}

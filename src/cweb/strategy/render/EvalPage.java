package cweb.strategy.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.velocity.tools.generic.MathTool;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.jpa.enums.Questionnaire;
import cweb.strategy.BaseStgy;

public class EvalPage implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	public EvalPage(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		if (servlet.getUser() == null)
			return false;
		else
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

		t = new Template("vmfiles/EvalPage.vm", servlet);

		Hashtable<String, Object> ht_u = User.getHashtable((User) servlet
				.getUser());
		t.put("user", ht_u);
		t.put("math", new MathTool());
		if (servlet.getUser().getState() == User.State.TRAINING) {

			List<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();

			for (Questionnaire qn : Questionnaire.list) {
				list.add(qn.getHashtable());
			}
			t.put("questionnaires", list.subList(0, list.size() - 1));
			servlet.print(t.render());
			
		} else {
			List<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();

			if (servlet.getUser().isDonePages()) {
				list.add(Questionnaire.Questionnaire6.getHashtable());
				t.put("questionnaires", list);

			} else {
				for (Questionnaire qn : Questionnaire.list) {
					list.add(qn.getHashtable());
				}
				t.put("questionnaires", list.subList(0, list.size() - 1));

			}

			servlet.print(t.render());
		}
	}
}

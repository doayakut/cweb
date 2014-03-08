package cweb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cweb.strategy.Answer;
import cweb.strategy.BaseStgy;
import cweb.strategy.Checkin;
import cweb.strategy.Delete;
import cweb.strategy.Edit;
import cweb.strategy.Export;
import cweb.strategy.Login;
import cweb.strategy.Logout;
import cweb.strategy.Signup;
import cweb.strategy.render.Choice;
import cweb.strategy.render.EvalPage;
import cweb.strategy.render.ExpPage;
import cweb.strategy.render.HomePage;
import cweb.strategy.render.RenderData;

@SuppressWarnings("serial")
public class SingleServlet extends BaseServlet {

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

		try {
			BaseStgy strategy = null;
			if (action == null)
				action = "home";
			
			System.out.println("request: " + action);

			if (action.compareToIgnoreCase("home") == 0) {
				strategy = new HomePage(this);
			}

			if (action.compareToIgnoreCase("login") == 0) {
				strategy = new Login(this);
			}

			if (action.compareToIgnoreCase("logout") == 0) {
				strategy = new Logout(this);
			}

			if (action.compareToIgnoreCase("signup") == 0) {
				strategy = new Signup(this);
			}
			
			if (action.compareToIgnoreCase("exp") == 0) {
				strategy = new ExpPage(this);
			}
			
			if (action.compareToIgnoreCase("eval") == 0) {
				strategy = new EvalPage(this);
			}
			
			if (action.compareToIgnoreCase("answer") == 0) {
				strategy = new Answer(this);
			}

			if (action.compareToIgnoreCase("edit") == 0) {
				strategy = new Edit(this);
			}

			if (action.compareToIgnoreCase("delete") == 0) {
				strategy = new Delete(this);
			}

			if (action.compareToIgnoreCase("choice") == 0) {
				strategy = new Choice(this);
			}
			
			if (action.compareToIgnoreCase("checkin") == 0) {
				strategy = new Checkin(this);
			}

			if (action.compareToIgnoreCase("export") == 0) {
				strategy = new Export(this);
			}
			
			if (action.compareToIgnoreCase("render") == 0) {
				strategy = new RenderData(this);
			}
			
			if (strategy != null) {
				if (!strategy.authenticate()) {
					this.redirect("/home");
					this.close();
					return;
				}

				strategy.execute();
				this.close();
				return;
			}

			this.redirect("/home");
			this.close();
		} catch (Exception e) {
			this.close();
			e.printStackTrace(this.getResponse().getWriter());
		}
		return;

	}

}

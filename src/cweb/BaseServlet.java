package cweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.geronimo.javamail.transport.smtp.SMTPTransport;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cweb.jpa.Experiment;
import cweb.jpa.User;
import cweb.jpa.service.AnswerService;
import cweb.jpa.service.ExperimentService;
import cweb.jpa.service.UserService;

/**
 * Servlet implementation class BaseServlet
 */
@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

	@Resource
	protected UserTransaction userTransaction;

	@PersistenceContext
	private EntityManager entityManager;

	
	private UserService userservice;
	private ExperimentService expservice;
	private AnswerService ansservice;
	
	private Experiment experiment;

	
	public enum MessageType {
		ERROR("alert-danger"), WARNING("alert-warning"), INFO("alert-info"), SUCCESS(
				"alert-success");
		public final String name;

		MessageType(String name) {
			this.name = name;
		}
	}

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	protected String action;
	
	protected Locale locale;


	public BaseServlet() {
		super();
		System.out.println("Constructor");
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * destroyes the instance and redirects to startpage
	 * 
	 * @return
	 */
	public String logout() {
		request.getSession().invalidate();
		return "index.xhtml?faces-redirect=true";
	}

	public boolean initBase(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if(!this.open()){
			return false; 
		}


		this.request = request;
		this.response = response;

		this.response.setCharacterEncoding("UTF-8");
		this.response.setContentType("text/html");
		this.action = request.getParameter("action");

		this.setUserservice(new UserService(getEntityManager()));
		this.setExpservice(new ExperimentService(getEntityManager()));
		this.setAnsservice(new AnswerService(getEntityManager()));

		if(this.expservice.findAllUsers().size() == 0)
			experiment = this.expservice.createExperiment();
		else 
			experiment = this.expservice.findAllUsers().get(0);

		return true;

	}

	public boolean open(){
		try {
			userTransaction.begin();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not Opened");
			return false;
		}

	}
	public void close() {
		try {
			
			
			
			if(this.userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
				this.userTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sendQuery();
			System.out.println("Could not Closed");

		}
	}

	public void forward(String url, String message, MessageType type)
			throws ServletException, IOException {
		request.setAttribute("message", message);
		request.setAttribute("message-type", type.name); // Set error.

		request.getRequestDispatcher(url).forward(request, response);
	}

	public void redirect(String url) throws IOException {
		response.sendRedirect("/cweb" + url);
	}

	public void redirect(String url, String message, MessageType type)
			throws IOException {
		if (url == null || url.isEmpty())
			url = new String("/");

		String connector;
		if (url.contains("?"))
			connector = "&";
		else {
			connector = "?";
		}
		response.sendRedirect("/cweb" + url + connector + "message="
				+ message + "&" + "message-type=" + type.name);
	}

	public User getUser() {
		HttpSession session = request.getSession();
		if(session.getAttribute("user") != null){
			User u = userservice.findUser(((User)session.getAttribute("user")).getId());
			return u;
		}
		else return null;
	}

	public UserService getUserservice() {
		return userservice;
	}

	public void setUserservice(UserService ps) {
		this.userservice = ps;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}	

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void print(String render) throws IOException {
		this.response.getWriter().print(render);
		
	}

	public void printStack(Exception e) throws IOException {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		Velocity.setProperty("file.resource.loader.path", this
				.getServletContext().getRealPath("/"));
		Velocity.init();
		VelocityContext context = new VelocityContext();
		context.put("content", sw.toString().replace(System.getProperty("line.separator"), "<br/>\n"));

		StringWriter w1 = new StringWriter();
		Velocity.mergeTemplate("vmfiles/Base.vm", "UTF-8", context, w1);

		this.print(w1.toString());
	}

	public ExperimentService getExpservice() {
		return expservice;
	}


	public void setExpservice(ExperimentService expservice) {
		this.expservice = expservice;
	}


	public AnswerService getAnsservice() {
		return ansservice;
	}


	public void setAnsservice(AnswerService ansservice) {
		this.ansservice = ansservice;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}


	public void sendQuery() {
		String to = "yakut1@umbc.edu";
		String fr = "isildoga@gmail.edu";

		Properties properties = System.getProperties();

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        
		properties.setProperty("mail.smtps.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtps.auth", "true");
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fr));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject("[cweb]");
			message.setText(getQuery());

	        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

	        t.connect("smtp.gmail.com", "isildoga@gmail.com", "s*nao2416");
	        t.sendMessage(message, message.getAllRecipients());      
	        t.close();
	        
			System.out.println("Sent message successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getQuery() {
		String query = "";
		Map<String, String[]> params = request.getParameterMap();
		Iterator<String> i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			String val = params.get(key)[0];
			query += key + "=" + val;
		}
		return query;
	}

}

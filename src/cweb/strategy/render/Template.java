package cweb.strategy.render;

import java.io.StringWriter;
import java.util.Hashtable;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cweb.BaseServlet;

public class Template {

	private BaseServlet servlet;
	private VelocityContext vcontext;
	private String file;
	private Hashtable<String, String> message;

	// Getters and Setters
	public VelocityContext getVcontext() {
		return vcontext;
	}

	public void setVcontext(VelocityContext vcontext) {
		this.vcontext = vcontext;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Template(String file, BaseServlet s) {
		this.vcontext = new VelocityContext();
		this.file = file;
		this.servlet = s;

		Velocity.setProperty("file.resource.loader.path", servlet
				.getServletContext().getRealPath("/"));
		Velocity.init();

		/* Getting any message */
		String msg = servlet.getRequest().getParameter("message");
		String msgType = servlet.getRequest().getParameter("message-type");
		if (msg != null) {
			message = new Hashtable<String, String>();

			message.put("content", msg);

			if (msgType == null)
				msgType = "alert-info";

			message.put("type", msgType);
		}
	}

	public void put(String string, Object o) {
		this.vcontext.put(string, o);
	}

	public String render() {
		StringWriter w = new StringWriter();
		Velocity.mergeTemplate(file, "UTF-8", vcontext, w);

		VelocityContext context = new VelocityContext();
		context.put("content", w.toString());
		context.put("message", message);

		StringWriter w1 = new StringWriter();
		Velocity.mergeTemplate("vmfiles/Base.vm", "UTF-8", context, w1);

		return w1.toString();
	}
	
	public String renderWO() {
		StringWriter w = new StringWriter();
		Velocity.mergeTemplate(file, "UTF-8", vcontext, w);

		return w.toString();
	}
	
}


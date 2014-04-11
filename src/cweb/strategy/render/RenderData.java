package cweb.strategy.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.apache.velocity.tools.generic.ValueParser;

import cweb.BaseServlet;
import cweb.jpa.User;
import cweb.strategy.BaseStgy;

public class RenderData implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	String[] avoid = { "Experiment"};
	
	public RenderData(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws IOException, IllegalArgumentException, IllegalAccessException {
		Template t = new Template("vmfiles/Data.vm", servlet);

		EntityManager em = servlet.getEntityManager();
		
		/* fetching person hashtables */
		List<Hashtable<String, Object>> ht_tables = new ArrayList<Hashtable<String, Object>>();
		
		for (EntityType<?> entity : em.getMetamodel().getEntities()) {
			if(Arrays.asList(avoid).contains(entity.getName()))
				continue;
			
		    Hashtable<String, Object> ht_table = new Hashtable<String, Object>();
		    
		    ht_table.put("name", entity.getName());
		    	
		    ht_table.put("attributes", entity.getDeclaredAttributes());
		    
		    Query query = em.createQuery("SELECT x FROM " + entity.getName() + " x");

		    List<Object> results = query.getResultList();
		    ht_table.put("items", results);
		    
		    
		    ht_tables.add(ht_table);
		    
		}
		t.put("tables", ht_tables);
		t.put("parser", new ValueParser());
		
		servlet.print(t.render());

	}

}

package cweb.jpa.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import cweb.jpa.Experiment;
import cweb.jpa.enums.Order;

public class ExperimentService {
	protected EntityManager em;
	protected UserTransaction ut;

	public ExperimentService(EntityManager em) {
		this.em = em;
	}

	public Experiment createExperiment() {
		Experiment emp = new Experiment();

		emp.setOrder(0);

		em.persist(emp);
		return emp;
	}

	public Experiment updateOrders(Experiment e) {
		if (e != null) {
			int order = e.getOrder();
			order++;

			if (order == Order.size * Order.size)
				order = 0;
			
			e.setOrder(order);

		}
		return e;
	}
	
	@SuppressWarnings("unchecked")
	public List<Experiment> findAllUsers() {
		Query query = em.createQuery("SELECT e FROM Experiment e");
		return (List<Experiment>) query.getResultList();
	}


}

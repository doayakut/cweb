package cweb.jpa.service;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import cweb.jpa.Answer;
import cweb.jpa.User;

public class AnswerService {
	protected EntityManager em;
	protected UserTransaction ut;

	public AnswerService(EntityManager em) {
		this.em = em;
	}
	public Answer createAnswer() {
		Answer emp = new Answer();
		
		em.persist(emp);
		return emp;
	}

	public void deleteAnswer(Answer a) {
		if (a != null) {
			em.remove(a);
		}
		return;
		
	}
}

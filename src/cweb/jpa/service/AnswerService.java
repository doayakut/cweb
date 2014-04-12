package cweb.jpa.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import cweb.jpa.Answer;
import cweb.jpa.User;
import cweb.jpa.enums.Question;

public class AnswerService {
	protected EntityManager em;
	protected UserTransaction ut;

	public AnswerService(EntityManager em) {
		this.em = em;
	}
	public List<Answer> getAnswers(User u, Question q){
		TypedQuery<Answer> query = em.createQuery("SELECT e FROM Answer e WHERE e.user = :user and e.question = :question", Answer.class);

		query.setParameter("question", q);
		query.setParameter("user", u);
		List<Answer> answers =  (List<Answer>) query.getResultList();
		return answers;
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

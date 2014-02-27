package cweb.jpa.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import cweb.jpa.User;
import cweb.jpa.User.State;
import cweb.jpa.enums.Method;
import cweb.jpa.enums.Order;
import cweb.jpa.enums.PageType;


public class UserService {
	protected EntityManager em;
	protected UserTransaction ut;

	public UserService(EntityManager em) {
		this.em = em;
	}
	public User createUser(int order) {
		User emp = new User();

		emp.setMethodOrder(getMethodOrder(order));
		emp.setPageOrder(getPageOrder(order));
		emp.setState(User.State.TRAINING);

		emp.setCreateDate(new Date());
		
		em.persist(emp);
		return emp;
	}


	private Order getMethodOrder(int currOrder) {
		return Order.values()[currOrder % 6];
	}

	private Order getPageOrder(int currOrder) {
		return Order.values()[currOrder / 6];
	}
	/*
	 * set User password and email combination
	 */
	public User setUserName(User p, String val) {
		if (p != null) {
			p.setName(val);
		}
		return p;
	}

	public User setUserGender(User p, String val){
		if (p != null) {
			p.setGender(val);
		}
		return p;
	}
	public User setUserCvtype(User p, String val){
		if (p != null) {
			p.setCvtype(val);
		}
		return p;
	}

	public User setUserState(User user, State state) {

		if (user != null) {
			user.setState(state);
		}
		return user;

	}

	public User setUserCurr(User user, int curr) {
		if(user != null){
			user.setCurr(curr);
		}
		return user;
		// TODO Auto-generated method stub
		
	}

	public void removeUser(User p) {
		em.remove(p);
	}

	/*
	 * find User
	 */

	public User findUser(long id) {
		return em.find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		Query query = em.createQuery("SELECT e FROM User e");
		return (List<User>) query.getResultList();
	}


	// Order for viewing pages changes how to get page index, type and method
	// Current order is : PageType(3) → Index(6) → Method(3)
	// Method is N % 3 
	// Index is (N / 3) % 6
	// Type is (N / 18) % 3
	public int getPageIndex(User u, int i) {
		return (i / 3) % 6;
		
	}

	public PageType getPageType(User u, int i) {
		int pagetype = (i / 18) % 6;
		return PageType.list.get(u.getPageOrder().get(pagetype));
		
	}

	public Method getMethod(User u, int i) {
		int method = i % 3;
		return Method.list.get(u.getMethodOrder().get(method));
	}

	public int getCurrPageIndex(User u) {
		return getPageIndex(u, u.getCurr());
	}

	public PageType getCurrPageType(User u) {
		return getPageType(u, u.getCurr());
	}

	public Method getCurrMethod(User u) {
		return getMethod(u, u.getCurr());
	}
}

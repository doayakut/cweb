package cweb.jpa;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Hashtable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import cweb.jpa.enums.Order;

@Entity
@Table(name = "users")
public class User implements Comparable<User> {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@Basic(fetch = FetchType.EAGER)
	private int curr;

	public enum State {
		TRAINING, TEST;
	}

	private State state;

	@Enumerated(EnumType.STRING)
	@Column(name = "method_order")
	private Order methodOrder;

	@Enumerated(EnumType.STRING)
	@Column(name = "page_order")
	private Order pageOrder;

	@Column(name = "date_created")
	private Date createDate;

	private String gender;

	private String cvtype;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurr() {
		return curr;
	}

	public void setCurr(int curr) {
		this.curr = curr;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Order getMethodOrder() {
		return methodOrder;
	}

	public void setMethodOrder(Order methodOrder) {
		this.methodOrder = methodOrder;
	}

	public Order getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(Order pageOrder) {
		this.pageOrder = pageOrder;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCvtype() {
		return cvtype;
	}

	public void setCvtype(String cvtype) {
		this.cvtype = cvtype;
	}

	@Override
	public int compareTo(User o) {
		return 0;
	}

	public static Hashtable<String, Object> getHashtable(User uoi) {
		Hashtable<String, Object> ht;

		if (uoi == null) {
			return null;
		}
		System.out.println("not null");
		ht = new Hashtable<String, Object>();

		System.out.println(ht);
		ht.put("id", uoi.getId());
		ht.put("cvtype", uoi.getCvtype());
		ht.put("gender", uoi.getGender());
		ht.put("name", uoi.getName());
		ht.put("task", uoi.getCurr());
		ht.put("state", uoi.getState().toString());

		return ht;
	}

	public boolean isCompleted() {
		if (curr <= 54)
			return false;
		else
			return true;
	}

	public boolean isDonePages() {
		if (curr >= 54)
			return true;
		else
			return false;
	}

	public String getAttributeStr(String a) {
		try {
			// string field
			Field f = User.class.getDeclaredField(a);
			Object val = f.get(this);
			if (val != null)
				return val.toString();
			else {
				final JoinColumn joinAnnotation = f
						.getAnnotation(JoinColumn.class);
				if (null != joinAnnotation) {
					return (!joinAnnotation.nullable()) ? "true" : "false";
				}

				final Column columnAnnotation = f.getAnnotation(Column.class);
				if (null != columnAnnotation) {
					return (!columnAnnotation.nullable()) ? "true" : "false";
				}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}
	
	public String toString(){
		return id + "-" + name;
		
	}

}

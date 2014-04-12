package cweb.jpa;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import cweb.jpa.enums.Question;

@Entity
@Table(name = "answers")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "answer_id")
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "question")
	private Question question;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	private Float value;

	@Column(name = "view_index")
	private int viewIndex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(int viewIndex) {
		this.viewIndex = viewIndex;
	}


	public String getAttributeStr(String a) {
		try {
			// string field
			Field f = Answer.class.getDeclaredField(a);
			Object val = f.get(this);
			if (val != null)
				return val.toString();
			else {
				if(f.getAnnotation(JoinColumn.class) == null){
					
				}
				else 
					return "";

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}

}

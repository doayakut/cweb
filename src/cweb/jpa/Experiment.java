package cweb.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Experiment")
public class Experiment {
	
	public int order;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	

}

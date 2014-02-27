package cweb.jpa.enums;

import java.util.ArrayList;
import java.util.List;

public enum Order {
	O1(0,1,2),
	O2(1,2,0),
	O3(2,0,1),
	O4(0,2,1),
	O5(2,1,0),
	O6(1,0,2);
	
	private List<Integer> order;

	private Order(int type0, int type1, int type2){
		order = new ArrayList<Integer>();
		order.add(type0);
		order.add(type1);
		order.add(type2);
	}
	
	public int get(int index){
		return order.get(index);
	}
	public static final int size = Order.values().length;

}

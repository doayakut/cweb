package cweb.jpa.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Method {
	DAVID, KULN, OURS, NORMAL;
	public static final List<Method> list = new ArrayList<Method>(Arrays.asList(Method.values()));
	public static final int length = Method.values().length;

}

package cweb.jpa.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PageType {
	SIMPLE, COMPLEX1, COMPLEX11;
	public static final List<PageType> list = new ArrayList<PageType>(Arrays.asList(PageType.values()));
	public static final int length = PageType.values().length;

}

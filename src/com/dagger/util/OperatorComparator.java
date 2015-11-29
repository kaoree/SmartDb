package com.dagger.util;
import java.util.Comparator;

import com.dagger.operator.Operator;

public class OperatorComparator implements Comparator<Operator> {

	@Override
	public int compare(Operator o1, Operator o2) {
		// TODO Auto-generated method stub
		if (o1 == o2) {
			return 0;
		}
		if (o1.getIndice() > o2.getIndice()) {
			return 1;
		} else if (o1.getIndice() == o2.getIndice()) {
			return 0;
		} else if (o1.getIndice() < o2.getIndice()) {
			return -1;
		}
		return 0;
	}

}

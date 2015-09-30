package com.accenture.mbank.util;

import java.util.Comparator;

import com.accenture.mbank.model.MovementsModel;

public class AmountComparator implements Comparator<MovementsModel>{

	@Override
	public int compare(MovementsModel lhs,
			MovementsModel rhs) {
		if (lhs.getAmount() > rhs.getAmount()) {
            return -1;
        }
        else if (lhs.getAmount() <  rhs.getAmount()) {
            return 1;
        }
        else {
            return 0;
        }
	}
}

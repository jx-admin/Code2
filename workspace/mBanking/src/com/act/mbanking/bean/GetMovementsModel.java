package com.act.mbanking.bean;

import java.util.List;

public class GetMovementsModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();
	
	private List<MovementsModel> movements;

	/**
	 * @return the movements
	 */
	public List<MovementsModel> getMovements() {
		return movements;
	}

	/**
	 * @param movements the movements to set
	 */
	public void setMovements(List<MovementsModel> movements) {
		this.movements = movements;
	}
	
	
}

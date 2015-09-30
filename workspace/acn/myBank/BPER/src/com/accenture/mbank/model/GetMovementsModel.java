package com.accenture.mbank.model;

import java.util.List;

public class GetMovementsModel {
	public ResponsePublicModel responsePublicModel = new ResponsePublicModel();
	
	private List<MovementsModel> movements;
	private String moreValues;

    /**
     * @return the moreValues
     */
    public String getMoreValues() {
        return moreValues;
    }

    /**
     * @param moreValues the moreValues to set
     */
    public void setMoreValues(String moreValues) {
        this.moreValues = moreValues;
    }

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

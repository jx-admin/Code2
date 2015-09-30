package org.accenture.product.lemonade.content;

import java.util.Vector;

import org.accenture.product.lemonade.model.LemonadeBean;

public interface DataBaseAdapter
{

	/**
	 */
	public void open();

	/**
	 */
	public void close();

	/**
	 */
	public long insert(LemonadeBean bean);

	/**
	 */
	public void update(LemonadeBean bean);

	/**
	 */
	public void delete(int id);

	/**
	 */
	public Vector<LemonadeBean> select();
}

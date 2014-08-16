
package com.shiningstone.accounter;

import com.shiningstone.accounter.db.MyDbHelper;

public abstract class DbData implements Cloneable {
	public abstract void AddToDb();
	protected MyDbHelper db = MyDbHelper.getInstance(null);
	public int Id;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}

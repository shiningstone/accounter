package com.shiningstone.accounter;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionData implements Parcelable {
	int		id;			// transaction record id, primary key
	int		type; 		// 0 means income, 1 means payout;
	int 	infoId;
	double	amount;
	int 	category;
	int 	subcategory;
	int 	account;
	int 	shop;
	int 	item;
	String 	date;
	String 	memo;

	public static final Parcelable.Creator<TransactionData> CREATOR = new Parcelable.Creator<TransactionData>() 
	{
		public TransactionData createFromParcel(Parcel in) {
			return new TransactionData(in);
		}
	
		public TransactionData[] newArray(int size) {
			return new TransactionData[size];
		}
	};

	private TransactionData(Parcel in) {
		id 				= in.readInt();
		type 			= in.readInt();
		infoId 			= in.readInt();
		amount 			= in.readDouble();
		category 	    = in.readInt();
		subcategory 	= in.readInt();
		account 		= in.readInt();
		shop 		    = in.readInt();
		item 		    = in.readInt();
		date 			= in.readString();
		memo 		    = in.readString();
	}
	
	public TransactionData(int type,int infoId, double money, int category, int subcategory, int account, int shop, int item, String date, String memo)
	{
		this.type = type;
		this.infoId = infoId;
		this.amount = money;
		this.category = category;
		this.subcategory = subcategory;
		this.account = account;
		this.shop = shop;
		this.item = item;
		this.date = date;
		this.memo = memo;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(type);
		dest.writeInt(infoId);
		dest.writeDouble(amount);
		dest.writeInt(category);
		dest.writeInt(subcategory);
		dest.writeInt(account);
		dest.writeInt(shop);
		dest.writeInt(item);
		dest.writeString(date);
		dest.writeString(memo);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
}

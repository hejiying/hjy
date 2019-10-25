package com.yc.net.bank;

import java.sql.SQLException;

public class BankDao {
	DbHelper db = new DbHelper();

	public void update(String cardno,float money) throws SQLException {
		String sql="update account set balance=balance + ? where accountid=?";
		db.update(sql, money,cardno);
	}
	
	public void update2(String cardno,float money) throws SQLException{
		String sql="update account set balance =balance - ? where accountid=?";
		db.update(sql, money,cardno);
	}
	
	public int create(String cardno) throws SQLException{
		String sql = "insert into account values(?,null)";
		return db.update(sql,cardno);

	}
	
	
	 
}

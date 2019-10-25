package com.yc.net.bank;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;

public class DbHelper {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	//加载驱动
	static{
		try {
			Class.forName(MyProperties.getInstance().getProperty("driverName"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//获取数据库的连接
	public Connection getConn() throws SQLException {
		conn=DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),
				MyProperties.getInstance());
		return conn;
	}
	//关闭资源
	public void closeALL(Connection conn, PreparedStatement pstmt,ResultSet rs){
		//关闭资源
		if(null!=rs){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(null!=pstmt){
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(null!=conn){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 返回多条记录查询操作  select * from table_name
	 */
	
	public List<Map<String,Object>> selectMutil(String sql,List<Object> params) throws Exception{
		List<Map<String,Object>>list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsList(pstmt,params);
			//获取结果集
			rs = pstmt.executeQuery();
			//根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames(rs);
			while(rs.next()){
				map = new HashMap<String,Object>();
				String typeName = null;
				Object obj = null;
				for(String name:columnNames){
					obj = rs.getObject(name);
				if(null!=obj){
					typeName =obj.getClass().getName();
				}
				if("oracle.sql.BLOB".equals(typeName)){
					//对图片进行处理
					BLOB blob = (BLOB)obj;
					InputStream in = blob.getBinaryStream();
					byte [] bt = new byte[(int)blob.length()];
					in.read(bt);
					map.put(name,bt);
					
				}else{
					map.put(name,obj);
				}
			}
				list.add(map);
		}
	} finally{
		closeALL(conn,pstmt,rs);
	}
			return list;
		
	}
	
	/**
	 * 单记录的查询 select * from table_name where id = ?\
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public Map<String,Object> selectSingle(String sql,List<Object> params) throws SQLException, IOException{
		Map<String,Object> map = null;
		try{
			conn = getConn();
			pstmt  = conn.prepareStatement(sql);
			//设置参数
			setParamsList(pstmt,params);
			//获取结果集
			rs = pstmt.executeQuery();
			//根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames(rs);
			if(rs.next()){
				map = new HashMap<String,Object>();
				String typeName = null;
				Object obj = null;
				for(String name:columnNames){
					obj=rs.getObject(name);
					if(null!=obj){
						typeName = obj.getClass().getName();
					}
					if("oracle.sql.BLOB".equals(typeName)){
						//对图片进行处理
						BLOB blob = (BLOB)obj;
						InputStream in = blob.getBinaryStream();
						byte [] bt = new byte [(int)blob.length()];
						in.read(bt);
						map.put(name,bt);
					}else{
						map.put(name,obj);
					}
				}
			}
		}finally{
			closeALL(conn,pstmt,rs);
		}
		return map;
	}
	/**
	 * 获取查询后的字段名
	 * @throws SQLException 
	 */
	public List<String> getAllColumnNames(ResultSet rs) throws SQLException{
		List<String> list = new ArrayList<String>();
		ResultSetMetaData data = rs.getMetaData();
		int count = data.getColumnCount();
		for(int i =1;i<=count;i++){
			String str = data.getColumnLabel(i);
			//添加列名到List集合中
			list.add(str);
		}
		return list;
	}
	//将集合设置到预编译对象中
	public void setParamsList(PreparedStatement pstmt,List<Object> params) throws SQLException{
		if(null == params || params.size()<=0){
			return ;
		}
		for(int i =0;i<params.size();i++){
			pstmt.setObject(i+1, params.get(i));
		}
	}
	/**
	 * 批处理操作  多个insert update delect 同一个事务
	 * sqls  多条sql语句
	 * params 多条sql语句的参数  每条sql语句参数小List集合中  多个再 封装到大的list集合  一一对应
	 * return
	 */
	public int update(List<String> sqls,List<List<Object>> params)throws SQLException{
		int result=0;
		try {
			conn = getConn();
			//设置事务手动提交
			conn.setAutoCommit(false);
			//循环sql语句
			if(null ==sqls || sqls.size()<=0){
				return result;
			}
			for(int i =0;i<sqls.size();i++){
				//获取sql语句并创建预编译对象
				pstmt = conn.prepareStatement(sqls.get(i));
				//获取对应的sql语句参数集合
				List<Object> param = params.get(i);
				//设置参数
				setParamsList(pstmt, param);
				//执行更新
				result = pstmt.executeUpdate();
				if(result<=0){
					return result;
				}
			}
		} catch (Exception e) {
			conn.rollback();
			result = 0;
		}finally{
			//还原事务的状态
			conn.setAutoCommit(true);
			closeALL(conn, pstmt, rs);
		}
		return result;
	}
	
	
	/**
	 * 更新操作  增删改
	 * @throws SQLException 
	 */
	public int update(String sql,Object...params) throws SQLException{
		int result = 0;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			//设置参数
			setParamsObject(pstmt, params);
			//执行
			result = pstmt.executeUpdate();
		}finally{
			closeALL(conn,pstmt,null);
		}
		return result;
	}
	//不定长参数 设置参数  传入的参数顺序必须和问号的的顺序一致
	public void setParamsObject(PreparedStatement pstmt,Object...params) throws SQLException{
		if(null ==params ||params.length<=0){
			return ;
		}
		for(int i=0;i<params.length;i++){
			pstmt.setObject(i+1, params[i]);
		}
	}
	/**
	 * 聚合函数操作 select count(*) from table_name
	 * sql
	 * patams
	 * return
	 * @throws SQLException 
	 */
	public double getPolymer(String sql , List<Object> params) throws SQLException{
		double result = 0;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			setParamsList(pstmt,params);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getDouble(1);
			}
		}finally{
			closeALL(conn,pstmt,rs);
		};
		return result;
	}
}












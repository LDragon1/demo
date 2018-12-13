package com.uwang.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 用户实现类
 * 持久层（持久化）
 *
 */
public class DBUtil  {

	public static String dbDriver;
	public static String dbUrl;
	public static String dbUser;
	public static String dbPassword;
	
	public static Connection connection = null;
	public static PreparedStatement preparedStatement = null;
	public static ResultSet resultSet = null;
	
	/**
	 * 加载类就会执行
	 */
	static {
		// 加载配置文件
		getConfig();
	}
	
	/**
	 * 读取properties配置文件的值
	 * 永远返回的都是String类型
	 */
	public static void getConfig() {
		InputStream inStream = null;
		try {
			//构建一个读取properties配置文件的实例
			Properties properties = new Properties();
			// 底层通过流的操作把配置文件拿进来
//			inStream = DBUtil.class.getClassLoader().getResourceAsStream("jdbc-config.properties");
			inStream = DBUtil.class.getClassLoader().getResourceAsStream("com/uwang/jdbc/config/jdbc-config.properties");
			// 把文件交给properties对象管理（加载）
			properties.load(inStream);
			dbDriver = properties.getProperty("jdbc.driver");
			dbUrl = properties.getProperty("jdbc.url");
			dbUser = properties.getProperty("jdbc.user");
			dbPassword = properties.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("找不到文件");
		}finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 创建连接
	 * @return
	 */
	public static Connection getConnection() {
		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到类……");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("链接数据库失败……");
		}
		return connection;
	}
	
	/**
	 * 封装新增、删除、修改
	 * @param sql
	 * @param pram
	 * @return
	 */
	public static int update(String sql, Object...  pram) {
		int result = 0;
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(pram.length !=0 && pram != null) {
				for(int i = 0; i < pram.length;  i++) {
					preparedStatement.setObject((i+1), pram[i]);
				}
			}
			// 查看自动拼装的sql
			System.out.println(preparedStatement.toString());
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(preparedStatement, connection);
		}
		return result;
	}
	

	/**
	 * 封装新增、删除、修改
	 * @param sql
	 * @param pram
	 * @return
	 */
	public static int update(String sql, List<Object> pram) {
		int result = 0;
		connection = DBUtil.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(pram.size() !=0 && pram != null) {
				for(int i = 0; i < pram.size();  i++) {
					preparedStatement.setObject((i+1), pram.get(i));
				}
			}
			// 查看自动拼装的sql
			System.out.println(preparedStatement.toString());
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(preparedStatement, connection);
		}
		return result;
	}
	
	
	
	
	/**
	 * 关闭resultSet
	 * @param resultSet
	 */
	public static void close(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("ResultSet关闭失败……");
			}
		}
	}
	
	/**
	 * 关闭preparedStatement
	 * @param resultSet
	 */
	public static void close(PreparedStatement preparedStatement) {
		if(preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("PreparedStatement关闭失败……");
			}
		}
	}
	
	/**
	 * 关闭preparedStatement
	 * @param resultSet
	 */
	public static void close(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection关闭失败……");
			}
		}
	}
	
	/**
	 * 关闭连接
	 * @param resultSet
	 * @param preparedStatement
	 * @param connection
	 */
	public static void close(ResultSet resultSet,  PreparedStatement preparedStatement, Connection connection) {
		close(resultSet);
		close(preparedStatement);
		close(connection);
	}
	
	/**
	 * 关闭连接
	 * @param preparedStatement
	 * @param connection
	 */
	public static void close(PreparedStatement preparedStatement, Connection connection) {
		close(preparedStatement);
		close(connection);
	}
	
}

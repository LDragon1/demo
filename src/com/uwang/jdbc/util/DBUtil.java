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
 * �û�ʵ����
 * �־ò㣨�־û���
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
	 * ������ͻ�ִ��
	 */
	static {
		// ���������ļ�
		getConfig();
	}
	
	/**
	 * ��ȡproperties�����ļ���ֵ
	 * ��Զ���صĶ���String����
	 */
	public static void getConfig() {
		InputStream inStream = null;
		try {
			//����һ����ȡproperties�����ļ���ʵ��
			Properties properties = new Properties();
			// �ײ�ͨ�����Ĳ����������ļ��ý���
//			inStream = DBUtil.class.getClassLoader().getResourceAsStream("jdbc-config.properties");
			inStream = DBUtil.class.getClassLoader().getResourceAsStream("com/uwang/jdbc/config/jdbc-config.properties");
			// ���ļ�����properties����������أ�
			properties.load(inStream);
			dbDriver = properties.getProperty("jdbc.driver");
			dbUrl = properties.getProperty("jdbc.url");
			dbUser = properties.getProperty("jdbc.user");
			dbPassword = properties.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�Ҳ����ļ�");
		}finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * ��������
	 * @return
	 */
	public static Connection getConnection() {
		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("�Ҳ����࡭��");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("�������ݿ�ʧ�ܡ���");
		}
		return connection;
	}
	
	/**
	 * ��װ������ɾ�����޸�
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
			// �鿴�Զ�ƴװ��sql
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
	 * ��װ������ɾ�����޸�
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
			// �鿴�Զ�ƴװ��sql
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
	 * �ر�resultSet
	 * @param resultSet
	 */
	public static void close(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("ResultSet�ر�ʧ�ܡ���");
			}
		}
	}
	
	/**
	 * �ر�preparedStatement
	 * @param resultSet
	 */
	public static void close(PreparedStatement preparedStatement) {
		if(preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("PreparedStatement�ر�ʧ�ܡ���");
			}
		}
	}
	
	/**
	 * �ر�preparedStatement
	 * @param resultSet
	 */
	public static void close(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection�ر�ʧ�ܡ���");
			}
		}
	}
	
	/**
	 * �ر�����
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
	 * �ر�����
	 * @param preparedStatement
	 * @param connection
	 */
	public static void close(PreparedStatement preparedStatement, Connection connection) {
		close(preparedStatement);
		close(connection);
	}
	
}

package util;

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

public class JDBCUtil {
	
	//싱글톤 패턴 : 인스턴스 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴
	private JDBCUtil() {
		
	}
	
	private static JDBCUtil instance;
	
	public static JDBCUtil getInstance() { //다른 곳에 빌려주는 메서드
		if(instance == null) { // 처음(null)일때만 객체를 생성한다.
			instance = new JDBCUtil();
		}
		return instance;
	}
	
	String url = "jdbc:oracle:thin:@192.168.32.74:1521:xe";
	String user = "PRJ";
	String password = "java";		
	
	//데이터베이스 접속 정보
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	/* 
	 * select 결과가 한줄로 나올수 있을 때 쓰는 메서드
	 * Map<String, Object> selectOne(String sql) //조회 결과가 한 줄, ? 없음
	 * Map<String, Object> selectOne(String sql, List<Object> param) // 조회 결과가 한 줄, ? 있음
	 * 
	 * select 결과가 여러줄이 나올수 있을 때 쓰는 메서드
	 * List<Map<String, Object>> selectOne(String sql) // 조회 결과 여러 줄, ? 없음 
	 * List<Map<String, Object>> selectOne(String sql, List<Object> param) // 조회 결과 여러 줄, ? 있음
	 * 
	 * select 제외한 나머지의 경우 쓰는 메서드
	 * int update(String sql) // 영향 받은 행 수를 int 타입으로 리턴, ? 없음
	 * int update(String sql, List<Object> param) // 영향 받은 행 수를 int 타입으로 리턴, ? 있음
	 */
	
	
	
	// 조회 결과가 한 줄, ? 없음
	public Map<String, Object> selectOne(String sql){
		
		HashMap<String, Object> map = null;

		try {
			// 입력한 정보로 데이터베이스에 연결
			con = DriverManager.getConnection(url, user, password);

			// 입력 받은 SQL 구문을 실행
			ps = con.prepareStatement(sql);

			// select : ResultSet을 리턴
			rs = ps.executeQuery();

			ResultSetMetaData metaData = rs.getMetaData(); // 메타데이터 : 데이터에 대한 데이터
			int columnCount = metaData.getColumnCount(); // 컬럼 수

			// 모든 데이터를 한 번에 가져올 수 없기 때문에 한 행씩 가져온다.
			// next()가 호출되면 첫 번째 행을 바라보게 되고 값을 추출할 수 있게 된다.
			// next()가 호출될 때마다 다음 행을 바라보게 된다.
			// 다음 행이 있으면 true, 없으면 false를 리턴한다.
			if (rs.next()) {// 키, 값을 맵에 저장(인덱스가 1부터 시작한다.)
				map = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// ResultSet, Statement, Connection 닫기
			// 반드시 닫아줘야 하므로 finally에서 실행(객체가 없을 때만)
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (ps != null) try { ps.close(); } catch (Exception e) {}
			if (con != null) try { con.close(); } catch (Exception e) {}
		}
		return map;
	}
	
	
	
	// 조회 결과가 한 줄, ? 있음
    public Map<String, Object> selectOne(String sql, List<Object> param){
        HashMap<String, Object> map = null;
		
		try {
			//입력한 정보로 데이터베이스에 연결
			con = DriverManager.getConnection(url, user,password);
			
			ps = con.prepareStatement(sql); //입력 받은 sql 구문 실행
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1, param.get(i));
			}
			
			rs = ps.executeQuery(); //select : ResultSet 리턴
			ResultSetMetaData metaData = rs.getMetaData(); //메타데이터 : 데이터에 대한 데이터
			int columnCount = metaData.getColumnCount(); //컬럼 수
			
			// 모든 데이터를 한 번에 가죠올 수 없기 때문에 한 행씩 가져온다.
			// next()가 호출되면 첫 번째 행을 바라보게 되고 값을 추출할 수 있게 된다. 
			// next()가 호출될 때마다 다음 행을 바라보게 된다.
			// 다음 행이 있으면 true, 없으면 false를 리턴한다.
			if (rs.next()) { // 키, 값을 맵에 저장
				map = new HashMap<>();				
				for (int i =1; i <= columnCount; i++) {
					map.put(metaData.getCatalogName(i), rs.getObject(i));
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// ResultSet, Statement, Connection 닫기
			// 반드시 닫아줘야 하므로  finally에서 실행 (객체가 없을 때만)
			if (rs != null) try { rs.close();} catch (Exception e) {}
			if (ps != null) try { ps.close();} catch (Exception e) {}
			if (con != null) try { con.close();} catch (Exception e) {}
		}
		return map;
    }
    
	
	
	// 조회 결과 여러 줄, ? 없음
	public List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			//입력한 정보로 데이터베이스 접속
			con = DriverManager.getConnection(url,user,password);
			
			//입력받은 sql 구문을 실행
			ps = con.prepareStatement(sql);
			
			//select : ResultSet을 리턴
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData(); //메타데이터 : 데이터에 대한 데이터 	
		 	int columnCount = metaData.getColumnCount(); //컬럼 수
			
		 	while (rs.next()) {
		 		HashMap<String, Object> map = new HashMap<>();
		 		for (int i = 1; i <= columnCount; i++) {
		 			map.put(metaData.getColumnName(i), rs.getObject(i));
		 		}
		 		list.add(map);
		 	}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			if (rs != null) try { rs.close();} catch (Exception e) {}
			if (ps != null) try { ps.close();} catch (Exception e) {}
			if (con != null) try { con.close();} catch (Exception e) {}
		}
		return list;
	}
	
	
	
	// 조회 결과 여러 줄, ? 있음	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		
		ArrayList<Map<String,Object>> list = new ArrayList<>();
		
	    try {			
	    	con = DriverManager.getConnection(url, user, password);
				
		
	    	ps = con.prepareStatement(sql); // 쿼리 만드는 부분
	    	for(int i = 0; i < param.size(); i++) { // param은 인덱스가 0부터 시작함(ArrayList)
	    		ps.setObject(i + 1,param.get(i)); //?는 인덱스가 1부터 시작
	    	}
	 	
	    	rs = ps.executeQuery();
	 	
	    	ResultSetMetaData metaData = rs.getMetaData();	 	
	 	
	    	int columnCount = metaData.getColumnCount();
	 	
	    	while(rs.next()) {
	    		HashMap<String,Object>map = new HashMap<>();
	    		for(int i = 1; i <= columnCount; i++) {
	    			map.put(metaData.getColumnName(i),  rs.getObject(i));
	    		} // 키, 값을 맵에 저장
	    		list.add(map); // 한 줄을 ArrayList에 저장
	    	}
	 	
	    } catch (SQLException e) {
			e.printStackTrace();
	    } finally { // 반드시 닫아줘야 하므로 finally에서 실행
	    	if(rs != null) try {rs.close(); } catch(Exception e) {}
	    	if(ps != null) try {ps.close(); } catch(Exception e) {}
	    	if(con != null) try {con.close(); } catch(Exception e) {}
	    } // 모든 데이터가 들어간 ArrayList를 리턴	
	    return list;   	    
	}
	
	
	
	// 영향 받은 행 수를 int 타입으로 리턴, ? 없음
	public int update(String sql){
		int result = 0;

		try {
			con = DriverManager.getConnection(url, user, password);

			ps = con.prepareStatement(sql);

			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 반드시 닫아줘야 하므로 finally에서 실행
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (ps != null) try { ps.close(); } catch (Exception e) {}
			if (con != null) try { con.close(); } catch (Exception e) {}
		}
		return result;	
	}
	


	// 영향 받은 행 수를 int 타입으로 리턴, ? 있음
	public int update(String sql, List<Object> param){
		int result = 0;

		try {
			con = DriverManager.getConnection(url, user, password);

			ps = con.prepareStatement(sql);
			for (int i = 0; i < param.size(); i++) { // param은 인덱스가 0부터 시작함(ArrayList)
				ps.setObject(i + 1, param.get(i)); // ?는 인덱스가 1부터 시작
			}

			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 반드시 닫아줘야 하므로 finally에서 실행한다.
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (ps != null) try { ps.close(); } catch (Exception e) {}
			if (con != null) try { con.close(); } catch (Exception e) {}
		}
		return result;	
	}
	
}



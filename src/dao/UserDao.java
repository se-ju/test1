package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.UserService;
import util.JDBCUtil;

public class UserDao {
	
	//싱글톤 패턴
		private UserDao() {}
		private static UserDao instance;
		public static UserDao getInstance() {
			if(instance == null) {
				instance = new UserDao();
			}
			return instance;
		}
		
		private JDBCUtil jdbc = JDBCUtil.getInstance();
		
		public int insertMember(Map<String, Object> param) {
			String sql = "INSERT INTO TB_JDBC_MEMBER VALUES (?, ?, ?)" ;
			
			List<Object> p = new ArrayList<>();
			p.add(param.get("MEM_ID"));
			p.add(param.get("PASSWORD"));
			p.add(param.get("MEM_NAME"));
			
			return jdbc.update(sql,p);
		}
		
		// 로그인
		public Map<String, Object> selectMember(String memId, String password) {
			
			String sql = "SELECT MEM_ID, PASSWORD, MEM_NAME"
					   + "  FROM TB_JDBC_MEMBER"
					   + " WHERE MEM_ID = ?"
					   + "   AND PASSWORD = ?";
			
			List<Object>param = new ArrayList<>();
			param.add(memId);
			param.add(password);
									
			return jdbc.selectOne(sql, param);
		}
		
		
		

}






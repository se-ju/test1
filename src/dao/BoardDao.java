package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.BoardService;
import util.JDBCUtil;

public class BoardDao {
	
			//싱글톤 패턴
			private BoardDao() {}
			private static BoardDao instance;
			public static BoardDao getInstance() {
				if(instance == null) {
					instance = new BoardDao();
				}
				return instance;
			}
			
			private JDBCUtil jdbc = JDBCUtil.getInstance();
			
			// 첫 화면 - 등록된 글을 내림차순으로 정리			
			public List<Map<String,Object>> selectBoardList(){
				String sql = "SELECT A.BOARD_NO"
						   + "      , A.TITLE"
						   + "      , A.CONTENT"
						   + "      , B.MEM_NAME"
						   + "      , TO_CHAR(A.REG_DATE, 'MM-DD') as REG_DATE"
						   + "   FROM TB_JDBC_BOARD A"
						   + "   LEFT OUTER JOIN TB_JDBC_MEMBER B ON A.MEM_ID = B.MEM_ID"
						   + " ORDER BY A.BOARD_NO DESC";
				return jdbc.selectList(sql);
			}
			
			// 등록
			public int insertBoard(String title, String content, String memId) {
				String sql = "INSERT INTO TB_JDBC_BOARD"
						+ "   VALUES ("
						+ "    (SELECT NVL(MAX(BOARD_NO), 0) + 1 FROM TB_JDBC_BOARD)"
						+ "          , ?"
						+ "          , ?"
						+ "          , ?"
						+ "          , SYSDATE)";
				
				ArrayList<Object> param = new ArrayList<>();
				param.add(title);
				param.add(content);
				param.add(memId);
				
				return jdbc.update(sql, param);
			}

			// 조회
			public Map<String, Object> selectBoard(int boardNo) {
				String sql = "SELECT A.BOARD_NO"
						+ "        , A.TITLE"
						+ "        , A.CONTENT"
						+ "        , B.MEM_NAME"
						+ "        , A.REG_DATE"
						+ "     FROM TB_JDBC_BOARD A"
						+ "     LEFT OUTER JOIN TB_JDBC_USER B ON A.USER_ID = B.USER_ID"
						+ "    WHERE A.BOARD_NO = ?";
				
				ArrayList<Object> param = new ArrayList<>();
				param.add(boardNo);
				
				return jdbc.selectOne(sql, param);
			}

			// 조회 - 수정
			public int updateBoard(String title, String content, int boardNo) {
				String sql = "UPDATE TB_JDBC_BOARD"
						+ "      SET TITLE = ?"
						+ "        , CONTENT = ?"
						+ "    WHERE BOARD_NO = ?";
				
				ArrayList<Object> param = new ArrayList<>();
				param.add(title);
				param.add(content);
				param.add(boardNo);
				
				return jdbc.update(sql, param);
			}

			// 조회 - 삭제
			public int deleteBoard(int boardNo){
				String sql = "DELETE FROM TB_JDBC_BOARD"
						+ "    WHERE BOARD_NO = ?";
				
				ArrayList<Object> param = new ArrayList<>();
				param.add(boardNo);
				
				return jdbc.update(sql, param);
			}
			

}

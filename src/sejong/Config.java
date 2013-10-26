package sejong;

import java.util.HashMap;

/**
 * @author Heoungjun Yu
 * @version 2013.04.13
 * 
 */
public class Config {
	/**
	 * �Խ��� ���� �Ľ��� ���ϸ� ��� ���
	 */
	public static final String NOTICE = "notice";
	public static final String ADMISSION = "admission";
	public static final String COLLEGE = "college";
	public static final String FOREIGN = "foreign";
	public static final String JOB = "job";
	public static final String SCHOOL = "school";

	/**
	 * Map ������ Key String ���� ����ϱ� ���� ���ڿ�. ���ο��� ���
	 */
	public static final String PREVIEW = "preview";
	public static final String CONTENT = "content";

	private HashMap<String, HashMap<String, String>> map;

	/**
	 * ���������� �Ľ��� ����� URL�� ��� ����
	 */
	public Config() {
		try {
			HashMap<String, String> value = new HashMap<String, String>(); // value �ӽ� ����
			map = new HashMap<String, HashMap<String,String>>();

			/* url input */
			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=57&curPage=1&board_seq=62911&search=&column=&sBoard_id=57",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=57&command=view&curPage=1&board_seq=");
			map.put(NOTICE, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=59&curPage=1&board_seq=62805&search=&column=&sBoard_id=59",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=59&command=view&curPage=1&board_seq=");
			map.put(ADMISSION, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=60&curPage=1&board_seq=62752&search=&column=&sBoard_id=60",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=60&command=view&curPage=1&board_seq=");
			map.put(COLLEGE, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=131&curPage=1&board_seq=62942&search=&column=&sBoard_id=131",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=131&command=view&curPage=1&board_seq=");
			map.put(FOREIGN, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=181&curPage=1&board_seq=62942&search=&column=&sBoard_id=181",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=181&command=view&curPage=1&board_seq=");
			map.put(JOB, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=197&curPage=1&board_seq=62947&search=&column=&sBoard_id=197",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=197&command=view&curPage=1&board_seq=");
			map.put(SCHOOL, value); // ���� �ʿ�... �̻��ϰ� �ȵ�.. ;

			/* end */
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
	}

	/**
	 * @param preview
	 * @param content
	 * @return preview, content �� Ű�� ������ ��
	 */
	public HashMap<String, String> mapping(String preview, String content) {
		HashMap<String, String> urls = new HashMap<String, String>();
		urls.put(PREVIEW, preview);
		urls.put(CONTENT, content);

		return urls;
	}

	/**
	 * ���� ���Ե� ���ڿ��� ����� HashMap ����
	 * 
	 * @return HashMap
	 */
	public HashMap<String, HashMap<String, String>> getMap() {
		return this.map;
	}
}

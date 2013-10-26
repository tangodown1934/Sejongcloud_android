package sejong;

/**
 * 게시글을 파싱해서 담는 클래스
 * 
 * @author Heoungjun Yu
 */
public class Article {
	private String title; // 글 제목
	private String content; // 글 내용
	private String id; // 해당
	private String date;
	private String user;
	private String hit;
	private String handle;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return this.date;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return this.user;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getHit() {
		return this.hit;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public String getHandle() {
		return this.handle;
	}
}


public class User 
{
	boolean isAdmin;
	private String id;
	private String pw;
	User() {
		id = "stu";
		pw = "pass";
		isAdmin = true;
	}
	public synchronized boolean isAdmin() {
		return isAdmin;
	}
	public synchronized void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public synchronized String getId() {
		return id;
	}
	public synchronized void setId(String id) {
		this.id = id;
	}
	public synchronized String getPw() {
		return pw;
	}
	public synchronized void setPw(String pw) {
		this.pw = pw;
	}
}

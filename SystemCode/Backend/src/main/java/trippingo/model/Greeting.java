package trippingo.model;

public class Greeting {

	private long id;
	private String message;
	public Greeting(long id, String message) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.message = message;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}

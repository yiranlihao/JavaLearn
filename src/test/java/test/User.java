package test;

public class User {

	public User() {

	}

	private int id;

	private int Tid;

	private String name;

	private long Pid;
	
	private static String name1;
	
	static{
		
		name1="lihao";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return Tid;
	}

	public void setTid(int tid) {
		Tid = tid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPid() {
		return Pid;
	}

	public void setPid(long pid) {
		Pid = pid;
	}

	public static String getName1() {
		return name1;
	}

	public static void setName1(String name1) {
		User.name1 = name1;
	}

}

package cs701.client;

public class Course {

	private String courseId;
	private String courseName;
	private String courseProf;
	private String courseDay;
	
	public Course(String id, String name, 
			String courseProf, String courseDay) {
		this.courseId = id;
		this.courseName = name;
		this.courseProf = courseProf;
		this.courseDay = courseDay;
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseProf() {
		return courseProf;
	}
	public void setCourseProf(String courseProf) {
		this.courseProf = courseProf;
	}
	
	public String getCourseDay() {
		return courseDay;
	}
	public void setCourseDay(String courseDay) {
		this.courseDay = courseDay;
	}
}

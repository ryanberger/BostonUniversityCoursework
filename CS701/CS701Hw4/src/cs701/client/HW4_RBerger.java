package cs701.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;

import com.google.gwt.user.client.ui.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import java.util.List;
import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HW4_RBerger implements EntryPoint {
  
	private Widget selectedDepartmentRow;
	private Department selectedDepartment;
	private VerticalPanel coursesWidget;
	private VerticalPanel selectedCoursesWidget;
	private List<Course> selectedCoursesList = new ArrayList<Course>();
	private final String departments = "<b>Departments</b><br/><br/>";
	private final String courseOfferings = "<b>Course Offerings</b><br/><br/>";
	private final String selectedCourses = "<br/><b>Selected Courses</b><br/><br/>";

	/**
	 * This is the entry point method.
     */
	public void onModuleLoad() {
		DockPanel mainPanel = new DockPanel();
		Document.get().setTitle ("HW4");
		mainPanel.setBorderWidth(5);
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		Widget header = createHeaderWidget();
		mainPanel.add(header, DockPanel.NORTH);
		mainPanel.setCellHeight(header, "30px");
		Widget footer = createFooterWidget();
		mainPanel.add(footer, DockPanel.SOUTH);
		mainPanel.setCellHeight(footer, "25px");
		
		HorizontalSplitPanel departmentsAndCourses = 
			new HorizontalSplitPanel();
		departmentsAndCourses.setSplitPosition("200px");
		Widget departments = createDepartmentsWidget();
		departmentsAndCourses.setLeftWidget(departments);
		Widget courses = createCoursesWidget();
		departmentsAndCourses.setRightWidget(courses);
		mainPanel.add(departmentsAndCourses, DockPanel.CENTER);
		
		
		VerticalSplitPanel selectedCourses = 
			new VerticalSplitPanel();
		selectedCourses.setSplitPosition("500px");
		Widget selectedWidget = createSelectedWidget();
		selectedCourses.setTopWidget(selectedWidget);
		mainPanel.add(selectedCourses, DockPanel.SOUTH);

		RootPanel.get().add(mainPanel);
	}

	/**
	 * Creates the header part of the layout.
	 */
	protected Widget createHeaderWidget() {
		return new Label("Course Selections");
	}
	
	/**
	 * Creates the footer part of the layout.
	 */
	protected Widget createFooterWidget() {
		HTML footer = new HTML("<em>Boston University</em>");
		footer.setStyleName("footer");
		return footer;
	}
	
	/**
	 * Creates the widget that will display the list of 
	 * departments on the left side of the screen
	 */
	protected Widget createDepartmentsWidget() {
		VerticalPanel facultyList = new VerticalPanel();
		facultyList.setWidth("100%");
		facultyList.add(new HTML(departments));
		List<Department> departments = getAllDepartments();
		for (final Department ins : departments) {
			Widget departmentRow = createDepartmentRow(ins);
			facultyList.add(departmentRow);
		}
		return facultyList;
	}
	
	/**
	 * Creates the widget that will display a department 
	 * in the departments list
	 */
	protected Widget createDepartmentRow(final Department department) {
		final Label row = new Label(department.getName());
		row.setWordWrap(false);
		row.setStyleName("departmentRow");
		row.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (row.equals(selectedDepartmentRow)) {
					return; // do nothing as it is already selected
				}
				markSelectedDept(selectedDepartmentRow, false);
				markSelectedDept(row, true);
				selectedDepartmentRow = row;
				selectedDepartment = department;
				updateCoursesList();
			}
		});
		return row;
	}
	
	/**
	 * Marks the given department row as selected/unselected
	 */
	protected void markSelectedDept(Widget departmentRow, boolean selected) {
		if (departmentRow == null) {
			return;
		}
		if (selected) {
			departmentRow.addStyleName("selectedDepartment");
			departmentRow.removeStyleName("unselectedDepartment");
		} else {
			departmentRow.addStyleName("unselectedDepartment");
			departmentRow.removeStyleName("selectedDepartment");
		}
	}
 
	/**
	 * Updates the courses shown on the right side of the screen 
	 * to match the currently selected department.
	 */
	public void updateCoursesList() {
		List<Course> courses = getCoursesForSelectedDepartment();
		coursesWidget.clear();
		coursesWidget.add(new HTML(courseOfferings));
		for (Course c : courses) {
			Widget courseRow = createCourseRow(c);
			coursesWidget.add(courseRow);
		}
	}
	
	public void updateSelectedCoursesList(Course course) {
		selectedCoursesWidget.clear();
		selectedCoursesWidget.add(new HTML(selectedCourses));
		for (Course c : selectedCoursesList) {
			Widget courseRow = createSelectedCourseRow(c);
			selectedCoursesWidget.add(courseRow);
		}
	}
	
	/**
	 * Creates the widget that will display the list of courses 
	 * on the right side of the screen
	 */
	public Widget createCoursesWidget() {
		coursesWidget = new VerticalPanel();
		coursesWidget.setWidth("100%");
		coursesWidget.add(new HTML(courseOfferings));
		return coursesWidget;
	}
	
	/**
	 * Creates the widget that will display the list of 
	 * selected courses on the bottom of the screen
	 */
	public Widget createSelectedWidget() {
		selectedCoursesWidget = new VerticalPanel();
		selectedCoursesWidget.setWidth("100%");
		selectedCoursesWidget.add(new HTML(selectedCourses));
		return selectedCoursesWidget;
	}

	/**
	 * Creates the widget that display a course in the courses list. 
	 * This widget shows the name of the course and a checkbox next to it).
	 */
	public Widget createCourseRow(final Course course) {
		HorizontalPanel row = new HorizontalPanel();
		final CheckBox checkbox = new CheckBox();
		
		if (isSelected(selectedCoursesList, course.getCourseId()))
		{
			checkbox.setValue(true);
		}
		
		checkbox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (checkbox.getValue() == true)
				{
					selectedCoursesList.add(course);
				}
				else
				{
					removeSelected(selectedCoursesList, course.getCourseId());
				}
				updateSelectedCoursesList(course);
			}
		});
		row.add(checkbox);
		row.add(new Label(course.getCourseId() + " - " + course.getCourseName()));
		return row;
	}
	
	/**
	 * Determine if a Course object is contained in a List
	 */
	private boolean isSelected(List<Course> courseList, String id)
	{
		for (Course c: courseList)
		{
			if (c.getCourseId() == id)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Remove a Course object from a List
	 */
	private void removeSelected(List<Course> courseList, String id)
	{
		int index = 0;
		for (Course c: courseList)
		{
			if (c.getCourseId() == id)
			{
				break;
			}
			index++;
		}
		courseList.remove(index);
	}
	
	/**
	 * Creates the widget that display all selected courses in the selected courses list. 
	 */
	public Widget createSelectedCourseRow(Course course) {
		HorizontalPanel row = new HorizontalPanel();
		row.add(new Label(course.getCourseName() + " (" + course.getCourseId() + ") by Prof. " +
				course.getCourseProf() + " on " + course.getCourseDay()));
		return row;
	}
	
	/**
	 * Returns a list of all available departments.
	 */
	public List<Department> getAllDepartments() {
		List<Department> deptList = new ArrayList<Department>();
		deptList.add(new Department("met", "MET Computer Science"));
		deptList.add(new Department("cas", "CAS Computer Science"));
		return deptList;
	}
	
	/**
	 * Returns a list of courses for the currently 
	 * selected department.
	 */
	public List<Course> getCoursesForSelectedDepartment() {
		List<Course> courses = new ArrayList<Course>();
		if (selectedDepartment == null) {
			return courses;
		}
		else if (selectedDepartment.getId().equals("met")) {
			courses.add(new Course("MET CS341", 
				"Data Structures", "Maslanka", "Wednesday"));
			courses.add(new Course("MET CS701", 
				"Advanced Web Application Development", "Kalathur", "Tuesday"));
			return courses;
		}
		else if (selectedDepartment.getId().equals("cas")) {
			courses.add(new Course("CAS CS105", 
				"Databases", "Sullivan", "Monday"));
			courses.add(new Course("CAS CS455", 
				"Computer Networks", "Crovella", "Thursday"));
			return courses;
		}
		return courses;
	}
	
}

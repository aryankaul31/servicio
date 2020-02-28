

import java.util.*;


import javafx.beans.property.SimpleStringProperty;

import java.time.*;

public class Student {
	/**
	 * The variables are initialized with protected in order to make sure that classes in other packages will not be able to access it
	 */
	protected SimpleStringProperty id;
	protected SimpleStringProperty lastName, firstName, serviceHours, grade; 
	protected LocalDate updatedDate;
	protected SimpleStringProperty category;
	protected SimpleStringProperty hoursDiff;
//______________________________________________________________________________________________________
//_____________________________________________________________________________________________________
		
	/**
	 * A constructor for the initialization of students within the edit student tab
	 * @param id Id of student
	 * @param lastName Last Name of the Student
	 * @param firstName First Name of the student
	 * @param grade Students grade
	 */
	public Student(String id, String lastName, String firstName, String grade) {
		
		this.id = new SimpleStringProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.grade = new SimpleStringProperty(grade);
	}
	/**
	 * This constructor exists for the difference in hours between reports, helping in determining the service hours 
	 * weekly nd monthly and the program category changes
	 * @param lastName A students last name
	 * @param firstName A students first name
	 * @param hoursDiff The difference in hours between 2 dates for a single person
	 * @param category The program category for a student
	 * @param updatedDate Irrelevant for this constructor, look at next constructor for use
	 */
	public Student(String lastName, String firstName, String hoursDiff, String category, LocalDate updatedDate) {
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.hoursDiff = new SimpleStringProperty(hoursDiff);
		this.category = new SimpleStringProperty(category);
		this.updatedDate = updatedDate;
	}
	/**
	 * This constructor exists for the report.txt because it gets date information for reports
	 * @param id A students ID number
	 * @param lastName A students last name
	 * @param firstName A students first  name
	 * @param grade A students grade
	 * @param serviceHours The amount of service hours a student has
	 * @param category The program category for a student
	 * @param updatedDate The latest date that hours or category was edited
	 */
	public Student(String id, String lastName, String firstName, String grade, String serviceHours, String category, LocalDate updatedDate) {
		
		this.id = new SimpleStringProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.category = new SimpleStringProperty(category);
		this.grade = new SimpleStringProperty(grade);
		this.serviceHours = new SimpleStringProperty(serviceHours);
		this.updatedDate = updatedDate;
		
	}

	
//_________________________________________________________________________________________________
	
	
	
	public String toString() {
		
		return ("ID:" + id + "\tName: " + lastName + ", " + firstName + "\tGrade: " + grade) + "\thours Difference: " + hoursDiff;
	}
	public String getHoursDiff() {
		return hoursDiff.get();
	}
	public String getId() {
		return id.get();
	}
	public void setId(SimpleStringProperty id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName.get();
	}
	public void setGrade(SimpleStringProperty grade) {
		this.grade = grade;
	}
	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}
	
	public void setHours(SimpleStringProperty serviceHours) {
		this.serviceHours = serviceHours;
	}
	public String getGrade() {
		return grade.get();
	}
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public String getServiceHours() {
		return serviceHours.get();
	}
	public String getCategory() {
		return category.get();
	}
	
	public void setCategory(SimpleStringProperty category) {
		this.category = category;
	}
	public LocalDate getDate() {
		return updatedDate;
	}

}

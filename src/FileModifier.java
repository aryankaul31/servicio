import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * This class is here in order to communicate with the database and upload new additions for dynamic backing 
 * to the database. THis communicates with both the reports and student text files for minimization of database errors.
 * @author Aryan Kaul
 *
 */
public class FileModifier {

	ObservableList<Student> students = FXCollections.observableArrayList();
	ObservableList<Student> reports = FXCollections.observableArrayList();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy");


/**
 * This method is used to return the student data 
 * @param fileName The file that the student data comes from
 * @return An Observable list of students in order to populate tableviews
 */
	public ObservableList<Student> getStudentData(String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));
			String currId = null;
			int counter = -1;
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] studentArray = s.split("\\|", 6);
				Student student = new Student(studentArray[0], studentArray[1], studentArray[2], studentArray[3], studentArray[4], studentArray[5], null);
				students.add(student);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found.");
		}

		return students;

	}

/**
 * This method is used to add the newly updated student to the report text file
 * @param fileName The file that is used for adding report data 
 * @param student The student that was recently updated for dynamic backing
 */
	public void addDataToReport(String fileName, Student student) {
		try {
			System.out.print(fileName);
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw  = new PrintWriter(fw);
			pw.write(student.getId() + "|" + student.getLastName() + "|" + student.getFirstName() + "|" + student.getGrade() + "|" + student.getServiceHours() + "|" + student.getCategory() + "|" + dtf.format(LocalDateTime.now()));
			pw.write("\n");
			pw.close();
			fw.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * This method is used in order to decipher the report file and find out at what time the students were updated last in order
	 * to include the proper information in the reports.
	 * @return An arraylist of the service hours at the updated time closest to a week before now.
	 */
	public ArrayList<Student> differenceInHours() {
		ArrayList<Student> hoursList = new ArrayList<Student>();
		reports = getReportData("src/reports.txt");
		ArrayList<ArrayList<Student>> separated = new ArrayList<ArrayList<Student>>();
		for(int i = 0; i < reports.size(); i++) {
			for(int z = reports.size() - 1; z > i;z--) {
				if(reports.get(i).getFirstName().equals(reports.get(z).getFirstName()) && reports.get(i).getLastName().equals(reports.get(z).getLastName())) {
					boolean isThere = false;
					for(int x = 0; x < separated.size(); x++) {
						if(separated.get(x).get(0).getFirstName().equals(reports.get(i).getFirstName()) && separated.get(x).get(0).getLastName().equals(reports.get(i).getLastName())) {
							isThere = true;
							separated.get(x).add(reports.get(i));
						}
					}
					if(isThere == false) {
						ArrayList<Student> newA = new ArrayList<Student>();
						newA.add(reports.get(i));
						separated.add(newA);
					}
				}
			}
		}
		for(int i = 0; i < separated.size(); i++) {
			int index = 0;
			for(int j = 0; j < separated.get(i).size(); j++) {
				if(separated.get(i).get(index).getDate().isAfter(separated.get(i).get(j).getDate()) && separated.get(i).get(j).getDate().isAfter(LocalDate.now().minusDays(7))){
					index = j;
				}
			}
			hoursList.add(separated.get(i).get(index));
		}
		return hoursList;
		
		
	}
	/**
	 * This method is essentially the same as the differenceinHours method but instead of weeks it is changed to months
	 * @return An arraylist of the service hours at the updated time closest to a month before now for reports
	 */
	public ArrayList<Student> differenceInHoursMonths() {
		System.out.println("started");
		ArrayList<Student> hoursList = new ArrayList<Student>();
		reports = getReportData("src/reports.txt");
		ArrayList<ArrayList<Student>> separated = new ArrayList<ArrayList<Student>>();
		for(int i = 0; i < reports.size(); i++) {
			for(int z = reports.size() - 1; z > i;z--) {
				if(reports.get(i).getFirstName().equals(reports.get(z).getFirstName()) && reports.get(i).getLastName().equals(reports.get(z).getLastName())) {
					boolean isThere = false;
					for(int x = 0; x < separated.size(); x++) {
						if(separated.get(x).get(0).getFirstName().equals(reports.get(i).getFirstName()) && separated.get(x).get(0).getLastName().equals(reports.get(i).getLastName())) {
							isThere = true;
							separated.get(x).add(reports.get(i));
						}
					}
					if(isThere == false) {
						ArrayList<Student> newA = new ArrayList<Student>();
						newA.add(reports.get(i));
						separated.add(newA);
					}
				}
			}
		}
		for(int i = 0; i < separated.size(); i++) {
			int index = 0;
			for(int j = 0; j < separated.get(i).size(); j++) {
				if(separated.get(i).get(index).getDate().isAfter(separated.get(i).get(j).getDate()) && separated.get(i).get(j).getDate().isAfter(LocalDate.now().minusMonths(1))){
					index = j;
				}
			}
			hoursList.add(separated.get(i).get(index));
		}
		return hoursList;
		
		
	}
/**
 * This method writes the student data, which is essentially every attribute of the student, to the fileName 
 * @param fileName The file which the student data is written to
 */
	public void writeStudentData(String fileName) {

		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);
			for (Student student : students) {
				pw.write(student.getId() + "|" + student.getLastName() + "|" + student.getFirstName() + "|" + student.getGrade() + "|" + student.getServiceHours() + "|" + student.getCategory());
				pw.write("\n");
			}
			

			pw.close();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	/**
	 * This method recieves the data from the report file
	 * @param fileName The file which this method reads from
	 * @return A list of students from the report data
	 */
	public ObservableList<Student> getReportData(String fileName){
		try {
			Scanner scan = new Scanner(new File(fileName));
			String currId = null;
			int counter = -1;
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] studentArray = s.split("\\|", 7);
				Student student = new Student(studentArray[0], studentArray[1], studentArray[2], studentArray[3], studentArray[4], studentArray[5], LocalDate.parse(studentArray[6], dtf));
				reports.add(student);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found.");
		}

		return reports;

	}

}
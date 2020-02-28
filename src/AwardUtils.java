import javafx.collections.ObservableList;
/**
 * This class is in charge of essentially the common methods needed that Java does not provide. Instead of 
 * continuously using this code, I put it into a separate class.
 * @author Aryan Kaul
 *
 */
public class AwardUtils {
	/**
	 * This method is used to find if a student that is going to be added is a duplicate to one already 
	 * in the array
	 * @param firstName The first name of a given student
	 * @param lastName The last name of a given student
	 * @param students The array of students
	 * @return Whether a given name is a duplicate of a name found in the students array
	 */
	public boolean isDuplicate(String firstName, String lastName, ObservableList<Student> students) {
		boolean isDuplicate = false;
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getFirstName().equals(firstName)
					&& students.get(i).getLastName().equals(lastName)) {
				isDuplicate = true;
			}
		}
		return isDuplicate;
	}
	/**
	 * This method is used to tell whether a string can be converted into an integer
	 * @param s A given string
	 * @return Boolean value based on if the given string can be converted to int
	 */
   public boolean isInteger(String s) {
	      boolean isValidInteger = false;
	      try
	      {
	         Integer.parseInt(s);
	 
	         // s is a valid integer
	 
	         isValidInteger = true;
	      }
	      catch (NumberFormatException ex)
	      {
	         // s is not an integer
	      }
	 
	      return isValidInteger;
	   }
   /**
    * This method gives the next ID numbers of any student that is going to be added to the list
    * @param students The list of students already in the program
    * @return The next ID number that should be used
    */
	int getNextIds(ObservableList<? extends Student> students) {
		int maxId = 0;
		int temp = 0;
		for (Student student : students) {
			temp = Integer.parseInt(student.getId());
			if (maxId < temp)
				maxId = temp;
		}
		return maxId;
	}



}

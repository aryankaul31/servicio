import java.io.FileNotFoundException;


import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * This class is designed in order to complete all functions of the GUI. 
 * The order is as follows
 * 1. The initial screen displays your login information in order to access the actual application
 * 2. Once in the application, you are immediately shown a simple and clean UI with a variety of different options such as reports or editing the
 * information of students. 
 * 3. If you no longer want to use the application, an exit button is displayed
 * 4. In all of the information editing tabs, a table view is shown with ability to enter/view/edit any information
 * 5. Keep in mind that you can only delete or add students in the edit student tab, the service hours and program categories tab 
 * are solely for accessing their respective information.
 * 6. Reports can be customized for weekly or monthly information. The service hours report contains a table view with the amount of  
 * service hours put in by every student in the time frame of your choice. The categories report displays text with both the weekly and monthly information
 * 
 * @author Aryan Kaul
 *
 */
public class Main extends Application {

	private TableView<Student> studentTable = new TableView<Student>();
	private TableView<Student> hoursTable = new TableView<Student>();
	private TableView<Student> categoryTable = new TableView<Student>();

	FileModifier fileReader = new FileModifier();
	AwardUtils awardUtils = new AwardUtils();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy");
	Label errMsg = new Label("");

	public ObservableList<Student> students;

	String reportsfileName;
	String studentsfileName;
	String css;

	private static int studentCount;

	Label err = new Label();

	public static void main(String[] args) {
		launch(args);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The code below is the code for the login screen, as well as the code for the main screen 
	 * that appears after the username and password have been correctly entered.
	 * The student button, when clicked, displays a table with a list of students. 
	 * New students may be added by entering a first and last name and clicking the 'Add' button.
	 * To delete a student  record, select a row of data in the table and click the 'Delete' button.
	 * A pop-up box asking for confirmation to delete the record will appear. Select 'Delete' to permanently delete the data, or
	 * 'Cancel' to keep the data.
	 */
	public void start(Stage primaryStage) throws FileNotFoundException {

		final Parameters params = getParameters();
		final List<String> parameters = params.getRaw();
		reportsfileName = !parameters.isEmpty() ? parameters.get(0) : "src/reports.txt";
		studentsfileName = !parameters.isEmpty() ? parameters.get(1) : "src/student.txt";
		students = fileReader.getStudentData(studentsfileName);
		Class<?> clazz = Main.class;
		InputStream input = clazz.getResourceAsStream("fbla-logo.png");
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		primaryStage.setTitle("Servicio");
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		primaryStage.show();
		
		studentCount = awardUtils.getNextIds(students);
		GridPane grid = new GridPane();
		grid.getStyleClass().addAll("pane");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(300, 300, 300, 300));
		grid.add(imageView, 0, 0);

		Scene scene = new Scene(grid, 300, 300);
		URL url = this.getClass().getResource("application.css");
		if (url == null) {
			System.out.println("Resource not found. Aborting.");
			System.exit(-1);
		}
		css = url.toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);

		Text title = new Text("Cupertino High School FBLA");
		title.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 35));
		grid.add(title, 0, 1, 7, 1);

		VBox adminBox = new VBox();
		adminBox.setPadding(new Insets(25, 25, 25, 25));
		Text adminScreenText = new Text("Admin Login");
		adminScreenText.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 20));
		Label userName = new Label("Username: ");
		userName.getStyleClass().add("labelText");
		TextField userTextField = new TextField();
		Label pw = new Label("Password: ");
		pw.getStyleClass().add("labelText");
		PasswordField pwField = new PasswordField();
		Text blank = new Text("");
		String style = "-fx-background-color: rgba(45, 228, 190, 1.0);";
		adminBox.setStyle(style);
		HBox userNameBox = new HBox();
		userNameBox.setPrefWidth(750);
		userNameBox.getChildren().addAll(userName, userTextField);
		HBox pwBox = new HBox();
		pwBox.getChildren().addAll(pw, pwField);
		
		adminBox.getChildren().addAll(adminScreenText, userNameBox, pwBox, blank);
		adminBox.setSpacing(20);
		adminBox.setBackground(
				new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.add(adminBox, 0, 2);

		Button ebtn = new Button("Exit");
		ebtn.getStyleClass().addAll("buttonLarge");
		Button btn = new Button("Submit");
		btn.getStyleClass().addAll("buttonLarge");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().addAll(btn, ebtn);
		grid.add(hbBtn, 0, 5);

		ebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (userTextField.getText() == null || pwField.getText() == null) {
					Text errorMsg = new Text("Error: Please enter your username and password.");
					grid.add(errorMsg, 1, 6);
				}

				if (!(userTextField.getText().equals("username")) || (!(pwField.getText().equals("password")))) {
					Text errorMsg = new Text("Error: Invalid username or password.");
					grid.add(errorMsg, 1, 6);
				}

				else {

					grid.getChildren().clear();
					grid.setPadding(new Insets(100, 100, 200, 100));

					Text registerLabel = new Text("Information");
					registerLabel.getStyleClass().addAll("heading");
					Text reportsLabel = new Text("Reports");
					reportsLabel.getStyleClass().addAll("heading");

					Button editStudentBtn = new Button("Add/Edit Student");
					editStudentBtn.getStyleClass().addAll("buttonLarge");
					Button editServiceHoursBtn = new Button("Add/Edit Service Hours");
					editServiceHoursBtn.getStyleClass().addAll("buttonLarge");
					Button editCategoryBtn = new Button("Edit Service Category");
					editCategoryBtn.getStyleClass().addAll("buttonLarge");
					Button totalHoursBtn = new Button("Total Service Hours");
					totalHoursBtn.getStyleClass().addAll("buttonLarge");
					Button programCategoriesBtn = new Button("Program Categories");
					programCategoriesBtn.getStyleClass().addAll("buttonLarge");
					Button qaBtn = new Button("Interactive Q&A");
					qaBtn.getStyleClass().addAll("buttonLarge");
					Button exitBtn = new Button("Exit");
					exitBtn.getStyleClass().addAll("buttonLarge");

					grid.add(imageView, 1, 0);
				
					grid.add(registerLabel, 0, 1);
					grid.add(editStudentBtn, 0, 2);
					grid.add(editServiceHoursBtn, 0, 3);
					grid.add(editCategoryBtn, 0, 4);


					grid.add(reportsLabel, 2, 1);
					grid.add(totalHoursBtn, 2, 2);
					grid.add(programCategoriesBtn, 2, 3);
					grid.add(qaBtn, 1, 8);
					grid.add(exitBtn, 2, 8);

					editStudentButtonAction(editStudentBtn, primaryStage);
					editServiceHoursButtonAction(editServiceHoursBtn, primaryStage);
					editCategoryButtonAction(editCategoryBtn, primaryStage);
					totalHoursButtonAction(totalHoursBtn, primaryStage);
					programCategoriesButtonAction(programCategoriesBtn, primaryStage);
					qaButtonAction(qaBtn, primaryStage);
					exitBtn.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							primaryStage.close();
						}

					});

				}
			}
		});
	}
	public void qaButtonAction(Button qaBtn, Stage primaryStage) {
		qaBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent args) {
				err.setVisible(false);
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Interactive Q&A");
				stage.setWidth(600);
				stage.setHeight(700);
				stage.setScene(scene);
				
				GridPane qaGrid = new GridPane();
				qaGrid.setPrefWidth(600);
				qaGrid.setPrefHeight(700);
				qaGrid.getStyleClass().addAll("pane");

		        Accordion accordion = new Accordion();

		        TitledPane pane1 = new TitledPane("\nWhat is Servicio? \n " , new Label("Servicio is an application that is available for use by local FBLA Chapters to keep track of \ntheir student's service hours and Community Service Awards Program levels."));
		        TitledPane pane2 = new TitledPane("\nWhat tools were used to build this application? \n "  , new Label("This app was made using JavaFX GUI controls and CSS in order to style the GUI to make it \na simple yet appealing design that our users could enjoy. "));
		        TitledPane pane3 = new TitledPane("\nHow is data stored within the app? \n ", new Label("Rather than serving data from a database in the cloud, we have developed a local \ndatastore." + 
		        		" All data never leaves the application. This ensures maximum security and speed \nwithin the " + 
		        		"application."));
		        TitledPane pane4 = new TitledPane("\nWhat was the hardest part of the application to implement? \n ", new Label("Without a doubt, the hardest part was perfecting the user interface. As a software \n" + 
		        		"developer, I know it is important to not overwhelm the user with interface elements.\n" + 
		        		"Therefore, I made it my goal to keep the program visually simple while retaining \nmany useful features.\n"));
		        TitledPane pane5 = new TitledPane("\nHow do I use this application? \n ", new Label(" 1. The initial screen displays your login information in order to access the application\n" + 
		        		" 2. Once in the application, you are immediately shown a simple and clean UI with a \nvariety of different options such as reports or editing the" + 
		        		" information \nof students. \n" + 
		        		" 3. If you no longer want to use the application, an exit button is displayed\n" + 
		        		" 4. In all of the information editing tabs, a table view is shown with ability to \nenter/view/edit any information\n" + 
		        		" 5. Keep in mind that you can only delete or add students in the edit student tab, the \nservice hours and program categories tabs" + 
		        		" are solely for accessing their \nrespective information.\n" + 
		        		" 6. Reports can be customized for weekly or monthly information. The service hours \nreport contains a table view with the amount of" + 
		        		" service hours put in by every student in the \ntime frame of your choice. The categories report displays text with both the weekly and \nmonthly information" + 
		        		""));
		        pane1.setStyle("-fx-background-color: rgba(45, 228, 190, 1.0);");
		        pane2.setStyle("-fx-background-color: rgba(45, 228, 190, 1.0);");
		        pane3.setStyle("-fx-background-color: rgba(45, 228, 190, 1.0);");
		        pane4.setStyle("-fx-background-color: rgba(45, 228, 190, 1.0);");

		        accordion.getPanes().addAll(pane1, pane2, pane3, pane4, pane5);
		        accordion.setStyle("-fx-background-color: rgba(45, 228, 190, 1.0);");
		        accordion.setMaxSize(500, 600);
		        Label paneTitle = new Label("				Interactive Q&A");
		        Font titleFont = new Font("Sans Serif", 25);
		        paneTitle.setStyle("-fx-font-weight: bold");
		        paneTitle.setFont(titleFont);
		        VBox vbox = new VBox();
		        Text dummy = new Text("");
		        Text dummy1 = new Text("\n\n\n");
		   
		        vbox.getChildren().addAll(dummy1, paneTitle, dummy, accordion);
		        qaGrid.setAlignment(Pos.TOP_CENTER);
				qaGrid.add(vbox, 0, 0);

				((Group) scene.getRoot()).getChildren().addAll(qaGrid);

				stage.setScene(scene);
				stage.show();

			}
		});
	}
/**
 * This method controls the view after the student button is clicked
 * @param editStudentBtn The button which is passed into this method for event handling
 * @param primaryStage The stage in which the view is created
 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editStudentButtonAction(Button editStudentBtn, Stage primaryStage) {
		editStudentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			public void handle(ActionEvent args) {
				err.setVisible(false);
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Student Members");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane editStudGrid = new GridPane();
				editStudGrid.setPrefWidth(700);
				editStudGrid.setPrefHeight(700);
				editStudGrid.getStyleClass().addAll("pane");

				Label label = new Label("Student Members");
				label.getStyleClass().addAll("heading");

				studentTable.setEditable(true);
				studentTable.setPrefHeight(300);
				studentTable.setPrefWidth(500);

				Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
					public TableCell call(TableColumn p) {
						return new Cell();
					}
				};
				studentTable.getColumns().clear();

				TableColumn<Student, String> idCol = new TableColumn<Student, String>("ID number");
				idCol.setPrefWidth(80);
				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
				firstNameCol.setPrefWidth(210);
				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
				lastNameCol.setPrefWidth(210);
				TableColumn<Student, String> gradeCol = new TableColumn<Student, String>("Grade");
				gradeCol.setPrefWidth(80);
				
				idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));

				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				firstNameCol.setCellFactory(cellFactory);

				firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
					public void handle(CellEditEvent<Student, String> t) {

						if (t.getNewValue().equals(null) || t.getNewValue().trim().equals("")) {
							System.out.println("Entered if for null firstName");
							err.setText("Error: Empty First Name. Please Enter Valid Value.");
							err.setVisible(true);
						}
						// Write method for the duplicate value entry.
						else {

							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow()))
									.setFirstName(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
							
						}

					}
				});

				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
				lastNameCol.setCellFactory(cellFactory);
				lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
					public void handle(CellEditEvent<Student, String> t) {

						if (t.getNewValue().equals(null) || t.getNewValue().trim().equals("")) {
							err.setText("Error: Empty Last Name. Please Enter Valid Value.");
							err.setVisible(true);
						}

						else {
							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow()))
									.setLastName(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
						}
					}
				});
				gradeCol.setCellValueFactory(new PropertyValueFactory<Student,String>("grade"));
				gradeCol.setCellFactory(cellFactory);
				gradeCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
					public void handle(CellEditEvent<Student, String> t) {
						if (!awardUtils.isInteger(t.getNewValue()) || (Integer.parseInt(t.getNewValue()) > 12 || Integer.parseInt(t.getNewValue()) < 6)) {
							err.setText("Error: Enter a valid value for grade.");
							err.setVisible(true);
						}
						// Write method for the duplicate value entry.
						else {
							
							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setGrade(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
							
						}

					}
				});
				gradeCol.setVisible(true);
				studentTable.setItems(students);
				studentTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, gradeCol);

				TextField firstNameField = new TextField();
				firstNameField.setPromptText("First Name");
				firstNameField.setMaxWidth(100);

				TextField lastNameField = new TextField();
				lastNameField.setPromptText("Last Name");
				lastNameField.setMaxWidth(100);

				TextField gradeField = new TextField();
				gradeField.setPromptText("Current Grade");
				gradeField.setMaxWidth(100);
				
				Button addButton = new Button("Add");
				addButton.getStyleClass().addAll("buttonSmall");
				Button exitButton = new Button("Back");
				exitButton.getStyleClass().addAll("buttonSmall");
				Button deleteButton = new Button("Delete Selected Student");
				deleteButton.getStyleClass().addAll("buttonXLarge");

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				HBox hb = new HBox();
				hb.setSpacing(3);
				HBox hb1 = new HBox();
				hb1.setSpacing(3);

				exitButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});

				addButton.setOnAction(new EventHandler<ActionEvent>() {

					boolean isDuplicate = false;

					public void handle(ActionEvent e) {
						
						if ((firstNameField.getText() != null && firstNameField.getText().trim().length() > 0)
								&& (lastNameField.getText() != null && lastNameField.getText().trim().length() > 0)) {
							
							isDuplicate = awardUtils.isDuplicate(firstNameField.getText(), lastNameField.getText(), students);

							if (isDuplicate) {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Duplicate Entry");
								alert.setHeaderText("Student(s) with the same first and last name exist(s). Are you sure you want to add this entry?");

								ButtonType buttonTypeDelete = new ButtonType("Add");
								ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

								alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

								Optional<ButtonType> result = alert.showAndWait();

								boolean cancelled = false;
								if (result.get().getText().equals("Add")){
									System.out.println("Adding...");
									if(awardUtils.isInteger(gradeField.getText()) && Integer.parseInt(gradeField.getText()) <= 12 && Integer.parseInt(gradeField.getText()) >= 6) {
										studentCount += 1;
										Student s  = new Student(Integer.toString(studentCount), lastNameField.getText(),
												firstNameField.getText(), gradeField.getText() , Integer.toString(0), "none", null);
										students.add(s);
										fileReader.addDataToReport(reportsfileName, s);
										lastNameField.clear();
										firstNameField.clear();
										gradeField.clear();
									}else {
										Alert alert1 = new Alert(AlertType.ERROR);
										alert1.setTitle("Add Student Failed");
										alert1.setHeaderText(null);
										alert1.setContentText("Error: Enter a valid integer for the student's grade");
										alert1.showAndWait();
									}
									
								}else {
									lastNameField.clear();
									firstNameField.clear();
									gradeField.clear();
								    cancelled = true;// ... user chose CANCEL or closed the dialog
								}
								
							}

							else {
								
								if(awardUtils.isInteger(gradeField.getText()) && Integer.parseInt(gradeField.getText()) <= 12 && Integer.parseInt(gradeField.getText()) >= 6) {
									studentCount += 1;
									Student s  = new Student(Integer.toString(studentCount), lastNameField.getText(),
											firstNameField.getText(), gradeField.getText(), Integer.toString(0), "none", null);
									students.add(s);
									fileReader.addDataToReport(reportsfileName, s);
									lastNameField.clear();
									firstNameField.clear();
									gradeField.clear();
								}else {
									Alert alert1 = new Alert(AlertType.ERROR);
									alert1.setTitle("Add Student Failed");
									alert1.setHeaderText(null);
									alert1.setContentText("Error: Enter a valid integer for the student's grade");
									alert1.showAndWait();
								}
			
								
							}

						} else {
							err.setText("Please Enter Valid First and Last Names.");
							err.setVisible(true);
						}
					}

				});

				deleteButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						Student delStudent = (Student) studentTable.getSelectionModel().getSelectedItem();
					
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirm Delete");
						alert.setHeaderText("Are you sure you want to delete this student?");

						ButtonType buttonTypeDelete = new ButtonType("Delete");
						ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

						alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

						Optional<ButtonType> result = alert.showAndWait();

						boolean cancelled = false;
						if (result.get().getText().equals("Delete")){
							System.out.println("Deleting...");
						}else {
						    cancelled = true;// ... user chose CANCEL or closed the dialog
						}
						
						if (!cancelled) {
							studentTable.getItems().remove(delStudent);
							for (int i = 0; i < students.size(); i++) {
								if (students.get(i).getId().equals(delStudent.getId())) {
									students.remove(i);
								}
							}
						}
					}

				});

				firstNameField.clear();
				lastNameField.clear();
				gradeField.clear();
				
				hb.getChildren().addAll(firstNameField, lastNameField, gradeField, addButton);
				hb1.getChildren().addAll(deleteButton, exitButton);
				vbox.getChildren().addAll(label, studentTable, err, hb, hb1);
				editStudGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(editStudGrid);

				stage.setScene(scene);
				stage.show();
			}

		});
	}
	
/**
 *  This method controls the view for the service hours button, displaying a table view with cumulative service hours for 
 *  every student
 * @param editServiceHourBtn The button which is passed into this method for event handling
 * @param primaryStage The stage in which the view is created
 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editServiceHoursButtonAction(Button editServiceHourBtn, Stage primaryStage) {
		editServiceHourBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {

				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Teacher Members");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane editServiceGrid = new GridPane();
				editServiceGrid.setPrefWidth(700);
				editServiceGrid.setPrefHeight(700);
				editServiceGrid.getStyleClass().addAll("pane");

				Label label = new Label("Service Hours");
				label.getStyleClass().addAll("heading");

				hoursTable.setEditable(true);
				hoursTable.setPrefHeight(300);
				hoursTable.setPrefWidth(500);
				hoursTable.getColumns().clear();

				Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
					public TableCell call(TableColumn p) {
						return new Cell();
					}
				};

				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
				firstNameCol.setPrefWidth(210);
				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
				lastNameCol.setPrefWidth(210);
				TableColumn<Student, String> hoursCol = new TableColumn<Student, String>("Service Hours");

				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				

				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
				
				
				hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("serviceHours"));
				hoursCol.setCellFactory(cellFactory);
				hoursCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>(){
					public void handle(CellEditEvent<Student, String> t) {
						if(awardUtils.isInteger(t.getNewValue())) {
							Student s = (Student) t.getTableView().getItems().get(t.getTablePosition().getRow());
							s.setHours(new SimpleStringProperty(t.getNewValue()));
							fileReader.addDataToReport("src/reports.txt", students.get(t.getTablePosition().getRow()));
							if(Integer.parseInt(t.getNewValue()) >= 500) {
								s.setCategory(new SimpleStringProperty("achievement"));
							}else if(Integer.parseInt(t.getNewValue()) >= 200) {
								s.setCategory(new SimpleStringProperty("service"));
							}else if(Integer.parseInt(t.getNewValue()) >= 50) {
								s.setCategory(new SimpleStringProperty("community"));
							}
						}
					}
					
				});
				hoursTable.setItems(students);
				hoursTable.getColumns().addAll(firstNameCol, lastNameCol, hoursCol);

				TextField firstNameField = new TextField();
				firstNameField.setPromptText("First Name");
				firstNameField.setMaxWidth(100);

				TextField lastNameField = new TextField();
				lastNameField.setPromptText("Last Name");
				lastNameField.setMaxWidth(100);

				TextField hoursField = new TextField();
				hoursField.setPromptText("Additional Service Hours");
				hoursField.setMaxWidth(100);
				Button addButton = new Button("Add");
				addButton.getStyleClass().addAll("buttonSmall");
				Button exitButton = new Button("Back");
				exitButton.getStyleClass().addAll("buttonSmall");

				Label err = new Label();
				err.setVisible(false);

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				HBox hb = new HBox();
				hb.setSpacing(3);

				exitButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				addButton.setOnAction(new EventHandler<ActionEvent>() {

					boolean isDuplicate = false;

					public void handle(ActionEvent e) {
						if ((firstNameField.getText() != null && firstNameField.getText().trim().length() > 0)
								&& (lastNameField.getText() != null && lastNameField.getText().trim().length() > 0)) {
							
							
							
							isDuplicate = awardUtils.isDuplicate(firstNameField.getText(), lastNameField.getText(), students);

							if (isDuplicate) {
								for(Student student: students) {
									if(student.getFirstName().equals(firstNameField.getText()) && student.getLastName().equals(lastNameField.getText())) {
										if(awardUtils.isInteger(hoursField.getText()) && Integer.parseInt(hoursField.getText()) > 0) {
											String s = Integer.toString(Integer.parseInt(student.getServiceHours()) + Integer.parseInt(hoursField.getText()));
											student.setHours(new SimpleStringProperty(s));
											if(Integer.parseInt(student.getServiceHours()) >= 500) {
												student.setCategory(new SimpleStringProperty("achievement"));
											}else if(Integer.parseInt(student.getServiceHours()) >= 200) {
												student.setCategory(new SimpleStringProperty("service"));
											}else if(Integer.parseInt(student.getServiceHours()) >= 50) {
												student.setCategory(new SimpleStringProperty("community"));
											}
											
											hoursTable.refresh();
											fileReader.addDataToReport("src/reports.txt", student);
											err.setVisible(false);
											break;
										}else {
											err.setText("Please enter a valid number of service hours");
											err.setVisible(true);
										}
									}
								}
							}
							else {
								err.setText("Please choose an existing student");
								err.setVisible(true);
							}

						} else {
							err.setText("Please Enter Valid First and Last Names.");
							err.setVisible(true);
						}

						firstNameField.clear();
						lastNameField.clear();
						hoursField.clear();
					}
				});


				hb.getChildren().addAll(firstNameField, lastNameField, hoursField, addButton);
				HBox hb1 = new HBox();
				hb1.getChildren().addAll(exitButton);
				hb1.setSpacing(3);
				vbox.getChildren().addAll(label, hoursTable, err, hb, hb1);
				editServiceGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(editServiceGrid);

				stage.setScene(scene);
				stage.show();
			}

		});
	}
/**
 * This method displays the service hour category for each student based on the 3 service hour categories, community,service, and achievement. This information is 
 * displayed using a tableview.
 * @param editCategoryBtn The button which is passed for event handling
 * @param primaryStage The stage in which the view is created
 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editCategoryButtonAction(Button editCategoryBtn, Stage primaryStage) {
		editCategoryBtn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			public void handle(ActionEvent e) {
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Program Categories");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane viewGrid = new GridPane();
				viewGrid.setPrefWidth(700);
				viewGrid.setPrefHeight(700);
				viewGrid.getStyleClass().addAll("pane");

				Label err = new Label();
				err.setVisible(false);

				Label label = new Label("Program Categories");
				label.getStyleClass().addAll("heading");

				categoryTable.setPrefHeight(300);
				categoryTable.setPrefWidth(500);
				
				Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
					public TableCell call(TableColumn p) {
						return new Cell();
					}
				};
				categoryTable.getColumns().clear();

				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
				firstNameCol.setPrefWidth(175);
				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
				lastNameCol.setPrefWidth(175);
				TableColumn<Student, String> categoryCol = new TableColumn<Student, String>("Category");
				categoryCol.setEditable(true);
				categoryCol.setPrefWidth(175);


				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));

				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));

				categoryCol.setCellValueFactory(new PropertyValueFactory<Student, String>("category"));
				categoryCol.setCellFactory(cellFactory);
				categoryCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>(){
					public void handle(CellEditEvent<Student, String> t) {
						if(t.getNewValue().toLowerCase().equals("community") || t.getNewValue().toLowerCase().equals("service") || t.getNewValue().toLowerCase().equals("achievement")) {
							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCategory(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
							fileReader.addDataToReport("src/reports.txt", students.get(t.getTablePosition().getRow()));

						}else {
							err.setText("Please enter a valid category");
							err.setVisible(true);
						}
						
					}
					
				});

				categoryTable.setItems(students);

				categoryTable.getColumns().addAll( firstNameCol, lastNameCol, categoryCol);

				Button cancelButton = new Button("Back");
				cancelButton.getStyleClass().addAll("buttonSmall");
				HBox hb1 = new HBox();
				hb1.getChildren().addAll(cancelButton);
				VBox vb = new VBox();
				vb.getChildren().addAll(hb1);

				
				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				hb1.setSpacing(3);
				vbox.getChildren().addAll(label, categoryTable, err, hb1);
				viewGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(viewGrid);

				stage.setScene(scene);
				stage.show();

			}
		});
	}
/**
 * This method displays a report using a tableview with the total hours of service by every student within a specified time frame
 * @param totalHoursBtn The button passed for event handling
 * @param primaryStage The stage whose view is controlled
 */ 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void totalHoursButtonAction(Button totalHoursBtn, Stage primaryStage) {
		totalHoursBtn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			public void handle(ActionEvent e) {
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Community Service Report");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				Text to = new Text("To");
				to.getStyleClass().add("alert");
				Text startDate = new Text();
				LocalDate.now().minusMonths(1);
				LocalDate odStart = LocalDate.now().minusDays(7);
				startDate.setText(LocalDate.now().minusDays(7).format(dtf));
				startDate.getStyleClass().add("alert");
				Text endDate = new Text();
				LocalDate odEnd = LocalDate.now();
				endDate.setText(LocalDate.now().format(dtf));
				endDate.getStyleClass().add("alert");

				Text serviceReportLabel = new Text("Community Service Weekly Report");
				serviceReportLabel.getStyleClass().addAll("heading");
				Text label = new Text("");

				TableView<Student> table = new TableView<Student>();
				table.setPrefSize(550, 300);
				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
				firstNameCol.setPrefWidth(200);
				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
				lastNameCol.setPrefWidth(200);
				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
				TableColumn<Student, String> hoursCol = new TableColumn<Student, String>("Service Hours");
				hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("hoursDiff"));
				
				
				hoursCol.setPrefWidth(150);

				List<Student> hoursList = new ArrayList<>();
				GridPane hoursGrid = new GridPane();
				hoursGrid.getStyleClass().addAll("pane");
				hoursGrid.setPrefWidth(700);
				hoursGrid.setPrefHeight(700);
				hoursGrid.setAlignment(Pos.CENTER);
				hoursList = fileReader.differenceInHours();
				ObservableList<Student> newList = FXCollections.observableArrayList();
				for(Student student: students) {
					boolean isDone = false;
					for(Student hoursStudent: hoursList){
						if(hoursStudent.getFirstName().equals(student.getFirstName()) && hoursStudent.getLastName().equals(student.getLastName())) {
							Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(Integer.parseInt(student.getServiceHours()) - Integer.parseInt(hoursStudent.getServiceHours())), student.getCategory(), student.getDate());
							newList.add(tempStudent);
							isDone = true;
						}
						
					}
					if(isDone == false) {
						Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(0), student.getCategory(), student.getDate());
						newList.add(tempStudent);
					}
				}
				table.setItems(FXCollections.observableArrayList(newList));
				table.getColumns().addAll(firstNameCol, lastNameCol, hoursCol);

				Text dummy = new Text("");
				Text dummy1 = new Text("");

				HBox hbox1 = new HBox();
				hbox1.setSpacing(10);
				hbox1.getChildren().addAll(startDate, to, endDate);

				hoursGrid.add(serviceReportLabel, 0, 0);
				hoursGrid.add(dummy, 0, 1);
				hoursGrid.add(hbox1, 0, 2);
				hoursGrid.add(label, 0, 3);
				hoursGrid.add(table, 0, 4);
				Button monthlyButton = new Button("Monthly");
				monthlyButton.getStyleClass().add("buttonSmall");

				Button printButton = new Button("Print");
				
				printButton.getStyleClass().add("buttonSmall");
				Button cancelButton = new Button("Exit");
				cancelButton.getStyleClass().add("buttonSmall");

				HBox hbox2 = new HBox();
				hbox2.setSpacing(10);
				hbox2.getChildren().addAll(monthlyButton, printButton, cancelButton);

				hoursGrid.add(dummy1, 0, 5);
				hoursGrid.add(hbox2, 0, 6);
				((Group) scene.getRoot()).getChildren().addAll(hoursGrid);
				stage.setScene(scene);
				stage.show();
				
				
				monthlyButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						Scene scene = new Scene(new Group());
						scene.getStylesheets().add(css);
						Stage stage = new Stage();
						stage.setTitle("Community Service Report");
						stage.setWidth(700);
						stage.setHeight(700);
						stage.setScene(scene);

						Text to = new Text("To");
						to.getStyleClass().add("alert");
						Text startDate = new Text();
						LocalDate.now().minusMonths(1);
						LocalDate odStart = LocalDate.now().minusMonths(1);
						startDate.setText(LocalDate.now().minusMonths(1).format(dtf));
						startDate.getStyleClass().add("alert");
						Text endDate = new Text();
						LocalDate odEnd = LocalDate.now();
						endDate.setText(LocalDate.now().format(dtf));
						endDate.getStyleClass().add("alert");

						Text serviceReportLabel = new Text("Community Service Monthly Report");
						serviceReportLabel.getStyleClass().addAll("heading");
						Text label = new Text("");

						TableView<Student> table = new TableView<Student>();
						table.setPrefSize(550, 300);
						TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
						firstNameCol.setPrefWidth(200);
						firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
						TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
						lastNameCol.setPrefWidth(200);
						lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
						TableColumn<Student, String> hoursCol = new TableColumn<Student, String>("Service Hours");
						hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("hoursDiff"));
						
						
						hoursCol.setPrefWidth(150);

						List<Student> hoursList = new ArrayList<>();
						GridPane hoursGrid = new GridPane();
						hoursGrid.getStyleClass().addAll("pane");
						hoursGrid.setPrefWidth(700);
						hoursGrid.setPrefHeight(700);
						hoursGrid.setAlignment(Pos.CENTER);
						hoursList = fileReader.differenceInHoursMonths();
						ObservableList<Student> newList = FXCollections.observableArrayList();
						for(Student student: students) {
							boolean isDone = false;
							for(Student hoursStudent: hoursList){
								if(hoursStudent.getFirstName().equals(student.getFirstName()) && hoursStudent.getLastName().equals(student.getLastName())) {
									Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(Integer.parseInt(student.getServiceHours()) - Integer.parseInt(hoursStudent.getServiceHours())), student.getCategory(), student.getDate());
									newList.add(tempStudent);
									isDone = true;
								}
								
							}
							if(isDone == false) {
								Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(0), student.getCategory(), student.getDate());
								newList.add(tempStudent);
							}
						}
						
						table.setItems(FXCollections.observableArrayList(newList));
						table.getColumns().addAll(firstNameCol, lastNameCol, hoursCol);

						Text dummy = new Text("");
						Text dummy1 = new Text("");

						HBox hbox1 = new HBox();
						hbox1.setSpacing(10);
						hbox1.getChildren().addAll(startDate, to, endDate);

						hoursGrid.add(serviceReportLabel, 0, 0);
						hoursGrid.add(dummy, 0, 1);
						hoursGrid.add(hbox1, 0, 2);
						hoursGrid.add(label, 0, 3);
						hoursGrid.add(table, 0, 4);
						Button printButton = new Button("Print");
						printButton.getStyleClass().add("buttonSmall");
						Button cancelButton = new Button("Cancel");
						cancelButton.getStyleClass().add("buttonSmall");

						HBox hbox2 = new HBox();
						hbox2.setSpacing(10);
						hbox2.getChildren().addAll(printButton, cancelButton);

						hoursGrid.add(dummy1, 0, 5);
						hoursGrid.add(hbox2, 0, 6);
						((Group) scene.getRoot()).getChildren().addAll(hoursGrid);
						stage.setScene(scene);
						stage.show();
						cancelButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								stage.close();
							}
						});
						printButton.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								PrinterJob printerJob = PrinterJob.createPrinterJob();
								if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(table))
									printerJob.endJob();

							}

						});
					}
				});
				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				printButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						PrinterJob printerJob = PrinterJob.createPrinterJob();
						if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(table))
							printerJob.endJob();

					}

				});
			}
		});
	}
	/**
	 * This method controls the view for the program categories report using simple text. This displays the total hours 
	 * achieved by every single program category in a  specified time frame.
	 * @param programCategoriesBtn The button passed for event handling
	 * @param primaryStage The stage of the view that is controlled
	 */
	public void programCategoriesButtonAction(Button programCategoriesBtn, Stage primaryStage) {
		programCategoriesBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// find books that are due as of date.now or later
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Service Categories Report");
				stage.setWidth(400);
				stage.setHeight(400);
				stage.setScene(scene);

				Text to = new Text("To");
				to.getStyleClass().add("alert");
				Text startDate = new Text();
				startDate.setText(LocalDate.now().minusDays(7).format(dtf));
				startDate.getStyleClass().add("alert");
				Text endDate = new Text();
				endDate.setText(LocalDate.now().format(dtf));
				endDate.getStyleClass().add("alert");

				Text to1 = new Text("To");
				to1.getStyleClass().add("alert");
				Text startDate1 = new Text();
				startDate1.setText(LocalDate.now().minusMonths(1).format(dtf));
				startDate1.getStyleClass().add("alert");
				Text endDate1 = new Text();
				endDate1.setText(LocalDate.now().format(dtf));
				endDate1.getStyleClass().add("alert");

				Text categoriesReportLabel = new Text("Category Report");
				Text label = new Text("");
				categoriesReportLabel.getStyleClass().addAll("heading");

				List<Student> hoursList = new ArrayList<>();
				GridPane categoriesGrid = new GridPane();
				categoriesGrid.getStyleClass().addAll("pane");
				categoriesGrid.setPrefWidth(400);
				categoriesGrid.setPrefHeight(400);
				categoriesGrid.setAlignment(Pos.CENTER);
				
				int community = 0;
				int service = 0;
				int achievement = 0;
				hoursList = fileReader.differenceInHours();
				ObservableList<Student> newList = FXCollections.observableArrayList();
				for(Student student: students) {
					boolean isDone = false;
					for(Student hoursStudent: hoursList){
						if(hoursStudent.getFirstName().equals(student.getFirstName()) && hoursStudent.getLastName().equals(student.getLastName())) {
							Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(Integer.parseInt(student.getServiceHours()) - Integer.parseInt(hoursStudent.getServiceHours())), student.getCategory(), student.getDate());
							newList.add(tempStudent);
							isDone = true;
						}
						
					}
					if(isDone == false) {
						Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(0), student.getCategory(), student.getDate());
						newList.add(tempStudent);
					}
				}
				for(Student student: newList) {
					if(student.getCategory().toLowerCase().equals("community")) {
						community += Integer.parseInt(student.getHoursDiff());
					}else if(student.getCategory().toLowerCase().equals("service")) {
						service += Integer.parseInt(student.getHoursDiff());
					}else if(student.getCategory().toLowerCase().equals("achievement")) {
						achievement += Integer.parseInt(student.getHoursDiff());
					}
				}
				VBox categories = new VBox();
				Text communityLabel = new Text("Community: " + community + " hours");
				Text serviceLabel = new Text("Service: " + service + " hours");
				Text achievementLabel = new Text("Achievement: " + achievement + " hours");
				categories.getChildren().addAll(communityLabel, serviceLabel, achievementLabel);
				Text dummy = new Text("");
				Text dummy1 = new Text("");

				HBox hbox1 = new HBox();
				hbox1.setSpacing(10);
				hbox1.getChildren().addAll(startDate, to, endDate);

				int community2 = 0;
				int service2 = 0;
				int achievement2 = 0;
				List<Student> hoursList2 = new ArrayList<>();
				hoursList2 = fileReader.differenceInHoursMonths();
				ObservableList<Student> newList2 = FXCollections.observableArrayList();
				for(Student student: students) {
					boolean isDone = false;
					for(Student hoursStudent: hoursList2){
						if(hoursStudent.getFirstName().equals(student.getFirstName()) && hoursStudent.getLastName().equals(student.getLastName())) {
							Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(Integer.parseInt(student.getServiceHours()) - Integer.parseInt(hoursStudent.getServiceHours())), student.getCategory(), student.getDate());
							newList2.add(tempStudent);
							isDone = true;
						}
						
					}
					if(isDone == false) {
						Student tempStudent = new Student(student.getLastName(), student.getFirstName(), Integer.toString(0), student.getCategory(), student.getDate());
						newList2.add(tempStudent);
					}
				}
				for(Student student: newList2) {
					if(student.getCategory().toLowerCase().equals("community")) {
						community2 += Integer.parseInt(student.getHoursDiff());
					}else if(student.getCategory().toLowerCase().equals("service")) {
						service2 += Integer.parseInt(student.getHoursDiff());
					}else if(student.getCategory().toLowerCase().equals("achievement")) {
						achievement2 += Integer.parseInt(student.getHoursDiff());
					}
				}
				HBox hbox2 = new HBox();
				hbox2.setSpacing(10);
				hbox2.getChildren().addAll(startDate1, to1, endDate1);
				
				
				VBox categories2 = new VBox();
				Text communityLabel2 = new Text("Community: " + community2 + " hours");
				Text serviceLabel2 = new Text("Service: " + service2 + " hours");
				Text achievementLabel2 = new Text("Achievement: " + achievement2 + " hours");
				categories2.getChildren().addAll(communityLabel2, serviceLabel2, achievementLabel2);

				categoriesGrid.add(categoriesReportLabel, 0, 0);
				categoriesGrid.add(label, 0, 1);
				categoriesGrid.add(hbox1, 0, 2);
				categoriesGrid.add(categories, 0, 3);
				categoriesGrid.add(dummy, 0, 4);
				categoriesGrid.add(hbox2, 0, 5);
				categoriesGrid.add(categories2, 0, 6);
				Button printButton = new Button("Print");
				printButton.getStyleClass().add("buttonSmall");
				Button cancelButton = new Button("Cancel");
				cancelButton.getStyleClass().add("buttonSmall");

				HBox hbox3 = new HBox();
				hbox3.setSpacing(10);
				hbox3.getChildren().addAll(printButton, cancelButton);
				Text dummy2 = new Text("");
				categoriesGrid.add(dummy2, 0, 7);
				categoriesGrid.add(dummy1, 0, 8);
				categoriesGrid.add(hbox3, 0, 9);
				((Group) scene.getRoot()).getChildren().addAll(categoriesGrid);
				stage.setScene(scene);
				stage.show();

				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				printButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						PrinterJob printerJob = PrinterJob.createPrinterJob();
						if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(categoriesGrid))
							printerJob.endJob();

					}

				});

			}
		});
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void stop() {

		fileReader.writeStudentData(studentsfileName);

		System.out.println("Stage is closing");
	}
}


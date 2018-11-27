
package code; //places compiled code into code directory

//required for java
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;

//required for javafx
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat.Type;

public class personalFinance extends Application {
    //main method. Required when working with VSCode/IDE
    public static void main(String[] args) {
        launch(args);
    }

    //start method
    @Override
    public void start(Stage primaryStage) {
        //holds expenses file to look at
        String expensesFile;
        //holds earnings file to look at
        String earningsFile;
        //holds the data read from API endpoint
        String readData;
        //holds the names of stocks
        ArrayList<String> stockNames = new ArrayList<String>();
        //holds the amount/number of stocks bought
        ArrayList<String> stockAmount = new ArrayList<String>();
        //holds the prices of stocks
        ArrayList<String> stockPrices = new ArrayList<String>();

        //ComboBox object that holds years
        ComboBox years = new ComboBox();
        //ComboBox object that holds months
        ComboBox months = new ComboBox();
        //ComboBox object that holds which files to add elements to
        ComboBox add = new ComboBox();
        //GridPane object to hold data read from expenses files
        GridPane expenses = new GridPane();
        //GridPane object to hold data read from stocks file
        GridPane stocks = new GridPane();
        //GridPane object that holds data read from bonds file
        GridPane bonds = new GridPane();
        //GridPane object to hold earnings
        GridPane earnings = new GridPane();
        //GridPane object to hold yearly summary
        GridPane yearlySummary = new GridPane();
        //GridPane object to hold monthly summary
        GridPane monthlySummary = new GridPane();
        //GridPane object to hold menus
        GridPane menuContainer = new GridPane();
        //GridPane object to hold stocks and bonds
        GridPane investingContainer = new GridPane();
        //GridPane object to hold summary of all expenses/earnings/stocks/etc.
        GridPane containerSummary = new GridPane();
        //GridPane object to hold elements that are needed to add to expenses and earnings file
        GridPane containerAdd = new GridPane();
        //BorderPane object that holds everything
        BorderPane root = new BorderPane();
        //ScrollPane object that holds expenses GridPane
        ScrollPane scrollpaneExpenses = new ScrollPane(expenses);
        //ScrollPane object that holds stocks GridPane
        ScrollPane scrollpaneStocks = new ScrollPane(stocks);
        //ScrollPane object that holds bonds
        ScrollPane scrollpaneBonds = new ScrollPane(bonds);
        //ScrollPane object that holds earnings GridPane
        ScrollPane scrollpaneEarnings = new ScrollPane(earnings);
        //Scene that holds root with dimensions width, height.
        Scene scene = new Scene(root, 750, 750);

        //Horizontal gap between each element inside GridPane
        expenses.setHgap(10);
        earnings.setHgap(10);
        investingContainer.setVgap(10);
        bonds.setHgap(10);
        containerSummary.setHgap(100);
        containerSummary.setPadding(new Insets(0, 50, 0, 50)); //padding around whole grid; (top, right, bottom, left)
        containerAdd.setPadding(new Insets(0, 20, 0, 20));
        yearlySummary.setHgap(10);
        monthlySummary.setHgap(10);
        stocks.setHgap(10);
        //adding menu options/ComboBox to menuContainer
        menuContainer.add(years, 0, 0);
        menuContainer.add(months, 1, 0);
        menuContainer.add(add, 2, 0);
        menuContainer.add(containerAdd, 3, 0);
        //adding stocks and bonds to investing container
        investingContainer.add(scrollpaneStocks, 0, 0);
        investingContainer.add(scrollpaneBonds, 0, 1);
        //adding monthly and yearly summary to summary container
        containerSummary.add(yearlySummary, 0, 0);
        containerSummary.add(monthlySummary, 1, 0);
        //adding elements to BorderPane
        root.setTop(menuContainer);
        root.setLeft(investingContainer);
        root.setCenter(scrollpaneExpenses);
        root.setRight(scrollpaneEarnings);
        root.setBottom(containerSummary);
        //set the title of the window
        primaryStage.setTitle("Personal Finance");
        //add scene to stage
        primaryStage.setScene(scene);
        //make stage unresizable
        primaryStage.setResizable(false);
        //show the current stage
        primaryStage.show();

        //call method to populate ComboBox years and months. Adds functionality to them as well
        populateComboBoxYears(years, months, expenses, earnings, monthlySummary, yearlySummary);
        //call method to populate ComboBox add. Adds functionality to them as well
        populateComboBoxAdd(add, containerAdd, years.getValue().toString(), months.getValue().toString());
        //call method to populate GridPane containerAdd
        populateContainerAdd(add.getValue().toString(), containerAdd, years.getValue().toString(), months.getValue().toString());

        //call method to get obtain stock names and stock amount
        getStockNamesAmount(stockNames, stockAmount);
        //calls getStockPrices me11thod. passing an arraylist of stock names. the method will retrieve the prices of each stock
        readData = getStockPrices(stockNames);
        //calls method to parse read data
        parseReadData(readData, stockPrices, stockNames.size());
        //call method to populate GridPane stocks with stock names, stock amount, and stock prices
        populateStocks(stocks, stockNames, stockAmount, stockPrices);
        //call method to populate bonds GridPane
        populateBonds(bonds);

        //call method get expenses file name based off of whats selected on ComboBox years and ComboBox months
        expensesFile = getExpensesFile(years.getValue().toString(), months.getValue().toString());
        //call method to get earnings file name based off of whats seleced on ComboBox years and ComboBox months
        earningsFile = getEarningsFile(years.getValue().toString(), months.getValue().toString());
        //call method to pass a file to read from and the GridPane expenses to add data to
        populateExpenses(expensesFile, expenses);
        //call method to pass a file to read from and the GridPane expenses to add data to
        populateEarnings(earningsFile, earnings);
        //calls method to calculate monthly summary
        calculateMonthlySummary(expensesFile, earningsFile, monthlySummary);
        //calls method to calculate yearly summary
        calculateYearlySummary(yearlySummary, years.getValue().toString());
    }

    //populates the ComboBox years and months with the years starting from initial recording to current year and the months in a year
    //adds functionality to ComboBoxes
    //Parameters: ComboBox to add years to, ComboBox to add months to, GridPane to add data to, GridPane to add data to, GridPane to add data to, GridPane to add data to
    public void populateComboBoxYears(ComboBox years, ComboBox months, GridPane expenses, GridPane earnings, GridPane monthlySummary, GridPane yearlySummary) {
        //start year to count from. changes based on the year when START recording expenses
        Integer startYear = 2018;
        //variable to get current year
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        //array to hold the months of the year
        String monthArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //variable that holds current month
        Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        //loops through the start year to current year and add years into ComboBox
        for(Integer i = startYear; i <= currentYear; i++) {
            //add item to ComboBox
            years.getItems().add(i.toString());
        }
        //set default item of ComboBox to current year
        years.getSelectionModel().select(currentYear.toString());

        //for loop that lopps through all items in monthArray and sdd them into ComboBox
        for(int i = 0; i < 12; i++) {
            months.getItems().add(monthArray[i]);
        }
        //set number of visible items displayed at once
        months.setVisibleRowCount(12);
        //set default value of ComboBox to current month
        months.getSelectionModel().select(monthArray[currentMonth]);

        //listener for when ComboBox years and months is changed
        //.addListener(property of item itself, old item, new item)
        years.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
            //calls method to obtain expenses file name
            String expensesFile = getExpensesFile(years.getValue().toString(), months.getValue().toString());
            //calls method to obtain earnings file name
            String earningsFile = getEarningsFile(years.getValue().toString(), months.getValue().toString());
            //calls method to read the expenses file
            populateExpenses(expensesFile, expenses);
            //calls method to read earnings file
            populateEarnings(earningsFile, earnings);
            //calls method to calculate monthly summary
            calculateMonthlySummary(expensesFile, earningsFile, monthlySummary);
            //calls method to calculate yearly summary
            calculateYearlySummary(yearlySummary, years.getValue().toString());
        });
        months.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
            //calls method to obtain expenses file name
            String expensesFile = getExpensesFile(years.getValue().toString(), months.getValue().toString());
            //calls method to obtain earnings file name
            String earningsFile = getEarningsFile(years.getValue().toString(), months.getValue().toString());
            //calls method to read the expenses file
            populateExpenses(expensesFile, expenses);
            //calls method to read earnings file
            populateEarnings(earningsFile, earnings);
            //calls method to calculate monthly summary
            calculateMonthlySummary(expensesFile, earningsFile, monthlySummary);
        });
    }

    //populates the ComboBox add with elements. On change will call a method that will change add/subtract the number of text fields that users can input into and add elements to Expenses or Earnings file
    //Parameters: ComboBox to add elements to, GridPane to add mpdes to - passes to populateContainerAdd(), String that holds year - passes to populateContainerAdd(), String that holds month - passes to populateContainerAdd()
    public void populateComboBoxAdd(ComboBox add, GridPane containerAdd, String year, String month) {
        //add elements to ComboBox add
        add.getItems().addAll("Expenses", "Earnings", "Bonds");
        //set default value of ComboBox add
        add.getSelectionModel().select(0);

        //adds funcionality to ComboBox, when an element is selected to something
        add.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            //calls method to add/subtract number of nodes in container add that are needed to add data to specified file
            populateContainerAdd(add.getValue().toString(), containerAdd, year, month);
        });
    }

    //On change to either one will change add/subtract the number of text fields that users can input into and add elements to Expenses or Earnings file
    //Parameters: String to determine type of file to add to, GridPane to add data too, String that holds value from ComboBox years, String that holds value from ComboBox months
    public void populateContainerAdd(String type, GridPane containerAdd, String year, String month) {
        //if ComboBox add selected is Expenses
        if(type.equals("Expenses")) {
            //clears containerAdd
            containerAdd.getChildren().clear();

            //textfield object that contains name
            TextField name = new TextField();
            //textfield obeject that contains amount
            TextField amount = new TextField();
            //textfield that contains date
            TextField date = new TextField();
            //textfield that contains payment method
            TextField paymentMethod = new TextField();
            //textfield that contains type of purchase
            TextField typeOfPurchase = new TextField();
            //button object that will take value from textfields and add to file
            Button add = new Button("Add");
            
            //change the size of textfields
            name.setPrefColumnCount(5);
            amount.setPrefColumnCount(5);
            date.setPrefColumnCount(5);
            paymentMethod.setPrefColumnCount(5);
            typeOfPurchase.setPrefColumnCount(5);

            //add node to containerAdd
            containerAdd.add(name, 0, 0);
            containerAdd.add(amount, 1, 0);
            containerAdd.add(date, 2, 0);
            containerAdd.add(paymentMethod, 3, 0);
            containerAdd.add(typeOfPurchase, 4, 0);
            containerAdd.add(add, 5, 0);

            //add functionality to add button
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //variable to hold file directory
                    String file = getExpensesFile(year, month);
                    //variable holds temp string to be added to file
                    String temp = name.getText() + "," + amount.getText() + "," + date.getText() + "," + paymentMethod.getText() + "," + typeOfPurchase.getText();
                    
                    try {
                        //writer to write to file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                        //add enter to file
                        writer.append("\n");
                        //writes string to file
                        writer.append(temp);
                        //closes file
                        writer.close();
                    }
                    catch (IOException exception) {
                        System.out.println(exception);
                    }

                }
            });
        }
        //if ComboBox add selected is Earnings
        else if(type.equals("Earnings")) {
            //clears containerAdd
            containerAdd.getChildren().clear();

            //textfield object that contains earnings type
            TextField earningsType = new TextField();
            //textfield obeject that contains amount
            TextField amount = new TextField();
            //textfield that contains date
            TextField date = new TextField();
            //textfield that contains note
            TextField note = new TextField();
            //button object that will take value from textfields and add to file
            Button add = new Button("Add");
            
            //change the size of textfields
            earningsType.setPrefColumnCount(5);
            amount.setPrefColumnCount(5);
            date.setPrefColumnCount(5);
            note.setPrefColumnCount(5);

            //add node to containerAdd
            containerAdd.add(earningsType, 0, 0);
            containerAdd.add(amount, 1, 0);
            containerAdd.add(date, 2, 0);
            containerAdd.add(note, 3, 0);
            containerAdd.add(add, 4, 0);

            //add functionality to add button
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //variable to hold file directory
                    String file = getEarningsFile(year, month);
                    //variable holds temp string to be added to file
                    String temp = earningsType.getText() + "," + amount.getText() + "," + date.getText() + "," + note.getText();
                    
                    try {
                        //writer to write to file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                        //add enter to file
                        writer.append("\n");
                        //writes string to file
                        writer.append(temp);
                        //closes file
                        writer.close();
                    }
                    catch (IOException exception) {
                        System.out.println(exception);
                    }

                }
            });
        }
        //if ComboBox add selected Bonds
        else if(type.equals("Bonds")) {
            //clears containerAdd
            containerAdd.getChildren().clear();

            //textfield object that contains price per bond
            TextField price = new TextField();
            //textfield obeject that number of bonds
            TextField numberOfBonds = new TextField();
            //textfield that contains maturity date
            TextField MDate = new TextField();
            //button object that will take value from textfields and add to file
            Button add = new Button("Add");
            
            //change the size of textfields
            price.setPrefColumnCount(5);
            numberOfBonds.setPrefColumnCount(5);
            MDate.setPrefColumnCount(5);

            //add node to containerAdd
            containerAdd.add(price, 0, 0);
            containerAdd.add(numberOfBonds, 1, 0);
            containerAdd.add(MDate, 2, 0);
            containerAdd.add(add, 3, 0);

            //add functionality to add button
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //variable to hold file directory
                    File file = new File("files\\bonds.csv");
                    //variable holds temp string to be added to file
                    String temp = price.getText() + "," + numberOfBonds.getText() + "," + MDate.getText();
                    
                    try {
                        //writer to write to file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                        //add enter to file
                        writer.append("\n");
                        //writes string to file
                        writer.append(temp);
                        //closes file
                        writer.close();
                    }
                    catch (IOException exception) {
                        System.out.println(exception);
                    }

                }
            });
        }
    }

    //determine expenses file name based off of what is selected in ComboBox years and ComboBox months
    //Parameters: String year, String month
    public String getExpensesFile(String year, String month) {
        //array to hold the months of the year
        String monthArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //file is set to the desitnation of files\currentYear\monthNumber_monthName.csv
        return "files\\" + year + "\\" + (Arrays.asList(monthArray).indexOf(month)+1) + "_" + month + "\\" + "expenses.csv";
    }

    //determine earnings file name based off of what is seleced in ComboBox eyars and ComboBox months
    //Parameters: String year, String month
    public String getEarningsFile(String year, String month) {
        //variable that holds current month
        //array to hold the months of the year
        String monthArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //file is set to the desitnation of files\currentYear\monthNumber_monthName.csv
        return "files\\" + year + "\\" + (Arrays.asList(monthArray).indexOf(month)+1) + "_" + month + "\\" + "earnings.csv";
    }

    //read from expenses file that contains expenses and populate expenses GridPane
    //Parameters: String (file) to read from, GridPane to add data to
    public void populateExpenses(String expensesFile, GridPane expenses) {
        //clears expenses GridPane
        expenses.getChildren().clear();

        //add titel to expenses
        expenses.add(new Label("Name"), 0, 0);
        expenses.add(new Label("Amount"), 1, 0);
        expenses.add(new Label("Date"), 2, 0);
        expenses.add(new Label("Method"), 3, 0);
        expenses.add(new Label("Type"), 4, 0);

        //try to open file
        try {
            //creates object scanner that opens up file to read
            Scanner scanner = new Scanner(new File(expensesFile));
            //splits input by delimiter
            scanner.useDelimiter("[,\\r\\n]+");
            
            //variables to determine column and row position
            int row = 1;

            //while file has input
            while(scanner.hasNextLine() == true)
            {
                //get name from file
                expenses.add(new Label(scanner.next()), 0, row);
                //get amount from file
                expenses.add(new Label(scanner.next()), 1, row);
                //get date from file
                expenses.add(new Label(scanner.next()), 2, row);
                //get purchase method from file
                expenses.add(new Label(scanner.next()), 3, row);
                //get purchase type from file
                expenses.add(new Label(scanner.next()), 4, row);
                //add 1 to row, new purchase
                row++;
            }

            //close scanner
            scanner.close();
        }
        //unable to open file
        catch (FileNotFoundException e) {
            //prints error in command line
            System.out.println(e);
            //prints error on screen
            expenses.add(new Label(e.toString()), 0, 0);
        }
    }

    //read from earnings file that contians earnings and populates earnings GridPane
    //Parameters: String (file) to read from, GridPane to add data to
    public void populateEarnings(String earningsFile, GridPane earnings) {
        //clears expenses GridPane
        earnings.getChildren().clear();

        //add title to earnings
        earnings.add(new Label("Type"), 0, 0);
        earnings.add(new Label("Amount"), 1, 0);
        earnings.add(new Label("Date"), 2, 0);
        earnings.add(new Label("Note"), 3, 0);

        //try to open file
        try {
            //creates object scanner that opens up file to read
            Scanner scanner = new Scanner(new File(earningsFile));
            //splits input by delimiter
            scanner.useDelimiter("[,\\r\\n]+");
            
            //variables to determine column and row position
            int row = 1;

            //while file has input
            while(scanner.hasNextLine() == true)
            {
                //get type from file
                earnings.add(new Label(scanner.next()), 0, row);
                //get amount from file
                earnings.add(new Label(scanner.next()), 1, row);
                //get date from file
                earnings.add(new Label(scanner.next()), 2, row);
                //get note from file
                earnings.add(new Label(scanner.next()), 3, row);
                //add 1 to row, new earning
                row++;
            }

            //close scanner
            scanner.close();
        }
        //unable to open file
        catch (FileNotFoundException e) {
            //prints error in command line
            System.out.println(e);
            //prints error on screen
            earnings.add(new Label(e.toString()), 0, 0);
        }
    }

    //read from arraylist and populates GridPane with stock names, amount, and prices
    //Parameters: GridPane to add elements to, ArrayList that holds stockNames, ArrayList that holds stockAmount, ArrayList that holds stockPrices
    public void populateStocks(GridPane stocks, ArrayList<String> stockNames, ArrayList<String> stockAmount, ArrayList<String> stockPrices) {
        //creates DecimalFormat object that determines the number of decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        //rounsd the last positional places of DecimalFormat down
        df.setRoundingMode(RoundingMode.FLOOR);
        //variable to hold total
        Double total = Double.valueOf(0.00);
        //variables to determine row position
        int row = 2;

        //add title to stocks GridPane
        stocks.add(new Label("Stocks:"), 0, 0);
        stocks.add(new Label("Ticker"), 0, 1);
        stocks.add(new Label("Amount"), 1, 1);
        stocks.add(new Label("Price"), 2, 1);
        stocks.add(new Label("Total"), 3, 1);

        //for loop that loops through arralylists and adds their values to GridPane
        for(int i = 0; i < stockNames.size(); i++) {
            //add stock name to GridPane
            stocks.add(new Label(stockNames.get(i)), 0, row);
            //add stock amount to GridPane
            stocks.add(new Label(stockAmount.get(i)), 1, row);
            //add stock price to Gridpane
            stocks.add(new Label(stockPrices.get(i)), 2, row);
            //add total cost of stocks (stock amount * stock prices)
            stocks.add(new Label(df.format(Double.parseDouble(stockAmount.get(i)) * Double.parseDouble(stockPrices.get(i)))), 3, row);
            total = total + (Double.parseDouble(stockAmount.get(i)) * Double.parseDouble(stockPrices.get(i)));
            //increase row by 1, new stock name
            row++;
        }

        //adds total amount (price) next to title
        stocks.add(new Label(df.format(total)), 1, 0);
    }

    //read from file and populates bonds GridPane
    public void populateBonds(GridPane bonds) {
        //creates DecimalFormat object that determines the number of decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        //rounsd the last positional places of DecimalFormat down
        df.setRoundingMode(RoundingMode.FLOOR);
        //creates a date format object that formats date into "mm/dd/yyy"
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyy");
        //creates date object
        Date date = new Date();

        //variable holds total price of bonds after maturity
        Double total = Double.valueOf(0.00);
        //variable holds total price of bonds after discount
        Double totalDiscount = Double.valueOf(0.00);

        //add title to GridPane
        bonds.add(new Label("Bonds:"), 0, 0);
        bonds.add(new Label("Profit:"), 0, 1);
        bonds.add(new Label("Price"), 0, 2);
        bonds.add(new Label("#"), 1, 2);
        bonds.add(new Label("MDate"), 2, 2);

        //try to read from file
        try {
            //new scanner object to read from file
            Scanner scanner = new Scanner(new File("files\\bonds.csv"));
            //splits input by delimiter
            scanner.useDelimiter("[,\\r\\n]+");

            //variable to hold price
            String price;
            //variable to hold amount of a particular bond
            String amount;
            //variable to hold maturity date
            String MDate;

            //variable to keep track of current row in GridPane
            int row = 3;

            //while file has input
            while(scanner.hasNextLine() == true)
            {
                //obtain price from file
                price = scanner.next();
                //obtain amount from file
                amount = scanner.next();
                //obtain maturity date from file
                MDate = scanner.next();

                //add price to GridPane
                bonds.add(new Label(price), 0, row);
                //add amount to GridPane
                bonds.add(new Label(amount), 1, row);

                //label to hold maturity date
                Label temp = new Label(MDate);
                //if maturity date is the same as current date

                //try catch for .parse(MDate) just incase of MDate is in the wrong format
                try {
                    if(MDate.equals(sdf.format(date))) {
                        //change background color of label to green
                        temp.setStyle("-fx-background-color:green");
                    }
                    //maturity date is in the past
                    else if (sdf.parse(MDate).after(date)) {
                        //change background color of label to red
                        temp.setStyle("-fx-background-color:red");
                    }
                }
                catch (ParseException e) {
                    System.out.println(e);
                }


                //add maturity date to GridPane
                bonds.add(temp, 2, row);
                
                //new set of bonds, new row
                row++;

                //update totals
                total = total + (100 * Double.parseDouble(amount));
                totalDiscount = totalDiscount + (Double.parseDouble(price) * Double.parseDouble(amount));
            }
            
            //closes scanner
            scanner.close();
        }
        //unable to read from file
        catch (FileNotFoundException e) {
            //prints error in command line
            System.out.println(e);
        }

        //add totals next to bonds title
        bonds.add(new Label(df.format(total)), 1, 0);
        bonds.add(new Label(df.format(totalDiscount)), 2, 0);
        bonds.add(new Label(df.format(total - totalDiscount)), 1, 1);
    }

    //method that obtains the names and amount of stocks bought
    //Parameters: ArrayList to add stock names to, ArrayList to add number/amount bought to
    public void getStockNamesAmount(ArrayList<String> stockNames, ArrayList<String> stockAmount) {
        try {
            //creates object scanner that opens up file to read
            Scanner scanner = new Scanner(new File("files\\stocks.csv"));
            //splits input by delimiter
            scanner.useDelimiter("[,\\r\\n]+");
            
            //while not at end of file
            //stocks.csv is formmated: stockName, numberOfStock
            while(scanner.hasNext()) {
                //adds stock name to arraylist
                stockNames.add(scanner.next());
                //add number of stock to arraylist
                stockAmount.add(scanner.next());
            }

            //close scanner
            scanner.close();
        }
        catch (FileNotFoundException e){
            //prints error in command line
            System.out.println(e);
        }
    }

    //get stock prices from URL endpoint
    //Parameters: GridPane to get children from
    public String getStockPrices(ArrayList<String> stockName) {
        //holds the name of stocks to be passed to endpoint
        String stockNames = "";
        //for loop to add names of stocks from arraylist to variable
        for(int i = 0; i < stockName.size(); i++) {
            //append current value of arraylist to variable
            stockNames = stockNames + stockName.get(i) + ","; 
        }
        //removes the lest character in variable (,)
        stockNames = stockNames.substring(0, stockNames.length()-1);
        
        try {
            //holds URL for enpoint
            URL url = new URL("https://api.iextrading.com/1.0/stock/market/batch?symbols=" + stockNames + "&types=delayed-quote&range=ytd");

            //opens connection. sends a GET request
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //creates a new reader to read the information obtained from request
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            //temp reading variable
            String temp = "";
            //variable to hold read data
            String inputLine = "";
            //while there is still input being read
            while((temp = in.readLine()) != null) {
                //append temp to read data
                inputLine = inputLine + temp;
            }
            //close buffered reader
            in.close();
            //disconnect from endpoint
            con.disconnect();
            
            //returns the data read from API endpoint
            return inputLine;
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        //return empty data
        return stockNames;
    }

    //parses the read data. will obtain the prices of stocks and places them inside an array list
    //Parameters: String to read from, ArayList to add prices to, Integer is the size of ArrayList that holds the stock names
    public void parseReadData(String readData, ArrayList<String> stockPrices, int stockNamesSize) {
        //variable that holds what value to look for in read data
        String lookFor = "\"delayedPrice\":";
        //variable to hold delimiter for when price of stock ends
        String delimiter = ",";
        //temp variable to what is being read
        String temp = "";
        //varaible to hold current location of substring
        int currentLocation = 0;
        //holds the current size of stockPrices
        int currentSize = 0;
        //loops through readData while currentSize is less than stockNamesSize
        while (currentSize < stockNamesSize) {
            //if the read data starting at position currentLocation and ending at position currentLocaiton + lookFor.length() is equal to the string that we are looking for (lookFor)
            if(readData.substring(currentLocation, currentLocation + lookFor.length()).equals(lookFor)) {
                //set currentLocation to end of lookFor string
                currentLocation = currentLocation + lookFor.length();
                //loop that adds characters to temp until it finds the delimiter
                //while current substring is not equal to delimiter
                while(!readData.substring(currentLocation, currentLocation + 1).equals(delimiter)) {
                    //add read character to temp
                    temp = temp + readData.substring(currentLocation, currentLocation + 1);
                    //increase currentLocation by 1, move forward 1 position
                    currentLocation++;
                }
                //converts temp to integer and adds it to array list
                stockPrices.add(temp);
                //resets temp
                temp = "";
                //increase currentLocation by 1, move forward 1 position
                currentLocation++;
                //increase crrentSize by 1, found the price of a stock
                currentSize++;
            }
            //if current substring is not equal to what we are looking for (lookFor)
            else {
                //increase currentLocation by 1, move forward 1 position
                currentLocation++;
            }
        }
    }

    //calculates monthly expenses based off of file read
    //Parameters: String to read file from, GridPane to add data to
    public void calculateMonthlySummary(String expensesFile, String earningsFile, GridPane monthlySummary) {
        //clears monthly summary
        monthlySummary.getChildren().clear();
        //creates DecimalFormat object that determines the number of decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        //rounsd the last positional places of DecimalFormat down
        df.setRoundingMode(RoundingMode.FLOOR);

        //variable to hold total entertainment expenses
        Double entertainment = Double.valueOf(0.00);
        //variable to hold total investing expenses
        Double investing = Double.valueOf(0.00);
        //variable to hold total bills expenses
        Double bills = Double.valueOf(0.00);
        //variable to hold total gas expenses
        Double gas = Double.valueOf(0.00);
        //variable to hold total groceries expenses
        Double groceries = Double.valueOf(0.00);
        //variable to hold total other expenses
        Double otherExpenses = Double.valueOf(0.00);
        //variable to hold total of all expenses
        Double totalExpenses = Double.valueOf(0.00);

        //variable to hold dividend earnings
        Double dividend = Double.valueOf(0.00);
        //variable to hold paycheck earnings
        Double paycheck = Double.valueOf(0.00);
        //variable to hold bonds earnings
        Double bond = Double.valueOf(0.00);
        //varialbe to hold other earnings
        Double otherEarnings = Double.valueOf(0.00);
        //variable to hold total of all earnings
        Double totalEarnings = Double.valueOf(0.00);

        //arraylist to hold purchase types
        ArrayList<String> purchaseType = new ArrayList<String>();
        //prepopulate purchaseTypes with: E - Entertainment, I - Investing, B - Bills, G - Gas, Gr - Groceries, O - Other
        purchaseType.add("E");
        purchaseType.add("I");
        purchaseType.add("B");
        purchaseType.add("G");
        purchaseType.add("Gr");
        purchaseType.add("O");

        //arraylist to hold earnings types
        ArrayList<String> earningsType = new ArrayList<String>();
        earningsType.add("Dividend");
        earningsType.add("Paycheck");
        earningsType.add("Bond");
        earningsType.add("Other");

        //adds label to GridPane
        monthlySummary.add(new Label("Monthly Summary:"), 0, 0);

        //try to open expenses file
        try {
            //creates object scanner that opens up file to read
            Scanner scanner = new Scanner(new File(expensesFile));
            //splits input by delimiter
            scanner.useDelimiter("[,\\r\\n]+");

            //while file has another line to parse
            //string read from file = name,amount,date,purchaseMethod,purcahseType
            while(scanner.hasNextLine() == true) {
                //holds name of purcahse
                String name = scanner.next();
                //holds amount of purchase
                Double amount = Double.parseDouble(scanner.next());
                //holds date of purchase
                String date = scanner.next();
                //holds purchase method
                String method = scanner.next();
                //holds purchase type
                String type = scanner.next();
                
                //add the purcahse amount to appropriate total depending on purchaseType
                switch(type) {
                    case "E":
                        entertainment = entertainment + amount;
                        break;
                    case "I":
                        investing = investing + amount;
                        break;
                    case "B":
                        bills = bills + amount;
                        break;
                    case "G":
                        gas = gas + amount;
                        break;
                    case "Gr":
                        groceries = groceries + amount;
                        break;
                    case "O":
                        otherExpenses = otherExpenses + amount;
                        break;
                    default:
                        System.out.println(type + ": Not valid");
                        break;
                }
            }
            //closes scanner
            scanner.close();

            //calculate total expenses
            totalExpenses = entertainment + investing + bills + gas + groceries + otherExpenses;

            //add elements to GridPane
            monthlySummary.add(new Label("Entertainment:"), 0, 1);
            monthlySummary.add(new Label(df.format(entertainment)), 1, 1);
            monthlySummary.add(new Label("Investing:"), 0, 2);
            monthlySummary.add(new Label(df.format(investing)), 1, 2);
            monthlySummary.add(new Label("Bills:"), 0, 3);
            monthlySummary.add(new Label(df.format(bills)), 1, 3);
            monthlySummary.add(new Label("Gas:"), 0, 4);
            monthlySummary.add(new Label(df.format(gas)), 1, 4);
            monthlySummary.add(new Label("Groceries:"), 0, 5);
            monthlySummary.add(new Label(df.format(groceries)), 1, 5);
            monthlySummary.add(new Label("Other:"), 0, 6);
            monthlySummary.add(new Label(df.format(otherExpenses)), 1, 6);
            monthlySummary.add(new Label("Total:"), 0, 7);
            monthlySummary.add(new Label(df.format(totalExpenses)), 1, 7);
        }
        //unable to open expenses file
        catch (FileNotFoundException e) {
            //prints error in command line
            System.out.println(e);
            //prints error on screen
            monthlySummary.add(new Label(e.toString()), 0, 0);
        }

        //try to open earnings file
        try {
             //creates object scanner that opens up file to read
             Scanner scanner = new Scanner(new File(earningsFile));
             //splits input by delimiter
             scanner.useDelimiter("[,\\r\\n]+");

            //while file has another line to parse
            //string read from file = name,amount,date,purchaseMethod,purcahseType
            while(scanner.hasNextLine()) {
                //holds earnings name
                String type = scanner.next();
                //holds earnings amount
                Double amount = Double.parseDouble(scanner.next());
                //holds earnings date
                String date = scanner.next();
                //holds earnings note
                String method = scanner.next();
                
                //add the purcahse amount to appropriate total depending on purchaseType
                switch(type) {
                    case "Dividend":
                        dividend = dividend + amount;
                        break;
                    case "Paycheck":
                        paycheck = paycheck + amount;
                        break;
                    case "Bond":
                        bond = bond + amount;
                        break;
                    case "Other":
                        otherEarnings = otherEarnings + amount;
                        break;
                    default:
                        System.out.println(type + ": Not valid");
                        break;
                }
            }

            //closes scanner
            scanner.close();

            //calculate total earnings
            totalEarnings = paycheck + dividend + bond + otherEarnings;

            //add elements to GridPane
            monthlySummary.add(new Label("Paycheck:"), 2, 1);
            monthlySummary.add(new Label(df.format(paycheck)), 3, 1);
            monthlySummary.add(new Label("Dividend:"), 2, 2);
            monthlySummary.add(new Label(df.format(dividend)), 3, 2);
            monthlySummary.add(new Label("Bonds:"), 2, 3);
            monthlySummary.add(new Label(df.format(bond)), 3, 3);
            monthlySummary.add(new Label("Others:"), 2, 4);
            monthlySummary.add(new Label(df.format(otherEarnings)), 3, 4);
            monthlySummary.add(new Label("Total:"), 2, 5);
            monthlySummary.add(new Label(df.format(totalEarnings)), 3, 5);
        }
        //unable to open earnings files file
        catch (FileNotFoundException e) {
            //prints error in command line
            System.out.println(e);
            //prints error on screen
             monthlySummary.add(new Label(e.toString()), 0, 0);
        }

        //add difference between earnings and expenses to GridPane
        monthlySummary.add(new Label(df.format(totalEarnings - totalExpenses)), 1, 0);
    }

    //calculates yearly expenses based off of file read
    //Parameters: GridPane to add data to, String containing current year
    public void calculateYearlySummary(GridPane yearlySummary, String currentYear) {
        //clears monthly summary
        yearlySummary.getChildren().clear();
        //creates DecimalFormat object that determines the number of decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        //rounsd the last positional places of DecimalFormat down
        df.setRoundingMode(RoundingMode.FLOOR);

        //variable to hold total entertainment expenses
        Double entertainment = Double.valueOf(0.00);
        //variable to hold total investing expenses
        Double investing = Double.valueOf(0.00);
        //variable to hold total bills expenses
        Double bills = Double.valueOf(0.00);
        //variable to hold total gas expenses
        Double gas = Double.valueOf(0.00);
        //variable to hold total groceries expenses
        Double groceries = Double.valueOf(0.00);
        //variable to hold total other expenses
        Double otherExpenses = Double.valueOf(0.00);
        //variable to hold total of all expenses
        Double totalExpenses = Double.valueOf(0.00);

        //variable to hold dividend earnings
        Double dividend = Double.valueOf(0.00);
        //variable to hold paycheck earnings
        Double paycheck = Double.valueOf(0.00);
        //variable to hold bonds earnings
        Double bond = Double.valueOf(0.00);
        //varialbe to hold other earnings
        Double otherEarnings = Double.valueOf(0.00);
        //variable to hold total of all earnings
        Double totalEarnings = Double.valueOf(0.00);

        //arraylist to hold purchase types
        ArrayList<String> purchaseType = new ArrayList<String>();
        //prepopulate purchaseTypes with: E - Entertainment, I - Investing, B - Bills, G - Gas, Gr - Groceries, O - Other
        purchaseType.add("E");
        purchaseType.add("I");
        purchaseType.add("B");
        purchaseType.add("G");
        purchaseType.add("Gr");
        purchaseType.add("O");

        //arraylist to hold earnings types
        ArrayList<String> earningsType = new ArrayList<String>();
        earningsType.add("Dividend");
        earningsType.add("Paycheck");
        earningsType.add("Bond");
        earningsType.add("Other");

        //array to hold the months of the year
        String monthArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        //adds label to GridPane
        yearlySummary.add(new Label("Yearly Summary:"), 0, 0);
        
        //for loop that loops through all elements of monthArray
        for(int i = 0; i < 12; i++) {
            //assigns directory path of year + month expenses to variable
            String expensesFile = "files\\" + currentYear + "\\" + (i+1) + "_" + monthArray[i] + "\\" + "expenses.csv";
            //assigns directory path of year + month earnings to variable
            String earningsFile = "files\\" + currentYear + "\\" + (i+1) + "_" + monthArray[i] + "\\" + "earnings.csv";

            //try to open expenses file
            try {
                //creates object scanner that opens up file to read
                Scanner scanner = new Scanner(new File(expensesFile));
                //splits input by delimiter
                scanner.useDelimiter("[,\\r\\n]+");

                //while file has another line to parse
                //string read from file = name,amount,date,purchaseMethod,purcahseType
                while(scanner.hasNextLine() == true) {
                    //holds name of purcahse
                    String name = scanner.next();
                    //holds amount of purchase
                    Double amount = Double.parseDouble(scanner.next());
                    //holds date of purchase
                    String date = scanner.next();
                    //holds purchase method
                    String method = scanner.next();
                    //holds purchase type
                    String type = scanner.next();
                    
                    //add the purcahse amount to appropriate total depending on purchaseType
                    switch(type) {
                        case "E":
                            entertainment = entertainment + amount;
                            break;
                        case "I":
                            investing = investing + amount;
                            break;
                        case "B":
                            bills = bills + amount;
                            break;
                        case "G":
                            gas = gas + amount;
                            break;
                        case "Gr":
                            groceries = groceries + amount;
                            break;
                        case "O":
                            otherExpenses = otherExpenses + amount;
                            break;
                        default:
                            System.out.println(type + ": Not valid");
                            break;
                    }
                }
                //closes scanner
                scanner.close();
            }
            //unable to open expenses file
            catch (FileNotFoundException e) {
                //prints error in command line
                System.out.println(e);
                //prints error on screen
                yearlySummary.add(new Label(e.toString()), 0, 0);
            }

            //try to open earnings file
            try {
                //creates object scanner that opens up file to read
                Scanner scanner = new Scanner(new File(earningsFile));
                //splits input by delimiter
                scanner.useDelimiter("[,\\r\\n]+");

                //while file has another line to parse
                //string read from file = name,amount,date,purchaseMethod,purcahseType
                while(scanner.hasNextLine()) {
                    //holds earnings name
                    String type = scanner.next();
                    //holds earnings amount
                    Double amount = Double.parseDouble(scanner.next());
                    //holds earnings date
                    String date = scanner.next();
                    //holds earnings note
                    String method = scanner.next();
                    
                    //add the purcahse amount to appropriate total depending on purchaseType
                    switch(type) {
                        case "Dividend":
                            dividend = dividend + amount;
                            break;
                        case "Paycheck":
                            paycheck = paycheck + amount;
                            break;
                        case "Bond":
                            bond = bond + amount;
                            break;
                        case "Other":
                            otherEarnings = otherEarnings + amount;
                            break;
                        default:
                            System.out.println(type + ": Not valid");
                            break;
                    }
                }

                //closes scanner
                scanner.close();
            }
            //unable to open earnings files file
            catch (FileNotFoundException e) {
                //prints error in command line
                System.out.println(e);
                //prints error on screen
                yearlySummary.add(new Label(e.toString()), 0, 0);
            }
        }

        //calculate total expenses
        totalExpenses = entertainment + investing + bills + gas + groceries + otherExpenses;
        //calculate total earnings
        totalEarnings = paycheck + dividend + bond + otherEarnings;

        //add expenses elements to GridPane
        yearlySummary.add(new Label("Entertainment:"), 0, 1);
        yearlySummary.add(new Label(df.format(entertainment)), 1, 1);
        yearlySummary.add(new Label("Investing:"), 0, 2);
        yearlySummary.add(new Label(df.format(investing)), 1, 2);
        yearlySummary.add(new Label("Bills:"), 0, 3);
        yearlySummary.add(new Label(df.format(bills)), 1, 3);
        yearlySummary.add(new Label("Gas:"), 0, 4);
        yearlySummary.add(new Label(df.format(gas)), 1, 4);
        yearlySummary.add(new Label("Groceries:"), 0, 5);
        yearlySummary.add(new Label(df.format(groceries)), 1, 5);
        yearlySummary.add(new Label("Other:"), 0, 6);
        yearlySummary.add(new Label(df.format(otherExpenses)), 1, 6);
        yearlySummary.add(new Label("Total:"), 0, 7);
        yearlySummary.add(new Label(df.format(totalExpenses)), 1, 7);

        //add earnings elements to GridPane
        yearlySummary.add(new Label("Paycheck:"), 2, 1);
        yearlySummary.add(new Label(df.format(paycheck)), 3, 1);
        yearlySummary.add(new Label("Dividend:"), 2, 2);
        yearlySummary.add(new Label(df.format(dividend)), 3, 2);
        yearlySummary.add(new Label("Bonds:"), 2, 3);
        yearlySummary.add(new Label(df.format(bond)), 3, 3);
        yearlySummary.add(new Label("Others:"), 2, 4);
        yearlySummary.add(new Label(df.format(otherEarnings)), 3, 4);
        yearlySummary.add(new Label("Total:"), 2, 5);
        yearlySummary.add(new Label(df.format(totalEarnings)), 3, 5);

        //add difference between earnings and expenses to GridPane
        yearlySummary.add(new Label(df.format(totalEarnings - totalExpenses)), 1, 0);
    }
}
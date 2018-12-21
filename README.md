# Personal-Finance
Java application that helps me personally keep track of my expenses, stock prices, and gains

# Required Files:
A folder that contains the year(s) since start of recording expenses
  Must contain at least one folder, current year. Example: 2018
  If a folder's name (year) is less then current year, need additional folders that increment by 1 until there is a folder that holds current year. Example: Current year is 2018. Oldest folder is 2016. Need folders 2016, 2017, and 2018.
  
Inside each year folder needs 12 months folder. labeled 1_January, 2_February, 3_March, etc.

Inside each months folder needs 2 csv files: expenses.csv, earnings.csv
  .csv files may be empty
  if not empty, expenses csv files need to be formmatted: NameOfPurchase, Amount, Date, PurchaseMethod, TypeOfPurchase
  if not empty, earnings csv files need to be formmatted: TypeOfEarnings, Amount, Date, Notes

In the same directory as year(s) folders. Need a stocks.csv file
  format of stocks.csv: NameOfStock, NumberOfStocks

In the same directory as year(s) folders. Need a bonds.csv file
	format of bonds.csv: Price per 100, Number of bond, maturity date

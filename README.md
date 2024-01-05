# Steam Investment Manager

## Application work example:

https://github.com/IvanKolchanov/SteamInvestmentManager/assets/83294629/d0bc08ff-6616-4452-b28a-b52f99b25851

## UI:
Adding new item: <br>
![image](https://github.com/IvanKolchanov/SteamInvestmentManager/assets/83294629/fa0671ee-9b80-40dd-bb5b-827136926f83)
New item showed after adding: <br>
![image](https://github.com/IvanKolchanov/SteamInvestmentManager/assets/83294629/820d3012-0699-42d0-a294-3cc458ca271b)
Item change dialog:
![image](https://github.com/IvanKolchanov/SteamInvestmentManager/assets/83294629/a0379f89-170d-494e-9bd4-4a65c9ed7c3d)

The project's purpose is to have all of your investments displaied in a convenient and goodlooking manner on your phone.
The project could be imporved to have more information about buy's and sell's, more convenient import methods, more data, rapid price change notifications.
Sadly one of the technologies used in the project is library GSON, which is outdated for the current version of JDK and can only work with JDK 15 or earlier.

This project was one of my earliest works in Android development and object-oriented development in general, so it lacks in structure and has a lot of inefficient solutions.
However, it is a working parcer application with nice representation of data, that satisfies the intended purpose.


## DESPCRIPTION OF CLASSES
* MainActivity - initializes the application work, saves data and gets it from preference
* ItemsUpdatingThread - contains function for updating the prices for all of the items currently displayed in the app, runs constantly in the background
* ItemAddingThread - is used when a new item is added to the program through the steam link, gets a lot of information important for representation of data.
P.S. Java Soup or other data-scraping libraries haven't been used in the application, therefore it scrapes information by manually looking through HTML code.

Util Classes:
* CurrencyData - used to get the currency symbols for the prices, has a function for getting the current actual conversion rate between 2 currencies in Steam
* DownloadBitmapImage - a class implementing Callable method, used for downloading item icons for the user interface
* DonwloadPageHtmlCode - a class implementing Callable method, used for downloading HTML code of a website with a given link
* EnteringURLDialog - a class extending DialogFragment that manages the launch of adding a new item menu (putting in link, buy price, buy amount)
* PriceoverviewSteamItem - a data class for use with GSON library for when getting the current price of an item
* SettingsDialog - a class extending DialogFragment that manages the launch of Settings menu
* SteamGetURLCreator - a class dedicated to creating a priceoverview link for a created item
* SteamItem - a data class for a steamItem, used for saving all the data needed for representing the items in user interface
* SteamItemAdapter - used to manage the representation of items in the list in the user interface
* SteamItemInformationDialog - a class extending DialogFragment that manages the representation of the item, when user clicks on it
* SteamItemsListViewData - a data class

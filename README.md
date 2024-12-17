# Recipe-Handler terminal version

This repository demonstrates a basic java application that scans recipe files with ".cook" format.
## Installation

Clone the repository
```
git clone https://github.com/nikos-kaparos/Recipe-Handler.git
```

## Build the project with Maven:
```
mvn clean package
```

## Usage
Run 
```bash
java -jar OOP2_PROJECT-1.0-SNAPSHOT.jar <file>
```
Run many files 
```bash
java -jar OOP2_PROJECT-1.0-SNAPSHOT.jar -list <file1> <file2>
```

## Features

-   **Prints**:  The Ingredients, Cookware, Time and Steps needed for the recipe.
-   **Print Groceries**: The app print all ingredients for the recipes that are given as input in the terminal.


## Technologies

-   Java 21
-   Maven 3.3.0

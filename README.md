# SwagLabTesting

SwagLabTesting is an automated testing project for the Swag Labs e-commerce website. It is written in Java and uses Selenium WebDriver and TestNG for testing. The project aims to test the functionality of the Swag Labs website by automating the testing process.

## Test Resources

The following resources are used for testing the Swag Labs website:

- **Selenium WebDriver :** A browser automation tool that allows you to interact with web pages as a user would.
- **TestNG :** A testing framework that allows you to write and run tests in Java.
- **ChromeDriver :** A WebDriver implementation that allows you to automate Google Chrome.
- **Maven :** A build automation tool that manages dependencies and builds the project.

## Project Structure

The project is structured as follows:

SwagLabTesting/

```
├── src/
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   ├── BaseTest.java
│       │   │   └── TestController.java
│       │   ├── testcases/
│       │   │   ├── UserLogin.java
│       │   │   ├── Products.java
│       │   │   └── CheckOut.java
│       │   └── utilities/
│       │       ├── ExcelDataReaderWriter.java
│       │       ├── ImageLoad.java
│       │       └── Snapshot.java
│       │
│       └── resources/
│           ├── configfiles/
│           │   ├── config.properties
│           │   └── locators.properties
│           ├── reports/
│           │   └── SwagLabTestReport.html
│           ├── screenshots/
│           └── testdata/
│               ├── Regression.xlsx
│               └── swagLabTestData.xlsx
├── pom.xml
├── testng.xml
└── README.md
```

## Getting Started

To get started with the project, clone the repository and import the project into your preferred IDE. Ensure that you have the necessary dependencies installed, which can be found in the `pom.xml` file.

### Prerequisites

- Java JDK 8 or later
- Maven

### Installing

1. Clone the repository:

```
git clone https://github.com/Soumyajit7/SwagLabTesting.git
```

2. Change to the project directory:

```
cd SwagLabTesting
```

3. Build the project:

```
mvn clean install
```

4. Run the tests:

```
mvn test
```

### Test Data

The test data is stored in `src/test/resources/testdata/swagLabTestData.xlsx`. It contains the data required for testing different scenarios in the Swag Labs website. And `src/test/resources/testdata/Regression.xlsx.` is defined for particular testcases wanted to run.

### Test Report

The test report is generated using Extent Reports and can be found in the `src/test/resources/reports/SwagLabTestReport.html` directory. It provides a detailed overview of the test results, including a summary of passed, failed, and skipped tests, as well as logs and screenshots for each test.
[Report](./src/test/resources/repots/SwagLabTestReport.html)

## Built With

- Java
- Selenium WebDriver
- TestNG
- Maven
- Extent Reports

## Author

- Soumyajit Pan - [GitHub](https://github.com/Soumyajit7)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.


![]('./src/test/resources/repots/SwagLabTestReport.html')

htmltools::includeHTML("./src/test/resources/repots/SwagLabTestReport.html")

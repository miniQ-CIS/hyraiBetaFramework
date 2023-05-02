package stepDefinitions;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import pages.CommonPage;
import utilities.BrowserUtilities;
import utilities.DatabaseUtilities;
import utilities.ConfigurationReader;
import utilities.Driver;

public class Hooks {

     /* By the looks of it these variables namely ( WebDriver driver, CommonPage commonPage, Actions actions, BrowserUtilities browserUtilities)
    and although a bit different but still including (boolean isHeadless, String browserType, boolean isFullScreen, int width and lastly int height)
    are all variables that we are creating to be ready for instantiation (if not already instantiated) with the use of methods below that get
    triggered with the use of annotations. So atm that is all about this Hooks class now-


    (I figure we will need to create variable such as ApiUtilities apiUtilities in the future in this Hooks class when we move on to be testing.
     */

    /* And the WebDriver type below is from selenium (As far as I understand, this global "driver" variable doesnt break to signleton
     as this driver gets instantiated below - in setup method with the use of Driver.getDriver method which means singleton gets applied */
    public static WebDriver driver;

    /* And the CommonPage type below is referring to an abstract class that we have created - and we know that we cant create
    object from abstract classes - so what are we going to do with this? we will see
     */
    public static CommonPage commonPage;
    /* Below is Actions reference type from selenium
     */
    public static Actions actions;
    /*Below is BrowserUtils object that is also instantiated, it is a class that we have created.
     */
    public static BrowserUtilities browserUtilities = new BrowserUtilities();

    /* ********* These below five variables that we have created are obviously different from the ones above -
    looks like we will use them to configure the browser that we have - (in the past we use to do those inside the Driver class right?)
     */

    /*
    Does this mean, for example we are hard-codding some data in hooks class that will allow us to change the flow of our test execution?
    Will I be needing to come here for example and change browserType to firefox if I want my tests to run on firefox?
    I guess not - That might be done with annotations that we have? I am not sure atm
     */
    public static boolean isHeadless = false;
    public static String browserType = "chrome";

    public static boolean isFullScreen = true;
    public static int width;
    public static int height;

    /*

    ↑↑↑↑ Above are some global variables - some instantiated some not ↑↑↑↑

    Clearly you can group the above and below into 2 different groups

    ↓↓↓↓ Below are the methods ↓↓↓↓

     */


    /* In total there are 8 methods - 6 Before and 2 After
    3 of them has order number 0, so they will run first
     */


    /*
    What does this below one do? Lets take this slow -> this method will be running BEFORE each and every SCENARIO
    in our project (unless we specify with @TAGS)

    !!! SO ANY SCENARIO THAT DOES NOT HAVE THE TAG @headless WILL NOT TRIGGER THIS METHOD AND NOTHING WILL HAPPEN

    so we specify here with a tag "@headless" so if any of our scenarios have the "@headless" tag it will do what?
    it will run the method setIsHeadless and then it will make the above global "isHeadless" variable true
    Notice that isHeadless variable was on false in default

    !!! AND ONE OF THE MOST IMPORTANT DETAIL HERE IS THAT IN DRIVER CLASS WE IMPORT BOTTOM 5 STATIC VARIABLES *** TO BE USED IN THE SETTING
    UP OF THE DRIVER - AS YOU HAVE NOTICED NOW THAT BOTTOM 5 VARIABLE IS RELATED TO THE CONFIG OF THE DRIVER
    UNLIKE THE 4 ABOVE, THOSE 4 WILL BE USED IN HERE, THE HOOKS CLASS AND DONT NEED TO BE IMPORTED IN THE DRIVER CLASS, THEIR JOB IS HERE
     */
    @Before(value = "@headless", order = 0)
    public void setIsHeadless() {
        isHeadless = true;
    }

    /* Just like the above @Before method this method uses the order=0 for the reason of these variables need to be assigned prior to
    the instatiation of the Driver - that is why the below @Before with the @UI tag has the order 1 not 0 - I bet there would be some bugs or
    problems if its order was 0 as well or -1? , I checked it creates problems such as giving chrome browser although we have firefox tag, as expected
     */
    @Before(value = "@firefox", order = 0)
    public void setIsFirefox() {
        browserType = "firefox";
    }

    /*
    Just like the 2 methods above this method changes the variables values that will be ready to be used in the configuration of the Driver
    that will be created in the next step.
     */
    @Before(value = "@iPhone12", order = 0)
    public void setiPhone12() {
        isFullScreen = false;
        width = 390;
        height = 844;
    }


    /* And finally this last 2 methods - OK, I am back

    Lets understand this method - it has order 1 and tag @UI so if a scenario has @UI tag this will be triggered however
    even though it will be triggered it will wait for other @Before method with lower order number such as 0 to be completed first
    if they are triggered as well - like all of the 3 @before methods above - the reason is clear, above 3 methods are needed to create a
    driver session with desired config when needed.

    if non of those @before method with order 0 are triggered this method will create a session with default config
    in our case with:

    isHeadless - false
    browserType - chrome
    isFullscreen - true

    then as you can see below in the first line of the body it will instantiate the WebDriver object with the use of
    Driver classes getDriver() method.

    NOTICE THAT WHEN THIS METHOD IS CALLED WE HAVE STARTED WITH THE SINGLETON DESING PATTERN APPROACH AS THIS METHOD
    APPLIES IF CONDITION WITHIN IT THAT RESTRICTS IT TO CREATE A NEW SESSION IF THERE IS ALREADY EXISTING ONE.

    WHEN THIS LINE IS DONE WE WILL HAVE OUR SESSION STARTED AND WE WILL HAVE A BROWSER OPENING WAITING FOR ORDERS

    THE OTHER 2 LINES INSTATIATES THE STATIC GLOBAL VARIABLES WE HAVE CREATED ABOVE WHICH ARE VERY LIKELY GOING TO BE
    NEEDED IN OUR EXECUTION SO WE CREATE THEM HERE AND MAKE THEM READY FOR USE

    ALTOUGH IT FEELS WEIRD FOR YOU KNOW YOU CAN TRY AND SEE THAT below variables

    driver
    commonPage
    actions

    can now be called in any of our stepDefinitions classes

    browserUtilities could have already been called as it was instantiated anyways.

    THAT IS ABOUT IT I GUESS WE UNDERSTOOD I BELIEVE EVERYTHING IN THIS HOOKS CLASS AND HOW THEY WORK.

    LETS MOVE TO THE DRIVER AND THEN COMMON PAGE AND HOME PAGE AND THEN ENUMS

     */

    @Before(order = 1, value = "@UI")
    public void setup() {

        driver = Driver.getDriver();
        commonPage = new CommonPage() {
        };
        actions = new Actions(driver);
    }

    /* between these 2 ui methods I will start with this as its easier - as you can see whenever we create a session to use by the use
    of @UI annotation we involuntarily terminate it at the end as they are triggered with the same annotation

    at the end of the scenario tearDown() method will be called and this method

    IF SCENARIO FAILED takes screenshot and attaches it as mentioned

    then closes the driver - I dont know why its commented out atm - not a big deal

    I remember learning about this Scenario object which basicaly carries information of the currently run scenario
    such as whether it has faield or not? its id etc.



     */

    @After(value = "@UI")
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshots");
        }
//        Driver.closeDriver();

    }

    /* Below 2 are 2 simple methods that are triggered with the same annotation "@DB" one at the start of the scenario and one at the
    end of the scenario. I checked the order that they will run to see if for example this before db method will run before order 0? and
    it did not - so I understand that if we mention no order it runs after the ordered ones end

    And what this method does is simply create a connection using the method from DatabaseUtilities class called createConnection()
    and notice that since this is a @before method this will take place just at the start of the scenario

     */

    @Before("@DB")
    public void setupDatabase() {
        //DatabaseUtilities.createConnection();

    }

    /*
    and as you might have guessed this method will terminate the connection that was created by the method above - notice that
    both of these are triggered with the same annotation, so involuntarily we will tent to end any connections that we have created
    weather we like it or not :) which is good
     */

    @After("@DB")
    public void closeDatabase() {
        // DatabaseUtilities.closeConnection();

    }

    /* I skipped the two @UI methods above as they will link us to understanding the Driver class - so lets check this one real quick
    and atm I dont know what it does.. hmm lets see -> so there is a @company annotation and whenever a scenario has this annotation on
    top of it this method will be triggered at the start of that scenario

    companyLogin method will be triggered and what does this method do? atm it just has a print statement in it
    which prints the value of the keys "companyUser_email & companyUser_password" -

    Looks like in the future this we will use this like part of our pre-conditions, that is what it reminds me
    if the scenario is done with the role of company then we will have this annotation and it will at the start of the scenario
    log us in as a company user getting the credentials from the configuraiton.properties -

    So I guess we wouldnt need a seperate step like "Given User logged in as a company" as this will be done here

   but I guess we will need to have initialised the driver already before we come to this method, hence the order of the creation of the session is 1
   so its before this method but another thing is I guess we will need to use get method inside this method and give credentials and access
   to webelements of email input box and password input box and such. just thinking aloud.
     */

    @Before("@company")
    public void companyLogin() {
        System.out.println(
                "email : " + ConfigurationReader.getProperty("companyUser_email") +
                        " password : " + ConfigurationReader.getProperty("companyUser_password")
        );
    }




}

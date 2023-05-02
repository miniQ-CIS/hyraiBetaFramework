package utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.HashMap;

import static stepDefinitions.Hooks.*;


public class Driver {

    /*
     Creating the private constructor so this class' object
     is not reachable from outside
      */
    private Driver() {
    }

    /*
    Making our 'driver' instance private so that it is not reachable from outside the class.
    We make it static, because we want it to run before everything else, and also we will use it in a static method
     */
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    /*
    Creating re-usable utility method that will return same 'driver' instance everytime we call it.
     */

    /* Okay we are taking this slow and I am not gonna type everything I am talking aloud  now, that will waste lots of time, but here we go

    -So i dont 100% know what we are doing with this hashMap and

    -2nd I dont know why we have these isFullScreen, isHeadless etc commented out here, we actually dont need them here right
    as they are been instantiated with the use of Hooks class so these are redundant here I suppose?

    Moving on

    Hmmm Apperantly we have something called ChromeOption which I also dont know what it is but sounds like
    it is about the config of the browser - and looks like that hashMap we have created is created to be used to
    for populating the ChromeOptions obj with some method called setExperimentalOption below, okay fair enough.

    Move before if(isHeadless) for more commentary.



     */
    public static WebDriver getDriver() {
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("download.default_directory", System.getProperty("user.dir") + "\\target");
        // isFullScreen = true;
        // isHeadless = true;
        // browserType = "chrome";
        //  browserType = "firefox";

//***********Burayi false yaparak browser i gorebiliriz*******************************************
        // isHeadless = false;

        //setting various capabilities for browsers
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        /* So now we are doing something to that ChromeOptions object with the use of addArguments method
        and we are doing it based on these previously set global variables from the Hooks class that were triggered through the
        use of annotations, so eg. if isHeadless annotation is used, the default value false will be turned true in the Hooks class
        and this variable below will be true and so triggered making some config changes to the chromeOption variable apperantly

        else it will not be headless and make some other config settings

        MOVE BEFORE FirefoxOptions for more commentary.

         */

        if (isHeadless) {
            chromeOptions.addArguments("use-fake-ui-for-media-stream");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--remote-allow-origins=*");
        } else {
            chromeOptions.addArguments("use-fake-ui-for-media-stream");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--remote-allow-origins=*");
        }

        /* feels like we are doing the similar stuff with an object from FirefoxOptions
        but altough not important probably why dont we have the exact same stuff as we did above for chrome

         */

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (isHeadless) {
            firefoxOptions.addArguments("--headless");
            firefoxOptions.addArguments("--disable-gpu");
            firefoxOptions.addArguments("--window-size=1920,1080");
//            firefoxOptions.addArguments("use-fake-ui-for-media-stream");

        } else {
            firefoxOptions.addArguments("--disable-gpu");
            firefoxOptions.addArguments("--window-size=1920,1080");
//            firefoxOptions.addArguments("use-fake-ui-for-media-stream");

        }

        /* Here we do again similar stuff to an object from EdgeOptions class

         ****** AND WE FINALLY MOVE TO OUR SINGLETON LOGIC *****

         MOVE THERE FOR MORE COMMENTARY

         */


        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("use-fake-ui-for-media-stream");


        /* Below I if null we start trigger the switch statement as we know how singleton works

        but i dont know what synchronized does like that small detail feels like

        the commented part below sounds wrong, as we are NOT reading it from
        configuration.properties file rather we are using Hooks class, isn't that so?

        MOVE AT THE SWITCH STATEMENT FOR MORE COMMENTARY

         */

        if (driverPool.get() == null) {
            synchronized (Driver.class) {

            /*
            We read our browser type from configuration.properties file using
            .getProperty method we create in ConfigurationReader class.
             */
                // String browserType = ConfigurationReader.getProperty("browser");

            /*
            Depending on the browser type our switch statement will determine
            to open specific type of browser/driver
             */
                switch (browserType) {

                    /* SO above variable browserType is instantiated in Hooks class as chrome and if not changed by
                    annotation such as @firefox it will stay as chrome

                    now below is just simple switch statement instantiaing WebDriver ChromeDriver inside ThreadLocal driverpool
                    or WebDriver FirefoxDriver etc.

                    currently we assume I guess we dont have ie, edge and safari in our computer so we create
                    exception if they are tried to be triggered we can simply change it here I guess if that is not the case

                    OMG i just read it with my eyes again and it actually checkes what OS you have and based on that inferres
                    whether you have ie,edge or safari that is smart for my inexperrienced mind

                    so It will create exception if your os is not windows and you try to open ie or edge as they
                    can only be found in windows os

                    same thing for the safari if your os is not mac

                    if those if conditions are untrue so if you actually have the browser type the expection will not be thrown
                    and your execution will not be halted and you will create the browser in desired type

                    JUMP TO if(isFullScreen) FOR MORE COMMENTARY
                     */

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver(chromeOptions));
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver(firefoxOptions));
                        break;

                    case "ie":
                        if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                            throw new WebDriverException("Your OS doesn't support Internet Explorer");
                        WebDriverManager.iedriver().setup();
                        driverPool.set(new InternetExplorerDriver());
                        break;

                    case "edge":
                        if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                            throw new WebDriverException("Your OS doesn't support Edge");
                        WebDriverManager.edgedriver().setup();
                        driverPool.set(new EdgeDriver());
                        break;

                    case "safari":
                        if (!System.getProperty("os.name").toLowerCase().contains("mac"))
                            throw new WebDriverException("Your OS doesn't support Safari");
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driverPool.set(new SafariDriver());
                        break;
                }

                /* So here is simple I guess but I believe this if (isFullScreen) for example has to come after all of the
                above stuff as we can make the actual settings of making a browser fullscreen if there is a browser object at hand
                so when we reach this point we will have a browser at hand

                and if isFullScreen is true (again set in Hooks class with the use of annotations with default value true)
                we will maximeze the browser as you can see if not
                then we will automatically have Dimension object variable again set within the Hooks class when triggered by
                @iPhone12 annotation making isFullScreen false and adding dimensions on the iPhone12


                at the end of it all we add the implicit wait for  our WebDriver, whatever browser type it is.

                 */
                if (isFullScreen) {

                    driverPool.get().manage().window().maximize();
                } else {
                    Dimension dimension = new Dimension(width, height);
                    driverPool.get().manage().window().setSize(dimension);
                }
                driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            }
        }

        /* ****** AND WITH THE ENDING TOUCH TO OUR METHOD WebDriver.getDriver() we return whatever WebDriver
        object we have just instantiated.

        ************ IN THE NEXT USE OF getDriver method our driver our driverPool.get() method will not return null
        as we have just created and will return the already created object

         */
        /*
        Same driver instance will be returned every time we call Driver.getDriver(); method
         */
        return driverPool.get();
    }

    /*
    This method makes sure we have some form of driver session or driver id has.
    Either null or not null it must exist.
     */
    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}

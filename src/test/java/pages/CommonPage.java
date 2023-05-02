package pages;

import org.openqa.selenium.support.PageFactory;

import static stepDefinitions.Hooks.driver;

public abstract class CommonPage {

    String name;
    public CommonPage(){
        /* SO WILL WE BE RIGHT TO SAY THAT IN THE OLD FRAMEWORK TYPE WE WILL BE USING THE getDriver() of the Driver class everytime we need
        the driver, but in this framework getDriver() of the Driver class will be used automatically once when triggeered in Hooks class
        so it will be used only once per scenario?

        at the end of scenario it will be terminated by driver close and will be created again triggered again or not for the next scenario
        that we are testing

         */

        PageFactory.initElements(driver, this);
    }


    /*
    so currently I dont know what is the homePage variable doing there and I also dont know what getHomePage() is doing

    initial thoughts - firstly I guess if we are creating a homePage field and then later on instantiating it with getHomePage()
    method we ought to do it for every other page classes that we will create in the future - both of these stuff (one private variable
    and one method to instantiate that private variable)

    But I am a bit confused now, we instantiate the homePage here with the use of getHomePage method when we want but we also
    in our stepDefinition classes instantiate it normally by saying "HomePage hp = new HomePage();" so what is going on? I didnt understand
    the full logic of it yet


     */

    /* ******** So final thoughts, I realised that I have forgotten some basic but complex and easy to grasp applications
    of Java (eg various rules and applications and functionalities of contsturctors, maybe abstracts and maybe access variables and
    statics and instance variables, where what can be instantiated and why can it not etc) but thats all right

    Moving on from that - here what happens is that - stuff get triggered by the @UI annotation from the feature file
    This runs the method of setup() which does couple of things but what we are focusing here is that it calls for the constructor
    of the abstract class CommonPage, !!! HERE WE ARE NOT CREATING AN OBJECT OF THE CommonPage CLASS AS IT IS IMPOSSIBLE SINCE
    THAT CLASS IS ABSTRACT, BUT WE ARE JUST CALLING ITS CONSTRUCTOR- (this is where I get a bit dizzy with constructors, inheritence and
    stuff like that but that is alright lets move on with what we have established) - SO WE ARE CALLING THE CONSTRUCTOR TO ACTUALLY
    NOT CREATE AN OBJECT AS I HAVE MENTIONED BUT TO DO SOMETHING AMAZING WHICH IS USING THE CONSTRUCTOR OF THE CommonPage CLASS
    TO *** INITIALIZE THE STATE OF OBJECTS THAT ARE CREATED BY THEIR SUBCLASSES) LIKE HomePage FOR EXAMPLE.

    AND WHY DO WE DO THAT YOU MIGHT ASK - I BELIEVE IT IS BECAUSE -----> WE HAVE A DEFAULT CONSTRUCTOR WITH "PageFactory.initElements(driver, this);" METHOD
    IN IT AND WHEN WE EXTEND DIFFERENT PAGES TO THIS COMMONPAGE CLASS (WE CREATED THIS CLASS FOR OTHER CLASSES TO EXTEND TO IT)
    THIS CONSTRUCTOR WITH THE METHOD IN IT WILL BE INHERITED BY THE SUBCLASSES AND IN INVISIBILITY IT WILL BE THERE AND DOING THE SAME THING
    FOR ALL OF THE SUBCLASSES AUTOMATICALLY.

    ------------ AND SECOND MATTER --- WHICH LOOKS ALSO COMPLICATED AND UNUSUAL FROM OUR USE TO FRAMEWORK IS THAT -------
    BUT I THINK I GOT IT -- HERE GOES NOTHING

    SO IN OUR BOOTCAMP FRAMEWORK WE DONT HAVE THE BELOW 2 STUFF -

    I MEAN
    -private HomePage homePage;
    -public HomePage getHomePage() method

    BUT WE HAVE THEM HERE AND WHY DO WE HAVE THEM HERE?

    SOOO....

    IN OUR BOOTCAMP FRAMEWORK, IF YOU CHECK, IN OUR STEPDEFINITION CLASSES WE ALWAYS CREATED THE OBJECT OF THE PAGE WE ARE TESTING
    AT THE TOP BEFORE WE START TO ADD ACTUAL CODES THAT DO THE TESTS. WE CREATED THE OBJECT JUST THAT IS HOW WE ACCESSED ELEMENTS AND
    MAYBE METHOD IF WE HAVE IN IT.

    BUT HERE WE DO THAT CREATION HERE IN THIS CLASS WITH THE USE OF 2 THINGS BELOW

    AND YOU CAN SEE THE AFFECT IT CREATES IN OUR STEPDEFINITION CLASSES, THAT WE DONT NEED TO CREATE ANY OBJECT OF THE CLASS THAT WE
    ARE TESTING

    INSTEAD WE SIMPLE NEED TO IMPORT THE GLOBAL VARIABLE commonPage THAT HAS BEEN INSTANTIATED IN HOOKS CLASS
    AND THEN IF WE WANT TO ACCESS ANY OTHER SUB PAGE WE ACCESS IT THROUGH THAT commonPage VARIABLE AND USE THE METHOD BELOW
    getHomePage..

    SO I THINK WE WILL NEED TO COPY BOTH OF THE 2 THINGS FOR EVERY PAGE WE HAVE UNDER OUR PAGES PACKAGE.



     */
    private HomePage homePage;

    public HomePage getHomePage(){
        if(homePage==null){
            homePage = new HomePage();
        }
        return homePage;
    }
}

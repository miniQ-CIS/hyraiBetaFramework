package enums;

public enum LINKS {


    /* Here we are in an Enum and we use this enum for the same purpose that we use configuration.properties for.

    this way is better in a way looks like as we dont need to go and get the key and paste it in our stepDefinition classes

    rather we create enum and give types below such as BASEURL

    and we THEN we use this enums constructor to force the assigning of the variable when we create it.

    SO APPERATNYL WE CANT HAVE ANOTHER TYPE BELOW SUCH AS BASEURL WITHOUT using the constructor again to give
    what it contains.

    ****** I WILL NEED TO COMEBACK HERE TO BETTER UNDERSTAND ****** BUT THAT IS HOW IT WORKS




     */

    BASEURL("https://hyrai.com/")
    ;
    private String getLink;

    LINKS(String getLink) {
        this.getLink = getLink;
    }

    public String getLink() {
        return getLink;
    }
}

package src.com.manastaneja.hackernews;

public class StoryFailureException extends Exception {
    private String mainString;

    public StoryFailureException(String str1){
        mainString = str1;
    }

    public String customPrintStackTrace(){
        return ("Exception occurred: " + mainString);
    }
}

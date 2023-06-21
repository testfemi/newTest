package utility;

import helper.Log;
import helper.ScenarioContext;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;

public class ScreenshotHandler {

    public static File captureScreenshot(){
        try{
            return ((TakesScreenshot) ScenarioContext.driver).getScreenshotAs(OutputType.FILE);
        } catch (NullPointerException driverIsNull){
            if(ScenarioContext.driver == null){
                Log.warn("Cannot capture screenshot, WebDriver has not been created");
            }else {
                Log.error("Failed to capture screenshot");
            }
        }
        return null;
    }

}

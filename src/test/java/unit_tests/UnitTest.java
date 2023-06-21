package unit_tests;

import helper.Log;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class UnitTest {

    @BeforeMethod
    public void doBeforeEachTestMethod(Method method){
        Log.info("Running unit test: %s.%s",
                this.getClass().getSimpleName(),
                method.getName());
    }

}
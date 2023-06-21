package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/* Specify what Cucumber parameters to use here when executing scenarios */
@CucumberOptions (
        monochrome = true,
        features = {"src\\test\\resources\\features"},
        plugin = {"pretty"
                //, "json:target/json/file.json"
        },
        glue = {"stepdefinitions"},
        tags = "@SauceDemoTest1"
)

/*
  This class solely exists for the purpose of integrating the
  CucumberJVM with TestNG (by inheriting {@link AbstractTestNGCucumberTests}),
  which ultimately then allows us to automate test execution
  with this Automation Framework (i.e., Maven Project) by
  using Selenium.
 */
public class TestRunner extends AbstractTestNGCucumberTests {}
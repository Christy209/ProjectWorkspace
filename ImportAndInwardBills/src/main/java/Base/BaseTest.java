package Base;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.util.Supplier;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Listeners;
import Utilities.WindowHandle;
import org.openqa.selenium.remote.RemoteWebDriver;

@SuppressWarnings("unused")
@Listeners(Utilities.TestReportListener.class)
public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ThreadLocal<WebDriver> driver1 = new ThreadLocal<WebDriver>();
    protected ThreadLocal<WebDriverWait> wait1 = new ThreadLocal<WebDriverWait>();
    public org.apache.logging.log4j.Logger logger;

  @BeforeClass
  public WebDriver getDriver() {
      return driver;
  }
  
  public WebDriverWait getWait() {
      return wait;
  }
    public void selectionsolution() {
    	
        try {
        
        	logger =LogManager.getLogger(this.getClass());
        	getDriver().switchTo().frame(0);
            WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appSelect")));
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByValue("CoreServer");
            WindowHandle.handleAlertIfPresent(driver);

        } catch (Exception e) {
            System.out.println("No iframe or error during switching: " + e.getMessage());    
            }
   }
    
    
}


package CRM;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject{

   	public static void waitAndFill(WebDriverWait wait, String locatorType, String locatorValue, String input) {
	    By by;

	    switch (locatorType.toLowerCase()) {
	        case "id":
	            by = By.id(locatorValue);
	            break;
	        case "name":
	            by = By.name(locatorValue);
	            break;
	        case "xpath":
	            by = By.xpath(locatorValue);
	            break;
	        case "css":
	            by = By.cssSelector(locatorValue);
	            break;
	        default:
	            throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
	    }

	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	        element.clear();
	        element.sendKeys(input);
	}
   	
   	public static void Frame(WebDriverWait wait) {
		try { 
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("topStyledFrame")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));

    	
		} catch (Exception ed) {
			ed.printStackTrace();
		}

	}
   	public static void iframe(WebDriverWait wait) {
		try { 
	      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
	      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("dynamicTabFrm")));

    	
		} catch (Exception ed) {
			ed.printStackTrace();
		}
   	}
		public static void Frames(WebDriverWait wait) {
			try { 
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//html//frameset//frame")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FilterForm")));

    	
		} catch (Exception ed) {
			ed.printStackTrace();
		}
}
		
		public static void Tbs(WebDriverWait wait) {
			try { 
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));

    	
		} catch (Exception ed) {
			ed.printStackTrace();
		}
		}
			public static void button(WebDriverWait wait) {
				try { 
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("buttonFrm")));

			} catch (Exception ed) {
				ed.printStackTrace();
			}
}
			
			  public static String switchToThirdWindow(WebDriver driver, String parentWindowHandle, String secondWindowHandle) {
				  String Windowhandle = driver.getWindowHandle();
				  Set<String> handlesAfterLastName = driver.getWindowHandles();
                  String thirdWindowHandle = handlesAfterLastName.stream()
                      .filter(handle -> !handle.equals(Windowhandle) && !handle.equals(secondWindowHandle))
                      .findFirst()
                      .orElseThrow(() -> new RuntimeException("Third window not found"));

                  driver.switchTo().window(thirdWindowHandle);
                  driver.switchTo().defaultContent();
				return thirdWindowHandle;
			    }
			  
				
			  public static String switchToSecondWindow(WebDriver driver, String parentWindowHandle, String secondWindowHandle) {
				  String Windowhandle = driver.getWindowHandle();
                  Set<String> handlesAfterAddRel = driver.getWindowHandles();
                  String secondWindowHandle1 = handlesAfterAddRel.stream()
                      .filter(handle -> !handle.equals(Windowhandle))
                      .findFirst()
                      .orElseThrow(() -> new RuntimeException("Second window not found"));

                  driver.switchTo().window(secondWindowHandle1);
                  driver.switchTo().defaultContent();
				return secondWindowHandle1;
			    }
}

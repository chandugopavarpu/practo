package pages;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import practo.Base;

public class HomePage extends Base {
    By city = By.xpath("//*[@id='c-omni-container']/div/div[1]/div/input");
    By speciality = By.xpath("//*[@id='c-omni-container']/div/div[2]/div/input");
    By clear = By.xpath("//*[@id='c-omni-container']/div/div[1]/div[1]/span[2]/span/i");
  
    public void enter_city() throws InterruptedException {
        driver.findElement(city).click();
        driver.findElement(clear).click(); 
        Thread.sleep(1000);
        driver.findElement(city).sendKeys(properties.getProperty("city"));
        Thread.sleep(3000);
        driver.findElement(city).sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(1000);
        driver.findElement(city).sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(1000);
        driver.findElement(city).sendKeys(Keys.ENTER); 
    }

    public void speciality() throws InterruptedException {
        driver.findElement(speciality).sendKeys(properties.getProperty("speciality"));
        Thread.sleep(3000);
        driver.findElement(speciality).sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(1000);
        driver.findElement(speciality).sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(1000);
        driver.findElement(speciality).sendKeys(Keys.ENTER); 
    }

    public Boolean validate_speciality_of_all_doctors() {
        List<WebElement> doctorList = driver.findElements(By.xpath("//*[@id='container']/div/div[4]/div/div[1]/div/div"));

        int matchingSpecialitiesCount = 0;

        for (WebElement doctor : doctorList) {
            WebElement specialityLabel = doctor.findElement(By.xpath("//span[contains(text(), 'Dentist')]"));
            if (specialityLabel.getText().contains("Dentist")) {
                matchingSpecialitiesCount++;
            }
        }
        test = report.createTest("Test 1 - Validate Speciality of All Doctors");
        if (matchingSpecialitiesCount == doctorList.size()) {
            test.log(Status.PASS, "All doctors are of the same speciality: Dentist");
        } else {
            test.log(Status.PASS, "Not all doctors are of the same speciality");
        }
		return matchingSpecialitiesCount == doctorList.size();
    }
    
  
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}

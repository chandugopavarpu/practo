package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import practo.Base;

public class FiltersPage extends Base {
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    String actual_fee;
    String verified_fee; 

    public void clickfilters() {
        driver.findElement(By.xpath("//*[@id='container']/div/div[3]/div/div/header/div[1]/div/div[4]/i")).click();
        List<WebElement> fees = driver.findElements(By.xpath("//*[@id='container']/div/div[3]/div/div/header/div[2]/div/div[2]/div/label"));
        actual_fee = fees.get(1).getText();
        fees.get(1).click();
    }

    public Boolean verify_all_the_consultation_fee() {
        List<WebElement> verified_fee_elements = driver.findElements(By.xpath("//span[contains(@data-qa-id, 'consultation_fee')]"));
        boolean allFeesValid = true;
        for (WebElement feeElement : verified_fee_elements) {
            String feeText = feeElement.getText().replaceAll("[^0-9]", ""); 
            int feeValue;
            try {
                feeValue = Integer.parseInt(feeText);
            } catch (NumberFormatException e) {
                allFeesValid = false;
                continue; 
            }

            if (feeValue < 500 || feeValue > 1000) {
                allFeesValid = false;
            }
        }

        test = report.createTest("Test 3 - Verify Consultation Fee");
        if (allFeesValid) {
            test.log(Status.PASS, "All doctors displayed have a consultation fee between 500 and 1000.");
        } else {
            test.log(Status.FAIL, "Some doctors have a consultation fee outside the range of 500 to 1000.");
        }
		return allFeesValid;

    }
   
}

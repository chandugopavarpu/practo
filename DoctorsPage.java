package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import practo.Base;


public class DoctorsPage extends Base {
    
    private String name1;
    private String name2;
    private String date2;
    private String ts2;
    private String selectedDate; 

   
    public void book_clinic_visit() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); 
        List<WebElement> clinicVisitButtons = getClinicVisitButtons();
        if (clinicVisitButtons.size() > 2) {
            clickClinicVisitButton(wait, clinicVisitButtons.get(2));
        } else {
            System.out.println("The expected number of buttons was not found.");
            return; 
        }
        captureDoctorName();
        selectedDate = selectAvailableSlot(); // Store selected date
        clickTimeSlot();
    }

    private List<WebElement> getClinicVisitButtons() {
        return driver.findElements(By.xpath("//button[contains(text(), 'Book Clinic Visit')]"));
    }

    private void clickClinicVisitButton(WebDriverWait wait, WebElement button) {
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    private void captureDoctorName() {
        List<WebElement> name = driver.findElements(By.xpath("//button[contains(text(), 'Book Clinic Visit')]/preceding::h2"));
        name1 = name.get(2).getText();
    }

    private String selectAvailableSlot() throws InterruptedException {
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='u-pos-rel c-slots-header__daybar ']/div"));
        String selectedDate = ""; // Store the selected date

        for (int i = 0; i < list.size(); i++) {
            String text = driver.findElement(By.xpath("//div[@class='u-pos-rel c-slots-header__daybar ']/div[" + (i + 1) + "]/div[2]")).getText();
            String text1 = "No Slots Available";
            if (!text.equals(text1)) {
                selectedDate = driver.findElement(By.xpath("//div[@class='u-pos-rel c-slots-header__daybar ']/div[" + (i + 1) + "]/div[1]")).getText(); // Capture the date
                driver.findElement(By.xpath("//div[@class='u-pos-rel c-slots-header__daybar ']/div[" + (i + 1) + "]/div[1]")).click();
                break;
            }
        }
        return selectedDate; 
    }

    private void clickTimeSlot() {
        ts2 = driver.findElement(By.xpath("//span[contains(.,'PM')]")).getText();
        driver.findElement(By.xpath("//span[contains(., 'PM')]")).click();
        System.out.println("Slot clicked");
    }

    public void getname() {
        System.out.println(name1);
        name2 = driver.findElement(By.xpath("//div[contains(@data-qa-id, 'doctor_name')]")).getText();
        System.out.println(name2);
        validateName();
    }

    private void validateName() {
        test = report.createTest("Test 2 - validateName");
        if (name1.equalsIgnoreCase(name2)) {
            test.log(Status.PASS, "Name is same");
            System.out.println("Name is same");
        } else {
            test.log(Status.FAIL, "Not same");
            System.out.println("Not same");
        }
    }

    public void gettime() {
        String ts1 = driver.findElement(By.xpath("//span[contains(., 'PM')]")).getText();
        System.out.println(ts1);
        validateTimeSlot(ts1);
    }

    private void validateTimeSlot(String ts1) {
        // Normalize the time strings
        String normalizedTs1 = normalizeTime(ts1);
        String normalizedTs2 = normalizeTime(ts2);

        test = report.createTest("Test 2 - validateTimeSlot");
        if (normalizedTs1.equals(normalizedTs2)) {
            test.log(Status.PASS, "The time slots are the same");
            System.out.println("The time slots are the same: " + normalizedTs1);
        } else {
            test.log(Status.FAIL, "The time slots are different");

            System.out.println("The time slots are different: " + normalizedTs1 + " vs " + normalizedTs2);
        }
    }
    private String normalizeTime(String time) {
        // Remove leading zero if present
        if (time.startsWith("0")) {
            time = time.substring(1);
        }
        return time;
    }

    public void getdate() {
        String date1 = driver.findElement(By.xpath("//span[contains(., '2024')]")).getText();
        System.out.println("Booked Date: " + date1);
        validateDate(date1); // Validate the booked date
    }


    private void validateDate(String bookedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // Get today's and tomorrow's dates
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate selectedDate;

        // Determine the selected date based on input
        if (this.selectedDate.equalsIgnoreCase("Today")) {
            selectedDate = today; // Use today's date
        } else if (this.selectedDate.equalsIgnoreCase("Tomorrow")) {
            selectedDate = tomorrow; // Use tomorrow's date
        } else {
            try {
                selectedDate = LocalDate.parse(this.selectedDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format: " + bookedDate);
                return;
            }
        }

        // Compare the selected date with the booked date
        LocalDate bookedLocalDate;
        try {
            bookedLocalDate = LocalDate.parse(bookedDate, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid booked date format: " + bookedDate);
            return;
        }

        if (selectedDate.equals(bookedLocalDate)) {
            System.out.println("The selected date and booked date are the same: " + selectedDate);
        } else {
            System.out.println("The selected date and booked date are different: " + selectedDate + " vs " + bookedLocalDate);
        }
    }


    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}

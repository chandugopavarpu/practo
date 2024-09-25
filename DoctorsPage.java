package pages;

import java.util.List;
import java.util.Random;

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
    
    private String selected_name;
    private String selected_doctor;
    private String date;
    private String ts2;
    private String SelectedDate;
    LocalDate selectedDate;

    public static int generateRandomIntegerInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min; 
    }
    int random_int = generateRandomIntegerInRange(0, 10);

    public void book_clinic_visit() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); 
        List<WebElement> clinicVisitButtons = getClinicVisitButtons();
        if (clinicVisitButtons.size() > random_int) {
            clickClinicVisitButton(wait, clinicVisitButtons.get(random_int));
        } else {
            System.out.println("The expected number of buttons was not found.");
            return; 
        }
        captureDoctorName();
        SelectedDate = selectAvailableSlot();
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
        selected_name = name.get(random_int).getText();
    }

    private String selectAvailableSlot() throws InterruptedException {
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='u-pos-rel c-slots-header__daybar ']/div"));
        String selectedDate = ""; 

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
    }

    public Boolean getname() {
        selected_doctor = driver.findElement(By.xpath("//div[contains(@data-qa-id, 'doctor_name')]")).getText();
        System.out.println("Selected doctor name: " + selected_doctor);
        return validateName();
    }

    public Boolean gettime() {
        String ts1 = driver.findElement(By.xpath("//span[contains(., 'PM')]")).getText();
        System.out.println(ts1);
        return validateTimeSlot(ts1);
    }

    public Boolean getdate() {
        String date1 = driver.findElement(By.xpath("//span[contains(., '2024')]")).getText();
        System.out.println("Booked Date: " + date1);
        return validateDate(date1); 
    }

    private Boolean validateName() {
        Boolean validateName = false;
        test = report.createTest("Test 2 - validateName");
        if (selected_name.equalsIgnoreCase(selected_doctor)) {
            validateName = true;
            test.log(Status.PASS, "Name is same");
        } else {
            validateName = false;
            test.log(Status.FAIL, "Name is Not same");
        }
        return validateName;
    }

    private String normalizeTime(String time) {
        if (time.startsWith("0")) {
            time = time.substring(1);
        }
        return time;
    }

    private Boolean validateTimeSlot(String ts1) {
        String normalizedTs1 = normalizeTime(ts1);
        String normalizedTs2 = normalizeTime(ts2);
        Boolean validateTimeSlot = false;
        test = report.createTest("Test 2 - validateTimeSlot");
        if (normalizedTs1.equals(normalizedTs2)) {
            validateTimeSlot = true;
            test.log(Status.PASS, "The time slots are the same");
            System.out.println(normalizedTs1);
        } else {
            validateTimeSlot = false;
            test.log(Status.FAIL, "The time slots are different");
            System.out.println(normalizedTs1 + " vs " + normalizedTs2);
        }
        return validateTimeSlot;
    }

    private Boolean validateDate(String bookedDate) {
        Boolean validateDate = false;
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (SelectedDate.contains("Today")) {
            selectedDate = today;
        } else if (SelectedDate.contains("Tomorrow")) {
            selectedDate = tomorrow;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                selectedDate = LocalDate.parse(SelectedDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format: " + SelectedDate);
                return false;
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedSelectedDate = selectedDate.format(formatter);

        String trimmedSelectedDate = trimMonth(formattedSelectedDate);

        String trimmedBookedDate = bookedDate.trim();

        System.out.println("Selected Date: " + trimmedSelectedDate);
        System.out.println("Booked Date: " + trimmedBookedDate);

        test = report.createTest("Test 2 - validateDate");
        if (trimmedSelectedDate.equals(trimmedBookedDate)) {
            validateDate = true;
            test.log(Status.PASS, "The selected date and booked date are the same");
            System.out.println("Dates match: " + trimmedBookedDate);
        } else {
            validateDate = false;
            test.log(Status.FAIL, "The selected date and booked date are different");
            System.out.println("Dates do not match: " + trimmedBookedDate + " vs " + trimmedSelectedDate);
        }
        return validateDate;
    }

    public static String trimMonth(String date) {
        return date.substring(0, 3) + date.substring(4); 
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}

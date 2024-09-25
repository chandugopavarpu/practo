package practotestfile;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DoctorsPage;
import pages.FiltersPage;
import pages.HomePage;
import practo.Base;

public class PractoTest extends Base {
    HomePage homePage = new HomePage();
    DoctorsPage doctorsPage = new DoctorsPage();
    FiltersPage filtersPage = new FiltersPage();

    @BeforeTest
    @Parameters({"browser"})
    public void setUpTest(String browser) {
        setUp(browser);
        homePage.setDriver(driver); 
        doctorsPage.setDriver(driver); 
        filtersPage.setDriver(driver); 
    }

    @Test(priority = 1)
    public void test1() throws InterruptedException {
        Base.report();
        openUrl();
        homePage.enter_city();
        homePage.speciality();
        if (homePage.validate_speciality_of_all_doctors()) {
            System.out.println("All doctors are of the same speciality: Dentist");
        } else {
            System.out.println("Not all doctors are of the same speciality");
        }
    }

    @Test(priority = 2)
    public void test2() throws InterruptedException {
        openUrl();
        homePage.enter_city();
        homePage.speciality();
        doctorsPage.book_clinic_visit();
        if (doctorsPage.getname()) {
            System.out.println("Doctor's Name is same");
        } else {
            System.out.println("Doctor's Name is Not same");
        }
        if (doctorsPage.gettime()) {
            System.out.println("The time slots are the same");
        } else {
            System.out.println("The time slots are different");
        }
        if (doctorsPage.getdate()) {
            System.out.println("The selected date and booked date are the same");
        } else {
            System.out.println("The selected date and booked date are different");
        }
    }

    @Test(priority = 3)
    public void test3() throws InterruptedException {
        openUrl();
        homePage.enter_city();
        homePage.speciality();
        filtersPage.clickfilters();
        if (filtersPage.verify_all_the_consultation_fee()) {
            System.out.println("All doctors displayed have a consultation fee between 500 and 1000.");
        } else {
            System.out.println("Some doctors have a consultation fee outside the range of 500 to 1000.");
        }
        Base.flush();
    }

    @AfterTest
    public void tearDown() {
        closeUrl(); 
    }
}

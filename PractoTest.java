package practotestfile;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.DoctorsPage;
import pages.FiltersPage;
import pages.HomePage;
import practo.Base;

public class PractoTest extends Base {
    HomePage hp = new HomePage();
    DoctorsPage dp = new DoctorsPage();
    FiltersPage fp = new FiltersPage();

   

    @BeforeTest
    public void setUpTest() {
        setUp();
        hp.setDriver(driver); 
        dp.setDriver(driver); 
        fp.setDriver(driver); 
    }

    @Test(priority = 1)
    public void test1() throws InterruptedException {
    	Base.report();
        openUrl();
        hp.enter_city();
        hp.speciality();
        if (hp.validate_speciality_of_all_doctors()) {
            System.out.println("All doctors are of the same speciality: Dentist");
        } else {
            System.out.println("Not all doctors are of the same speciality");
        }
       
    }

    @Test(priority = 2)
    public void test2() throws InterruptedException {
        openUrl();
        hp.enter_city();
        hp.speciality();
        if(hp.validate_speciality_of_all_doctors()) {
            System.out.println("All doctors are of the same speciality: Dentist");
        } else {
            System.out.println("Not all doctors are of the same speciality");
        }
        dp.book_clinic_visit();
        dp.getname();
        dp.gettime();
        dp.getdate();
        
    }

   @Test(priority = 3)
    public void test3() throws InterruptedException {
        openUrl();
        hp.enter_city();
        hp.speciality();
        fp.clickfilters();
        if (fp.verify_all_the_consultation_fee()) {
            System.out.println("All doctors displayed have a consultation fee between 500 and 1000.");
        } else {
            System.out.println("Some doctors have a consultation fee outside the range of 500 to 1000.");
        }
        Base.flush();
        
    }

    @AfterTest
    public void tearDown() {
        
        closeUrl(); // Close the browser after tests are done
    }
}

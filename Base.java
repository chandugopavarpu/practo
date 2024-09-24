package practo;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Base {
    protected WebDriver driver;
    private Properties properties;
    public static ExtentSparkReporter htmlreport;
    public static ExtentReports report;
    public ExtentTest test;
    SoftAssert sa=new SoftAssert();
    
    public static void report() {
        htmlreport = new ExtentSparkReporter("C:\\Users\\ChandanaG\\Pictures\\practo1.html");
        htmlreport.config().setReportName("Report on Practo");
        htmlreport.config().setDocumentTitle("Practo Automation Tests");
        htmlreport.config().setTheme(Theme.DARK);

        report = new ExtentReports();
        report.setSystemInfo("Environment", "TestEnv");
        report.setSystemInfo("TesterName", "Chandana");
        report.attachReporter(htmlreport);
    }
    
    
    public Base() {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("C:\\Users\\ChandanaG\\eclipse-workspace\\Myproject\\src\\main\\java\\practoconfig\\practo.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Setup method for initializing the WebDriver based on the browser property
    public void setUp() {
        String browser = getProperty("browser");  // Use the current instance's getProperty method
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getTimeout()));
       
    }
    

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getTimeout() {
        return Integer.parseInt(properties.getProperty("timeout"));
    }

    public void openUrl() {
        driver.get(getProperty("url"));
    }

    
    public static void flush()
    {
    	report.flush();
    }
    public void closeUrl() {
        if (driver != null) {
            driver.close();
        }
    }
}

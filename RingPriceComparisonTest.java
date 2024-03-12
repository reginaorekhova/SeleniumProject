package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;



    public class RingPriceComparisonTest {

        private WebDriver driver;

        //Chrome Driver connection
        public <IEDriverServer_Win32_2, C, chromedriver> void setUp() {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
            driver = new ChromeDriver();
        }

        private double getNumeric(String s)
        {
            Pattern pattern = Pattern.compile("\\d+");

            // Create a matcher to find matches
            Matcher matcher = pattern.matcher(s);

            // Create StringBuilder to store the found numbers
            StringBuilder numbers = new StringBuilder();

            // While there are matches
            while (matcher.find()) {
                numbers.append(matcher.group());
            }
            String result = numbers.toString();
            return Double.parseDouble(result);
        }

        @Test
        public void testRingPricesComparison() {
            // Open Cartier page
            driver.get("https://www.cartier.com/en-us/jewelry/rings/");
            // Find all items with ring prices
            List<WebElement> cartierPriceElements = driver.findElements(By.cssSelector("div.price"));

            // Calculate the sum of all prices for rings
            double cartierTotalPrice = 0;
            for (WebElement element : cartierPriceElements) {
                WebElement priceElement = element.findElement(By.cssSelector("span.value"));
                double price = Double.parseDouble(priceElement.getAttribute("content"));
                cartierTotalPrice += price;
            }
            // Calculate the average price for rings on the Cartier page
            double cartierAveragePrice = cartierTotalPrice / cartierPriceElements.size();

            // Open Tiffany page
            driver.get("https://www.tiffany.com/jewelry/shop/rings/sort-relevance/?page=view-all");
            // Find all items with ring prices
            List<WebElement> tiffanyPriceElements = driver.findElements(By.cssSelector("p.product-tile__details_price"));
            double tiffanyTotalPrice = 0;
            for (WebElement element : tiffanyPriceElements) {
                String priceText = element.getAttribute("textContent");
                double price = getNumeric(priceText);
                tiffanyTotalPrice += price;
            }
            // Calculate the average price for rings on the Tiffany page
            double tiffanyAveragePrice = tiffanyTotalPrice / tiffanyPriceElements.size();

            System.out.println(cartierAveragePrice);
            System.out.println(tiffanyAveragePrice);

            // Compare average prices
            if (cartierAveragePrice > tiffanyAveragePrice) {
                System.out.println("Average price of rings on Cartier is higher.");
            } else if (tiffanyAveragePrice > cartierAveragePrice) {
                System.out.println("Average price of rings on Tiffany is higher.");
            } else {
                System.out.println("Average prices of rings on both websites are equal.");
            }
        }

        @AfterClass
        public void tearDown() {
            driver.quit();
        }
    }


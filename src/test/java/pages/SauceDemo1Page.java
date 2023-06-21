package pages;

import helper.ScenarioContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import utility.ElementUtil;

import java.util.*;

public class SauceDemo1Page {
    @FindBy(className = "product_sort_container")
    private WebElement searchFilter;

    @FindBy(xpath = "//button[contains(text(),'Add to cart')]")
    private WebElement addToCartButton;


     public void selectFilter(String dropdownOption) {
        Select objSelect =new Select(searchFilter);
        objSelect.selectByVisibleText(dropdownOption);
    }

    public void selectProductHighestPrice() {
        List<WebElement> list_of_products = ScenarioContext.driver.findElements(By.xpath("//div[@class='inventory_item_name']"));
        List<WebElement> list_of_products_price = ScenarioContext.driver.findElements(By.xpath("//div[@class='inventory_item_price']"));
        String product_name;
        String product_price;
        Float int_product_price;
        HashMap<Float, String> map_final_products = new HashMap<Float,String>();
        for(int i=0;i<list_of_products.size();i++) {
            product_name = list_of_products.get(i).getText();
            product_price = list_of_products_price.get(i).getText();
            int_product_price= Float.parseFloat(product_price.replaceAll("[$,]", ""));
            map_final_products.put(int_product_price,product_name);
        }


        Set<Float> allkeys = map_final_products.keySet();
        ArrayList<Float> array_list_values_product_prices = new ArrayList<Float>(allkeys);
        Collections.sort(array_list_values_product_prices);
        //Highest Product is
        Float high_price = array_list_values_product_prices.get(array_list_values_product_prices.size()-1);
        System.out.println("High Product Price is: " + high_price + " Product name is: " + map_final_products.get(high_price));

        //click on add to cart button next to High product price
        WebElement addToCart= ScenarioContext.driver.findElement(By.xpath("//div[contains(text(),'"+map_final_products.get(high_price)+"')]//ancestor::div[@class='inventory_item_label']//following-sibling::div//button[contains(text(),'Add to cart')]"));
        ElementUtil.click(addToCart);


    }

    public void selectProductLowestPrice() {
        List<WebElement> list_of_products = ScenarioContext.driver.findElements(By.xpath("//div[@class='inventory_item_name']"));
        List<WebElement> list_of_products_price = ScenarioContext.driver.findElements(By.xpath("//div[@class='inventory_item_price']"));
        String product_name;
        String product_price;
        Float int_product_price;
        HashMap<Float, String> map_final_products = new HashMap<Float,String>();
        for(int i=0;i<list_of_products.size();i++) {
            product_name = list_of_products.get(i).getText();
            product_price = list_of_products_price.get(i).getText();
            int_product_price= Float.parseFloat(product_price.replaceAll("[$,]", ""));
            map_final_products.put(int_product_price,product_name);
        }


        Set<Float> allkeys = map_final_products.keySet();
        ArrayList<Float> array_list_values_product_prices = new ArrayList<Float>(allkeys);
        Collections.sort(array_list_values_product_prices);
        //Lowest Product is
        Float low_price = array_list_values_product_prices.get(0);
        System.out.println("low Product Price is: " + low_price + " Product name is: " + map_final_products.get(low_price));

        //click on add to cart button next to low product price
        WebElement addToCart= ScenarioContext.driver.findElement(By.xpath("//div[contains(text(),'"+map_final_products.get(low_price)+"')]//ancestor::div[@class='inventory_item_label']//following-sibling::div//button[contains(text(),'Add to cart')]"));
        ElementUtil.click(addToCart);

    }

}

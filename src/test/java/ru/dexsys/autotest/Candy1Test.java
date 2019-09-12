package ru.dexsys.autotest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

public class Candy1Test {
    private WebDriver webDriver;
    private WebDriver webDriver2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candy1Test that = (Candy1Test) o;
        return Objects.equals(webDriver, that.webDriver);
    }

    @Before
    public void init() {
//        System.setProperty("webdriver.chrome.webDriver", "src/test/resources/chromedriver.exe"); //Эта строка и следующая - почти идентичны. Указываю необходимые системе драйвер и его расположение
        WebDriverManager.chromedriver().setup();//устанавливаю драйвер для хрома (на видео не было нужно)
        webDriver = new ChromeDriver(); //создаю объект - открываю окно хрома
//        webDriver.manage().window().maximize(); //раскрываю окно во весь экран
        webDriver.get("https://market.yandex.ru/"); //перехожу на страницу Я.Маркета
    }

    @Test //проверка1: на какую страницу переходит пользователь после нажатия кнопки "отложенные" на главной странице
    public void candyTest1() throws InterruptedException {
        webDriver.findElement(By.xpath("//span[@data-title = 'Отложенные']")).click(); //Нахожу кнопку "Отложенные" и кликаю на неё
        Assert.assertTrue(webDriver.getCurrentUrl().contains("https://market.yandex.ru/my/wishlist?track=head"));//проверяю переход на указанную страницу
    }

    @Test //Проверка2: проверяю, совпадают ли товары, которые я добавляю с главной страницы с теми, что лежат в корзине.
    public void candyTest2() throws InterruptedException {
        WebElement someProduct1 = webDriver.findElement(By.xpath("//span[text()='₽']/.."));
        someProduct1.click(); //Нахожу первый товар на странице и кликаю на него
        String someText1 = webDriver.getTitle(); //получаю текст со страницы1.
        String particalSomeText1 = someText1.substring(0,someText1.indexOf(" — "));

        WebElement button = webDriver.findElement(By.partialLinkText("Отложить"));
        button.click(); //Нажимаю на кнопку "Отложить"
        webDriver.get("https://market.yandex.ru/my/wishlist?track=rmmbr"); //перехожу в "Отложенные"

        WebElement someProduct2 = (new WebDriverWait(webDriver,10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='n-wishlist-list']//a"))); //c умным waiter))
        someProduct2.click();
        String particalSomeText2 = someText1.substring(0,someText1.indexOf(" — "));
        Assert.assertEquals(particalSomeText1, particalSomeText2); //проверяю, совпадает ли то, что я отложил в корзину с тем, что там реально находится
    }

    @Test //Проверяю, как работает цифра в кнопке отложенные на главной странице.
    public void candyTest3() throws InterruptedException {
        // Добавляю один элемент в отложенные
        WebElement someProduct1 = webDriver.findElement(By.xpath("//span[text()='₽']/.."));
        someProduct1.click(); //Нахожу первый товар на странице и кликаю на него
        //проверяю, какое число товаров "указано" в кнопке
        WebElement button = webDriver.findElement(By.partialLinkText("Отложить"));
        button.click(); //Нажимаю на кнопку "Отложить"

        webDriver.get("https://market.yandex.ru/"); //перехожу на страницу Я.Маркета
        WebElement numberInWishList = (new WebDriverWait(webDriver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href = '/my/wishlist?track=head']/span/span")));
        String countNumberOfWishList = numberInWishList.getText();
        Assert.assertEquals(countNumberOfWishList, "1");
    }

    @Test
    public void candyTest4() throws InterruptedException{ //проверю, обновляются ли данные в другом окне
        WebElement someProduct1 = webDriver.findElement(By.xpath("//span[text()='₽']/.."));
        someProduct1.click(); //Нахожу первый товар на странице и кликаю на него
        WebElement button1 = webDriver.findElement(By.partialLinkText("Отложить"));
        button1.click(); //Нажимаю на кнопку "Отложить"
        webDriver.get("https://market.yandex.ru/"); //перехожу на страницу Я.Маркета
        WebElement numberInWishList = (new WebDriverWait(webDriver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href = '/my/wishlist?track=head']/span/span")));
        String countNumberOfWishList1 = numberInWishList.getText();

        webDriver2 = new ChromeDriver(); //создаю объект 2
        webDriver2.get("https://market.yandex.ru/"); //перехожу на страницу Я.Маркета
        WebElement numberInWishList2 = (new WebDriverWait(webDriver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href = '/my/wishlist?track=head']/span/span")));
        String countNumberOfWishList2 = numberInWishList.getText();

        Assert.assertTrue(countNumberOfWishList1.equals(countNumberOfWishList2)); //проверка, совпадают ли числа на разных страницах
    }

    @After
    public void tearDown() {
        webDriver.get("https://market.yandex.ru/"); //перехожу на страницу Я.Маркета
        WebElement numberInWishList = (new WebDriverWait(webDriver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href = '/my/wishlist?track=head']/span/span")));
        String countNumberOfWishList = numberInWishList.getText();

        if(!countNumberOfWishList.equals("")){
            webDriver.get("https://market.yandex.ru/my/wishlist?track=rmmbr"); //перехожу в "Отложенные"
            WebElement cleanContent = (new WebDriverWait(webDriver,10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@value = 'remove']")));
            cleanContent.click();
        }
        if (webDriver != null) {
            webDriver.quit();
        }
        if (webDriver2 != null) { // TODO узнать, как закрывать ЛЮБОЙ элемент класса WebDriver
            webDriver.quit();
        }
    }
}
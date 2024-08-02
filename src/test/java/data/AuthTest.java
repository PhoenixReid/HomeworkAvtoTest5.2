package data;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

//java -jar ./artifacts/app-ibank.jar -P:profile=test
public class AuthTest
{

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void sendingTheFormTest() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[id=\"root\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(exactText("  Личный кабинет"));

    }

    @Test
    void unregisteredUserTest(){
        var registeredUser = getUser("active");
        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
    @Test
    void blockerUserTest(){
        var registeredUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void incorrectLoginTest(){
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").setValue(getRandomLogin());
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void incorrectPasswordTest(){
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(getRandomPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}


package order_test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom2.order.Order;
import diplom2.order.OrderSteps;
import diplom2.user.User;
import diplom2.user.UserSteps;

public class GetUserOrdersTest {
    private String token;
    private Integer number;
    private User user;
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;

    @Before
    @Step("Тестовые данные")
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        user = new User("90fnb9gg5u@yandex.ru", "19877834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"});
    }

    @Test
    @DisplayName("Получение заказов авторизированным пользователем")
    @Description("Проверяем, что можно получить список заказов авторизированного пользователя")
    public void getAuthUserOrdersTest() {
        ValidatableResponse responseCreateOrder = orderSteps.newOrderCreate(token, order);
        ValidatableResponse responseGetAuthOrders = orderSteps.getAuthUserOrders(token);
        responseGetAuthOrders.assertThat()
                .statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true))
                .and()
                .body("orders.ingredients", Matchers.notNullValue())
                .and()
                .body("orders._id", Matchers.notNullValue())
                .and()
                .body("orders.status", Matchers.notNullValue())
                .and()
                .body("orders.number", Matchers.notNullValue())
                .and()
                .body("orders.createdAt", Matchers.notNullValue())
                .and()
                .body("orders.updatedAt", Matchers.notNullValue())
                .and()
                .body("total", Matchers.notNullValue())
                .and()
                .body("totalToday", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизированным пользователем")
    @Description("Проверяем, что нельзя получить список заказов неавторизированного пользователя")
    public void getNotAuthOrdersTest() {
        ValidatableResponse responseGetNotAuthOrders = orderSteps.getNotAuthUserOrders();
        responseGetNotAuthOrders.assertThat()
                .statusCode(401)
                .and()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo( "You should be authorised"));
    }

    @After
    @Step("Удаление тестовых данных")
    public void deleteUser() {
        if (!(token == null || token.isEmpty())) {
            orderSteps.userDelete(token);
            System.out.println("DELETED");
        } else {
            System.out.println("NOT DELETED");
        }
    }
}

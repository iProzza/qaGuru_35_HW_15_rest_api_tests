package api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReqresTests extends BaseTest {

    @Test
    @DisplayName("Проверка списка данных пользователей")
    void usersListTest() {
        Response response = Requests.getUsers(2);

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().getString("page"));
        assertFalse(response.jsonPath().getList("data").isEmpty());
    }

    @Test
    @DisplayName("Проверка данных пользователя")
    void singleUserTest() {
        Response response = Requests.getUser(2);
        assertEquals(200, response.statusCode());

        assertEquals(200, response.statusCode());
        assertEquals(2, response.jsonPath().getInt("data.id"));
        assertNotNull(response.jsonPath().getString("data.email"));
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void createUserTest() {
        Response response = Requests.createUser("morpheus", "leader");

        assertEquals(201, response.statusCode());
        assertEquals("morpheus", response.jsonPath().getString("name"));
        assertEquals("leader", response.jsonPath().getString("job"));
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Проверка изменения полей у пользователя")
    void updateUserTest() {
        Response response = Requests.updateUser(2, "neo", "the one");

        assertEquals(200, response.statusCode());
        assertEquals("neo", response.jsonPath().getString("name"));
        assertEquals("the one", response.jsonPath().getString("job"));
    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    void deleteUserTest() {
        Response response = Requests.deleteUser(2);

        assertEquals(204, response.statusCode());
    }

}

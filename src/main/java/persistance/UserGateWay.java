package persistance;

import application.entities.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

public interface UserGateWay {
    void saveUser(User user);
    User getUserByUuid(UUID uuid);
    void updateUser(User user);
    void deleteUser(User user);
    User getUserByLogin(String login);
}

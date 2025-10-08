package dao.implement;

import dao.GenericDAO;
import model.User;
import java.util.List;
import java.util.Map;

public class UserDAO extends GenericDAO<User> {

    @Override
    public List<User> findAll() {
        return queryGenericDAO(User.class);
    }

    @Override
    public int insert(User user) {
        return insertGenericDAO(user);
    }

    // Lấy user theo ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM [USER] WHERE userId = ?";
        List<User> list = queryGenericDAO(User.class, sql, Map.of("userId", id));
        return list.isEmpty() ? null : list.get(0);
    }

    // Lấy user theo email (ví dụ cho login)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM [USER] WHERE email = ?";
        List<User> list = queryGenericDAO(User.class, sql, Map.of("email", email));
        return list.isEmpty() ? null : list.get(0);
    }
}

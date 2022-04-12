package ru.javaschool.JavaSchoolBackend2.dao;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.JavaSchoolBackend2.entity.User;
import ru.javaschool.JavaSchoolBackend2.enums.Position;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Transactional
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    public User save(String name, String password, String position) {
        User user = new User(name, password, Position.valueOf(position));
        entityManager.persist(user);
        entityManager.flush();
        return user;

    }



    public User findByUserName(String name) {
        List<User> currentUser = entityManager.createQuery("From User as user where user.name =\'" + name + "\'", User.class).getResultList();
        if(!currentUser.isEmpty()) {
            return currentUser.get(0);
        }
        return null;
    }


    public void update(User user){
        entityManager.merge(user);
    }

}
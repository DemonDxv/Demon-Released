package dev.demon.base.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.base.user
 */
public class UserManager {
    public static List<User> Users;

    public UserManager() {
        Users = Collections.synchronizedList(new ArrayList<>());
    }

    public User getUser(UUID uuid) {
        for (User user : Users) {
            if (user.getPlayer().getUniqueId() == uuid || user.getPlayer().getUniqueId().equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        if (!Users.contains(user) && (System.currentTimeMillis() - user.lastJoin) < 100L) {
            Users.add(user);
        }
    }

    public void removeUser(User user) {
        if (Users.contains(user)) {
            Users.remove(user);
        }
    }

    public List<User> getUsers() {
        return Users;
    }
}

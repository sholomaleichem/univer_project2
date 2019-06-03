
package com.my_cash_machine.service;

import com.my_cash_machine.domen.User;

import java.util.Collection;

public interface UserService {
    User save(User user);

    Boolean delete(int id);

    User update(User user);

    User findById(int id);

    User findByEmail(String email);

    Collection<User> findAll();
}

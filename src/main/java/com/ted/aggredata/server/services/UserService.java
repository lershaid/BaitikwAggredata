/*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.List;

/**
 * Public interface to the user management service
 */
public interface UserService {


    /**
     * Creates a new entity in the database
     *
     * @param entity
     * @param accountstate
     * @return
     */
    public User createUser(User entity, int accountstate);


    /**
     * Deletes a user from the database
     *
     * @param entity
     * @return
     */
    public void deleteUser(User entity);

    /**
     * Changes a user password
     *
     * @param entity  the user to be modified
     * @param enabled true if enabled, false otherwise
     * @return
     */
    public User changeUserStatus(User entity, boolean enabled);


    /**
     * Changes the role of the user int he system
     *
     * @param entity
     * @param role
     * @return
     */
    public User changeUserRole(User entity, String role);


    /**
     * Changes a users's password
     *
     * @param entity
     * @return
     */
    public User changePassword(User entity, String newPassword);

    /**
     * Changes changes the user's username
     *
     * @param entity
     * @return
     */
    public User changeUserName(User entity, String newUsername);


    /**
     * Returns a user object with the given username
     *
     * @param username
     * @return
     */
    public User getUserByUserName(String username);

    public User saveUser(User entity);

    /**
     * Returns a user object with the given activation key
     *
     * @param key
     * @return
     */
    public User getUserByActivationKey(String key);

    /**
     * Returns all users in the system
     * @return
     */
    public List<User> findUsers();

    /**
     * Ensures the user config is valid. If not, corrects it with defaults
     * @param user
     */
    public void checkUserConfig(User user);

    /**
     * Returns the password for the given user
     * @param user
     * @return
     */
    public String getPassword(User user);


    /***
     * Find users for the given substring
     * @return
     */
    public List<User> findUsers(String substring);


    /***
     * Finds users for the specified group
     * @param group
     * @return
     */
    public List<User>findUsers(Group group);

    /***
     * Finds a user by its id
     */
    public User findUser(Long userId);

}

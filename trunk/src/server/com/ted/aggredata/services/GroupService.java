/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package server.com.ted.aggredata.services;

import server.com.ted.aggredata.model.Group;
import server.com.ted.aggredata.model.User;

import java.util.List;

/**
 * Public interface to the user management service
 */
public interface GroupService {

    /**
     * Creates a new group
     * @param user The user creating the group. This user will be added as a ADMIN of the group.
     * @param description
     */
    public void createGroup(User user, String description);

    /**
     * Deletes a group from the system
     * @param group
     */
    public void deleteGroup(Group group);

    /**
     * Returns all groups for the specified user
     * @param user
     * @return
     */
    public List<Group> getByUser(User user);

    /**
     * Adds a user w/ the specified role in the group;
     * @param user
     * @param group
     * @param role
     */
    public void addUserToGroup(User user, Group group, Group.Role role);

    /**
     * Removes the user from the group
     * @param user
     * @param group
     */
    public void removeUserFromGroup(User user, Group group);

    /**
     * Changes the role of the user
     * @param user
     * @param group
     * @param role
     */
    public void changeUserRole(User user, Group group, Group.Role role);
}

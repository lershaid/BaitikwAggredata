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

package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing the user object
 */
public class UserDAO extends AbstractDAO<User> {

    @Autowired
    GatewayDAO gatewayDAO;

    @Autowired
    GroupDAO groupDAO;


    public static final String DELETE_USER_QUERY = "delete from aggredata.user where id=?";
    public static final String GET_BY_USERNAME_QUERY_SESSION = "select * from aggredata.user where username= ?";
    public static final String GET_BY_KEY_QUERY_SESSION = "select * from aggredata.user where activationKey= ?";
    public static final String CREATE_USER_QUERY = "insert into aggredata.user (username, activationKey, defaultGroupId, role,  state, firstName, lastName, middleName, address, city, addrState, zip, custom1, custom2, custom3, custom4, custom5, companyName, PhoneNumber, timezone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String COUNT_USER_QUERY = "select count(*) from  aggredata.user where username=?";
    public static final String UPDATE_PASSWORD = "update aggredata.user set password=? where id = ?";
    public static final String UNIQUE_KEY_CHECK = "select count(*) from  aggredata.user where activationKey=?";
    public static final String SAVE_USER_QUERY_SESSION = "update aggredata.user set username=?, activationKey=?, defaultGroupId=?, role=?, state=?, firstName=?, lastName=?, middleName=?, address=?, city=?, addrState=?, zip=?, custom1=?, custom2=?, custom3=?, custom4=?, custom5=?, companyName=?, PhoneNumber=?, timezone=? where id=?";
    public static final String LOOKUP_PASSWORD = "select password from  aggredata.user where id=?";
    public static final String SELECT_USERS_QUERY = "select * from aggredata.user";
    public static final String GET_BY_GROUP_QUERY = " select u.* from aggredata.user u, aggredata.usergroup ug where u.id=ug.userId and ug.groupId=? and ug.role > 0";
    //Delete queries if a user is deleted

    public static final String GET_BY_USERNAME_SUBSTRING_QUERY = "select * from aggredata.user where username like ? or lastName like ? or firstName like ? or companyName like ? order by lastName, firstName, companyName";


    public UserDAO() {
        super("aggredata.user");
    }


    private RowMapper<User> rowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setActivationKey(rs.getString("activationKey"));
            user.setDefaultGroupId(rs.getShort("defaultGroupId"));
            user.setRole(rs.getString("role"));
            user.setAccountState(rs.getInt("state"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setMiddleName(rs.getString("middleName"));
            user.setCity(rs.getString("city"));
            user.setZip(rs.getString("zip"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setAddrState(rs.getString("addrState"));
            user.setAddress(rs.getString("address"));
            user.setCompanyName(rs.getString("companyName"));
            user.setCustom5(rs.getString("custom5"));
            user.setCustom4(rs.getString("custom4"));
            user.setCustom3(rs.getString("custom3"));
            user.setCustom2(rs.getString("custom2"));
            user.setCustom1(rs.getString("custom1"));
            user.setTimezone(rs.getString("timezone"));
            return user;
        }
    };

    public List<User> findUsers() {
        try {
            return getJdbcTemplate().query(SELECT_USERS_QUERY, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    /**
     * Find users by matching the substring
     * @param subString
     * @return
     */
    public List<User> findUsers(String subString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%").append(subString).append("%");

        try {
            return getJdbcTemplate().query(GET_BY_USERNAME_SUBSTRING_QUERY, new Object[]{stringBuilder.toString(),stringBuilder.toString(),stringBuilder.toString(),stringBuilder.toString()},  getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return new ArrayList<User>();
        }
    }


    /**
     * Finds users for the specific group
     * @param group
     * @return
     */
    public List<User> findUsers(Group group) {
        try {
            return getJdbcTemplate().query(GET_BY_GROUP_QUERY,new Object[]{group.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return new ArrayList<User>();
        }
    }

    public User getUserByUserName(String username) {

        try {
            if (logger.isDebugEnabled()) logger.debug("looking up user object for username " + username);
            return getJdbcTemplate().queryForObject(GET_BY_USERNAME_QUERY_SESSION, new Object[]{username}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    public User getUserByKey(String key) {
        try {
            if (logger.isDebugEnabled()) logger.debug("looking up user object for key " + key);
            return getJdbcTemplate().queryForObject(GET_BY_KEY_QUERY_SESSION, new Object[]{key}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<User> getRowMapper() {
        return rowMapper;
    }

    public User create(User user) {
        if (getJdbcTemplate().queryForInt(COUNT_USER_QUERY, user.getUsername()) == 0) {
            if (logger.isDebugEnabled()) logger.debug("creating new user " + user);
            getJdbcTemplate().update(CREATE_USER_QUERY, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole(), user.getAccountState(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getAddress(), user.getCity(), user.getAddrState(), user.getZip(), user.getCustom1(), user.getCustom2(), user.getCustom3(), user.getCustom4(), user.getCustom5(), user.getCompanyName(), user.getPhoneNumber(), user.getTimezone());
        } else {
            if (logger.isDebugEnabled()) logger.debug("User already exists. Skipping create. user=" + user);
        }
        return getUserByUserName(user.getUsername());
    }


    public User save(User user) {
        if (user.getId() == null) {
            logger.debug("id is null. creating new user");
            return create(user);
        } else {
            if (logger.isDebugEnabled()) logger.debug("Saving user " + user);
            getJdbcTemplate().update(SAVE_USER_QUERY_SESSION, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole(), user.getAccountState(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getAddress(), user.getCity(), user.getAddrState(), user.getZip(), user.getCustom1(), user.getCustom2(), user.getCustom3(), user.getCustom4(), user.getCustom5(), user.getCompanyName(), user.getPhoneNumber(), user.getTimezone(), user.getId());
            return user;
        }
    }

    public void updatePassword(User user, String password) {
        if (user.getId() == null) {
            logger.error("id is null. skipping password update.");
        } else {
            if (logger.isDebugEnabled()) logger.debug("Saving user " + user);
            getJdbcTemplate().update(UPDATE_PASSWORD, password, user.getId());
        }
    }

    public void delete(User user) {
        //We need to remove all data associated with the user.

        List<Gateway> gatewayList = gatewayDAO.findByUserAccount(user);
        List<Group> groupList = groupDAO.findGroupsByUser(user, Group.Role.OWNER);
        for (Group group : groupList) {
            if (group.getRole() == Group.Role.OWNER) {
                groupDAO.delete(group);
            } else {
                groupDAO.removeGroupMembership(user, group);
            }
        }

        groupList = groupDAO.findGroupsByUser(user, Group.Role.READONLY);
        for (Group group : groupList) {
                groupDAO.removeGroupMembership(user, group);
        }


        for (Gateway gateway : gatewayList) gatewayDAO.delete(gateway);

        if (logger.isDebugEnabled()) logger.debug("removing " + user + " from user table");
        getJdbcTemplate().update(DELETE_USER_QUERY, user.getId());
    }


    /**
     * Checks to see if an activation key is already assigned to a user
     *
     * @param key
     * @return
     */
    public Boolean isUniqueKey(String key) {
        return (getJdbcTemplate().queryForInt(UNIQUE_KEY_CHECK, key) == 0);
    }

    /**
     * Used server side to lookup the password (for activation authentication
     * @param user
     * @return
     */
    public String getPassword(User user){
        List result =  getJdbcTemplate().queryForList(LOOKUP_PASSWORD, user.getId());
        if (result != null && result.size() > 0){
            LinkedCaseInsensitiveMap map = (LinkedCaseInsensitiveMap)result.get(0);
            return (String)map.get("password");
        }
        return null;
    }
}

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
import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing the Gateway Object
 */
public class GatewayDAO extends AggreDataDAO<Gateway> {

    public static String CREATE_GATEWAY_QUERY = "insert into aggredata.gateway (locationId, userAccountId, gatewaySerialNumber, state, securityKey, description) values (?,?,?,?,?,?)";
    public static String SAVE_GATEWAY_QUERY = "update aggredata.gateway set locationId=?, userAccountId=?, gatewaySerialNumber=?, state=?, securityKey=?, description=? where id=?";
    public static String GET_BY_SERIAL_NUMBER_QUERY = "select id, locationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway where gatewaySerialNumber=?";
    public static String GET_BY_LOCATION_QUERY = "select id, locationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway where locationId=?";
    public static String GET_BY_USER_ACCOUNT_QUERY = "select id, locationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway where userAccountId=?";

    public GatewayDAO() {
        super("aggredata.gateway");
    }

    private RowMapper<Gateway> rowMapper = new RowMapper<Gateway>() {
        public Gateway mapRow(ResultSet rs, int rowNum) throws SQLException {
            Gateway gateway = new Gateway();
            gateway.setId(rs.getLong("id"));
            gateway.setLocationId(rs.getLong("locationId"));
            gateway.setUserAccountId(rs.getLong("userAccountId"));
            gateway.setGatewaySerialNumber(rs.getString("gatewaySerialNumber"));
            gateway.setState(rs.getBoolean("state"));
            gateway.setSecurityKey(rs.getString("securityKey"));
            gateway.setDescription(rs.getString("description"));
            return gateway;
        }
    };

    public void create(Gateway gateway) {
        getJdbcTemplate().update(CREATE_GATEWAY_QUERY, gateway.getLocationId(), gateway.getUserAccountId(), gateway.getSecurityKey(), gateway.getState(), gateway.getSecurityKey(), gateway.description);
    }

    @Override
    public void save(Gateway gateway) {
        getJdbcTemplate().update(SAVE_GATEWAY_QUERY, gateway.getLocationId(), gateway.getUserAccountId(), gateway.getSecurityKey(), gateway.getState(), gateway.getSecurityKey(), gateway.description, gateway.getId());
    }


    /**
     * Returns the MTU's for the given gateway
     *
     * @param user
     * @return
     */
    public List<Gateway> getByUserAccount(User user) {
        try {
            return getJdbcTemplate().query(GET_BY_USER_ACCOUNT_QUERY, new Object[]{user.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    /**
     * Returns the MTU's for the given gateway
     *
     * @param location
     * @return
     */
    public List<Gateway> getByLocation(Location location) {
        try {
            return getJdbcTemplate().query(GET_BY_LOCATION_QUERY, new Object[]{location.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    /**
     * Returns the mtu for the given serial number
     *
     * @param serialNumber
     * @return
     */
    public Gateway getBySerialNumber(String serialNumber) {
        try {
            return getJdbcTemplate().queryForObject(GET_BY_SERIAL_NUMBER_QUERY, new Object[]{serialNumber}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<Gateway> getRowMapper() {
        return rowMapper;
    }
}

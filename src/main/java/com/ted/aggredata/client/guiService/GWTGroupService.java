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

package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.EnergyDataHistory;
import com.ted.aggredata.model.EnergyDataHistoryQueryResult;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.Group;

import java.util.List;

/**
 * Client Side Interface for the service to check user sessions.
 */
@RemoteServiceRelativePath("GWTGroupService")
public interface GWTGroupService extends RemoteService {

    /**
     * Finds groups for the specific user
     *
     * @return
     */
    public List<Group> findGroups();

    /**
     * Creates a new group
     *
     * @param description
     */
    public Group createGroup(String description);

    /**
     * Saves a group
     *
     * @param group
     */
    public Group saveGroup(Group group);

    /**
     * Delete Group
     */
    public void deleteGroup(Group group);


    /**
     * Returns the history for the given type
     * @param historyType
     * @param group
     * @param startTime
     * @param endTime
     * @return
     */
    public EnergyDataHistoryQueryResult getHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime);


}
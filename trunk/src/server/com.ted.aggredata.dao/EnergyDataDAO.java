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

package server.com.ted.aggredata.dao;

import java.io.Serializable;

/***
 * Single entry of power com.ted.aggredata.dao
 */
public class EnergyDataDAO implements Serializable{
    private Integer mtuId;
    private Integer timestamp;
    private double rate;
    private double energy;

    public EnergyDataDAO()
    {

    }

    /**
     * unique id of the mtu that logged this com.ted.aggredata.dao point
     * @return
     */
    public Integer getMtuId() {
        return mtuId;
    }

    public void setMtuId(Integer mtuId) {
        this.mtuId = mtuId;
    }

    /**
     * unix epoch timestamp of the entr
     * @return
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * rat e in effect when the energy value was stored.
     * @return
     */
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * total cumulative watt hours recorded by the mtu for the specified time
     * @return
     */
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("EnergyDataDAO{");
//        b.append(",id:" + getId());
        b.append(",timestamp:" + timestamp);
        b.append(", rate:" + rate);
        b.append(", energy:" + energy);
        b.append(", mtuId:" + mtuId);
        b.append("}");
        return b.toString();
    }
}

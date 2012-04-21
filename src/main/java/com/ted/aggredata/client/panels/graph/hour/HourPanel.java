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

package com.ted.aggredata.client.panels.graph.hour;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ted.aggredata.client.panels.graph.BarGraphPanel;
import com.ted.aggredata.model.Enums;

import java.util.Date;


public class HourPanel extends BarGraphPanel {

    interface MyUiBinder extends UiBinder<Widget, HourPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    VerticalPanel graphPanel;

    public HourPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        graphPanel.add(barGraphPanel);
    }


    @Override
    protected Date fixStartDate(Date startDate) {
        Date theDate = new Date(startDate.getTime());
        theDate.setMinutes(0);
        return theDate;
    }

    @Override
    protected Date fixEndDate(Date endDate) {
        Date theDate = new Date(endDate.getTime() + (3600*1000));
        theDate.setMinutes(0);
        return theDate;
    }


    @Override
    protected DateTimeFormat getDateTimeFormat() {
        return DateTimeFormat.getFormat("MM/dd/yyyy h a");
    }

    @Override
    protected Enums.HistoryType getHistoryType() {
        return Enums.HistoryType.HOURLY;
    }



}

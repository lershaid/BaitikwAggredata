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

package com.ted.aggredata.client.panels.graph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.visualization.client.*;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.model.*;


import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Superclass for the bar graph panels
 */
public abstract class BarGraphPanel extends Composite implements GraphOptionChangeable {

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);
    final DashboardConstants dashboardConstants = DashboardConstants.INSTANCE;
    final NumberFormat currencyFormat = NumberFormat.getCurrencyFormat();

    String BACKGROUND_COLOR = "transparent";

    public static final int GRAPH_WIDTH = 880;
    public static final int GRAPH_HEIGHT = 560;



    static Logger logger = Logger.getLogger(BarGraphPanel.class.toString());
    protected Group group;
    protected Date startDate;
    protected Date endDate;
    protected Enums.GraphType graphType;
    protected EnergyDataHistoryQueryResult historyResult = null;

    protected VerticalPanel barGraphPanel;
    protected Label barGraphTitle;

    ColumnChart barChart = null;

    /**
     * Callback to handle the loading of history data.
     */
    Runnable onLoadCallback = new Runnable() {
        public void run() {
            if (historyResult != null)
            {
                logger.fine("Callback received. Drawing.");

                //Create the visualization
                if (barChart == null) {
                    barChart = new ColumnChart(createTable(), createOptions());
                    barGraphPanel.add(barChart);

                    barGraphPanel.getElement().getStyle().setBackgroundColor(BACKGROUND_COLOR);
                } else {
                    barChart.draw(createTable(), createOptions());
                }
            }   else {
                logger.severe("historyResult is null!");
            }
        }
    };

    protected void setGraphingPanel(VerticalPanel vp, Label graphTitle) {
        this.barGraphPanel = vp;
        this.barGraphTitle = graphTitle;
    }

    public void onGraphOptionChange(Group group, Date startDate, Date endDate, Enums.GraphType graphType) {

        this.group = group;
        this.startDate = fixStartDate(startDate);
        this.endDate = fixEndDate(endDate);

        if (logger.isLoggable(Level.FINE)) logger.fine("Graph Option Change Called: " + group + " " + this.startDate + " " + this.endDate + " " + graphType);


        this.graphType = graphType;


        groupService.getHistory(getHistoryType(), group, this.startDate.getTime()/1000, this.endDate.getTime()/1000, new TEDAsyncCallback<EnergyDataHistoryQueryResult>() {
            @Override
            public void onSuccess(EnergyDataHistoryQueryResult energyDataHistoryQueryResult) {
                logger.fine("history data returned. Drawing");
                historyResult = energyDataHistoryQueryResult;
                VisualizationUtils.loadVisualizationApi(onLoadCallback, ColumnChart.PACKAGE);

            }
        });


    }

    /**
     * Method used to add the necessary padding before the start of the date query.
     * @param startDate
     * @return
     */
    protected abstract Date fixStartDate(Date startDate);

    /**
     * Method used to add the necessary padding after the end of the date query.
     * @param startDate
     * @return
     */
    protected abstract Date fixEndDate(Date startDate);


    public static String toTitleCase(String string){
        String result = "";
        for (int i = 0; i < string.length(); i++){
            String next = string.substring(i, i + 1);
            if (i == 0){
                result += next.toUpperCase();
            } else {
                result += next.toLowerCase();
            }
        }
        return result;
    }


    protected Options createOptions() {

        DateTimeFormat dateTimeFormat = getDateTimeFormat();
        StringBuffer title = new StringBuffer();

        title.append(toTitleCase(getHistoryType().toString()));
        title.append(" ");

        if (graphType.equals(Enums.GraphType.COST)) title.append(dashboardConstants.graphCost());
        else if (graphType.equals(Enums.GraphType.ENERGY)) title.append(dashboardConstants.graphEnergy());

        title.append(" ").append(dashboardConstants.graphDateRange()).append(" ");
        title.append(dateTimeFormat.format(startDate)).append(" ").append(dashboardConstants.graphTo()).append(" ");

        Date titleEndDate = new Date(endDate.getTime());
        CalendarUtil.addDaysToDate(titleEndDate, -1);
        title.append(dateTimeFormat.format(titleEndDate));
        logger.fine("TITLE:" + title);


        Options options = ColumnChart.createOptions();
        options.setWidth(GRAPH_WIDTH);
        options.setHeight(GRAPH_HEIGHT);

        barGraphTitle.setText(title.toString());

        options.setBackgroundColor(BACKGROUND_COLOR);
        options.setLegend(LegendPosition.RIGHT);

        TextStyle legendStyle = TextStyle.create();
        legendStyle.setColor("#FFFFFF");
        legendStyle.setFontName("Arial, Verdana, Trebuchet, sans-serif");
        legendStyle.setFontSize(8);


        options.setLegendTextStyle(legendStyle);
        options.setFontName("Arial, Verdana, Trebuchet, sans-serif");
        options.set("is3D", true);


        TextStyle vAxisStyle = TextStyle.create();
        vAxisStyle.setColor("#FFFFFF");
        vAxisStyle.setFontName("Arial, Verdana, Trebuchet, sans-serif");
        vAxisStyle.setFontSize(12);



        AxisOptions vAxisOptions = AxisOptions.create();
        vAxisOptions.setTextStyle(vAxisStyle);
        vAxisOptions.setMinValue(0);

        if (graphType.equals(Enums.GraphType.ENERGY)) vAxisOptions.setTitle(dashboardConstants.graphTypeEnergy());
        else if (graphType.equals(Enums.GraphType.COST)) vAxisOptions.setTitle(dashboardConstants.graphTypeCost());
        vAxisOptions.setTitleTextStyle(vAxisStyle);

        options.setVAxisOptions(vAxisOptions);



        TextStyle hAxisStyle = TextStyle.create();
        hAxisStyle.setColor("#FFFFFF");
        hAxisStyle.setFontName("Arial, Verdana, Trebuchet, sans-serif");

//        int autosize = historyResult.getNetHistoryList().size() / 7;
//        autosize = 12 - (autosize*2);
//        if (autosize < 0) autosize=1;
//        hAxisStyle.setFontSize(autosize);
        hAxisStyle.setFontSize(10);


        AxisOptions hAxisOptions = AxisOptions.create();
        hAxisOptions.setTextStyle(hAxisStyle);
        hAxisOptions.setTitleTextStyle(hAxisStyle);
        options.setHAxisOptions(hAxisOptions);



        TextStyle titleStyle = TextStyle.create();
        titleStyle.setColor("#FFFFFF");
        titleStyle.setFontSize(16);
        titleStyle.setFontName("Arial, Verdana, Trebuchet, sans-serif");

        options.setTitleTextStyle(titleStyle);


        ChartArea ca = ChartArea.create();
//        ca.setHeight(GRAPH_HEIGHT-185);
        ca.setWidth(GRAPH_WIDTH-250);
        ca.setLeft(60);
        ca.setTop(5);

        options.setChartArea(ca);

        options.set("is3D", "true");



        return options;
    }


    protected AbstractDataTable createTable() {
        DataTable data = DataTable.create();
        data.addColumn(AbstractDataTable.ColumnType.STRING, "Task");


        //Only show NET if we have more than one gateway in the group.
        if (historyResult.getGatewayList().size() > 1)
        {
            StringBuffer descriptionBuffer = new StringBuffer();
            descriptionBuffer.append("Total " + toTitleCase(graphType.toString()));

            if (graphType.equals(Enums.GraphType.ENERGY)) {
                Double v = historyResult.getNetEnergyTotal();
                if (v == null) v = 0d;
                v = round(v/1000, 1);
                if (v != 0) {
                    descriptionBuffer.append(" (").append(v + " kWh").append(")");
                }
            } else {
                Double v = historyResult.getNetCostTotal();
                if (v == null) v = 0d;
                v = round(v, 2);
                if (v != 0) {
                    descriptionBuffer.append(" (").append(currencyFormat.format(v)).append(")");
                }
            }

            data.addColumn(AbstractDataTable.ColumnType.NUMBER, descriptionBuffer.toString());

        }


        for (Gateway gateway: historyResult.getGatewayList()) {
            StringBuffer descriptionBuffer = new StringBuffer();
            descriptionBuffer.append(gateway.getDescription());

            if (graphType.equals(Enums.GraphType.ENERGY)) {
                Double v = historyResult.getGatewayEnergyTotalList().get(gateway.getId());
                if (v == null) v = 0d;
                v = round(v/1000, 1);
                if (v != 0) {
                    descriptionBuffer.append(" (").append(v + " kWh").append(")");
                }
            } else {
                Double v = historyResult.getGatewayCostTotalList().get(gateway.getId());
                if (v == null) v = 0d;
                v = round(v, 2);
                if (v != 0) {
                    descriptionBuffer.append("(").append(currencyFormat.format(v)).append(")");
                }
            }

            data.addColumn(AbstractDataTable.ColumnType.NUMBER, descriptionBuffer.toString());
        }



        int rowCount = historyResult.getNetHistoryList().size();
        if (logger.isLoggable(Level.FINE)) logger.fine("Total Results:" + rowCount);
        data.addRows(rowCount);

        for (int i=0; i < rowCount; i++)
        {
            String title = getDateTimeFormat().format(historyResult.getNetHistoryList().get(i).getHistoryDate().getTime());

            //Set the month
            int col = 0;
            data.setValue(i, col++, title);

            //Only show NET if we have more than one gateway in the group.
            if (historyResult.getGatewayList().size() > 1)
            {
                if (graphType.equals(Enums.GraphType.ENERGY)) {
                    double energy = round(historyResult.getNetHistoryList().get(i).getEnergy() / 1000, 3);
                    data.setCell(i, col++, energy, energy + " kWh", null);
                }
                else {
                    double cost = round(historyResult.getNetHistoryList().get(i).getCost(), 2);
                    data.setCell(i, col++, cost, currencyFormat.format(cost), null);
                }
            }


            for (Gateway gateway: historyResult.getGatewayList()) {
                List<EnergyDataHistory> gwList =historyResult.getGatewayHistoryList().get(gateway.getId());
                if (gwList == null || gwList.size()==0) {
                    logger.fine("Skipping " + gateway + " since there is no history for it");
                    data.setValue(i, col++, 0);
                }   else {
                    if (graphType.equals(Enums.GraphType.ENERGY)) {
                        double energy = round(gwList.get(i).getEnergy()/1000, 3);
                        data.setCell(i, col++, energy, energy + " kWh", null);
                    }
                    else {
                        double cost = round(gwList.get(i).getCost(), 2);
                        data.setCell(i, col++, cost, currencyFormat.format(cost), null);

                    }
                }
            }

        }

        return data;
    }

    private double round(double value, int decimalPlaces) {
        double multiplier = 1;
        for (int i=0; i < decimalPlaces; i++)
        {
            multiplier = multiplier * 10;
        }

        double roundedValue = value * multiplier;
        roundedValue = Math.round(roundedValue);
        roundedValue = roundedValue / multiplier;
        return roundedValue;

    }

    protected abstract DateTimeFormat getDateTimeFormat();
    protected abstract Enums.HistoryType getHistoryType();

}

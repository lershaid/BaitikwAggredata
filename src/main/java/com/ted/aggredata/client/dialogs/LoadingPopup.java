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

package com.ted.aggredata.client.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.widgets.SmallButton;

import java.util.logging.Logger;


public class LoadingPopup extends PopupPanel {

    static Logger logger = Logger.getLogger(LoadingPopup.class.toString());

    interface MyUiBinder extends UiBinder<Widget, LoadingPopup> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Label messageLabel;

    public LoadingPopup() {
        setWidget(uiBinder.createAndBindUi(this));
        this.getElement().getStyle().setBorderColor("#c3c1c1");
        this.getElement().getStyle().setBackgroundColor("#1c1c1c");
        this.center();
        int top = this.getAbsoluteTop() - 100;
        int left = this.getAbsoluteLeft();
        this.setPopupPosition(left, top);

    }
}
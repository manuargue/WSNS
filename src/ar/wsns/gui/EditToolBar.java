/**
 * Copyright 2011, Manuel Argüelles
 * 
 * This file is part of WSNS
 * 
 * WSNS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WSNS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with WSNS.  If not, see <http://www.gnu.org/licenses/>.
 */

package ar.wsns.gui;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * @author Manuel Argüelles
 */
public class EditToolBar extends JToolBar {

    public final static String ADD_STATION = "addEstation";
    public final static String ADD_LINK = "addLink";
    public final static String DELETE = "delete";

    public EditToolBar(Canvas canvasListener) {
        super("Network edition");
        addButtons(canvasListener);
        setFloatable(false);
    }

    private void addButtons(Canvas canvasListener) {
        JButton button = null;
        button = makeEditButton("station", ADD_STATION, "Add a weather station",
                                "Weather station", canvasListener);
        add(button);
        button = makeEditButton("link", ADD_LINK, "Add a link", "Link",
                                canvasListener);
        add(button);
        addSeparator();
        button = makeEditButton("delete", DELETE,"Delete a component",
                                "Delete", canvasListener);
        add(button);
    }

    private JButton makeEditButton(String imageName, String actionCommand,
                                   String toolTipText, String altText,
                                   Canvas canvasListener) {
        /* look for the image */
        String imgLocation = "icons/" + imageName + ".gif";
        URL imageURL = getClass().getResource(imgLocation);

        /* create and initialize the button */
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(canvasListener);

        if (imageURL != null)
            button.setIcon(new ImageIcon(imageURL, altText));
        else {
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }
        
        return button;
    }

}

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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Manuel Argüelles
 */
public class LinkDialog extends JDialog implements ActionListener {
    
    public static String OK_BTN = "okButton";
    private Canvas canvas;
    private JTextField bwTxt;
    private JTextField distanceTxt;

    public LinkDialog(Container owner) {
        canvas = (Canvas) owner;
        canvas.setCurrentBandWidth(LinkItem.BAND_WIDTH);
        canvas.settCurrentDistance(LinkItem.DISTANCE);
        setLocationRelativeTo(owner);
        initComponents();

    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        /* band width title */
        c.insets = new Insets(15, 15, -10, 5);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Bandwidth:"), c);

        /* distance title */
        c.insets = new Insets(0, 15, 0, 5);
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Distance:"), c);

        c.weighty = .2;
        /* band width text field */
        bwTxt = new JTextField(String.valueOf(LinkItem.BAND_WIDTH));
        c.insets = new Insets(15, 0, -10, 15);
        c.gridx = 1;
        c.gridy = 0;
        add(bwTxt, c);

        /* distance text field */
        distanceTxt = new JTextField(String.valueOf(LinkItem.DISTANCE));
        c.insets = new Insets(0, 0, 0, 15);
        c.gridx = 1;
        c.gridy = 1;
        add(distanceTxt, c);

        /* OK button */
        JButton okBtn = new JButton("Ok");
        okBtn.setActionCommand(OK_BTN);
        okBtn.addActionListener(this);

        c.insets = new Insets(-10, 15, 10, 15);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 2;
        c.weightx = .8;
        c.gridx = 0;
        c.gridy = 2;
        add(okBtn, c);
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Link options");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 180);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        int bw = -1;
        int distance = -1;
        try {
            bw = Integer.parseInt(bwTxt.getText());
            distance = Integer.parseInt(distanceTxt.getText());
        } catch (NumberFormatException ex) {
            return;
        }
        if (bw > 0 && distance > 0) {
            /* set the bandwidth and distance values in the canvas */
            canvas.setCurrentBandWidth(bw);
            canvas.settCurrentDistance(distance);
            /* destroy this dialog */
            dispose();
        }

        
    }

}

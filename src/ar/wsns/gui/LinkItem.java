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

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Manuel Argüelles
 */
public class LinkItem {

    public final static int DISTANCE = 100;
    public final static int BAND_WIDTH = 50;

    private NodeItem nodeFrom;
    private NodeItem nodeTo;

    public LinkItem(NodeItem nodeFrom, NodeItem nodeTo) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
    }

    public LinkItem(NodeItem nodeFrom) {
        this(nodeFrom, null);
    }

    public NodeItem getNodeFrom() {
        return nodeFrom;
    }

    public NodeItem getNodeTo() {
        return nodeTo;
    }

    public void setNodeFrom(NodeItem nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public void setNodeTo(NodeItem nodeTo) {
        this.nodeTo = nodeTo;
    }

    public Point getPos() {
        final int x = Math.abs(nodeFrom.getPos().getX() + nodeTo.getPos().getX()) / 2;
        final int y = Math.abs(nodeFrom.getPos().getY() + nodeTo.getPos().getY()) / 2;
        return new Point (x, y);
    }

    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.drawLine(nodeFrom.getPos().getX(), nodeFrom.getPos().getY(),
                   nodeTo.getPos().getX(), nodeTo.getPos().getY());
    }

}

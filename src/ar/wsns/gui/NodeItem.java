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
public class NodeItem {

    protected Point pos;
    protected final int ID;
    protected static final int width = 20;
    protected static final int height = 20;

    public NodeItem(Point pos, int id) {
        this.pos = pos;
        ID = id;
    }

    public int getID() { return ID; }

    public Point getPos() { return pos; }

    public void setPos(Point pos) { this.pos = pos; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public void paint(Graphics g) {
        final int x = pos.getX() - width / 2;
        final int y = pos.getY() - height / 2;
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }
}

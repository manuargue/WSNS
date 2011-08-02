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

package coretest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Manuel Argüelles
 */
public class TestDataBase {

    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:res/statistics.db");
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery("select * from station1;");

        System.out.println("sensor\tdate\t\tmax\t\tavg\t\t\tmin");
        while (rs.next()) {
            System.out.println(rs.getString("sensor") + "\t" +
                               rs.getDate("date") + "\t" +
                               rs.getDouble("max") + "\t" +
                               rs.getDouble("average") + "\t" +
                               rs.getDouble("min"));
        }
        rs.close();
        conn.close();
    }

}

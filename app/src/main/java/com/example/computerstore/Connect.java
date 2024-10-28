package com.example.computerstore;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    Connect con;
    String uname, pass, ip,port, database;

    public Connection connection(){
        ip="192.168.0.139";
        database="ComputerStore";
        uname="aa";
        pass="113112111Tien";
        port="1433";
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String ConnectionURL=null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database + ";user=" + uname + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }
        return connection;
    }
}

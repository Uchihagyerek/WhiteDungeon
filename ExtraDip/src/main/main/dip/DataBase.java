package main.dip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBase {
    Connection conn;
    public DataBase(){
        conn=null;
        try{
            String url="jdbc:sqlite:src/db/maindb.db";
            conn=DriverManager.getConnection(url);
            System.out.println("Connected!");

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void close(){
        try {
            if(conn!=null) {
                conn.close();
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public String[] getMonster(){
        String[] monster;
        List<String[]> monsters = new ArrayList<String[]>();
        Random rand=new Random();

        // In the database there is a column named "beaten" if 2 it means it's a normal monster, if 0 it means not beaten boss, if 1
        // it means beaten boss.
        try {
            Statement stm = conn.createStatement();
            ResultSet rs =stm.executeQuery("SELECT * FROM monsters WHERE boss=0");

            while (rs.next()){
                monster=new String[5];
                monster[0]=rs.getString("id");
                monster[1]=rs.getString("name");
                monster[2]=rs.getString("health");
                monster[3]=rs.getString("mana");
                monster[4]=rs.getString("damage");

                monsters.add(monster);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        monster= monsters.get(rand.nextInt(monsters.size()));

        return monster;
    }public String[] getBoss(){
        String[] monster;
        List<String[]> monsters = new ArrayList<String[]>();
        Random rand=new Random();

        // In the database there is a column named "beaten" if 2 it means it's a normal monster, if 0 it means not beaten boss, if 1
        // it means beaten boss.
        try {
            Statement stm = conn.createStatement();
            ResultSet rs =stm.executeQuery("SELECT * FROM monsters WHERE boss=1");

            while (rs.next()){
                monster=new String[5];
                monster[0]=rs.getString("id");
                monster[1]=rs.getString("name");
                monster[2]=rs.getString("health");
                monster[3]=rs.getString("mana");
                monster[4]=rs.getString("damage");

                monsters.add(monster);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        monster= monsters.get(rand.nextInt(monsters.size()));

        return monster;
    }
    public String[] getSave(){
        String[] save=new String[9];

        try{
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery("SELECT * FROM save");

            while(rs.next()){

                save[0]=rs.getString("playerlevel");
                save[1]=rs.getString("currentxp");
                save[2]=rs.getString("currenthealth");
                save[3]=rs.getString("currentmana");
                save[4]=rs.getString("helmet");
                save[5]=rs.getString("armor");
                save[6]=rs.getString("weapon");
                save[7]=rs.getString("consumables");
                save[8]=rs.getString("class");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return save;
    }

}

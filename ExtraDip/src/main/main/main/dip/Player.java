package main.dip;

import javax.swing.*;

public class Player extends Entity{
    public String playerClass;
    public int exp;
    public int remainingExp;
    public int potionsCount;
    public int damage;
    Map map;
    public Player(String name, String playerClass, Map map){
        this.name=name;
        this.playerClass=playerClass;
        damage=1000;
        level=1;
        maxHealth=500;
        maxMana=500;
        health=maxHealth;
        mana=maxMana;
        exp=0;
        remainingExp=1000;
        this.map=map;
        potionsCount=3;
    }

    public void levelUp(){
        if(exp>=remainingExp) {
            if (remainingExp < exp)
                exp = exp - remainingExp;
            else
                exp = 0;
            level++;
            maxHealth += level * 100;
            maxMana += level * 100;
            remainingExp *= 1.2;
            health = maxHealth;
            mana = maxMana;
            levelUp();
        }
    }
    int attack() {
        int damage=0;


        return damage;
    }
    void die(){
        int restart= JOptionPane.YES_NO_OPTION;
        restart=JOptionPane.showConfirmDialog(null,"Would you like to restart?","You died!",restart);
        if(restart==JOptionPane.YES_OPTION){
            map.playerDead=true;
        map.generateMap();
        }else if (restart==JOptionPane.NO_OPTION){
            System.exit(1);

        }
    }
}

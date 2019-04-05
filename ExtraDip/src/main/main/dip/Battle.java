package main.dip;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Battle extends Canvas {

    DataBase db=new DataBase();
    static JFrame thisFrame;
    Map map;
    Monster enemy;
    JProgressBar enemyHealth;
    JProgressBar playerHealth;
    JProgressBar playerMana;
    JProgressBar playerExp;
    JButton attack;
    JButton spell;
    JButton items;
    JButton escape;
    JLabel level;
    Player player;
    public Battle(Map map, Player player) {
        String[] monster;
        setSize(900, 900);

        if (map.monsters > 0)
            monster = db.getMonster();
        else
            monster=db.getBoss();

        this.map = map;
        this.player = player;

        for (int i = 0; i < monster.length; i++) {
            System.out.println(monster[i] + ", ");
        }
        enemy = new Monster(monster[1], Integer.parseInt(monster[2]), Integer.parseInt(monster[3]), Integer.parseInt(monster[4]), player);

    }


    @Override
    public void update(Graphics g) {paint(g);}
    @Override
    public void paint(Graphics ig) {
        BufferedImage image = new BufferedImage(900, 900, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect (0,0,900,900);
        BufferedImage sprite;
        try {
            sprite=ImageIO.read(new File("src/sprites/"+enemy.name+".jpg"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            sprite=null;
        }
        ig.drawImage(image, 0,0,this);
        ig.drawImage(sprite,sprite.getWidth()/2,20,null);

    }
    public void ui(JFrame frame){
        thisFrame=frame;
        attack=new JButton("Attack");
        attack.setBounds(50,750,200, 50);
        attack.setBackground(Color.WHITE);
        attack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attackBtn();
            }
        });

        spell=new JButton("Spell");
        spell.setBounds(250,750,200, 50);
        spell.setBackground(Color.WHITE);
        spell.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spell();
            }
        });

        items=new JButton("Items");
        items.setBounds(50, 800,200,50);
        items.setBackground(Color.WHITE);
        items.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                items();
            }
        });

        escape=new JButton("Escape");
        escape.setBounds(250, 800, 200,50);
        escape.setBackground(Color.WHITE);
        escape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                escape();
            }
        });

        enemyHealth=new JProgressBar();
        int maxhealth=enemy.health;
        enemyHealth.setMaximum(maxhealth);
        enemyHealth.setValue(maxhealth);
        enemyHealth.setBounds(0,0,900,30);
        enemyHealth.setString(enemy.name+": "+enemy.health+"/"+maxhealth);
        enemyHealth.setStringPainted(true);
        enemyHealth.setForeground(Color.RED);

        playerHealth=new JProgressBar();
        playerHealth.setMaximum(player.maxHealth);
        playerHealth.setValue(player.health);
        playerHealth.setBounds(500,750,350,50);
        playerHealth.setString("Health: "+player.health+"/"+player.maxHealth);
        playerHealth.setStringPainted(true);
        playerHealth.setForeground(Color.GREEN);

        playerExp=new JProgressBar();
        playerExp.setMaximum(player.remainingExp);
        playerExp.setValue(player.exp);
        playerExp.setBounds(0,880,900,20);
        playerExp.setString("Exp: "+player.exp+"/"+player.remainingExp);
        playerExp.setStringPainted(true);
        playerExp.setForeground(Color.magenta);

        level=new JLabel();
        level.setText("Lvl: "+player.level);
        level.setBounds(430,870,50,10);
        level.setBackground(Color.lightGray);
        level.setVisible(true);

        frame.add(attack);
        frame.add(spell);
        frame.add(items);
        frame.add(escape);
        frame.add(enemyHealth);
        frame.add(playerHealth);
        frame.add(playerExp);
        frame.add(level);

    }


    private void attackBtn() {
        attack();
        manageButtons();
        Thread myThread=new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enemyAttack();
                manageButtons();
            }
        };
        myThread.start();
    }
    private void attack(){
        System.out.println("ATTACK!!");
        enemyHealth.setValue(enemyHealth.getValue()-100);
        enemyHealth.setString(enemy.name+": "+enemyHealth.getValue()+"/"+enemyHealth.getMaximum());

        if (enemyHealth.getValue()<=0){
            map.setFocusable(true);
            map.requestFocus();
            thisFrame.dispose();
            enemy.die();
        }
    }
    private void enemyAttack(){

        player.health-=enemy.attack();
        playerHealth.setValue(player.health);
        playerHealth.setString("Health: "+player.health+"/"+player.maxHealth);
        if(playerHealth.getValue()<=0){
            player.die();
            player=new Player(player.name,player.playerClass,map);
        }


    }
    private void spell() {
        System.out.println("Defend");
        JOptionPane.showConfirmDialog(this, "halo");
    }
    private void items() {

    }
    private void escape() {
        map.setFocusable(true);
        map.requestFocus();
        thisFrame.dispose();
    }
    private void manageButtons(){
        if (attack.isEnabled()){
            attack.setEnabled(false);
            spell.setEnabled(false);
            items.setEnabled(false);
            escape.setEnabled(false);
        }else{
            attack.setEnabled(true);
            spell.setEnabled(true);
            items.setEnabled(true);
            escape.setEnabled(true);
        }

    }






}

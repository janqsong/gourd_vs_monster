package com.sjq.gourd.creature;

public class Creature {
    protected String name;
    protected int health_value;
    protected int attack_value;
    protected int defense_value;
    protected int attack_speed;
    protected int move_speed;

    public Creature(String name, int h_v, int a_v, int d_v, int a_s, int m_s) {
        this.name = name;
        health_value = h_v;
        attack_value = a_v;
        defense_value = d_v;
        attack_speed = a_s;
        move_speed = m_s;
    }
}

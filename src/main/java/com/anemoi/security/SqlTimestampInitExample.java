package com.anemoi.security;
import java.sql.Timestamp;



public class SqlTimestampInitExample {

    

    public static void main(String[] args) {

        long now = System.currentTimeMillis();

        Timestamp sqlTimestamp = new Timestamp(now);

        System.out.println("currentTimeMillis     : " + now);

        System.out.println("SqlTimestamp          : " + sqlTimestamp);

        System.out.println("SqlTimestamp.getTime(): " + sqlTimestamp.getTime());

    }

}
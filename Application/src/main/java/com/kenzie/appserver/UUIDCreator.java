package com.kenzie.appserver;

import java.util.UUID;

public class UUIDCreator {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(UUID.randomUUID());
        }
    }
}

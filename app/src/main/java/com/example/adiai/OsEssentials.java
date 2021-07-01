package com.example.adiai;

public class OsEssentials {
    private int themeMode = 0;
    public int getthemeMode(){
        return themeMode;
    }
    public void changeTheme(){
        themeMode = 1 - themeMode;
    }
}

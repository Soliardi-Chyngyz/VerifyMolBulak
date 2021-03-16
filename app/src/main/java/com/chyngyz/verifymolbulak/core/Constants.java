package com.chyngyz.verifymolbulak.core;

import java.util.regex.Pattern;

public class Constants {

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=!])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");
}

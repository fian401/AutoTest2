package com.tester.model;

import lombok.Data;

@Data
public class LoginCase {
        private int id;
        private String userName;
        private String password;
        private String expected;
        private String params;
        private String parm;
}

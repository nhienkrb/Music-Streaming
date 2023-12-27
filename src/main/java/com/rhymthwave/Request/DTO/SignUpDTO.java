package com.rhymthwave.Request.DTO;

import java.util.Date;

public record SignUpDTO  (String email, String password,String username,Date birthday, int gender,String country){

}

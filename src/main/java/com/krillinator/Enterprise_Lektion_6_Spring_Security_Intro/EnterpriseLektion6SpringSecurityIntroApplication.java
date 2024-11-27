package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.AppPasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class EnterpriseLektion6SpringSecurityIntroApplication {

	public static void main(String[] args) {
		 SpringApplication.run(EnterpriseLektion6SpringSecurityIntroApplication.class, args);

	}

}

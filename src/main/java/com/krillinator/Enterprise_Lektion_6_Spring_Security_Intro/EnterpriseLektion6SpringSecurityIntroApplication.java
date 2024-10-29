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
		ApplicationContext apc = SpringApplication.run(EnterpriseLektion6SpringSecurityIntroApplication.class, args);

		System.out.println("---ADMIN---");
		System.out.println(
				UserRole.ADMIN.getListOfPermissions()
		);

		System.out.println("---USER---");
		System.out.println(
				UserRole.USER.getListOfPermissions()
		);

		System.out.println("---GetAuthorities---");
		System.out.println(UserRole.ADMIN.name()); // Should not return ROLE_ + name
		System.out.println(
				UserRole.ADMIN.getAuthorities()
		);

		System.out.println("---BEANS---");


		for(String s: apc.getBeanDefinitionNames()) {
			System.out.println(s);
		}

		System.out.println(
                Arrays.toString(UserRole.values())
		);

		UserRole.ADMIN.name();

	}

}

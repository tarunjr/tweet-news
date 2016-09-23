package org.tarun.learning.tweetnews.trends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.lang.StringBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {
   public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
   }
   @Override
	 public void run(String... args) {
     StringBuilder sb = new StringBuilder();
       for (String option : args) {
           sb.append(" ").append(option);
       }
       sb = sb.length() == 0 ? sb.append("No Options Specified") : sb;
       System.out.println(String.format("App launched with following options: %s", sb.toString()));
	}
}

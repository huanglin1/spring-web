package com.hl.learning.springweb;

import com.hl.learning.springweb.utils.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebApplication {

	public static void main(String[] args) {
		new FileUtil().saveString2TxtFile("123","23565346346");
		new FileUtil().saveString2TxtFile("123","55555");
		new FileUtil().saveString2TxtFile("123","55555");
		new FileUtil().saveString2TxtFile("123","ieuthogoiwjowhefohawoethowhegowaheoghiweogwhowehg");
		new FileUtil().saveString2TxtFile("123","55555KKKK");
		SpringApplication.run(SpringWebApplication.class, args);
	}

}

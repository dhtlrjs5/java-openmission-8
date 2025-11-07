package com.maple.mapleinfo;

import com.maple.mapleinfo.service.CubeService;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapleinfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapleinfoApplication.class, args);
		CubeService cubeService = new CubeService();
		for (int i = 0; i < 3; i++) {
			cubeService.useCube(Grade.RARE, CubeType.ADDITIONAL);
			cubeService.useCube(Grade.EPIC, CubeType.ADDITIONAL);
			cubeService.useCube(Grade.UNIQUE, CubeType.ADDITIONAL);
			cubeService.useCube(Grade.LEGENDARY, CubeType.ADDITIONAL);
		}
	}

}

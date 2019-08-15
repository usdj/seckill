package com.usdj.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gerrydeng
 * @date 2019-07-27 21:00
 * @Description:
 */

@SpringBootApplication(scanBasePackages = {"com.usdj.seckill"})
@RestController
@MapperScan("com.usdj.seckill.dao")
public class SeckillApplication {

//	@Autowired
//	private UserDOMapper userDOMapper;
//
//	@RequestMapping("/")
//	public String home() {
//		UserDO userDO = userDOMapper.selectByPrimaryKey(1);
//		if(userDO == null){
//			return "Null";
//		}
//		return userDO.getName();
//	}
	
	public static void main(String[] args){
	    SpringApplication.run(SeckillApplication.class, args);
	}

}

package com.metis.sales;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.InputStream;
import java.lang.reflect.Constructor;

@DisplayName("java 单元测试示例")
public class simple2Test {


   @Test
   @DisplayName("读取 yaml 对象")
   public  void  yamlReadOne() {
       InputStream steam = new ClassPathResource("/test.yaml").getStream();

       Yaml yaml = new Yaml();

       // Customer ret = yaml.loadAs(steam, Customer.class) ;

       Person ret = yaml.loadAs(steam, Person.class) ;


       System.out.println(ret);
   }

}

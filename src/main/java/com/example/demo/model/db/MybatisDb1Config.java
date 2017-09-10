package com.example.demo.model.db;

import com.example.db1.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by donghao04 on 2017/7/28.
 */
@Configuration
@MapperScan(basePackageClasses = UserMapper.class, sqlSessionFactoryRef = "sqlSessionFactory1")
public class MybatisDb1Config {
    @Autowired
    @Qualifier("ds1")
    private DataSource dataSource1;

    @Bean
    public SqlSessionFactory sqlSessionFactory1() throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource1);

        return factoryBean.getObject();
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate1() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory1());
        return template;
    }
}

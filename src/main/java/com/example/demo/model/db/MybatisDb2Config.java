package com.example.demo.model.db;

import com.example.db2.UserInfoMapper;
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
@MapperScan(basePackageClasses = UserInfoMapper.class, sqlSessionFactoryRef = "sqlSessionFactory2")
public class MybatisDb2Config {

    @Autowired
    @Qualifier("ds2")
    private DataSource dataSource2;

    @Bean
    public SqlSessionFactory sqlSessionFactory2() throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource2);

        return factoryBean.getObject();
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate2() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory2());
        return template;
    }
}

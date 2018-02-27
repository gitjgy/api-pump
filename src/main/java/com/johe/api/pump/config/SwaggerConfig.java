package com.johe.api.pump.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger-ui的配置
 * @author ScienJus
 * @date 2015/7/10.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/** 
     * 创建API应用 
     * apiInfo() 增加API相关信息 
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现， 
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。 
     *  
     * @return 
     */  
    @Bean  
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("Authorization").description("Bearer <your token>").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
        return new Docket(DocumentationType.SWAGGER_2)  
                .apiInfo(apiInfo())  
                .select()  
                .apis(RequestHandlerSelectors.basePackage("com.johe.api.pump")) //swagget扫描目录  
                .paths(PathSelectors.any())  
                .build()
                .globalOperationParameters(pars);  
    }  
      
    /** 
     * 创建该API的基本信息（这些基本信息会展现在文档页面中） 
     * 访问地址：http://项目实际地址/swagger-ui.html 
     * @return 
     */  
    private ApiInfo apiInfo() {  
        return new ApiInfoBuilder()  
                .title("泵站信息管理平台,PDA相关 APIs")  
                .description("此API将会迭代发布，更多接口，敬请期待!")  
                .termsOfServiceUrl("johsoft.com")  
                .contact("Jianggy")  
                .version("1.0")  
                .build();  
    }  
}

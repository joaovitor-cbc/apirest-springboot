package com.apirest.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*Anotar uma classe com @Configuration indica que a classe pode 
 *ser usada pelo contêiner Spring IoC como uma fonte de definições 
 *de bean. A anotação @Bean diz ao Spring que um método anotado com 
 *@Bean retornará um objeto que deve ser registrado como um bean no 
 *contexto do aplicativo Spring. A classe @Configuration mais simples 
 *possível seria a seguinte  
 */
@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

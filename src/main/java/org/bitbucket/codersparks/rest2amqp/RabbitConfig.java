package org.bitbucket.codersparks.rest2amqp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.bitbucket.codersparks.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	private static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean
	public Marshaller marshaller() {
		try {
			return JAXBContext.newInstance(Data.class).createMarshaller();
		}catch(JAXBException e) {
			logger.error("Exception caught when creating data marshaller", e);
			return null;
		}
	}
	
	@Bean
	public Unmarshaller unmarshaller() {
		try {
			return JAXBContext.newInstance(Data.class).createUnmarshaller();
		} catch (JAXBException e) {
			logger.error("Exceptioncaught when creating data unmarshaller", e);
			return null;
		}
	}
	

	
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("amq.topic");
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory);
	}
	
}

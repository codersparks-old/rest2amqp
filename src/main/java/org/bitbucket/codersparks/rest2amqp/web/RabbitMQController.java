package org.bitbucket.codersparks.rest2amqp.web;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.bitbucket.codersparks.model.Data;
import org.bitbucket.codersparks.rest2amqp.exception.NoMessageException;
import org.bitbucket.codersparks.rest2amqp.exception.QueueAlreadyExistsException;
import org.bitbucket.codersparks.rest2amqp.exception.QueueNameException;
import org.bitbucket.codersparks.rest2amqp.exception.QueueNotFoundException;
import org.bitbucket.codersparks.rest2amqp.exception.UnknowMessageTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQController.class);

	@Autowired
	private Marshaller marshaller;
	
	@Autowired
	private Unmarshaller unmarshaller;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private TopicExchange exchange;
	
	private String generateRoutingKey(AbstractExchange exchange, String queueName) {
		return exchange.getName() + "_" + queueName;
	}
	
	@RequestMapping(
			value="/queue/{name}",
			method=RequestMethod.PUT
			)
	public ResponseEntity<Object> createQueue(@PathVariable String name) throws QueueAlreadyExistsException, QueueNameException {
		
		logger.info("Create queue called, name: " + name);
		
		if(name == null || name.length() < 3) {
			String error = "Queue name is null or less than 3 characters, name: " +name;
			logger.error(error);
			throw new QueueNameException(error);
		}
		
		Properties properties = rabbitAdmin.getQueueProperties(name);
		
		if(properties != null) {
			String error = "Queue with name: " + name + " already exists";
			logger.error(error);
			throw new QueueAlreadyExistsException(error);
		}
		
		Queue queue = new Queue(name, true, false, false);
		
		Binding binding = BindingBuilder.bind(queue).to(exchange).with(generateRoutingKey(exchange, queue.getName()));
		rabbitAdmin.declareQueue(queue);
		rabbitAdmin.declareBinding(binding);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
		
	}
	
	
	@RequestMapping(
				value="/queue/{name}",
				method=RequestMethod.DELETE
			)
	public ResponseEntity<Object> deleteQueue(@PathVariable String name) throws QueueNotFoundException {
		
		logger.info("Delete queue called with name: " + name);
		
		Properties properties = rabbitAdmin.getQueueProperties(name);
		
		if(properties == null) {
			String error = "Queue with name: " + name + " does not exist";
			logger.error(error);
			throw new QueueNotFoundException(error);
		}
		
		rabbitAdmin.deleteQueue(name);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(
			value="/queue/{name}",
			consumes={
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_XML_VALUE
			},
			method=RequestMethod.POST
		)
	public ResponseEntity<Object> addItemToQueue(@PathVariable String name, @RequestBody Data data) throws QueueNameException, QueueNotFoundException, JAXBException {
		
		logger.info("Add item to queue called with queue name: "+ name+ " and data: " + data.toString());
		
		if(name == null || name.length() < 3) {
			String error = "Queue name is null or less than three characters: " + name;
			logger.error(error);
			throw new QueueNameException(error);
		}
		
		Properties properties = rabbitAdmin.getQueueProperties(name);
		
		if(properties == null) {
			String error = "Cannot add data to non-existing queue: " + name;
			logger.error(error);
			throw new QueueNotFoundException("Cannot find queue with name: " + name);
		}
		
		//rabbitTemplate.convertAndSend(generateRoutingKey(exchange, name), data);
		
		StringWriter xmlWriter = new StringWriter();
		marshaller.marshal(data, xmlWriter);
		String dataXml = xmlWriter.toString();
		logger.info("Sending data to exchange: " + exchange.getName() + " with data: " + dataXml);
		rabbitTemplate.convertAndSend(exchange.getName(), generateRoutingKey(exchange, name), dataXml);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
		
	}
	
	
	@RequestMapping(
				value="/queue/{name}",
				produces={
						MediaType.APPLICATION_JSON_VALUE,
						MediaType.APPLICATION_XML_VALUE,
						MediaType.TEXT_XML_VALUE
						
				},
				method=RequestMethod.GET
			)
	public ResponseEntity<Data> getItemFromQueue(@PathVariable String name) throws QueueNotFoundException, JAXBException, NoMessageException, UnknowMessageTypeException {
		
		ResponseEntity<Data> response;
		
		logger.info("Attempting to receive item from queue");
		
		Properties properties = rabbitAdmin.getQueueProperties(name);
		if(properties == null) {
			String error = "Cannot find queue with name: " + name;
			logger.error(error);
			throw new QueueNotFoundException(error);
		}
		
		int messageCount = Integer.parseInt(properties.get("QUEUE_MESSAGE_COUNT").toString());
		
		if(messageCount > 0) {
			rabbitTemplate.setQueue(name);
			Object message = rabbitTemplate.receiveAndConvert();
			
			if(message instanceof String) {
				logger.debug("Message is of type string");
				String dataString = (String) message;
				
				logger.debug("Value of string: " + dataString);
				Data data = (Data) unmarshaller.unmarshal(new ByteArrayInputStream(dataString.getBytes()));
				
				response = new ResponseEntity<Data>(data, HttpStatus.OK);
			} else {
				throw new UnknowMessageTypeException("Message is of unknown type: " + message.getClass());
			}
		} else {
			logger.warn("No messages to receive, throwing NoMessageException");
			throw new NoMessageException("No messages to receive");
		}
		
		return response;
	}

}

//package com.notmorron.orderserver.broker.config;
//
//import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.core.JmsTemplate;
//
//@Configuration
//class ActiveMqConfiguration {
//
//    @Value("${spring.artemis.host}")
//    private String host;
//
//    @Value("${spring.artemis.port}")
//    private String port;
//
//    @Value("${spring.artemis.user}")
//    private String username;
//
//    @Value("${spring.artemis.password}")
//    private String password;
//
//    @Bean
//    public ActiveMQJMSConnectionFactory artemisConnectionFactory() {
//        String uri = "tcp://" + host + ":" + port;
//        return new ActiveMQJMSConnectionFactory(uri, username, password);
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(artemisConnectionFactory());
//        return jmsTemplate;
//    }
//}
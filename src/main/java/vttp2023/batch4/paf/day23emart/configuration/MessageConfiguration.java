package vttp2023.batch4.paf.day23emart.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import vttp2023.batch4.paf.day23emart.services.PurchaseOrderEventSubscriber;

@Configuration
public class MessageConfiguration {

    @Bean
    JedisConnectionFactory JedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean("poPubSub")
    ChannelTopic topic() {
        return new ChannelTopic("po-channel");
    }
    
    @Autowired
    PurchaseOrderEventSubscriber poSubscriber;

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter();
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(JedisConnectionFactory());
        container.addMessageListener(poSubscriber, topic());
        return container;

    }
}

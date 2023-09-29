package br.com.siecola.aws_project01.services;

import br.com.siecola.aws_project01.enuns.EventType;
import br.com.siecola.aws_project01.model.Envelope;
import br.com.siecola.aws_project01.model.Product;
import br.com.siecola.aws_project01.model.ProductEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductPublisher {

    private static final Logger log = LoggerFactory.getLogger(ProductPublisher.class);

    @Autowired
    private AmazonSNS sns;

    @Autowired
    @Qualifier("productEventsTopic")
    private Topic topic;

    @Autowired
    private ObjectMapper objectMapper;


    public void publishProductEvent(Product product, EventType type, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductCode(product.getCode());
        productEvent.setProductId(product.getId());
        productEvent.setUsername(username);

        Envelope envelope = new Envelope();
        envelope.setType(type);
        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));
            String messageJson = objectMapper.writeValueAsString(envelope);
            PublishResult publish = sns.publish(topic.getTopicArn(), objectMapper.writeValueAsString(envelope));
            log.info("Event published: Message id: {} - Message - {}", publish.getMessageId(), messageJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to create product event message");
        }


    }

}

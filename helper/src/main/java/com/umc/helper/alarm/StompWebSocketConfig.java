package com.umc.helper.alarm;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 소켓에 연결하기 위한 엔드포인트 지정
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/team")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /*어플리케이션 내부에서 사용할 path를 지정할 수 있음*/
    // /pub로 시작하는 주소를 구독한 Subscriber들에게 메시지를 전달하도록 한다. setApplicationDestinationPrefixes는 클라이언트가 서버로 메시지를 발송할 수 있는 경로의 prefix를 지정한다.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); //Client에서 SEND 요청을 처리 -> /pub로 시작하는 STOMP 메시지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅됨.
        registry.enableSimpleBroker("/sub"); // /sub로 시작하는 "destination" 헤더를 가진 메시지를 브로커로 라우팅. 해당 경로로 SimpleBroker를 등록. SimpleBroker는 해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행
    }


}

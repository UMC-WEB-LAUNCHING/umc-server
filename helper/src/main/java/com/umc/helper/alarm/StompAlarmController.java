package com.umc.helper.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompAlarmController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/team/enter"
    @MessageMapping(value = "/team/enter")
    public void enter(Message message){
        message.setPopup_message(message.getMemberName() + "님이 "+message.getName()+"을(를) "+message.getTeamName()+"-"+message.getFolderName()+"폴더에 추가했습니다.");
        template.convertAndSend("sub/team/alarm/");
    }

    @MessageMapping(value = "/chat/message")
    public void message(Message message){
        //template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}

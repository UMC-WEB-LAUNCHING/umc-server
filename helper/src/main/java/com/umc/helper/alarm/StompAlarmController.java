package com.umc.helper.alarm;

import com.umc.helper.alarm.model.Message;
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
    // 클라이언트에서 메시지를 받음.
    @MessageMapping(value = "/team/enter")
    public void enter(Message message){
        if(message.getLastModifiedDate().isEqual(message.getUploadDate())){
            // 업로드
            message.setPopup_message(message.getMemberName() + "님이 "+message.getName()+"을(를) "+message.getTeamName()+"-"+message.getFolderName()+"폴더에 추가했습니다.");
        }
        else{
            message.setPopup_message(message.getMemberName() + "님이 "+message.getTeamName()+"-"+message.getFolderName()+"폴더의 "+message.getName()+"을(를) 수정했습니다.");
        }
        template.convertAndSend("sub/team/alarm/"+message.getTeamId(),message); // /sub/team/alarm/팀ID를 구독한 유저에게 해당 메시지를 보냄.
    }

    @MessageMapping(value = "/chat/message")
    public void message(Message message){
        //template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}

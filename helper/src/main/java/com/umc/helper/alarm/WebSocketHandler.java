//package com.umc.helper.alarm;
//
//import com.google.gson.Gson;
//import com.umc.helper.folder.FolderRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class WebSocketHandler extends TextWebSocketHandler {
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
//    //로그인 한 인원 전체
//    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
//    // 1:1로 할 경우
//    private Map<String, WebSocketSession> userSessionsMap = new HashMap<String, WebSocketSession>();
//
//    private final FolderRepository folderRepository;
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {//클라이언트와 서버가 연결
//        // TODO Auto-generated method stub
//        logger.info("Socket 연결");
//        sessions.add(session);
//        logger.info(currentUserName(session));//현재 접속한 사람
//        String senderId = currentUserName(session);
//        userSessionsMap.put(senderId,session);
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {// 메시지
//        // TODO Auto-generated method stub
//        logger.info("ssesion"+currentUserName(session));
//
//        Gson gson=new Gson();
//
//        Message msg=gson.fromJson(message.getPayload(),Message.class);
//        Long teamId=folderRepository.findById(msg.getFolderId()).getTeam().getTeamIdx();
//
//        //List<WebSocketSession> receiver=userSessionsMap.get
//
//
//
//
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {//연결 해제
//        // TODO Auto-generated method stub
//        logger.info("Socket 끊음");
//        sessions.remove(session);
//        userSessionsMap.remove(currentUserName(session),session);
//    }
//
//
//    private String currentUserName(WebSocketSession session) {
//        Map<String, Object> httpSession = session.getAttributes();
//        MemberVO loginUser = (MemberVO)httpSession.get("login");
//
//        if(loginUser == null) {
//            String mid = session.getId();
//            return mid;
//        }
//        String mid = loginUser.getMemberId();
//        return mid;
//
//    }
//}

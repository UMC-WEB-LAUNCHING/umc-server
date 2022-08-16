package com.umc.helper.alarm;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {

    private Long memberId;
    private String memberName;
    private Long folderId;
    private String folderName;
    private LocalDateTime uploadDate;
    private String category; // memo, file, image, link
    private Long teamId;
    private String teamName;
    private Long id; // 아이템 idx
    private String name; //아이템 이름
    private String popup_message;
}

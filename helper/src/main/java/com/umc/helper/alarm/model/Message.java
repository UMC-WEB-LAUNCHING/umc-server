package com.umc.helper.alarm.model;

import com.umc.helper.file.model.GetFilesResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message implements Comparable<Message>{

    private Long memberId; // 팀 내 폴더에 업로드한 사람 idx
    private String memberName; // 팀 내 폴더에 업로드한 사람 이름
    private Long folderId; // 팀 내 폴더 idx
    private String folderName; // 팀 내 폴더 이름
    private LocalDateTime uploadDate; // 업로드 시간
    private LocalDateTime lastModifiedDate; // 최근 수정 시간
    private String category; // memo, file, image, link
    private Long teamId; // 팀 idx
    private String teamName; // 팀 이름
    private Long id; // 아이템 idx
    private String name; //아이템 이름
    private String popup_message;

    @Override
    public int compareTo(Message Message){
        System.out.println("Message: "+Message.getLastModifiedDate());

        if(Message.getLastModifiedDate().compareTo(this.getLastModifiedDate())==1){
            return 1;
        }
        else if(Message.getLastModifiedDate().compareTo(this.getLastModifiedDate())==-1){
            return -1;
        }
        return 0;
    }
}

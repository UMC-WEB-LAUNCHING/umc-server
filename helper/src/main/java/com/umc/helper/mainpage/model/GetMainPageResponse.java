package com.umc.helper.mainpage.model;

import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.team.model.GetTeamsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GetMainPageResponse {
    private GetTeamsResponse teamList;
    private List<GetFoldersResponse> myFolderList;
    private List<GetFoldersResponse> teamFolderList;
    private List<GetFoldersResponse> allFolderList;
    //private List<GetFilesResponse> allFileList;
    private List<GetItemResponse> allItemList;
    private Long memberId;
    private String memberName;
}

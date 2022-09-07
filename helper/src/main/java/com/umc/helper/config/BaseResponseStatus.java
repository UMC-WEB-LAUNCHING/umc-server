package com.umc.helper.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    METHOD_NOT_ALLOWED(false,2004,"http method를 확인해주세요"),

    // member
    // 로그인
    USERS_EMPTY_USER_EMAIL(false, 2010, "유저 이메일 값을 확인해주세요."),
    USERS_EMPTY_USER_PASSWORD(false,2011,"유저 비밀번호 값을 확인해주세요"),
    USER_EMPTY(false,2012,"존재하지 않는 유저입니다."),

    // 회원가입
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2017, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_NAME(false, 2018, "이름을 입력해주세요."),


    POST_USERS_EXISTS_EMAIL(false,2019,"중복된 이메일입니다."),

    // folder
    FOLDER_EMPTY(false,2020,"존재하지 않는 폴더입니다"),
    INVALID_UPLOADER(false,2021,"폴더에 접근 권한이 없는 유저입니다."),
    EXIST_FOLDER_NAME(false,2022,"중복된 폴더 이름입니다."),
    // file
    FILE_EMPTY(false,2025,"존재하지 않는 파일입니다."),
    EXIST_FILE_NAME(false,2026,"중복된 파일 이름입니다."),

    // team member
    TEAM_MEMBER_EMPTY(false,2030,"존재하지 않는 팀원입니다."),

    // image
    IMAGE_EMPTY(false,2033,"존재하지 않는 이미지입니다."),
    EXIST_IMAGE_NAME(false,2034,"중복된 이미지 이름입니다."),

    // link
    LINK_EMPTY(false,2036,"존재하지 않는 링크입니다."),
    EXIST_LINK_NAME(false,2037,"중복된 링크 이름입니다."),

    // memo
    MEMO_EMPTY(false,2040,"존재하지 않는 메모입니다."),
    MEMO_SIZE_EXCEED(false,2041,"글자 수 500자를 초과했습니다."),
    EXIST_MEMO_NAME(false,2042,"중복된 메모 이름입니다."),

    // team
    INVALID_DELETE_TEAM(false,2044,"팀을 삭제할 수 없는 유저입니다."),
    TEAM_EMPTY(false,2045,"존재하지 않는 팀입니다."),
    TEAM_NAME_EMPTY(false,2046,"팀 이름을 입력해주세요."),
    //bookmark
    BOOKMARK_EMPTY(false,2050,"존재하지 않는 북마크입니다."),

    //trash
    RESTORE_INVALID_USER(false,2055,"복구 권한이 없는 유저입니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

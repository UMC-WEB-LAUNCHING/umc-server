package com.umc.helper.mainpage;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.mainpage.model.GetMainPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/main/{memberId}")
    public BaseResponse<GetMainPageResponse> getMainPage(@PathVariable("memberId")Long memberId){

        GetMainPageResponse result=mainPageService.getMainPage(memberId);

        return new BaseResponse<>(result);
    }
}

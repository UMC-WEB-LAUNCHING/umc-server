package com.umc.helper.search;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.search.model.GetSearchedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public BaseResponse<List<GetSearchedResponse>> getSearchItems(@RequestParam String word,@RequestParam Long memberId){
        List<GetSearchedResponse> getSearchedRes=searchService.getSearchItems(word,memberId);

        return new BaseResponse<>(getSearchedRes);
    }

}

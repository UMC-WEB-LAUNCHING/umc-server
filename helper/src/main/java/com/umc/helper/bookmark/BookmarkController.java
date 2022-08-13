package com.umc.helper.bookmark;

import com.umc.helper.bookmark.model.GetBookmarksResponse;
import com.umc.helper.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private Logger logger= LoggerFactory.getLogger(BookmarkController.class);

    private final BookmarkService bookMarkService;

    /**
     * delete bookmark
     * @param bookmarkId
     * @return result
     */
    @DeleteMapping("/bookmark/{bookmarkId}")
    public BaseResponse<String> deleteBookmark(@PathVariable("bookmarkId") @Valid Long bookmarkId){
        String result=bookMarkService.deleteBookmark(bookmarkId);

        return new BaseResponse<>(result);
    }

    /**
     * retreive bookmarks
     * @param memberId
     * @return getBookmarksRes
     */
    @GetMapping("/bookmark/{memberId}")
    public BaseResponse<List<GetBookmarksResponse>> getBookmarks(@PathVariable("memberId") @Valid Long memberId){
        List<GetBookmarksResponse> getBookmarksRes=bookMarkService.retrieveBookmarks(memberId);

        return new BaseResponse<>(getBookmarksRes);
    }
}

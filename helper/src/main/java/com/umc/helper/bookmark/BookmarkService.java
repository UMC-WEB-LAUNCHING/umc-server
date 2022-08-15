package com.umc.helper.bookmark;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.GetBookmarksResponse;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;


    /**
     *  북마크 삭제
     */
    @Transactional
    public String deleteBookmark(Long bookmarkId){
        Bookmark bookmark=bookmarkRepository.findById(bookmarkId);
        bookmark.notExistBookmark();

        bookmarkRepository.remove(bookmark);

        return "북마크 삭제 성공";
    }

    /**
     * 북마크 조회
     */
    @Transactional
    public List<GetBookmarksResponse> retrieveBookmarks(Long memberId){
        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        List<Bookmark> bookmarks=bookmarkRepository.findAllByMemberId(memberId);
        List<GetBookmarksResponse> result=bookmarks.stream()
                .map(bm->new GetBookmarksResponse(bm))
                .collect(toList());
        Collections.sort(result);

        return result;
    }
}

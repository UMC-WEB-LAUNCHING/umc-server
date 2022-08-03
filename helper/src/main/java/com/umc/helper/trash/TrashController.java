package com.umc.helper.trash;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.trash.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrashController {

    private final TrashService trashService;

    Logger logger= LoggerFactory.getLogger(TrashController.class);

    /**
     * retrieve trash - 휴지통 조회
     * @param memberId
     * @return getTrash
     */
    @GetMapping("trash/{memberId}")
    public BaseResponse<List<GetTrashResponse>> getTrash(@PathVariable("memberId") Long memberId){

        List<GetTrashResponse> getTrash=trashService.retrieveTrash(memberId);

        return new BaseResponse<>(getTrash);

    }

    /**
     * 휴지통 영구 삭제 - 선택항목
     * delete selected items
     * @param deleteItemReqs
     * @return
     */
    @DeleteMapping("trash/item")
    public BaseResponse<List<DeleteItemsResponse>> deleteItem(@RequestBody DeleteItemsRequestList deleteItemReqs){
        List<DeleteItemsResponse> deletedItems=trashService.deleteItems(deleteItemReqs);

        return new BaseResponse<>(deletedItems);
    }

    /**
     * 휴지통 영구 삭제 - 전체 삭제
     * delete all items
     * @param memberId
     * @return
     */
    @DeleteMapping("trash/{memberId}")
    public BaseResponse<DeleteAllResponse> deleteAll(@PathVariable("memberId") Long memberId){
        DeleteAllResponse deletedAll=trashService.deleteAll(memberId);

        return new BaseResponse<>(deletedAll);
    }

    /**
     * 휴지통 항목 복구
     * restore item
     * @param itemCategory
     * @param itemId
     * @return patchRestoreItemRes
     */
    @PatchMapping("trash/restore/{itemCategory}/{itemId}/{memberId}")
    public BaseResponse<PatchRestoreItemResponse> restoreItem(@PathVariable("itemCategory") String itemCategory, @PathVariable("itemId") Long itemId,@PathVariable("memberId") Long memberId){
        PatchRestoreItemResponse patchRestoreItemRes=trashService.restoreItem(itemCategory,itemId);

        return new BaseResponse<>(patchRestoreItemRes);
    }
}

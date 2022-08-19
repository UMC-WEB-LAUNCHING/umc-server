package com.umc.helper.trash;

import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.trash.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public BaseResponse<List<GetTrashResponse>> getTrash(@PathVariable("memberId") @Valid Long memberId){
        List<GetTrashResponse> getTrash=trashService.retrieveTrash(memberId);
        logger.info(">retrieved trash");

        return new BaseResponse<>(getTrash);

    }

    /**
     * 휴지통 영구 삭제 - 선택항목
     * delete selected items
     * @param deleteItemReqs
     * @return
     */
    @DeleteMapping("trash/item")
    public BaseResponse<List<DeleteItemsResponse>> deleteItems(@RequestBody @Valid DeleteItemsRequestList deleteItemReqs){
        List<DeleteItemsResponse> deletedItems=trashService.deleteItems(deleteItemReqs);
        logger.info(">delete selected trash");

        return new BaseResponse<>(deletedItems);
    }
    @DeleteMapping("trash/delete/{item_case}/{itemId}/{memberId}")
    public BaseResponse<DeleteItemResponse> deleteItem(@PathVariable("item_case") String item_case, @PathVariable("itemId") Long itemId, @PathVariable("memberId") Long memberId){
        logger.info("item_case: {}",item_case);
        DeleteItemResponse deletedItem=trashService.deleteItem(item_case,itemId,memberId);

        return new BaseResponse<>(deletedItem);
    }

    /**
     * 휴지통 영구 삭제 - 전체 삭제
     * delete all items
     * @param memberId
     * @return
     */
    @DeleteMapping("trash/{memberId}")
    public BaseResponse<DeleteAllResponse> deleteAll(@PathVariable("memberId") @Valid Long memberId){
        DeleteAllResponse deletedAll=trashService.deleteAll(memberId);
        logger.info(">delete all trash");
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
    public BaseResponse<PatchRestoreItemResponse> restoreItem(@PathVariable("itemCategory") @Valid String itemCategory, @PathVariable("itemId") @Valid Long itemId,@PathVariable("memberId") @Valid Long memberId){
        PatchRestoreItemResponse patchRestoreItemRes=trashService.restoreItem(itemCategory,itemId,memberId);

        logger.info("restored trash item");
        return new BaseResponse<>(patchRestoreItemRes);
    }
}

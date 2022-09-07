package com.umc.helper.trash;

import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.file.FileRepository;
import com.umc.helper.file.FileService;
import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.image.ImageRepository;
import com.umc.helper.image.ImageService;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.link.model.Link;
import com.umc.helper.mainpage.model.GetItemResponse;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.member.model.Member;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.memo.model.Memo;
import com.umc.helper.trash.exception.RestoreInvalidUser;
import com.umc.helper.trash.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashService {

    private final MemoRepository memoRepository;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;
    private final LinkRepository linkRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    private final FolderRepository folderRepository;

    private final FileService fileService;
    private final ImageService imageService;
    Logger logger= LoggerFactory.getLogger(TrashController.class);

    /**
     * 휴지통 조회
     */
    @Transactional
    public List<GetTrashResponse> retrieveTrash(Long memberId){

        List<GetTrashResponse> allTrash=new ArrayList<>();

        List<Memo> getMemoTrash=memoRepository.findTrashByMemberId(memberId);
        for(Memo memo:getMemoTrash){
            GetMemosResponse m=new GetMemosResponse(memo);
            allTrash.add(new GetTrashResponse(memo.getStatusModifiedDate(),"memo",m,null,null,null,null));
        }
        logger.info("trash memo size: {}",allTrash.size());
        List<File> getFileTrash=fileRepository.findTrashByMemberId(memberId);
        for(File file:getFileTrash){
            GetFilesResponse f=new GetFilesResponse(file);
            allTrash.add(new GetTrashResponse(file.getStatusModifiedDate(),"file",null,f,null,null,null));
        }

        List<Image> getImageTrash=imageRepository.findTrashByMemberId(memberId);
        for(Image image:getImageTrash){
            GetImagesResponse i=new GetImagesResponse(image);
            allTrash.add(new GetTrashResponse(image.getStatusModifiedDate(),"image",null,null,null,i,null));
        }

        List<Link> getLinkTrash=linkRepository.findTrashByMemberId(memberId);
        for(Link link:getLinkTrash){
            GetLinksResponse l=new GetLinksResponse(link);
            allTrash.add(new GetTrashResponse(link.getStatusModifiedDate(),"link",null,null,l,null,null));
        }

        List<Folder> getFolderTrash=folderRepository.findTrashByMemberId(memberId);
        for(Folder folder:getFolderTrash){
            GetFoldersResponse f=new GetFoldersResponse(folder);
            allTrash.add(new GetTrashResponse(folder.getStatusModifiedDate(),"folder",null,null,null,null,f));
        }
        logger.info("allTrash size: {}",allTrash.size());
        Collections.sort(allTrash);

        return allTrash;
    }

    /**
     *  휴지통 선택된 항목 삭제
     */
    @Transactional
    public List<DeleteItemsResponse> deleteItems(DeleteItemsRequestList deleteItemReqs){
        List<DeleteItemsRequest> items=deleteItemReqs.getItems();
        List<DeleteItemsResponse> result=new ArrayList<>();
        for(DeleteItemsRequest item:items){
           String category = null;
           Long id = null;
           Long memberId = null;
           // TODO: 중복코드 리팩토링 필요
            if(item.getCategory().equals("file")){
                File file=fileRepository.findById(item.getId());
                category="file";
                id=file.getId();
                memberId=file.getMember().getId();
                bookmarkRepository.removeBookmarkFile(file.getId(),memberId);
                fileService.deleteFromS3(file.getFileName());
                fileRepository.remove(file);
            }
            else if(item.getCategory().equals("image")){
                Image image=imageRepository.findById(item.getId());
                category="image";
                id=image.getId();
                memberId=image.getMember().getId();
                bookmarkRepository.removeBookmarkImage(image.getId(),memberId);
                imageService.deleteFromS3(image.getFileName());
                imageRepository.remove(image);
            }
            else if(item.getCategory().equals("memo")){
                Memo memo=memoRepository.findById(item.getId());
                category="memo";
                id=memo.getId();
                memberId=memo.getMember().getId();
                bookmarkRepository.removeBookmarkMemo(memo.getId(),memberId);
                memoRepository.remove(memo);
            }
            else if(item.getCategory().equals("link")){
                Link link=linkRepository.findById(item.getId());
                category="link";
                id=link.getId();
                memberId=link.getMember().getId();
                logger.info("deleting link");
                bookmarkRepository.removeBookmarkLink(link.getId(),memberId);
                linkRepository.remove(link);
            }
            else if(item.getCategory().equals("folder")){
                Folder folder=folderRepository.findById(item.getId());
                category="folder";
                id=folder.getId();
                memberId=folder.getCreatorId();
                bookmarkRepository.removeBookmarkFolder(folder.getId(),memberId);
                memoRepository.removeEveryByFolderId(folder.getId());
                fileRepository.removeEveryByFolderId(folder.getId());
                linkRepository.removeEveryByFolderId(folder.getId());
                imageRepository.removeEveryByFolderId(folder.getId());

                folderRepository.remove(folder);
            }
            result.add(new DeleteItemsResponse(category,id,memberId));
        }

        return result;
    }

    /**
     *  휴지통 단일 선택 항목 삭제
     */
    @Transactional
    public DeleteItemResponse deleteItem(String item_case,Long itemId,Long memberId){
        logger.info("item_case: {}",item_case);
        Member member=memberRepository.findById(memberId).get();
        String category = null;
        Long id = null;
        String name=null;
        if(item_case.equals("folder")){
            Folder folder=folderRepository.findById(itemId);
            category="folder";
            id=folder.getId();
            name=folder.getFolderName();
            bookmarkRepository.removeBookmarkFolder(folder.getId(),memberId);
            memoRepository.removeEveryByFolderId(folder.getId());
            fileRepository.removeEveryByFolderId(folder.getId());
            linkRepository.removeEveryByFolderId(folder.getId());
            imageRepository.removeEveryByFolderId(folder.getId());
        }
        else if(item_case.equals("file")){
            File file=fileRepository.findById(itemId);
            category="file";
            id=file.getId();
            name= file.getOriginalFileName();
            bookmarkRepository.removeBookmarkFile(file.getId(),memberId);
            fileService.deleteFromS3(file.getFileName());
            fileRepository.remove(file);
        }
        else if(item_case.equals("image")){
            Image image=imageRepository.findById(itemId);
            category="image";
            id=image.getId();
            name=image.getOriginalFileName();
            bookmarkRepository.removeBookmarkImage(image.getId(),memberId);
            imageService.deleteFromS3(image.getFileName());
            imageRepository.remove(image);
        }
        else if(item_case.equals("link")){
            logger.info("item case is link");
            Link link=linkRepository.findById(itemId);
            category="link";
            id=link.getId();
            name=link.getName();
            bookmarkRepository.removeBookmarkLink(link.getId(),memberId);
            linkRepository.remove(link);
        }
        else if(item_case.equals("memo")){
            Memo memo=memoRepository.findById(itemId);
            category="memo";
            id=memo.getId();
            name=memo.getName();
            bookmarkRepository.removeBookmarkMemo(memo.getId(),memberId);
            memoRepository.remove(memo);
        }

        return new DeleteItemResponse(category,id,name);
    }

    /**
     *  휴지통 전체 삭제
     */
    @Transactional
    public DeleteAllResponse deleteAll(Long memberId){
        //TODO: 코드 리팩토링 필요

        // 북마크된 메모 삭제 후 메모 영구 삭제
        List<Memo> deletedMemo=memoRepository.findTrashByMemberId(memberId);
        for(Memo m:deletedMemo){
            Long memoId=m.getId();
            bookmarkRepository.removeBookmarkMemo(memoId,memberId);
        }
        int memoDeletedCount=memoRepository.removeTrashByMemberId(memberId);

        // 북마크된 링크 삭제 후 링크 영구 삭제
        List<Link> deletedLink=linkRepository.findTrashByMemberId(memberId);
        for(Link l:deletedLink){
            Long linkId=l.getId();
            bookmarkRepository.removeBookmarkLink(linkId,memberId);
        }
        int linkDeletedCount=linkRepository.removeTrashByMemberId(memberId);

        // 북마크된 이미지 삭제 후 이미지 영구 삭제
        List<Image> deletedImage=imageRepository.findTrashByMemberId(memberId);
        for(Image i:deletedImage){
            Long imageId=i.getId();
            bookmarkRepository.removeBookmarkImage(imageId,memberId);
            imageService.deleteFromS3(i.getFileName());
        }
        int imageDeletedCount=imageRepository.removeTrashByMemberId(memberId);

        // 북마크된 파일 삭제 후 파일 영구 삭제
        List<File> deletedFile=fileRepository.findTrashByMemberId(memberId);
        for(File f:deletedFile){
            Long fileId=f.getId();
            bookmarkRepository.removeBookmarkFile(fileId,memberId);
            fileService.deleteFromS3(f.getFileName());
        }
        int fileDeletedCount=fileRepository.removeTrashByMemberId(memberId);

        // 북마크된 폴더 삭제 후 폴더 영구 삭제
        List<Folder> deletedFolder=folderRepository.findTrashByMemberId(memberId);
        for(Folder f:deletedFolder){
            Long folderId=f.getId();
            bookmarkRepository.removeBookmarkFolder(folderId,memberId);
            memoRepository.removeEveryByFolderId(folderId);
            fileRepository.removeEveryByFolderId(folderId);
            linkRepository.removeEveryByFolderId(folderId);
            imageRepository.removeEveryByFolderId(folderId);
        }
        int folderDeletedCount=folderRepository.removeTrashByMemberId(memberId);


        return new DeleteAllResponse(memoDeletedCount,linkDeletedCount,imageDeletedCount,fileDeletedCount,folderDeletedCount);
    }

    /**
     * 휴지통 항목 복구
     */
    @Transactional
    public PatchRestoreItemResponse restoreItem(String itemCategory,Long itemId,Long memberId){

        Member member=memberRepository.findById(memberId).get();
        if(member.getId()!=memberId) throw new RestoreInvalidUser(); // 휴지통 항목 복구 권한 확인

        if(itemCategory.equals("file")){
            File file=fileRepository.findById(itemId);
            file.setStatusModifiedDate(LocalDateTime.now());
            file.setStatus(Boolean.TRUE);
        }
        else if(itemCategory.equals("image")){
            Image image=imageRepository.findById(itemId);
            image.setStatusModifiedDate(LocalDateTime.now());
            image.setStatus(Boolean.TRUE);
        }
        else if(itemCategory.equals("memo")){
            Memo memo=memoRepository.findById(itemId);
            memo.setStatusModifiedDate(LocalDateTime.now());
            memo.setStatus(Boolean.TRUE);
        }
        else if(itemCategory.equals("link")){
            Link link=linkRepository.findById(itemId);
            link.setStatusModifiedDate(LocalDateTime.now());
            link.setStatus(Boolean.TRUE);
        }
        else if(itemCategory.equals("folder")){
            Folder folder=folderRepository.findById(itemId);
            folder.setStatusModifiedDate(LocalDateTime.now());
            folder.setStatus(Boolean.TRUE);
        }

        return new PatchRestoreItemResponse(itemCategory,itemId);
    }

}

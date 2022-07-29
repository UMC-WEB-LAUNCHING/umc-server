package com.umc.helper.folder;

import com.umc.helper.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder,Long> {


}

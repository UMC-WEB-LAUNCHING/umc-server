package com.umc.helper.link;

import com.umc.helper.link.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepositoryJpa extends JpaRepository<Link, Long> {


}

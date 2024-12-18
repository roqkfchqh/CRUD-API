package com.example.crud.infrastructure.persistence;

import com.example.crud.domain.board_root.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable);
}

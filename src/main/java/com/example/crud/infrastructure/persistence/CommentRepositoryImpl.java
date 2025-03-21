package com.example.crud.infrastructure.persistence;

import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.entities.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable){
        QComment comment = QComment.comment;

        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetchOne();

        return PageableExecutionUtils.getPage(comments, pageable, () -> total);
    }
}

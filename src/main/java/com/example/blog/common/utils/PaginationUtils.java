package com.example.blog.common.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class PaginationUtils {

    public static Pageable buildPageable(int pageIndex, int pageSize, Sort sort) {
        return PageRequest.of(
                pageIndex - 1, // Spring Data 分页从0开始
                pageSize,
                sort
        );
    }

    public static Map<String, Object> buildPageResult(Page<?> page) {
        return Map.of(
                "data", page.getContent(),
                "pageSize", page.getSize(),
                "pageIndex", page.getNumber() + 1,
                "pageCount", page.getTotalPages(),
                "total", page.getTotalElements()
        );
    }
}

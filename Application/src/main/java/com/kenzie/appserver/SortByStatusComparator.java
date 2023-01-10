package com.kenzie.appserver;


import com.kenzie.appserver.controller.model.BookmarkResponse;

import java.util.Comparator;

public class SortByStatusComparator implements Comparator<BookmarkResponse> {
    @Override
    public int compare(BookmarkResponse o1, BookmarkResponse o2) {
        return o1.getReadStatus().compareTo(o2.getReadStatus());
    }
}

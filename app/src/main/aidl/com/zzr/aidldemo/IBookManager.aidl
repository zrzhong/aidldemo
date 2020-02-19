// IBookManager.aidl
package com.zzr.aidldemo;
import com.zzr.aidldemo.Book;

// Declare any non-default types here with import statements

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);
}

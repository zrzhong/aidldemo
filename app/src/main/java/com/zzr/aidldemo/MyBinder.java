package com.zzr.aidldemo;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端的具体实现
 */
public class MyBinder extends IBookManager.Stub {
    private static final String TAG = "MyBinder";
    private List<Book> bookList = new ArrayList<>();

    public MyBinder() {
        Book book = new Book(0, "android");
        Book book2 = new Book(1, "java");
        Book book3 = new Book(2, "c++");
        Book book4 = new Book(3, "ios");

        bookList.add(book);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        Log.i(TAG, "getBookList: ");
        synchronized (bookList) {
            return bookList;
        }
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        synchronized (bookList) {
            if (book != null && !bookList.contains(book)) {
                bookList.add(book);
                Log.i(TAG, "addBook: " + book.bookName);
            }
        }
    }
}

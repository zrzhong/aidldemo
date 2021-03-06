package com.zzr.aidldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IBookManager bookManager;
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //binder死亡会回调该方法
            if (bookManager == null) {
                return;
            }
            bookManager.asBinder().unlinkToDeath(deathRecipient, 0);
            bookManager = null;
            //重新绑定远程服务
            bindService(new Intent("com.zzr.aidldemo.RemoteService").setPackage(getPackageName()),
                    serviceConnection, BIND_AUTO_CREATE);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: 连接成功");
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                //设置死亡代理
                service.linkToDeath(deathRecipient, 0);
                List<Book> bookList = bookManager.getBookList();
                Log.i(TAG, "book size: " + bookList.size());
                Book book = new Book(4, "python");
                bookManager.addBook(book);
                Log.i(TAG, "book size2: " + bookManager.getBookList().size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: 连接失败");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定服务
        Intent intent = new Intent("com.zzr.aidldemo.RemoteService");
//        startService(intent);
        //不加这句话android5.0 以上会报：Service Intent must be explitict 即服务意图必须是明确的
        intent.setPackage(getPackageName());
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}

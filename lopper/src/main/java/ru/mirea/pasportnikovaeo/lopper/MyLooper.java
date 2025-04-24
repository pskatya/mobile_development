package ru.mirea.pasportnikovaeo.lopper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                // Обработка нового сообщения с возрастом и профессией
                if (msg.getData().containsKey("AGE") && msg.getData().containsKey("JOB")) {
                    int age = msg.getData().getInt("AGE");
                    String job = msg.getData().getString("JOB");

                    try {
                        // Имитация обработки с задержкой (уже установлена через sendMessageDelayed)
                        Log.d("MyLooper", "Processing age and job info...");

                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("result", String.format("Вам %d лет и вы работаете %s. Сообщение обработано с задержкой %d секунд", age, job, age));
                        message.setData(bundle);
                        mainHandler.sendMessage(message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Оригинальная обработка сообщения
                else if (msg.getData().containsKey("KEY")) {
                    String data = msg.getData().getString("KEY");
                    Log.d("MyLooper get message: ", data);

                    int count = data.length();
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", String.format("The number of letters in the word %s is %d", data, count));
                    message.setData(bundle);
                    mainHandler.sendMessage(message);
                }
            }
        };
        Looper.loop();
    }
}
package ru.mirea.pasportnikovaeo.serviceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat;


public	class	PlayerService	extends	Service	{
    private	MediaPlayer	mediaPlayer;
    public	static	final	String	CHANNEL_ID	=	"ForegroundServiceChannel";
    @Override
    public	IBinder	onBind(Intent	intent)	{
        throw	new	UnsupportedOperationException("Not	yet	implemented");
    }
    @Override
    public	int	onStartCommand(Intent	intent,	int	flags,	int	startId)	{
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new	MediaPlayer.OnCompletionListener()	{
            public	void	onCompletion(MediaPlayer	mp)	{
                stopForeground(true);
            }
        });
        return	super.onStartCommand(intent,	flags,	startId);
    }
    @Override
    public	void	onCreate()	{
        super.onCreate();
        NotificationCompat.Builder	builder	=	new	NotificationCompat.Builder(this,	CHANNEL_ID)
                .setContentText("Playing....")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new	NotificationCompat.BigTextStyle()
                        .bigText("best	player..."))
                .setContentTitle("Music	Player is playing Katusha");
        int	importance	=	NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel	channel	=	new	NotificationChannel(CHANNEL_ID,	"Катюша",	importance);
        channel.setDescription("MIREA	Channel");
        NotificationManagerCompat	notificationManager	=	NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1,	builder.build());
        mediaPlayer=	MediaPlayer.create(this,	R.raw.music);
        mediaPlayer.setLooping(false);
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        Log.d("PlayerService", "Режим звука: " + am.getRingerMode()); // 0=без звука, 1=вибро, 2=норма
        Log.d("PlayerService", "Громкость музыки: " + am.getStreamVolume(AudioManager.STREAM_MUSIC));
    }
    @Override
    public	void	onDestroy()	{
        stopForeground(true);
        mediaPlayer.stop();
    }
    }
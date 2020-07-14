package com.mgtv.exodemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import com.mgtv.exodemo.MyApplication;
import com.mgtv.exodemo.R;


public class ExoPlayerActivity extends BaseActivity {

    private Context mContext;
    private PlayerView mPlayerView = null;
    private SimpleExoPlayer mPlayer;
    private DataSource.Factory mediaDataSourceFactory;
    private MediaSource mediaSourceHls;
    //    private UdpDataSource udpDataSourceRtp;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private String videoUrlRtp = "rtp://239.76.245.115:1234";
    private String videoUrlHls = "http://10.255.30.137:8082/EDS/RedirectPlay/000000000000/vod/f95e41c67ca2410c89b335ee5f5eecb8/3cc6fae6d07740aa8d57934e26cc2632?UserToken=123456789&UserName=6830018";


    private void initExoPlayer() {
        mPlayer = new SimpleExoPlayer.Builder(mContext).build();
        mPlayerView.setPlayer(mPlayer);
        Uri playHlsUri = Uri.parse(videoUrlHls);
//        Uri playRtpUri = Uri.parse(videoUrlRtp);
        /**使用dash的解析库*/
/*        dashMediaSource = new DashMediaSource(playHlsUri,mediaDataSourceFactory,
                new DefaultDashChunkSource.Factory(mediaDataSourceFactory
        ),null,null);*/

        /**====解析hls===begin*/
        mediaSourceHls = new HlsMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(playHlsUri, null, null);
        Log.d("exo", "Mr.xw==mediaSource==" + mediaSourceHls);
        mPlayer.prepare(mediaSourceHls);
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(true);
        /**====解析hls===end*/

        /**====解析rtp===begin 这块需要复写udp方法，暂未实现*/

/*        mediaSourceHls = new UdpDataSource(mediaDataSourceFactory)
                .createMediaSource(playHlsUri, null, null);
        Log.d("exo", "Mr.xw==mediaSource==" + mediaSourceHls);
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(true);*/
        /**====解析rtp===end*/


    }


    @Override
    public int getLayoutRes() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return R.layout.exoplayer;
    }

    @Override
    public void init() {
        mContext = this;
        mPlayerView = findViewById(R.id.exo_vr);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        initExoPlayer();
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((MyApplication) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? null : null);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseData();
    }

    private void releaseData() {
        if (isFinishing()) {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
            if (mPlayerView!=null){
                mPlayerView.destroyDrawingCache();
                mPlayerView = null;
            }
        }
    }
}

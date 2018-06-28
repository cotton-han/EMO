package com.example.jihu02.diary;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Base64;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;


public class MainActivity extends AppCompatActivity {
        Button btn;
        private Context mContext;
        private SessionCallback sessionCallback;
        final String TAG = "KeyHash";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mContext = getApplicationContext();

            btn = (Button) findViewById(R.id.loginbutton);
            SpannableString content = new SpannableString("기존 계정으로 로그인");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            btn.setText(content);

            ActionBar bar = getSupportActionBar();
            bar.hide();

            //getHashKey(mContext);

            try{
                PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
                for(Signature signature : info.signatures){
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String key= new String(Base64.encode(md.digest(), 0));
                    Log.d(TAG, key);
            }
            } catch (Exception e){
                Log.d("error", "PackageInfo error is " + e.toString());
            }

            sessionCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(sessionCallback);
            Session.getCurrentSession().checkAndImplicitOpen();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
                return ;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        public void request(){
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.d("error", "Session Closed Error is " + errorResult.toString());
                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile result) {
                    Toast.makeText(MainActivity.this, "사용자 이름은 " + result.getNickname(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            request();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("error", "Session Fail Error is " + exception.getMessage().toString());
        }
    }

        /*public static String getHashKey(Context context){
            final String TAG = "KeyHash";
            String keyHash = null;
            try {
                PackageInfo info =
                        context.getPackageManager().getPackageInfo(context.getPackageName(),
                                PackageManager.GET_SIGNATURES);

                for (Signature signature:info.signatures){
                    MessageDigest md;
                    md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    keyHash = new String(Base64.encode(md.digest(), 0));
                    Log.d(TAG, keyHash);
                }
            }catch (Exception e){
                Log.e("name not found",e.toString());
            }
            if (keyHash != null) {
                return keyHash;
            } else {
                return null;
        }
        }*/

        //카카오버튼
        public void onKakaoButtonClicked(View view) {
            //실행은 되지만 릴리즈 키를 다시등록해야함
        }

        //회원가입버튼
        public void onJoinButtonClicked(View view) {
            JoinDialog join = new JoinDialog(this);
            join.setTitle("회원가입");
            join.show();
        }

        //기존계정으로 로그인 버튼
        public void onLoginButtonClicked(View view) {
            LoginDialog login = new LoginDialog(this);
            login.setTitle("로그인");
            login.show();
        }
}

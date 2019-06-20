package com.xxf.web3j;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public void copyTextToClipboard(@NonNull CharSequence charSequence) {
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb == null) {
            return;
        }
        cmb.setPrimaryClip(ClipData.newPlainText("text", charSequence));
    }

    EditText decode_keystore_et, decode_pwd_et;
    TextView decode_keystore_tv, decode_private_tv;
    Button bt_decode;


    EditText encode_pwd_et;
    TextView encode_keystore_tv, encode_private_tv;
    Button bt_encode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decode_keystore_et = findViewById(R.id.decode_keystore_et);
        decode_pwd_et = findViewById(R.id.decode_pwd_et);
        decode_keystore_tv = findViewById(R.id.decode_keystore_tv);
        decode_private_tv = findViewById(R.id.decode_private_tv);
        bt_decode = findViewById(R.id.bt_decode);

        bt_decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decode();
            }
        });


        encode_pwd_et = findViewById(R.id.encode_pwd_et);
        encode_keystore_tv = findViewById(R.id.encode_keystore_tv);
        encode_private_tv = findViewById(R.id.encode_private_tv);
        bt_encode = findViewById(R.id.bt_encode);


        bt_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
        encode_keystore_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyTextToClipboard(encode_keystore_tv.getText());
                Toast.makeText(v.getContext(), "复制keystore成功",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private File getFile() {
        File file = new File(getCacheDir(), "test.keystore");
        return file;
    }

    private void create() {
        String pwd = encode_pwd_et.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "pwd is null", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            WalletResult walletResult = WalletUtils._createWallet(pwd, getFile());
            encode_keystore_tv.setText(walletResult.getKeyStoreFileContent());
            encode_private_tv.setText(walletResult.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "创建失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void decode() {
        String pwd = decode_pwd_et.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "pwd is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(decode_keystore_et.getText())) {
            Toast.makeText(this, "keystore is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String keystore = decode_keystore_et.getText().toString();
        FileUtils.writeStr(getFile(), keystore, false);
        try {
            WalletResult walletResult = WalletUtils._openWallet(pwd, getFile());
            decode_keystore_tv.setText(walletResult.getKeyStoreFileContent());
            decode_private_tv.setText(walletResult.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "解密失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

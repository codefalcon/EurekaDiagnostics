package org.eurekachild.mathdiagnostics;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class Addition extends Activity implements View.OnClickListener {

    private String doubleEscapeTeX(String s) {
        String t="";
        for (int i=0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') t += '\\';
            if (s.charAt(i) != '\n') t += s.charAt(i);
            if (s.charAt(i) == '\\') t += "\\";
        }
        return t;
    }

    private boolean mmltoggle = false;

    public void onClick(View v) {
        TextView text = (TextView)findViewById(R.id.textview6);
        switch (v.getId()) {
            case R.id.button0:
                text.setText(text.getText() + "0");
                break;
            case R.id.button1:
                text.setText(text.getText() + "1");
                break;
            case R.id.button2:
                text.setText(text.getText() + "2");
                break;
            case R.id.button3:
                text.setText(text.getText() + "3");
                break;
            case R.id.button4:
                text.setText(text.getText() + "4");
                break;
            case R.id.button5:
                text.setText(text.getText() + "5");
                break;
            case R.id.button6:
                text.setText(text.getText() + "6");
                break;
            case R.id.button7:
                text.setText(text.getText() + "7");
                break;
            case R.id.button8:
                text.setText(text.getText() + "8");
                break;
            case R.id.button9:
                text.setText(text.getText() + "9");
                break;
            case R.id.buttonBkspc:
                String answer = (String)text.getText();
                if(!answer.isEmpty())
                    text.setText(answer.substring(0,answer.length()-1));
                break;
            case R.id.buttonSubmit:
                Toast.makeText(Addition.this,"Submit",Toast.LENGTH_SHORT).show();
                //TextView textsub = (TextView)findViewById(R.id.textview6);
                //text.setText(text.getText() + "0");
                WebView w = (WebView) findViewById(R.id.webviewadd);
                mmltoggle=false;
                w.loadUrl("javascript:document.getElementById('mmlout').innerHTML='';");
                w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                        +doubleEscapeTeX("\\  7\\\\+6")+"\\\\]';");
                w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
                break;
        }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);
        WebView w = (WebView) findViewById(R.id.webviewadd);
        w.setWebChromeClient(new WebChromeClient());

        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setBuiltInZoomControls(true);
        s.setAllowFileAccess(true);

        String html2 = assetTexttoStr("addition.html");
        w.loadDataWithBaseURL("http://bar/",html2,"text/html","utf-8","");

        Button but = (Button) findViewById(R.id.button0);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button1);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button2);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button3);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button4);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button5);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button6);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button7);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button8);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.button9);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.buttonBkspc);
        but.setOnClickListener(this);
        but = (Button) findViewById(R.id.buttonSubmit);
        but.setOnClickListener(this);

               // Toast.makeText(Addition.this, "" + position, Toast.LENGTH_SHORT).show();
    }

    protected String assetTexttoStr(String filename) {

        AssetManager assetManager = getAssets();

        // To load text file
        InputStream input;

        try {
            input = assetManager.open(filename);
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            // byte buffer into a string
            String text = new String(buffer);
            return text;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //don't reload the current page when the orientation is changed
        super.onConfigurationChanged(newConfig);
    }
}

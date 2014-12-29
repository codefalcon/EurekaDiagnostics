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


public class Addition extends Activity {

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
        String html =
                "<link href=\"file:///android_asset/diagnostic.css\" type=\"text/css\" rel=\"stylesheet\"/>"
                +"<link href=\"file:///android_asset/jquery/jquery.mobile.custom.structure.css\" type=\"text/css\" rel=\"stylesheet\"/>"
                +"<link href=\"file:///android_asset/jquery/jquery.mobile.custom.theme.css\" type=\"text/css\" rel=\"stylesheet\"/>"
                +"<link href=\"file:///android_asset/bootstrap/css/bootstrap.css\" type=\"text/css\" rel=\"stylesheet\"/>"
                +"<link href=\"file:///android_asset/bootstrap/css/bootstrap-theme.css\" type=\"text/css\" rel=\"stylesheet\"/>"
                + "<script src=\"file:///android_asset/jquery/jquery.mobile.custom.js\"></script>"
                + "<script src=\"file:///android_asset/bootstrap/js/bootstrap.js\"></script>"
                + "<button>3</button>"
                +"<container><block><button id=\"b3\" type=\"button\" class=\"btn btn-block btn-info\">5</button></block></container>";
        w.loadDataWithBaseURL("http://bar/",html2,"text/html","utf-8","");

        Button but = (Button) findViewById(R.id.button0);
        but.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(Addition.this,"0",Toast.LENGTH_SHORT).show();
                TextView text = (TextView)findViewById(R.id.textview6);
                text.setText(text.getText() + "0");
            }}
            );


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

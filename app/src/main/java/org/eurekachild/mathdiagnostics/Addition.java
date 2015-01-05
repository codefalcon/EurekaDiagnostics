package org.eurekachild.mathdiagnostics;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import android.os.Bundle;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


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

    Random r = new Random();

    private int currentType = 1;
    private int currentLevel =1 ;

    private String getQuestionForType(int qnType) {
        String question = "";
        int op1 = 0;
        int op2 = 0;
        String operand = "+";

        switch (qnType) {
            case 1:
                //1D + 1D
                op1 = getRandomNumberinRange(1,99);
                op2 = getRandomNumberinRange(1,9);
                break;
            case 2:
                //2D + 2D w/o carry
                op1 = getRandomNumberinRange(1,8);
                op2 =  getRandomNumberSum1D(op1);
                int digits = getRandomNumberinRange(1,8);
                op1 = op1*10+digits;
                op2 = op2*10 + getRandomNumberSum1D(digits);
                break;
            default:
                op1=0;
                op2=0;
                break;
        }
        question = generateTexForProblem(op1,op2, operand);
        return question;
    }

    private String generateTexForProblem(int op1, int op2, String operand) {

        String qn = "\\begin{array}{rr}" +
                "~&"+ Integer.toString(op1) +"\\\\" +
                operand + "& " + Integer.toString(op2) +
                "\\end{array}";
        return qn;
    }

    //Generate a random number in the range min to max both inclusive
    private int getRandomNumberinRange(int min, int max) {
        if(min < 0 || max < 0) return 0;
        return (r.nextInt(max-min+1) + min);
    }

    //Given a 1D +ve integer, generate a 1D random number such that the sum is also 1D
    private int getRandomNumberSum1D(int given) {
        if(given > 8 || given < 0) return 0;
        return (r.nextInt(9-given) + 1);
    }

    //Given a 1D +ve integer, generate a 1D random number such that the sum is 2D always
    private int getRandomNumberSum2D(int given) {
        if(given > 9 || given < 0) return 0;
        int min = 10-given;
        return getRandomNumberinRange(min,9);
    }

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
                        //+doubleEscapeTeX("\\  7\\\\+6")+"\\\\]';");
                        +doubleEscapeTeX(getQuestionForType(2))+"\\\\]';");
                w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
                if(currentType > 7) currentType =1;
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

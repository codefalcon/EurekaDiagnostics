package org.eurekachild.mathdiagnostics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
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

    class QuestionAnswer {
        int op1;
        int op2;
        int operand;
        int correctAnswer;
        int studentResponse;

        public QuestionAnswer() {
            op1=0;
            op2=0;
            operand=ADDITION;
            correctAnswer = 0;
            studentResponse = 0;
        }

        public QuestionAnswer(int op1, int op2, int operand) {
            this.op1 = op1;
            this.op2 = op2;
            this.operand = operand;
            this.correctAnswer = getAnswer(op1,op2,operand);
            this.studentResponse = 0;
        }

        public int getStudentResponse() {
            return studentResponse;
        }

        public void setStudentResponse(int response) {
            studentResponse = response;
        }

        public int getCorrectAnswer() {
            return correctAnswer;
        }

        public int getOp1() {
            return op1;
        }

        public int getOp2() {
            return op2;
        }

        public int getOperand() {
            return operand;
        }

        private int  getAnswer(int op1, int op2, int operand) {
            int corAns = 0;

            switch (operand) {
                case ADDITION:
                    corAns = op1 + op2;
                    break;
                case SUBRTRACTION:
                    corAns = op1 - op2;
                    break;
                case MULTIPLICATION:
                    corAns = op1 * op2;
                    break;
                case DIVISION:
                    corAns = op1 / op2;
                    break;
            }
            return corAns;
        }

    }

    QuestionAnswer qnArray[] = new QuestionAnswer[7];

    private boolean mmltoggle = false;

    Random r = new Random();

    private int currentType = 1;
    private int currentLevel =1 ;

    private final int ADDITION = 101;
    private final int SUBRTRACTION = 102;
    private final int MULTIPLICATION = 103;
    private final int DIVISION = 104;

    private QuestionAnswer getQuestionForType(int qnType, int operand) {
        int op1 = 0;
        int op2 = 0;
        int digit =0;

        switch (qnType+1) {
            case 1:
                //1D + 1D
                op1 = getRandomNumberinRange(1,9);
                op2 = getRandomNumberinRange(1,9);
                break;
            case 2:
                //2D + 2D w/o carry
                op1 = getRandomNumberinRange(1,8);
                op2 =  getRandomNumberSum1D(op1);
                digit = getRandomNumberinRange(1,8);
                op1 = op1*10+digit;
                op2 = op2*10 + getRandomNumberSum1D(digit);
                break;
            case 3:
                //2D + 1D with or w/o carry
                op1 = getRandomNumberinRange(1,9);
                op1 = op1*10 + getRandomNumberinRange(1,9);
                op2 = getRandomNumberinRange(1,9);
                break;
            case 4:
                //2D + 2D with zero ending
                op1 = getRandomNumberinRange(1,8);
                op2 = getRandomNumberSum1D(op1);
                op1 = op1*10 + getRandomNumberinRange(1,9);
                op2 = op2*10;
                break;
            case 5:
                //2D + 2D with carry
                op1 = getRandomNumberinRange(1,9);
                op2 =  getRandomNumberSum2D(op1);
                digit = getRandomNumberinRange(1,8);
                op1 = digit*10 + op1;
                op2 = getRandomNumberSum1D(digit)*10 + op2;
                break;
            case 6:
                //random 2D. Anything from 2-5
                digit = getRandomNumberinRange(2,5);
                return getQuestionForType(digit, operand);
            case 7:
                //3D + 3D with 2 carry overs
                op1 = getRandomNumberinRange(1,9);
                op2 =  getRandomNumberSum2D(op1);
                digit = getRandomNumberinRange(1,9);
                op1 = digit*10 + op1;
                op2 = getRandomNumberSum2D(digit)*10 + op2;
                digit = getRandomNumberinRange(1,8);
                op1 = digit*100 + op1;
                op2 = getRandomNumberSum1D(digit)*100 + op2;
                break;
            default:
                op1=0;
                op2=0;
                break;
        }
        return(new QuestionAnswer(op1,op2,operand));
    }
    private String getUpdateQuestionForType(int qnType, int operand) {
        updateQnArray(qnType, getQuestionForType(qnType,operand));
        return generateTexForProblem(qnArray[qnType]);
    }

    private void updateQnArray(int qnType, QuestionAnswer qnAns) {

        if(qnType > 6||qnType < 0) return;
        qnArray[qnType] = new QuestionAnswer(qnAns.op1, qnAns.op2, qnAns.operand);
    }

    private String generateTexForProblem(QuestionAnswer qnAns) {
        String qn = "";
        int op1 =  qnAns.op1;
        int op2 = qnAns.op2;
        int operand = qnAns.operand;

        switch (operand) {
            case ADDITION:
            qn = "\\begin{array}{rrr}" +
                    "~&" + Integer.toString(op1) + "&~\\\\" +
                    "+" + "& " + Integer.toString(op2) +
                    "&~\\end{array}";
                break;
        }
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

                if(currentType>0 && currentType < 8) {
                    int studentAnswer = Integer.parseInt(text.getText().toString());
                    qnArray[currentType-1].setStudentResponse(studentAnswer);
                    int corAns = qnArray[currentType-1].getCorrectAnswer();
                    if(corAns == studentAnswer)
                        Toast.makeText(Addition.this,"Correct",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Addition.this,"Wrong",Toast.LENGTH_SHORT).show();
                }
                if(currentType > 6)
                    currentType =0;
                text.setText("");
                //TextView textsub = (TextView)findViewById(R.id.textview6);
                //text.setText(text.getText() + "0");
                WebView w = (WebView) findViewById(R.id.webviewadd);
                mmltoggle=false;
                w.loadUrl("javascript:document.getElementById('mmlout').innerHTML='';");
                w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                        //+doubleEscapeTeX("\\  7\\\\+6")+"\\\\]';");
                        +doubleEscapeTeX(getUpdateQuestionForType(currentType++, ADDITION))+"\\\\]';");
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

        currentType = 0;
        currentLevel = 0;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}

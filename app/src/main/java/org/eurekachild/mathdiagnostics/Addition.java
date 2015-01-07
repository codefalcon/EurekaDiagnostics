package org.eurekachild.mathdiagnostics;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.Button;

import android.widget.GridLayout;
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

    private final int MAX_TYPE = 7;
    QuestionAnswer qnArray[] = new QuestionAnswer[MAX_TYPE];

    private SoundPool soundPool;
    private int soundID[] = {0,1,2,3,4};
    boolean soundLoaded[] = new boolean[5];

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

                // Getting the user sound settings
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                float actualVolume = (float) audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                float maxVolume = (float) audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volume = actualVolume / maxVolume;


                if(currentType>0 && currentType < 8) {
                    String str = text.getText().toString();
                    int studentAnswer = -1;
                    if(!str.isEmpty())
                        studentAnswer = Integer.parseInt(str);

                    qnArray[currentType-1].setStudentResponse(studentAnswer);
                    int corAns = qnArray[currentType-1].getCorrectAnswer();
                    if(corAns == studentAnswer) {
                        Toast.makeText(Addition.this, "Correct", Toast.LENGTH_SHORT).show();

                        if (currentType < 3 && soundLoaded[1])
                            soundPool.play(soundID[1], volume, volume, 1, 0, 1);
                        else if (currentType < 5 && soundLoaded[2])
                            soundPool.play(soundID[2], volume, volume, 1, 0, 1);
                        else if (currentType < 7 && soundLoaded[3])
                            soundPool.play(soundID[3], volume, volume, 1, 0, 1);
                    }
                    else {
                        Toast.makeText(Addition.this, "Wrong", Toast.LENGTH_SHORT).show();
                        if (soundLoaded[0])
                            soundPool.play(soundID[0], volume, volume, 1, 0, 1);

                    }
                }
                text.setText("");
                if(currentType > 6) {
                    currentType = 0;
                    GridLayout myGrid = (GridLayout)findViewById(R.id.gridviewkeypad);
                    myGrid.setVisibility(View.GONE);
                    TextView title = (TextView) findViewById(R.id.textview7);
                    title.setText("Addition-Summary");

                    showMathMLEquation(getSummaryMathStr(qnArray));

                    if (soundLoaded[4])
                        soundPool.play(soundID[4], volume, volume, 1, 0, 1);

                }
                else {
                    //TextView textsub = (TextView)findViewById(R.id.textview6);
                    //text.setText(text.getText() + "0");
                    showMathMLEquation(currentType++, ADDITION);
                }
                break;
        }
    }

    private String getSummaryMathStr(QuestionAnswer qnArray[]) {

        StringBuilder mathStr = new StringBuilder();
        mathStr.append("\\begin{array}{rrrcc|c}");
        mathStr.append("~&~&~&~&Student&Correct\\\\");
        mathStr.append("~&~&~&~&Response&Answer\\\\");

        for(int i = 0; i < MAX_TYPE; i++) {
            QuestionAnswer qnAns = qnArray[i];
            mathStr.append(
                    Integer.toString(qnAns.getOp1()) + "&" +
                    getOperandStr(qnAns.getOperand()) + "&" +
                    Integer.toString(qnAns.getOp2()) + "&=&\\style{color:" +
                    (qnAns.getStudentResponse() == qnAns.getCorrectAnswer()?"green":"red")+
                    "}{" +
                    Integer.toString(qnAns.getStudentResponse()) + "}&" +
                    Integer.toString(qnAns.getCorrectAnswer()) + "\\\\");
        }
        mathStr.append("\\end{array}");
        return mathStr.toString();
    }
    private String getOperandStr(int operand) {
        if(operand == ADDITION)
            return "+";
        else if(operand == SUBRTRACTION)
            return "-";
        else if(operand == MULTIPLICATION)
            return "X";
        else if(operand == DIVISION)
            return "÷";
        return "";
    }
    private void showMathMLEquation (int typeToShow, int operand) {
        WebView w = (WebView) findViewById(R.id.webviewadd);
        w.loadUrl("javascript:document.getElementById('mmlout').innerHTML='';");
        w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                //+doubleEscapeTeX("\\  7\\\\+6\\\\3+4\\\\5+6\\\\7+8\\\\1+2\\\\10+11\\\\a+b1")+"\\\\]';");
                +doubleEscapeTeX(getUpdateQuestionForType(typeToShow, operand))+"\\\\]';");
        w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
    }
    private void showMathMLEquation (String mathStr) {
        WebView w = (WebView) findViewById(R.id.webviewadd);
        w.loadUrl("javascript:document.getElementById('mmlout').innerHTML='';");
        w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                +doubleEscapeTeX(mathStr)+"\\\\]';");
        w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
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
        w.addJavascriptInterface(new Object() {
            //@JavascriptInterface
            public void showEquation() {
                showMathMLEquation(currentType++,ADDITION);
            //WebView ww = (WebView) findViewById(R.id.webviewadd);
            //Toast.makeText(Addition.this,"MathML Ready",Toast.LENGTH_SHORT).show();
        }}, "injectedObject");
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

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                if(status==0) {
                    for(int i = 0;i<5;i++) {
                        if(sampleId == soundID[i]) {
                            soundLoaded[i] = true;
                            break;
                        }
                    }
                }
            }
        });
        soundID[0] = soundPool.load(this, R.raw.ting1sec, 1);
        soundID[1] = soundPool.load(this, R.raw.clap2sec, 1);
        soundID[2] = soundPool.load(this, R.raw.clapcheer2sec, 1);
        soundID[3] = soundPool.load(this, R.raw.claphighcheer2sec, 1);
        soundID[4] = soundPool.load(this, R.raw.fireworks3sec, 1);

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
        //super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }
}

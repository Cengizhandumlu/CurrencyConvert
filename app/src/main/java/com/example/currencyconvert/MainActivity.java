package com.example.currencyconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tryText;
    TextView cadText;
    TextView usdText;
    TextView jpyText;
    TextView chfText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText=findViewById(R.id.tryText);
        cadText=findViewById(R.id.cadText);
        usdText=findViewById(R.id.usdText);
        jpyText=findViewById(R.id.jpyText);
        chfText=findViewById(R.id.chfText);


    }

    public void getRates(View view){

        DownloadData downloadData=new DownloadData();

        try{

            String url="http://data.fixer.io/api/latest?access_key=40dd39e83e81e61efd6a5247ec21790f&format=1";
            downloadData.execute(url); //verdigimiz url'yi doInBackground'dan alacak.

        }catch (Exception e){


        }

    }

    private class DownloadData extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {//strings dedigi bir strings dizisi param olarakta gecer.

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try{ //url'yi yanlıs kopyalarsam veya yanlıs verirsem app cokmemesi icin kesinlikle kullanmalıyız.

                url= new URL(strings[0]); //dizi olarak strings verdik. 0. elemanı
                httpURLConnection=(HttpURLConnection) url.openConnection();//burdan baglantıyı actık ve bir reader tanımlamamız gerekiyor.
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                //baglantı acıldı ve input stream'i kullanarak gelen cevapları okuyacagız.
                //bir int data olusturacagız.

                int data = inputStreamReader.read();//bir int deger verdi bize

                while (data > 0){//sıfırdan buyukse halen alacagımız bir data var demek ve karakter karakter karakter alacagız.

                    char character=(char) data;
                    result += character;//her gelecek karakteri result' a tek tek ekle.

                    data=inputStreamReader.read();//bir sonraki karaktere gecmek icin bunu yapıyoruz.

                }

                return result;// Bir hata yok ise bize result stringini verecek.

            }catch (Exception e){
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //data alındıktan sonraki islemleri burada yapacagız.

            //System.out.println("alınan data: " + s);

            try{//json objectler ile calısacagımız icin try and catch yapmamız en dogrusu

                JSONObject jsonObject=new JSONObject(s);//s den objelerimiz aldık.
                String base=jsonObject.getString("base");//burada yazdıgımız base'in datada gorunen base ile aynı olması lazım.
                //System.out.println("base:"+ base);
                String rates=jsonObject.getString("rates");
                //System.out.println("rates:" + rates);

                JSONObject jsonObject1=new JSONObject(rates);

                String turkishlira=jsonObject1.getString("TRY");
                tryText.setText("TRY: " + turkishlira);

                String usd=jsonObject1.getString("USD");
                usdText.setText("USD: " + usd);

                String cad=jsonObject1.getString("CAD");
                cadText.setText("CAD: " + cad);

                String jpy=jsonObject1.getString("JPY");
                jpyText.setText("JPY: " + jpy);

                String chf=jsonObject1.getString("CHF");
                chfText.setText("CHF: " + chf);



            }catch (Exception e){

            }
        }
    }
}

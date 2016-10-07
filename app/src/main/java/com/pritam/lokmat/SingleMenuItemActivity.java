package com.pritam.lokmat;

/**
 * Created by Pritam on 10/1/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pritam.lokmat.com.example.utility.AppConstant;
import com.pritam.lokmat.com.example.utility.HttpAsyncTask;
import com.pritam.lokmat.com.example.utility.ImageLoader;
import com.pritam.lokmat.com.example.utility.XMLParser;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
//http://115.112.141.11/lokmat/mobile_storyapp.php?catid=1&newsid=15449824
public class SingleMenuItemActivity  extends Activity {

    String URL;

    // XML node keys
    static final String[] str = {"menuid",  "catid", "newsid", "headline",  "thumb_img",  "publishon"};
    static final String[] str1 = {"synopsis","description","link","medium_image","relatednews","item", "rcatid",  "rnews",  "rheadline",};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        getActionBar().hide();

        URL = getResources().getString(R.string.detailurl);

        ((AppConstant) this.getApplication()).setCompressImage(false);

        findViewById(R.id.createby).setVisibility(View.GONE);
        findViewById(R.id.description_web).setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        ArrayList<String> intentstr = new ArrayList<String>();
        for(int j = 0; j < str.length;j++ )
            intentstr.add(j,in.getStringExtra(str[j]));

        URL= URL+ "catid="+intentstr.get(1)+"&newsid="+intentstr.get(2);
        getData();

        TextView title =  ((TextView) findViewById(R.id.createby));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("mailto:")
                        .buildUpon()
                        .appendQueryParameter("to" , "p.khose@rediffmail.com")
                        .appendQueryParameter("subject", "Lokmat Apps Feedback")
                        .appendQueryParameter("body", "Hello Pritam,\n\nRegards,\n\n")
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "p.khose@rediffmail.com");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Feedback via Email"));
                }


            }
        });

    }

    public void getData() {
        GetXMLTask1 task = new GetXMLTask1();
        task.execute(URL);

    }

    private class GetXMLTask1 extends HttpAsyncTask {
        @Override
        protected void onPostExecute(String output) {
            System.out.println("----------" + output);
            output = output.replaceAll("\b", " ");
            if (output.length() > 3) {
                callModulePage(output);
            } else {
                Toast.makeText(getApplicationContext(), "Content Not Available", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    public void callModulePage(String data) {

        XMLParser parser = new XMLParser();
        String xml =  data;//parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName("item");
        // looping through all item nodes <item>
        //for (int i = 0; i < nl.getLength(); i++)
        HashMap<String, String> map = new HashMap<String, String>();
        {
            // creating new HashMap

            Element e = (Element) nl.item(0);
            {
                map.put(str[0], parser.getValue(e, str[0]));
                map.put(str[1], parser.getValue(e, str[1]));
                map.put(str[2], parser.getValue(e, str[2]));
                Element line = (Element) (e.getElementsByTagName(str[3]).item(0));
                map.put(str[3], getCharacterDataFromElement(line));
                map.put(str1[0], getCharacterDataFromElement((Element) (e.getElementsByTagName(str1[0]).item(0))));
                map.put(str1[1], getCharacterDataFromElement((Element) (e.getElementsByTagName(str1[1]).item(0))));
                map.put(str1[2], getCharacterDataFromElement((Element) (e.getElementsByTagName(str1[2]).item(0))));
                map.put(str1[3], getCharacterDataFromElement((Element) (e.getElementsByTagName(str1[3]).item(0))));
                map.put(str[4], getCharacterDataFromElement((Element) (e.getElementsByTagName(str[4]).item(0))));
                }
            }

          // Displaying all values on the screen

         ImageLoader imageLoader = new ImageLoader(getApplicationContext());
        ImageView medium_image=(ImageView) findViewById(R.id.imageView); // medium_image
        imageLoader.DisplayImage(map.get(str1[3]), medium_image);

        findViewById(R.id.progressBar).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.name_label)).setText(map.get(str[3]));
        ((TextView) findViewById(R.id.cost_label)).setText(map.get(str1[0]));
        ((TextView) findViewById(R.id.description_label)).setText(map.get(str1[1]));

        String content =    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<body>\n" +
                            "\n" +
                            map.get(str1[1])+
                            "\n" +
                            "</body>\n" +
                            "</html>\n";

        findViewById(R.id.description_web).setVisibility(View.VISIBLE);
        ((WebView) findViewById(R.id.description_web)).loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        findViewById(R.id.description_web).setBackgroundColor(Color.parseColor("#FFFFFF"));
        findViewById(R.id.description_web).setVerticalScrollBarEnabled(true);
        findViewById(R.id.description_web).setHorizontalScrollBarEnabled(true);
        WebSettings webSettings = ((WebView) findViewById(R.id.description_web)).getSettings();
        webSettings.setJavaScriptEnabled(true);


        findViewById(R.id.createby).setVisibility(View.VISIBLE);

    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        ((AppConstant) this.getApplication()).setCompressImage(true);
        super.onBackPressed();  // optional depending on your needs
        finish();
    }
}


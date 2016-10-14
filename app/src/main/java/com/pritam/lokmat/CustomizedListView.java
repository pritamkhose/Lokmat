package com.pritam.lokmat;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pritam.lokmat.com.example.utility.AppConstant;
import com.pritam.lokmat.com.example.utility.HttpAsyncTask;
import com.pritam.lokmat.com.example.utility.Utility;
import com.pritam.lokmat.com.example.utility.XMLParser;

public class CustomizedListView extends Activity {
	// All static variables
	//static final String URL = "http://api.androidhive.info/music/music.xml";
	String URL;

	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";

	static final String[] str = {"menuid",  "catid", "newsid", "headline",  "thumb_img",  "publishon"};
	
	ListView list;
    LazyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().hide();

		URL = getResources().getString(R.string.listurl);
		((AppConstant) this.getApplication()).setCompressImage(true);

		findViewById(R.id.list).setVisibility(View.GONE);
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

		Intent in = getIntent();
		URL = URL+ in.getStringExtra("category").toString();
		((TextView) findViewById(R.id.cat)).setText(in.getStringExtra("catname").toString());

		if (Utility.isConnected(this)) {
			getData();
		}else {
			Toast.makeText(this, getResources().getString(R.string.netErr), Toast.LENGTH_SHORT).show();
			onBackPressed();
		}


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

	public void callModulePage(String data)
		{
		final ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml =  data;//parser.getXmlFromUrl(URL); // getting XML from URL
		Document doc = parser.getDomElement(xml); // getting DOM element

			NodeList nl = doc.getElementsByTagName(KEY_ITEM);
			// looping through all item nodes <item>
			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				/*map.put(KEY_ID, parser.getValue(e, KEY_ID));
				map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
				map.put(KEY_COST, "Rs." + parser.getValue(e, KEY_COST));
				map.put(KEY_DESC, parser.getValue(e, KEY_DESC));*/

				for(int j = 0; j < str.length;j++ ) {
					if(j == 3){
						NodeList title = e.getElementsByTagName(str[3]);
						Element line = (Element) title.item(0);
						map.put(str[j], getCharacterDataFromElement(line));
					} else {
						map.put(str[j], parser.getValue(e, str[j]));
					}
				}
				// adding HashList to ArrayList
				menuItems.add(map);
			}

		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, menuItems);        
        list.setAdapter(adapter);

			findViewById(R.id.list).setVisibility(View.VISIBLE);
			findViewById(R.id.progressBar).setVisibility(View.GONE);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				for(int j = 0; j < str.length;j++ )
					in.putExtra(str[j], menuItems.get(position).get(str[j]));
				startActivity(in);
			}
		});		
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
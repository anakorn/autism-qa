package com.appjam.team16;



import java.util.ArrayList;

import com.appjam.team16.R;
import com.appjam.team16.R.id;
import com.appjam.team16.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class ListViewCheckboxesActivity extends Activity {
 
 MyCustomAdapter dataAdapter = null;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.main);
 
  //Generate list View from ArrayList
  displayListView();
 
 // checkButtonClick();
 
 }
 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phuc_menu, menu);
		return true;
	}
 private void displayListView() {
 
  //Array list of countries
  ArrayList<questionObject> countryList = new ArrayList<questionObject>();
  questionObject country = new questionObject("AFG","Afghanistan",false);
  countryList.add(country);
  country = new questionObject("ALB","Albania",true);
  countryList.add(country);
  country = new questionObject("DZA","Algeria",false);
  countryList.add(country);
  country = new questionObject("ASM","American Samoa",true);
  countryList.add(country);
  country = new questionObject("AND","Andorra",true);
  countryList.add(country);
  country = new questionObject("AGO","Angola",false);
  countryList.add(country);
  country = new questionObject("AIA","Anguilla",false);
  countryList.add(country);
  country = new questionObject("AIA","Anguilla",false);
  countryList.add(country);
  country = new questionObject("AIA","Anguill",false);
  countryList.add(country);
  country = new questionObject("AIA","Angui",false);
  countryList.add(country);
  country = new questionObject("AIA","Ang",false);
  countryList.add(country);
  country = new questionObject("AIA","Ang",false);
  countryList.add(country);
  
 
  //create an ArrayAdaptar from the String Array
  dataAdapter = new MyCustomAdapter(this,
    R.layout.question_list, countryList);
  ListView listView = (ListView) findViewById(R.id.listView1);
  // Assign adapter to ListView
  listView.setAdapter(dataAdapter);
 
 
  listView.setOnItemClickListener(new OnItemClickListener() {
   public void onItemClick(AdapterView<?> parent, View view,
     int position, long id) {
    // When clicked, show a toast with the TextView text
	   questionObject country = (questionObject) parent.getItemAtPosition(position);
    Toast.makeText(getApplicationContext(),
      "Clicked on Row: " + country.getName(),
      Toast.LENGTH_LONG).show();
   }
  });
 
 }
 
 private class MyCustomAdapter extends ArrayAdapter<questionObject> {
 
  private ArrayList<questionObject> countryList;
 
  public MyCustomAdapter(Context context, int textViewResourceId,
    ArrayList<questionObject> countryList) {
   super(context, textViewResourceId, countryList);
   this.countryList = new ArrayList<questionObject>();
   this.countryList.addAll(countryList);
  }
 
  private class ViewHolder {
   TextView code;
   CheckBox name;
   Button btnEdit;
  }
 
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
 
   ViewHolder holder = null;
   Log.v("ConvertView", String.valueOf(position));
 
   if (convertView == null) {
   LayoutInflater vi = (LayoutInflater)getSystemService(
     Context.LAYOUT_INFLATER_SERVICE);
   convertView = vi.inflate(R.layout.question_list, null);
 
   holder = new ViewHolder();
   holder.code = (TextView) convertView.findViewById(R.id.code);
   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
   holder.btnEdit = (Button) convertView.findViewById(R.id.edit);
   convertView.setTag(holder);
 
    holder.name.setOnClickListener( new View.OnClickListener() { 
     public void onClick(View v) { 
      CheckBox cb = (CheckBox) v ; 
      questionObject country = (questionObject) cb.getTag(); 
      Toast.makeText(getApplicationContext(),
       "Clicked on Checkbox: " + cb.getText() +
       " is " + cb.isChecked(),
       Toast.LENGTH_LONG).show();
      country.setSelected(cb.isChecked());
     } 
    }); 
   }
   else {
    holder = (ViewHolder) convertView.getTag();
   }
 
   questionObject country = countryList.get(position);
   holder.code.setText(" (" +  country.getCode() + ")");
   holder.name.setText(country.getName());
   holder.name.setChecked(country.isSelected());
   holder.name.setTag(country);
 
   return convertView;
 
  }
 
 }
 
// private void checkButtonClick() {
// 
// 
//  //Button myButton = (Button) findViewById(R.id.findSelected);
// // myButton.setOnClickListener(new OnClickListener() {
// 
//   @Override
//   public void onClick(View v) {
// 
//    StringBuffer responseText = new StringBuffer();
//    responseText.append("The following were selected...\n");
// 
//    ArrayList<questionObject> countryList = dataAdapter.countryList;
//    for(int i=0;i<countryList.size();i++){
//    	questionObject country = countryList.get(i);
//     if(country.isSelected()){
//      responseText.append("\n" + country.getName());
//     }
//    }
// 
//    Toast.makeText(getApplicationContext(),
//      responseText, Toast.LENGTH_LONG).show();
// 
//   }
// });
 
// }
 
}
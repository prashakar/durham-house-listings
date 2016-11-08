package android.midterm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.midterm.R.id.listing;

/*
TODO:

1. Add this activity as a selection listener to the dropdown
2. Make the activity class implement the right interface to be a selection listener
3. Implement the selection listener event to populate the text fields with the selected
   housing project
4. Make this activity class implement the HousingDownloadListener interface provided   
5. Make this activity class implement the housingDataDownloaded() method.  This method will 
   populate the spinner with housing data (using the toString() method of the HousingData 
   class).
*/
public class MainActivity extends AppCompatActivity
                          /* TODO: Add HousingDownloadListener interface */
                          /* TODO: Add selection listener interface */ {
    private static final String URL = "http://csundergrad.science.uoit.ca/csci3230u/data/Affordable_Housing.csv";
    
    private ArrayList<HousingProject> housingProjectsFinal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// TODO:  Add this activity as a listener for the spinner
        

        // TODO:  Create an AsyncTask instance, and start it
        DownloadHousingTask downloadHousingTask = new DownloadHousingTask(new HousingDownloadListener() {
            @Override
            public void housingDataDownloaded(List<HousingProject> housingProjects) {
                housingProjectsFinal = (ArrayList<HousingProject>)housingProjects;
                for(HousingProject project: housingProjects){
                    System.out.println(project.getAddress());
                }
                Spinner spinner = (Spinner)findViewById(R.id.lstHousingProjects);
                SpinnerAdapter adapter = new SpinnerAdapter(getBaseContext(),housingProjectsFinal);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        HousingProject housingProject = (HousingProject) parent.getSelectedItem();

                        EditText numOfUnits = (EditText) findViewById(R.id.numOfUnits);
                        EditText address = (EditText) findViewById(R.id.address);
                        EditText municipality = (EditText) findViewById(R.id.municipality);
                        EditText latitude = (EditText) findViewById(R.id.latitude);
                        EditText longitude = (EditText) findViewById(R.id.longitude);

                        numOfUnits.setText(String.valueOf(housingProject.getNumUnits()));
                        address.setText(housingProject.getAddress());
                        municipality.setText(housingProject.getMunicipality());
                        latitude.setText(String.valueOf(housingProject.getLatitude()));
                        longitude.setText(String.valueOf(housingProject.getLongitude()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinner.setAdapter(adapter);
            }
        });
        downloadHousingTask.execute(URL);
    }



    // TODO:  Implement the handler method for the HousingDownloadListener interface
    //         - Populate the spinner with the downloaded data
    //        Hint:  Use an ArrayAdapter for this purpose


	// TODO:  Implement the item selection method to put the data from the selected
	//        housing project into the text fields of our UI
	
}

class SpinnerAdapter extends ArrayAdapter<HousingProject> {
    public SpinnerAdapter(Context context, ArrayList<HousingProject> projects) {
        super(context, R.layout.selected_housing_project, projects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HousingProject housingProject = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.selected_housing_project, parent, false);
        }
        // Lookup view for data population
        TextView textView = (TextView) convertView.findViewById(listing);

        // Populate the data into the template view using the data object
        textView.setText(String.valueOf(housingProject.getNumUnits())
                + housingProject.getAddress()
                + housingProject.getMunicipality()
                + String.valueOf(housingProject.getLongitude())
                + String.valueOf(housingProject.getLatitude()));

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}

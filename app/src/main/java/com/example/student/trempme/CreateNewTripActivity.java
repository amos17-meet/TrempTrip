package com.example.student.trempme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CreateNewTripActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    TextView tvStartPlace;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 1;


    TextView tvEndPlace;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 2;

    Button btnSetDepartureTime, btnSetDate, btnCreateNewTrip;
    TextView tvDepartureTime, tvDate;
    Spinner spinNumberOfAvailableSits;

    private int chosenHour;
    private int chosenMinute;
    private int chosenYear;
    private int chosenMonth;
    private int chosenDayOfMonth;
    private long departureTime;
    private int numberOfTrempists;
    private String toId;
    private String fromId;
    private double startLat;
    private double stringLon;


    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    LocationListener locationListener;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private boolean hasPermissions = false;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_trip);

        requestPermissions();
        setAutocompleteFragmentView();
        setGoogleAPIVar();

        if (hasPermissions) {
            Log.w("hasPermissions", "True");
            setCurrentPlace();
            setAutocompleteFragment();
        }


        setTvDepartureTime();
        setTvDate();

        setBtnSetDepartureTime();
        setBtnSetDate();
        setSpinNumberOfAvailableSits();

        setFirebaseVariables();
        setBtnCreateNewTrip();



    }



    private void setGoogleAPIVar(){
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
    }

    private void setBtnCreateNewTrip() {
        btnCreateNewTrip = findViewById(R.id.btnCreateNewTrip);
        btnCreateNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNewTrip();
            }
        });
    }

    private void setBtnSetDepartureTime() {
        btnSetDepartureTime = findViewById(R.id.btnSetDepartureTime);
        btnSetDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void setBtnSetDate() {
        btnSetDate = findViewById(R.id.btnSetDate);
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void setTvDepartureTime() {
        tvDepartureTime = findViewById(R.id.tvDepartureTime);
        Calendar myCalender = Calendar.getInstance();
        chosenHour = myCalender.get(Calendar.HOUR_OF_DAY);
        chosenMinute = myCalender.get(Calendar.MINUTE);
        String stringHour = chosenHour + "";
        String stringMinute = chosenMinute + "";
        if (chosenHour < 10) {
            stringHour = "0" + stringHour;
        }
        if (chosenMinute < 10) {
            stringMinute = "0" + stringMinute;
        }

        tvDepartureTime.setText(stringHour + ":" + stringMinute);
    }

    private void setTvDate() {
        tvDate = findViewById(R.id.tvDate);
        Calendar myCalender = Calendar.getInstance();
        chosenYear = myCalender.get(Calendar.YEAR);
        chosenMonth = myCalender.get(Calendar.MONTH);
        chosenDayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);
        chosenMonth = chosenMonth + 1;
        String stringMonth = chosenMonth + "";
        String stringDay = chosenDayOfMonth + "";
        if (chosenMonth < 10) {
            stringMonth = "0" + stringMonth;
        }
        if (chosenDayOfMonth < 10) {
            stringDay = "0" + stringDay;
        }
        chosenDayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(stringDay + "," + stringMonth + "," + chosenYear);
    }

    public void setSpinNumberOfAvailableSits() {
        spinNumberOfAvailableSits=findViewById(R.id.spinNumberOfAvailableSits);//fetch the spinner from layout file
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.number_of_trempists_array));//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNumberOfAvailableSits.setAdapter(adapter);
        //if you want to set any action you can do in this listener
        spinNumberOfAvailableSits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                numberOfTrempists = Integer.parseInt(arg0.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void setAutocompleteFragmentView() {
        tvStartPlace = findViewById(R.id.tvStartPlace);
        //Log.w("VIEW",tvStartPlace.toString());
        //autocompleteFragmentToView=findViewById(R.id.place_autocomplete_fragment_from);
        tvStartPlace.setBackgroundResource(R.drawable.border);
        //autocompleteFragmentFromView.findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        tvStartPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAutocompleteStartClicked();
                //v.setVisibility(View.GONE);
            }
        });

        tvEndPlace = findViewById(R.id.tvEndPlace);
        //Log.w("VIEW",tvEndPlace.toString());
        //autocompleteFragmentToView=findViewById(R.id.place_autocomplete_fragment_to);
        tvEndPlace.setBackgroundResource(R.drawable.border);
        //autocompleteFragmentToView.findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        tvEndPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAutocompleteEndClicked();
                //v.setVisibility(View.GONE);
            }
        });
    }


    private void setAutocompleteFragment() {
        Log.w("AutocompleteFragment", "here");
    }

    private void onAutocompleteStartClicked() {
        /*
        autocompleteFragmentFrom = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);
        Log.w("TAG","in onAutocompleteFragmentFromClicked");
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IL")
                .build();
        autocompleteFragmentFrom.setFilter(typeFilter);
        autocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.w("TAG-onPlaceSelected", "Place: " + place.getName());
                Log.w("TAG-onPlaceSelected", "PlaceID: " + place.getId());
                from=place.getId();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.w("TAG", "An error occurred: " + status);
            }
        });

*/
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("IL")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    private void onAutocompleteEndClicked() {
        Log.w("TAG", "in onAutocompleteFragmentToClicked");
        /*
        autocompleteFragmentTo = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_to);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IL")
                .build();
        autocompleteFragmentTo.setFilter(typeFilter);
        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.w("TAG-onPlaceSelected", "Place: " + place.getName());
                Log.w("TAG-onPlaceSelected", "PlaceID: " + place.getId());
                to=place.getId();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.w("TAG", "An error occurred: " + status);
            }
        });
*/


        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("IL")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_TO);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                tvStartPlace.setText(place.getName());
                fromId = place.getId();
                startLat = place.getLatLng().latitude;
                stringLon = place.getLatLng().longitude;
                Log.w("TAG-onActivityResult", "Place: " + place.getName());
                //autocompleteFragmentEditTextFrom.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Log.w("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_TO) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                tvEndPlace.setText(place.getName());
                toId = place.getId();
                Log.w("TAG-onActivityResult", "Place: " + place.getName());
                //autocompleteFragmentEditTextTo.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.w("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void showTimePicker() {
        final Calendar myCalender = Calendar.getInstance();
//        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
//        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    chosenHour = hourOfDay;
                    chosenMinute = minute;
                    tvDepartureTime.setText(hourOfDay + ":" + minute);


                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, chosenHour, chosenMinute, true);

        //TimePickerDialog timePickerDialog = new TimePickerDialog(this,, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Choose hour:");

        //timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();

    }

    public void showDatePicker() {
        final Calendar myCalender = Calendar.getInstance();
//        int year=myCalender.get(Calendar.YEAR);
//        int month=myCalender.get(Calendar.MONTH);
//        int dayOfMonth=myCalender.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                chosenYear = year;
                chosenMonth = month + 1;
                chosenDayOfMonth = dayOfMonth;
                tvDate.setText(dayOfMonth + "," + chosenMonth + "," + year);

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, chosenYear, chosenMonth, chosenDayOfMonth);

        datePickerDialog.show();

    }


    private void setFirebaseVariables() {
        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        setMyRef();
    }

    public void setMyRef() {
        Query myUser=database.getReference().child("User").child(userAuth.getUid());

        myUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("setMyRef",dataSnapshot.toString());
                String groupOfUser=dataSnapshot.getValue(String.class);
                myRef=database.getReference().child("Group").child(groupOfUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private boolean dataToMilSec() {
        boolean isMilSec = false;
        String myDate = chosenYear + "/" + chosenMonth + "/" + chosenDayOfMonth + " " + chosenHour + ":" + chosenMinute + ":00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date = sdf.parse(myDate);
            long millis = date.getTime();
            departureTime = millis;
            isMilSec = true;
        } catch (Exception e) {
            Log.w("TAG", e);
        }
        return isMilSec;

    }

    private void postNewTrip() {
        if (dataToMilSec()) {
            //Log.w("FROM", from);
            Log.w("TO", toId + "");
            final String uniqueID = UUID.randomUUID().toString();
            final Trip newTrip = new Trip(uniqueID,fromId,null,toId,null, departureTime, numberOfTrempists, userAuth.getUid(),null);
            Query myGroup=myRef;
            myGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Group group=dataSnapshot.getValue(Group.class);
                    List<Trip> trips=group.getTrips();
                    if(trips!=null){
                        myRef.child("trips").child(trips.size()+"").setValue(newTrip);
                    }
                    else{
                        trips=new ArrayList<>();
                        trips.add(newTrip);
                        myRef.child("trips").setValue(trips);
                    }
                    Toast.makeText(CreateNewTripActivity.this,"You have just created a new Trip",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateNewTripActivity.this,MainActivity.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void setCoordinationListener() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                startLat = location.getLatitude();
                stringLon = location.getLongitude();
                Log.w("Coordination", startLat + " " + stringLon);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void setCoordinationCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();

        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

//    private void setCurrentPlace()  {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try{
//            addresses = geocoder.getFromLocation(startLat, stringLon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String city = addresses.get(0).getLocality();
//            Log.w("city",city);
//        }catch (Exception e){
//            Log.w("Exception",e);
//        }
//
//    }

    private void requestPermissions() {
        Log.w("in set request", "here");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.w("need permission", "here");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            hasPermissions = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    hasPermissions = true;
                    setCoordinationListener();
                    setCoordinationCurrentLocation();
                    //setCurrentPlace();

                } else {

                    hasPermissions = false;
                }
                return;
            }

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void setCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                double maxLikelihoodScore=0;
                Place maxLikelihoodPlace = null;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if(maxLikelihoodScore<placeLikelihood.getLikelihood()){
                        maxLikelihoodScore=placeLikelihood.getLikelihood();
                        maxLikelihoodPlace=placeLikelihood.getPlace().freeze();
                    }
                    Log.i("TAG", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));

                }
                fromId=maxLikelihoodPlace.getId();
                tvStartPlace.setText(maxLikelihoodPlace.getName());
                likelyPlaces.release();
            }
        });
    }
}

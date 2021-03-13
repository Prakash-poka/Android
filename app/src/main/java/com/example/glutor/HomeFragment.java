package com.example.glutor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    TextView mornread,afterread,evenread,nightread,yesterdayavg,sugarStatus;
    private String mtext;
    private String atext;
    private String etext;
    private String ntext;
    private String tempcount;
    private String tDate;
    private String userId;
    private String temp ="N/A";
    private String yesterday;
    int tc,ct=0,mdata=0,edata=0,adata=0,ndata=0,avg=0;
    String Reading;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text";
    public static final String TEXT2 = "text2";
    public static final String TEXT3 = "text3";
    public static final String TEXT4 = "text4";
    public static final String COUNTER = "count";
    public static final String STOREDDATE = "14-12-2020";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mornread = rootView.findViewById(R.id.morningreading);
        afterread = rootView.findViewById(R.id.afternoonreading);
        evenread = rootView.findViewById(R.id.eveningreading);
        nightread = rootView.findViewById(R.id.nightreading);
        yesterdayavg = rootView.findViewById(R.id.yesterdaysreading);
        sugarStatus = rootView.findViewById(R.id.sugarstatus);


        //FireBase
        fauth = FirebaseAuth.getInstance();
        fstore =FirebaseFirestore.getInstance();
        userId = fauth.getCurrentUser().getUid();

   Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        tDate = df.format(c);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String yDate = sharedPreferences.getString(STOREDDATE, "13-Dec-2020");
        ct = Integer.parseInt(sharedPreferences.getString(COUNTER,"0"));
        SharedPreferences.Editor editor1 = sharedPreferences.edit();


        try {
            Date today = df.parse(tDate);
            Date lastUsed = df.parse(yDate);
            if (lastUsed.compareTo(today)<0){
                System.out.println("today is greater");
                if(ct!=0){
                    mdata = Integer.parseInt(sharedPreferences.getString(TEXT1,"0"));
                    adata = Integer.parseInt(sharedPreferences.getString(TEXT2,"0"));
                    edata = Integer.parseInt(sharedPreferences.getString(TEXT3,"0"));
                    ndata =  Integer.parseInt(sharedPreferences.getString(TEXT4,"0"));
                    avg =(int) (mdata+adata+edata+ndata)/ct;
                    ct=0;
                    editor1.putString(TEXT1,"N/A");
                    mornread.setBackgroundResource(R.drawable.rounded_textview);
                    mornread.setText(temp);
                    editor1.putString(TEXT2,"N/A");
                    afterread.setBackgroundResource(R.drawable.rounded_textview);
                    afterread.setText(temp);
                    editor1.putString(TEXT3,"N/A");
                    evenread.setBackgroundResource(R.drawable.rounded_textview);
                    evenread.setText(temp);
                    editor1.putString(TEXT4,"N/A");
                    nightread.setBackgroundResource(R.drawable.rounded_textview);
                    nightread.setText(temp);
                    editor1.putString(COUNTER,"0");
                    editor1.apply();
                    editor1.commit();
                }
                else
                    avg = 0;
                DocumentReference documentReference = fstore.collection("users").document(userId);
                Map<String,Object> user = new HashMap<>();
                user.put(yDate,String.valueOf(avg));
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });



            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
            String incontrol = "In control";
        String notcontrol ="Out of Control";

        try {
            Date ysdate = df.parse(tDate);
            Calendar ycalendar = Calendar.getInstance();
            ycalendar.setTime(ysdate);
            ycalendar.add(Calendar.DATE,-1);
            yesterday = df.format(ycalendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(Objects.requireNonNull(getActivity()), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getString(yesterday)!=null&&!(value.getString(yesterday)).equals("0")) {
                    yesterdayavg.setText(value.getString(yesterday));
                    if(Integer.parseInt(value.getString(yesterday))>=90 && Integer.parseInt(value.getString(yesterday))<=140) {
                        sugarStatus.setText(incontrol);
                        sugarStatus.setTextColor(getResources().getColor(R.color.green));
                    }
                    else{
                        sugarStatus.setText(notcontrol);
                        sugarStatus.setTextColor(getResources().getColor(R.color.red));
                    }


                }


            }
        });

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences1.edit();
        editor2.putString(STOREDDATE,tDate);
        editor2.apply();
        editor2.commit();






        mornread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                mydialog.setTitle("Enter Morning Reading");
                final EditText morninput = new EditText(getContext());
                morninput.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(morninput);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mornread.getText().toString().equals("N/A"))
                            ct=1;
                        else
                            ct = 0;
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        tDate = df.format(c);
                        Reading=morninput.getText().toString();
                        mornread.setBackgroundResource(R.drawable.rounded_textview_g);
                        mornread.setText(Reading);
                        mornread.setTextColor(getResources().getColor(R.color.white));

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT1,Reading);
                        editor.putString(STOREDDATE,tDate);
                        editor.apply();

                        tempcount = sharedPreferences.getString(COUNTER,"0");
                        tc = Integer.parseInt(tempcount);
                        tc=tc+ct;
                        tempcount= String.valueOf(tc);
                        editor.putString(COUNTER,tempcount);
                        editor.putString(STOREDDATE,tDate);
                        editor.apply();

                    }
                });
               mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
            mydialog.show();



            }
        });

        afterread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                mydialog.setTitle("Enter AfterNoon Reading");
                final EditText afterinput = new EditText(getContext());
                afterinput.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(afterinput);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (afterread.getText().toString().equals("N/A"))
                            ct=1;
                        else
                            ct = 0;
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        tDate = df.format(c);
                        Reading=afterinput.getText().toString();
                        afterread.setBackgroundResource(R.drawable.rounded_textview_g);
                        afterread.setText(Reading);
                        afterread.setTextColor(getResources().getColor(R.color.white));

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT2,Reading);
                        editor.putString(STOREDDATE,tDate);
                        editor.apply();

                        tempcount = sharedPreferences.getString(COUNTER,"0");
                        tc = Integer.parseInt(tempcount);
                        tc=tc+ct;
                        tempcount= String.valueOf(tc);
                        editor.putString(COUNTER,tempcount);
                        editor.apply();

                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.show();
            }
        });

        evenread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                mydialog.setTitle("Enter Evening Reading");
                final EditText eveninput = new EditText(getContext());
                eveninput.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(eveninput);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (evenread.getText().toString().equals("N/A"))
                            ct=1;
                        else
                            ct = 0;
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        tDate = df.format(c);
                        Reading=eveninput.getText().toString();
                        evenread.setBackgroundResource(R.drawable.rounded_textview_g);
                        evenread.setText(Reading);
                        evenread.setTextColor(getResources().getColor(R.color.white));


                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT3,Reading);
                        editor.putString(STOREDDATE,tDate);
                        editor.apply();


                        tempcount = sharedPreferences.getString(COUNTER,"0");
                        tc = Integer.parseInt(tempcount);
                        tc=tc+ct;
                        tempcount= String.valueOf(tc);
                        editor.putString(COUNTER,tempcount);
                        editor.apply();

                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.show();

            }
        });


        nightread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                mydialog.setTitle("Enter Night Reading");
                final EditText nightinput = new EditText(getContext());
                nightinput.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(nightinput);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nightread.getText().toString().equals("N/A"))
                            ct=1;
                        else
                            ct = 0;
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        tDate = df.format(c);
                        Reading=nightinput.getText().toString();
                        nightread.setBackgroundResource(R.drawable.rounded_textview_g);
                        nightread.setText(Reading);
                        nightread.setTextColor(getResources().getColor(R.color.white));

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT4,Reading);
                        editor.putString(STOREDDATE,tDate);
                        editor.apply();

                        tempcount = sharedPreferences.getString(COUNTER,"0");
                        tc = Integer.parseInt(tempcount);
                        tc=tc+ct;
                        tempcount= String.valueOf(tc);
                        editor.putString(COUNTER,tempcount);
                        editor.apply();

                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.show();
            }
        });
        loadData();
        updateViews();
        return rootView;
    }
 public void loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        mtext = sharedPreferences.getString(TEXT1,"N/A");
        atext = sharedPreferences.getString(TEXT2,"N/A");
        etext = sharedPreferences.getString(TEXT3,"N/A");
        ntext = sharedPreferences.getString(TEXT4,"N/A");


    }
    public void updateViews(){

        if(!mtext.equals("N/A"))
        {mornread.setBackgroundResource(R.drawable.rounded_textview_g);
            mornread.setTextColor(getResources().getColor(R.color.white));}
        else
        {mornread.setBackgroundResource(R.drawable.rounded_textview);
            }
        mornread.setText(mtext);
        if(!atext.equals("N/A")){
        afterread.setBackgroundResource(R.drawable.rounded_textview_g);
            afterread.setTextColor(getResources().getColor(R.color.white));}
        else
        {afterread.setBackgroundResource(R.drawable.rounded_textview);
            }
        afterread.setText(atext);
        if(!etext.equals("N/A")){
        evenread.setBackgroundResource(R.drawable.rounded_textview_g);
            evenread.setTextColor(getResources().getColor(R.color.white));}
        else {evenread.setBackgroundResource(R.drawable.rounded_textview);
        }
        evenread.setText(etext);
        if(!ntext.equals("N/A"))
        {
        nightread.setBackgroundResource(R.drawable.rounded_textview_g);
            nightread.setTextColor(getResources().getColor(R.color.white));}
        else {afterread.setBackgroundResource(R.drawable.rounded_textview);
        }
        nightread.setText(ntext);
    }


}
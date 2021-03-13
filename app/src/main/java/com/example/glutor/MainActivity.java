package com.example.glutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    //Variables
    Animation fromLeft,fromRight,toLeft,toRight;
    Button signIn,forgotPass,newUser,signUp,oldUser;
    TextInputLayout emailid,password,fullName,Age,Occupation,NewEmailid,NewPassword;
    TextView signinMsg;
    LinearLayout signInLayout;
    RelativeLayout signUpLayout;
    String tempup = "Sign Up To Continue";
    String tempin = "Sign In To Continue";
    String userId;
    String emailInput,passwordInput,fullNameInput,ageinput,occupationInput,emailsignUpInput,passwordsignUpInput;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    public void oldclick(View V){

        signinMsg.setText(tempin);
        signUpLayout.setAnimation(toRight);
        signUpLayout.setVisibility(View.GONE);
        signInLayout.setVisibility(View.VISIBLE);
        signInLayout.setAnimation(fromLeft);
    }

    public void newclick(View v)
    {
        signInLayout.setAnimation(toLeft);
        signInLayout.setVisibility(View.GONE);
        signUpLayout.setVisibility(View.VISIBLE);

        signinMsg.setText(tempup);
        signUpLayout.setAnimation(fromRight);
    }

    private boolean validateEmail(){
        emailInput = emailid.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()){
            emailid.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailid.setError("Invalid Email id");
            return false;
        }
        else {
        emailid.setError(null);
        return true;
        }
    }

    private boolean validatePassword(){
         passwordInput = password.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        }
        else {
           password.setError(null);
            return true;
        }
    }

    private boolean validateName(){
        fullNameInput = fullName.getEditText().getText().toString().trim();
        if (fullNameInput.isEmpty()){
            fullName.setError("Field can't be empty");
            return false;
        }
        else {
            fullName.setError(null);
            return true;
        }
    }
    private boolean validateAge(){
        ageinput = Age.getEditText().getText().toString().trim();
        if (ageinput.isEmpty()){
            Age.setError("Field can't be empty");
            return false;
        }
        else if (ageinput.equals("00")||ageinput.equals("0")){
            Age.setError("Age can't be 0");
            return false;
        }
        else {
            Age.setError(null);
            return true;
        }
    }
    private boolean validateOccupataion(){
        occupationInput = Occupation.getEditText().getText().toString().trim();
        if (occupationInput.isEmpty()){
            Occupation.setError("Field can't be empty");
            return false;
        }
        else {
            Occupation.setError(null);
            return true;
        }
    }

    private boolean validateNewEmail(){
        emailsignUpInput = NewEmailid.getEditText().getText().toString().trim();
        if (emailsignUpInput.isEmpty()){
            NewEmailid.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailsignUpInput).matches()){
            NewEmailid.setError("Invalid Email id");
            return false;
        }
        else {
            NewEmailid.setError(null);
            return true;
        }
    }

    private boolean validateNewPassword(){
        passwordsignUpInput = NewPassword.getEditText().getText().toString().trim();
        if (passwordsignUpInput.isEmpty()){
            NewPassword.setError("Field can't be empty");
            return false;
        }
        else {
            NewPassword.setError(null);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

        //hooks
       signIn=(Button) findViewById(R.id.signinbutton);
       newUser=(Button) findViewById(R.id.newuser);
       signUp=(Button) findViewById(R.id.signupbutton);
       oldUser=(Button) findViewById(R.id.olduser);
       signInLayout=(LinearLayout) findViewById(R.id.Signinlayout);
       signUpLayout=(RelativeLayout) findViewById(R.id.Signuplayout);
        emailid= findViewById(R.id.emailidedittext);
        password= findViewById(R.id.passwordedittext);
        signinMsg= findViewById(R.id.signinmsg);
        fullName= findViewById(R.id.fullname);
        Age= findViewById(R.id.age);
        Occupation = findViewById(R.id.occupation);
        NewEmailid = findViewById(R.id.signupEmail);
        NewPassword = findViewById(R.id.Signuppassword);

        AllianceLoader alliance = (AllianceLoader) findViewById(R.id.dotprogresssignIn);
        AllianceLoader alliance1 = (AllianceLoader) findViewById(R.id.dotprogresssignUp);



        //Animations
        fromLeft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
        fromRight = AnimationUtils.loadAnimation(this,R.anim.fromright);
        toRight = AnimationUtils.loadAnimation(this,R.anim.toright);
        toLeft = AnimationUtils.loadAnimation(this,R.anim.toleft);


        //Firebase
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

        //firestore
        fstore = FirebaseFirestore.getInstance();






        //onclick listeners
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail()) {
                    if (validatePassword()) {
                        //progressbar
                        alliance.setVisibility(View.VISIBLE);
                        //Firebase SignIn
                        fAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Logged In Succesful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "InValid" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    alliance.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateName()){
                    if(validateAge()){
                        if(validateOccupataion()){
                            if(validateNewEmail()) {
                                if (validateNewPassword()) {

                                    alliance1.setVisibility(View.VISIBLE);


                                    //Firebase SignUp

                                    fAuth.createUserWithEmailAndPassword(emailsignUpInput, passwordsignUpInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                userId = fAuth.getCurrentUser().getUid();
                                                DocumentReference documentReference = fstore.collection("users").document(userId);
                                                Map<String,Object> user = new HashMap<>();
                                                user.put("fnamedb",fullNameInput);
                                                user.put("agedb",ageinput);
                                                user.put("occupationdb",occupationInput);
                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG,"Onsuccess user profile created for "+userId);
                                                    }
                                                });
                                                Toast.makeText(getApplicationContext(), "Succesful", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "InValid" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                alliance1.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });

                                }
                            }
                        }
                    }
                }




            }
        });




    }
}
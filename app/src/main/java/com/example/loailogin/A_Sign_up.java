package com.example.ta5asosi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ta5asosi.Common.Current;
import com.example.ta5asosi.Model.Student;
import com.example.ta5asosi.Model.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.HashMap;
public class A_Sign_up extends AppCompatActivity {
    EditText Edt_Student_Sitting ,Edt_Student_SSN ,Edt_Student_Password ,Edt_Student_Password_Confirm;
    Button Btn_Sign_Up;
    FirebaseDatabase database;
    DatabaseReference Student_Firebase;
    String Table_FireBase= "Student";
    private ProgressDialog mD ;
    String CodeConfirm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_a__sign_up);
        //Ini
        Edt_Student_Sitting = (EditText)findViewById(R.id.edt_student_sitting);
        Edt_Student_SSN = (EditText)findViewById(R.id.edt_student_ssn);
        Edt_Student_Password = (EditText)findViewById(R.id.edt_Student_password);
        Edt_Student_Password_Confirm=(EditText)findViewById(R.id.edt_Student_password_confirm);
        mD=new ProgressDialog(A_Sign_up.this);
        Btn_Sign_Up = (Button)findViewById(R.id.btn_sign_up);
        //Database Fire base
        database = FirebaseDatabase.getInstance();
        Student_Firebase = database.getReference();
        CodeConfirm=getIntent().getStringExtra("IDCode");
        Btn_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edt_Student_Sitting.getText().toString().isEmpty())
                {
                    StyleableToast.makeText(A_Sign_up.this, "Please Fill Sitting Student", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();

                }else if (Edt_Student_SSN.getText().toString().isEmpty())
                {
                    StyleableToast.makeText(A_Sign_up.this, "Please Fill SSN Student", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();

                }else if (Edt_Student_Password.getText().toString().isEmpty())
                {
                    StyleableToast.makeText(A_Sign_up.this, "Please Fill Password", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();

                }else if (Edt_Student_Password_Confirm.getText().toString().isEmpty())
                {
                    StyleableToast.makeText(A_Sign_up.this, "Please Fill Repeat Password", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();

                }
                else {
                    if(Edt_Student_Password.getText().toString().equals(Edt_Student_Password_Confirm.getText().toString())){
                        if(Edt_Student_SSN.getText().toString().length()==10){
                            if(Edt_Student_Sitting.getText().toString().length()==6)
                            {
                            mD.setTitle("Sign Up");
                            mD.setMessage("Please Waiting!!!");
                            mD.show();
                            Firebase();}
                            else{
                                StyleableToast.makeText(A_Sign_up.this, "The sitting number is not 6 digits", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                            }
                        }else{
                            StyleableToast.makeText(A_Sign_up.this, "The SSN is not 10 digits", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();


                        }

                    }else{
                        StyleableToast.makeText(A_Sign_up.this, "Password does not match", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                    }


                }
            }
        });
    }

    private void Firebase()
    {
        Student_Firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Table_FireBase).child(Edt_Student_Sitting.getText().toString()).exists()){
                    final Student Class_Stu = dataSnapshot.child(Table_FireBase).child(Edt_Student_Sitting.getText().toString()).getValue(Student.class);
                        Class_Stu.setStuNumber(Edt_Student_Sitting.getText().toString());
                    if(Class_Stu.getSsn().equals(Edt_Student_SSN.getText().toString())){
                        HashMap<String,Object> addpass = new HashMap<>();
                        addpass.put("pass",Edt_Student_Password.getText().toString());
                        Student_Firebase.child(Table_FireBase).child(Edt_Student_Sitting.getText().toString()).updateChildren(addpass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    DatabaseReference FireBaseCodeConfirm = FirebaseDatabase.getInstance().getReference().child("ConfirmCode");
                                    HashMap<String ,Object>HashMap_Code = new HashMap<>();
                                    HashMap_Code.put("flag","True");
                                    FireBaseCodeConfirm.child(CodeConfirm).updateChildren(HashMap_Code);
                                    Intent obj = new Intent(A_Sign_up.this,A_Sign_in.class);
                                    StyleableToast.makeText(A_Sign_up.this, "Successfully registered", Toast.LENGTH_SHORT,R.style.myToast_Login).show();
                                    startActivity(obj);
                                finish();
                                }
                            }
                        });
                        mD.dismiss();
                    }
                    else {
                        StyleableToast.makeText(A_Sign_up.this, "SSN and sitting number are not compatible", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                        mD.dismiss();
                    }}
                else{
                    StyleableToast.makeText(A_Sign_up.this, "The student number "+Edt_Student_Sitting.getText().toString()+" is no exists", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                    mD.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}

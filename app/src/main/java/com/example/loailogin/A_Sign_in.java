package com.example.ta5asosi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ta5asosi.Common.Current;
import com.example.ta5asosi.Model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;

import io.paperdb.Paper;

public class A_Sign_in extends AppCompatActivity {
EditText Edt_Student_Id , Edt_Student_Password;
Button Btn_Sign_in, Btn_if_not_Regs ,Btn_Guest;
FirebaseDatabase Database;
DatabaseReference Student_Firebase;
String Table_FireBase_Student="Student";
CheckBox checkBox ;
private ProgressDialog mD ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_a__sign_in);
      //Ins

        Edt_Student_Id= (EditText)findViewById(R.id.edt_student_id_sign_in);
        Btn_if_not_Regs = (Button)findViewById(R.id.btn_if_not_Regs);
        Edt_Student_Password=(EditText)findViewById(R.id.edt_Student_password_sign_in);
        Btn_Guest=findViewById(R.id.btn_guest);
        Btn_Sign_in = (Button)findViewById(R.id.btn_sign_in_1);



        mD=new ProgressDialog(A_Sign_in.this);



        Database = FirebaseDatabase.getInstance();
        Student_Firebase = Database.getReference();
        Btn_Guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(A_Sign_in.this,A_Guest.class);
                startActivity(obj);
            }
        });
        Btn_if_not_Regs.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent obj = new Intent(A_Sign_in.this,A_Confirm_Code.class);
        startActivity(obj);
    }
});
      Paper.init(this);
        checkBox = findViewById(R.id.checkBox);



        Btn_Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edt_Student_Id.getText().toString().isEmpty())
                {
                    StyleableToast.makeText(A_Sign_in.this, "Please Fill Id Student", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();


                }else if (Edt_Student_Password.getText().toString().isEmpty()){
                    StyleableToast.makeText(A_Sign_in.this, "Please Fill Password", Toast.LENGTH_SHORT,R.style.myToast_Home_Warning).show();

                }else{
                    mD.setTitle("Sign In");
                    mD.setMessage("Please Waiting!!!");
                    mD.show();
                    Firebase();

                }

            }
        });

        String User=Paper.book().read(Current.User_Key);
    String Password =Paper.book().read(Current.PassWord_Key);
    if(User!=null&&Password!=null){

        if(!User.isEmpty()&&!Password.isEmpty()){
           Firebase2(User,Password);
        }
    }

    }
    private void Firebase2(final String u, final String Password) {



        Student_Firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Table_FireBase_Student).child(u).exists())
                {
                    Student Student_Class = dataSnapshot.child(Table_FireBase_Student).child(u).getValue(Student.class);
                    Student_Class.setStuNumber(u);
                    if(Student_Class.getPass().equals(Password)){
                        mD.dismiss();
                        Intent A_Home_Student = new Intent(A_Sign_in.this,A_Home_Student.class);
                        Current.Current_Stu = Student_Class ;
                        startActivity(A_Home_Student);
                        StyleableToast.makeText(A_Sign_in.this, "Signed in successfully ..", Toast.LENGTH_SHORT,R.style.myToast_Login).show();


                    }else{
                        mD.dismiss();
                        StyleableToast.makeText(A_Sign_in.this, "Wrong password!!", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                    }
                }else{
                    mD.dismiss();
                    StyleableToast.makeText(A_Sign_in.this, "This user does not exist", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Firebase() {
        if(checkBox.isChecked()){
            Paper.book().write(Current.User_Key,Edt_Student_Id.getText().toString());
            Paper.book().write(Current.PassWord_Key,Edt_Student_Password.getText().toString());
        }
        Student_Firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Table_FireBase_Student).child(Edt_Student_Id.getText().toString()).exists())
                {
                    Student Student_Class = dataSnapshot.child(Table_FireBase_Student).child(Edt_Student_Id.getText().toString()).getValue(Student.class);
                    Student_Class.setStuNumber(Edt_Student_Id.getText().toString());
                    if(Student_Class.getPass().equals(Edt_Student_Password.getText().toString())){
                        mD.dismiss();
                        Intent A_Home_Student = new Intent(A_Sign_in.this,A_Home_Student.class);
                        Current.Current_Stu = Student_Class ;
                        startActivity(A_Home_Student);
                        StyleableToast.makeText(A_Sign_in.this, "Signed in successfully ..", Toast.LENGTH_SHORT,R.style.myToast_Login).show();
                        
                    }else{
                        mD.dismiss();
                        StyleableToast.makeText(A_Sign_in.this, "Wrong password!!", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                    }
                }else{
                    mD.dismiss();
                    StyleableToast.makeText(A_Sign_in.this, "This user does not exist", Toast.LENGTH_SHORT,R.style.myToast_Login_Erorr).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

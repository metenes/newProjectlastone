package com.aksdev.projectsecondaryusage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SecondaryUsage extends AppCompatActivity implements Calculatable {
    static double totalSecondaryUsage;
    User user;

    //Coefficients
    final static double NUTRITION_CO = 0.00038; final static double PHARMACEUTICALS_CO = 0.00144; final static double CLOTHING_CO = 0.00022;
    final static double PAPER_PRODUCTS_CO = 0.00015; final static double IT_EQUIPMENT_CO = 0.00063; final static double MOTOR_VEHICLES = 0.00016;
    final static double FURNITURE_CO = 0.00017; final static double HOTELS_CO = 0.00021; final static double EDUCATION_CO = 0.00014;

    //Values
    static int nutrition, pharmaceuticals, clothing, paperProducts, itEquipment, motorVehicles, furniture, hotels, education;

    //Declarations of Texts
    TextView primaryText, pharmaceuticalsText, clothingText, paperProductText, itEquipmentText, motorVehiclesText,
    furnitureText, hotelsText, educationText, nutritionText;

    //Declarations of EditTexts
    EditText nutritionEditText, pharmaceuticalsEditText, clothingEditText, paperProductsEditText, itEquipmentEditText,
    motorVehiclesEditText, furnitureEditText, hotelsEditText, educationEditText;

    //Declaration of Buttons
    Button submitButton;

    DatabaseReference updateUserDataBaseRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_usage);
        Toast.makeText(this, "Please write how much you have spent on given areas this month", Toast.LENGTH_LONG).show();

        user = LogIn.userProfile;
        //FireBase references
        FirebaseDatabase firebaseDatabaseUser = FirebaseDatabase.getInstance();
        DatabaseReference userDataBaseRef =  firebaseDatabaseUser.getReference("Users");
        FirebaseAuth userAuth = FirebaseAuth.getInstance();


        if(!Objects.isNull(userAuth)){
            FirebaseUser firebaseUser = userAuth.getCurrentUser();
            if(!Objects.isNull(firebaseUser)){
                userId = firebaseUser.getDisplayName();
            }
        }
        if(!Objects.isNull(userId)){
            updateUserDataBaseRef = userDataBaseRef.child("Users").child(userId);
        }
        else{
            updateUserDataBaseRef = null;
        }

        //Instantiations
        nutritionEditText = findViewById(R.id.nutritionEditText);
        pharmaceuticalsEditText = findViewById(R.id.pharmaceuticalsEditText);
        clothingEditText = findViewById(R.id.clothingEditText);
        paperProductsEditText = findViewById(R.id.paperProductsEditText);
        itEquipmentEditText = findViewById(R.id.itEquipmentEditText);
        motorVehiclesEditText = findViewById(R.id.motorVehiclesEditText);
        furnitureEditText = findViewById(R.id.furnitureEditText);
        hotelsEditText = findViewById(R.id.hotelsEditText);
        educationEditText = findViewById(R.id.educationEditText);

        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("I am working!!!");
                if(!nutritionEditText.getText().toString().trim().equals("")){
                    nutrition = Integer.parseInt(nutritionEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("nutritionEmission").setValue(nutrition);
                    user.setNutritionEmission(nutrition + user.getNutritionEmission());
                }
                if(!pharmaceuticalsEditText.getText().toString().trim().equals("")){
                    pharmaceuticals = Integer.parseInt(pharmaceuticalsEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("pharmaceuticalsEmission").setValue(pharmaceuticals);
                    user.setPharmaceuticalEmission(user.getPharmaceuticalEmission() + pharmaceuticals);
                }
                if(!clothingEditText.getText().toString().trim().equals("")){
                    clothing = Integer.parseInt (clothingEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("clothingEmission").setValue(clothing);
                    user.setClothingEmission(clothing + user.getClothingEmission());
                }
                if(!paperProductsEditText.getText().toString().trim().equals("")){
                    paperProducts = Integer.parseInt(paperProductsEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("paperEmission").setValue(paperProducts);
                    user.setPaperProductEmission(paperProducts + user.getPaperProductEmission());
                }
                if(!itEquipmentEditText.getText().toString().trim().equals("")){
                    itEquipment = Integer.parseInt(itEquipmentEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("itEmission").setValue(itEquipment);
                    user.setItEmission(itEquipment + user.getItEmission());
                }
                if(!motorVehiclesEditText.getText().toString().trim().equals("")){
                    motorVehicles = Integer.parseInt(motorVehiclesEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("motorEmission").setValue(motorVehicles);
                    user.setMotorEmission(motorVehicles + user.getMotorEmission());
                }
                if(!furnitureEditText.getText().toString().trim().equals("")){
                    furniture = Integer.parseInt(furnitureEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("furnitureEmission").setValue(furniture);
                    user.setFurnitureEmission(furniture + user.getFurnitureEmission());
                }
                if(!hotelsEditText.getText().toString().trim().equals("")){
                    hotels = Integer.parseInt(hotelsEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("hotelEmission").setValue(hotels);
                    user.setHotelEmission(hotels + user.getHotelEmission());
                }
                if(!educationEditText.getText().toString().trim().equals("")){
                    education = Integer.parseInt(educationEditText.getText().toString());
                    userDataBaseRef.child(userAuth.getCurrentUser().getUid()).child("educationEmission").setValue(education);
                    user.setEducationEmission(education + user.getEducationEmission());
                }
                calculate();
                user.setSecondaryEmission(totalSecondaryUsage);
                user.changeScore((int) totalSecondaryUsage * 10 * (-1));
                DailyUsageMain.secondaryDisplay.setText(Math.round(totalSecondaryUsage*100.0)/100.0 + "");
                Toast.makeText(SecondaryUsage.this, "Successfully submitted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //nutrition, pharmaceuticals, clothing, paperProducts, itEquipment, motorVehicles, furniture, hotels, education;

    public void calculate(){
        totalSecondaryUsage = (nutrition * NUTRITION_CO) + (pharmaceuticals * PHARMACEUTICALS_CO) + (clothing * CLOTHING_CO)
                            + (paperProducts * PAPER_PRODUCTS_CO) + (itEquipment * IT_EQUIPMENT_CO) + (motorVehicles * MOTOR_VEHICLES)
                            + (furniture * FURNITURE_CO) + (hotels * HOTELS_CO) + (education * EDUCATION_CO);
    }


}
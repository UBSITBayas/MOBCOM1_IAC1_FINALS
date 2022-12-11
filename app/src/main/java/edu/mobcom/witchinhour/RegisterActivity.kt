package edu.mobcom.witchinhour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import edu.mobcom.witchinhour.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")



        emailFocusListener()
        phoneFocusListener()
        passwordFocusListener()
        conPasswordFocusListener()

        binding.btnRegisterRegisterPage.setOnClickListener {
            submitForm()
        }

        binding.btnLoginRegisterPage.setOnClickListener {
            val iRegister = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(iRegister)
        }
    }

    private fun submitForm()
    {
        binding.containerEmailAddRegisterPage.helperText = validEmail()
        binding.containerContactNumberRegisterPage.helperText = validPhone()
        binding.containerPasswordRegisterPage.helperText = validPassword()
        binding.containerConPasswordRegisterPage.helperText = validConPassword()


        val validEmail = binding.containerEmailAddRegisterPage.helperText == null
        val validContact = binding.containerContactNumberRegisterPage.helperText == null
        val validPassword = binding.containerPasswordRegisterPage.helperText == null
        val validConPassword = binding.containerConPasswordRegisterPage.helperText == null

        if (validEmail && validContact && validPassword && validConPassword)
            emailFirebase()
        else
            invalidForm()
    }

    private fun emailFirebase()
    {
        val Email = binding.inEmailAddRegisterPage.text.toString()
        val Contact = binding.inContactNumberRegisterPage.text.toString()
        val Password = binding.inPasswordRegisterPage.text.toString()
        val ConPassword = binding.inConPasswordRegisterPage.text.toString()
        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this){
            if(it.isSuccessful){
                resetForm()
                datapass()
                Toast.makeText(this, "New Account Created", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "User Creation Failed", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun  datapass()
    {
        val Email = binding.inEmailAddRegisterPage
        val Contact = binding.inContactNumberRegisterPage


        val iReg = Intent(this@RegisterActivity, ProfileActivity::class.java)
        with(iReg){
            putExtra("EmailA",Email.text.toString())
            putExtra("ContactN",Contact.text.toString())
        }
        startActivity(iReg)
    }

    private fun invalidForm()
    {
        var message = ""
        if(binding.containerEmailAddRegisterPage.helperText != null)
            message += "\n\nEmail: " + binding.containerEmailAddRegisterPage.helperText
        if(binding.containerContactNumberRegisterPage.helperText != null)
            message += "\n\nEmail: " + binding.containerContactNumberRegisterPage.helperText
        if(binding.containerPasswordRegisterPage.helperText != null)
            message += "\n\nPassword: " + binding.containerPasswordRegisterPage.helperText
        if(binding.containerConPasswordRegisterPage.helperText != null)
            message += "\n\nPhone: " + binding.containerConPasswordRegisterPage.helperText


    }

    private fun resetForm()
    {
        var message = "Email: " + binding.inEmailAddRegisterPage.text
        message += "\nPhone: " + binding.inContactNumberRegisterPage.text
        message += "\nPassword: " + binding.inPasswordRegisterPage.text
        message += "\nConfirm Password: " + binding.inConPasswordRegisterPage.text
        AlertDialog.Builder(this)
            .setTitle("Form submitted")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                binding.inEmailAddRegisterPage.text = null
                binding.inContactNumberRegisterPage.text = null
                binding.inPasswordRegisterPage.text = null
                binding.inConPasswordRegisterPage.text = null

                binding.containerEmailAddRegisterPage.helperText = getString(R.string.required)
                binding.containerContactNumberRegisterPage.helperText = getString(R.string.required)
                binding.containerPasswordRegisterPage.helperText = getString(R.string.required)
                binding.containerConPasswordRegisterPage.helperText = getString(R.string.required)
            }
            .show()
    }

    private fun emailFocusListener()
    {
        binding.inEmailAddRegisterPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerEmailAddRegisterPage.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.inEmailAddRegisterPage.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }
    private fun phoneFocusListener()
    {
        binding.inContactNumberRegisterPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerContactNumberRegisterPage.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.inContactNumberRegisterPage.text.toString()
        if (phoneText.isEmpty()){
            return "Cannot Accept Empty Fields"
        }
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return "Must be all Digits"
        }
        if(phoneText.length != 11)
        {
            return "Must be 11 Digits"
        }
        if (phoneText.isEmpty()){
            return "Cannot Accept Empty Fields"
        }
        return null
    }

    private fun passwordFocusListener()
    {
        binding.inPasswordRegisterPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerPasswordRegisterPage.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.inPasswordRegisterPage.text.toString()
        if (passwordText.isEmpty()){
            return "Cannot Accept Empty Fields"
        }
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }

    private fun conPasswordFocusListener()
    {
        binding.inConPasswordRegisterPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerConPasswordRegisterPage.helperText = validConPassword()
            }
        }
    }

    private fun validConPassword(): String?
    {
        val passwordText = binding.inPasswordRegisterPage.text.toString()
        val conpasswordText = binding.inConPasswordRegisterPage.text.toString()
        if (conpasswordText.isEmpty()){
            return "Cannot Accept Empty Fields"
        }
        if (conpasswordText.isNotEmpty()){
            if(!conpasswordText.equals(passwordText))
            {
                return "Password does not match"
            }
        }

        return null
    }
}
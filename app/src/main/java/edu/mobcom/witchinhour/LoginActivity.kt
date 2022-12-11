package edu.mobcom.witchinhour

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.mobcom.witchinhour.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        emailFocusListener()
        passwordFocusListener()

        binding.btnLoginLoginPage.setOnClickListener {
            submitForm()
             }

        binding.btnRegisterLoginPage.setOnClickListener {
            val iRegister = Intent(this@LoginActivity, ProfileActivity::class.java)
            startActivity(iRegister)
        }
    }



    private fun submitForm()
    {
        binding.containerEmailAddLoginPage.helperText = validEmail()
        binding.containerPasswordLoginPage.helperText = validPassword()

        val validEmail = binding.containerEmailAddLoginPage.helperText == null
        val validPassword = binding.containerPasswordLoginPage.helperText == null

        if (validEmail && validPassword)
            emailFirebase()
        else
            invalidForm()
    }

    private fun invalidForm()
    {
        var message = ""
        if(binding.containerEmailAddLoginPage.helperText != null)
            message += "\n\nEmail: " + binding.containerEmailAddLoginPage.helperText
        if(binding.containerPasswordLoginPage.helperText != null)
            message += "\n\nPassword: " + binding.containerPasswordLoginPage.helperText

    }

    private fun emailFirebase()
    {
        val email = binding.inEmailAddLoginPage.text.toString()
        val pwd = binding.inPasswordLoginPage.text.toString()
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this, "welcome $email", Toast.LENGTH_LONG).show()
                resetForm()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetForm()
    {
                binding.inEmailAddLoginPage.text = null
                binding.inPasswordLoginPage.text = null

                binding.containerEmailAddLoginPage.helperText = getString(R.string.required)
                binding.containerPasswordLoginPage.helperText = getString(R.string.required)

        val iLogin = Intent(this@LoginActivity, ProfileActivity::class.java)
        startActivity(iLogin)
    }

    private fun emailFocusListener()
    {
        binding.inEmailAddLoginPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerEmailAddLoginPage.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.inEmailAddLoginPage.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener()
    {
        binding.inPasswordLoginPage.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.containerPasswordLoginPage.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.inPasswordLoginPage.text.toString()
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

}





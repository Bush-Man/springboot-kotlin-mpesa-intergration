package com.bushman.mpesa_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bushman.mpesa_kotlin.ui.theme.MpesakotlinTheme
import dagger.hilt.android.AndroidEntryPoint


val MpesaPrimary = Color(0xFF009688)
val MpesaSecondary = Color(0xFF00796B)
val MpesaAccent = Color(0xFFFF9800)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val SurfaceColor = Color(0xFFF5F5F5)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpesakotlinTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceColor),
                    topBar = { TopBar() }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(top = 16.dp)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ColumnDisplay(
                                title = "Mobile App",
                                icon = Icons.Default.Check,
                                desc = "Kotlin-Jetpack Compose"
                            )
                            ColumnDisplay(
                                title = "Website Demo",
                                icon = Icons.Default.Check,
                                desc = "React-Javascript",
                                link = "http://link.com"
                            )
                            ColumnDisplay(
                                title = "Backend Api",
                                icon = Icons.Default.Check,
                                desc = "Springboot-Java",
                                link = "https://github.com"
                            )
                            Text(
                                text = "Enter amount as 1 for testing",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        item {
                            PaymentDetailsInput()
                        }
                        item {
                            Text(
                                text = "On your phone you will be asked to enter your pin to complete the payment. " +
                                        "OR You can also use our paybill number: SHOP_PAYBILL",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Confirm our online shop name on your phone is: SHOP_NAME before completing the payment.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Lipa Na Mpesa Integration Demo",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MpesaPrimary
        ),)
}

@Composable
private fun InputField(
    onValueChange: (String) -> Unit,
    value: String,
    placeHolder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeHolder, color = TextSecondary) },
        label = { Text(placeHolder, color = TextSecondary) },
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = MpesaPrimary,
            focusedBorderColor = MpesaPrimary,
            unfocusedBorderColor = TextSecondary,
            cursorColor = MpesaPrimary,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        )
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun PaymentDetailsInput(
    mpesaViewModel: MpesaViewModel = hiltViewModel()
) {
 val state = mpesaViewModel.state.value


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var phoneNumber by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var enabled = phoneNumber.isNotEmpty() && amount.isNotEmpty() || state.isLoading


        InputField(
            onValueChange = { phoneNumber = it },
            value = phoneNumber,
            placeHolder = "Phone number"
        )
        InputField(
            onValueChange = { amount = it },
            value = amount,
            placeHolder = "Amount"
        )

        Button(onClick = {
            mpesaViewModel.onMpesaEvent(MpesaEvents.InitializePaymentEvent(phoneNumber,amount))
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(enabled)MpesaSecondary else TextSecondary,
                contentColor = Color.White
            ),
            enabled = enabled,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Text(
               text =  if(state.isLoading){ "Loading..."}else{ "Proceed to pay"},
                style = MaterialTheme.typography.labelLarge)
        }

        if(state.data != null){
            Text(state.data.toString())
        }
        if(state.error != null){
            Text(state.error)
        }
    }
}

@Composable
fun ColumnDisplay(
    title: String,
    icon: ImageVector,
    desc: String,
    link: String? = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Magenta,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = desc,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if(link?.isNotEmpty() ==true) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Link",
                    tint = MpesaAccent,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = link,
                    color = MpesaAccent,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}
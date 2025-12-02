package com.example.userinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.userinfo.ui.theme.UserInfoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserFormScreen()
                }
            }
        }
    }
}

@Composable
fun UserFormScreen() {

    var inputName by rememberSaveable { mutableStateOf("") }
    var inputAge by rememberSaveable { mutableStateOf(25f) }
    var selectedSex by rememberSaveable { mutableStateOf("male") }
    var wantsNews by rememberSaveable { mutableStateOf(false) }

    var showSummary by rememberSaveable { mutableStateOf(false) }
    var nameError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )

        Image(
            painter = painterResource(id = R.drawable.ic_img),
            contentDescription = null,
            modifier = Modifier.size(160.dp)
        )

        OutlinedTextField(
            value = inputName,
            onValueChange = {
                inputName = it
                nameError = false
            },
            label = { Text(stringResource(R.string.name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = nameError,
            supportingText = {
                if (nameError) {
                    Text(
                        text = stringResource(R.string.name_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.age_label, inputAge.toInt()),
                style = MaterialTheme.typography.bodyLarge
            )
            Slider(
                value = inputAge,
                onValueChange = { inputAge = it },
                valueRange = 1f..100f,
                steps = 98
            )
        }

        GenderSelector(
            currentGender = selectedSex,
            onGenderChange = { selectedSex = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = wantsNews,
                onCheckedChange = { wantsNews = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.subscribe_label))
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (inputName.isBlank()) {
                    nameError = true
                    showSummary = false
                } else {
                    nameError = false
                    showSummary = true
                }
            }
        ) {
            Text(stringResource(R.string.submit_button))
        }

        if (showSummary) {
            UserSummaryCard(
                name = inputName,
                age = inputAge.toInt(),
                gender = selectedSex,
                subscribed = wantsNews
            )
        }
    }
}

@Composable
fun GenderSelector(
    currentGender: String,
    onGenderChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.gender_label),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            GenderOption(
                text = stringResource(R.string.male),
                selected = currentGender == "male",
                onClick = { onGenderChange("male") }
            )

            GenderOption(
                text = stringResource(R.string.female),
                selected = currentGender == "female",
                onClick = { onGenderChange("female") }
            )
        }
    }
}

@Composable
fun GenderOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun UserSummaryCard(
    name: String,
    age: Int,
    gender: String,
    subscribed: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = stringResource(R.string.result_name, name),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(R.string.result_age, age),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(
                    R.string.result_gender,
                    if (gender == "male")
                        stringResource(R.string.male)
                    else
                        stringResource(R.string.female)
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = if (subscribed)
                    stringResource(R.string.result_subscribe_yes)
                else
                    stringResource(R.string.result_subscribe_no),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

package com.example.taxcalculatorpakistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taxcalculatorpakistan.ui.theme.TaxCalculatorPakistanTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TaxCalculatorPakistanTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          TaxCalculatorScreen()
        }
      }
    }
  }
}

@Composable
fun TaxCalculatorScreen() {
  val text = remember { mutableStateOf("")}

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(100.dp), content = {
      OutlinedTextField(value = text.value, onValueChange = {
        text.value = it
      }, label = {
        Text(text = "Enter your monthly salary")
      }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType
      = KeyboardType.Number)
      )

      TaxYear2022_23(value = text.value)
      
      TaxYear2021_22(value = text.value)
    }
  )
}

@Composable
fun TaxYear2022_23(value: String) {
  val salary = StringToInt(value)
  var yearlyTax = CalculateYearlyTax2022_23(salary)
  Text(text = "Monthly Tax (2022-23): ${yearlyTax / 12}")
  Text(text = "Yearly Tax (2022-23): ${yearlyTax}")
}

@Composable
fun TaxYear2021_22(value: String) {
  val salary = StringToInt(value)
  var yearlyTax = CalculateYearlyTax2021_22(salary)
  Text(text = "Monthly Tax (2021-22): ${yearlyTax / 12}")
  Text(text = "Yearly Tax (2021-22): ${yearlyTax}")
}

fun StringToInt(value: String): Int {
  try {
    val salary = value.toInt()
    return salary
  } catch (e: Exception) {
    return 0
  }
}

fun CalculateYearlyTax2021_22(value: Int): Int  {
  var salary:Double = value.toDouble()
  var monthlyTax:Double = 0.0
  val taxSlabs = arrayOf<Double>(50_000.0, 50_000.0, 50_000.0, 58_333.33, 83_333.33, 125_000.0,
    250_000.0, 333_333.33, 15_00_000.0, 16_66_666.67, 20_83_333.33)
  var taxRates = arrayOf<Double>(0.0, 0.05, 0.1, 0.15, 0.175, 0.2,
    0.225, 0.25, 0.275, 0.3, 0.325, 0.35)

  if (salary > 50_000 && salary <= 62_50_000) {
    var index = 0
    do {
      val taxAddition = when(salary >= taxSlabs[index]) {
        true -> taxSlabs[index] * taxRates[index]
        false -> salary * taxRates[index]
      }
      monthlyTax = monthlyTax + taxAddition
      salary = salary-taxSlabs[index]
      index++
    } while (salary > 0)

  } else if(salary > 62_50_000) {
    monthlyTax = 17_85_000 + (salary-62_50_000)*0.35
  }
  return (monthlyTax * 12).toInt()
}

/// https://www.glxspace.com/2022/06/30/income-tax-rates-2022-23-for-salaried-persons-employees-with-slabs/
fun CalculateYearlyTax2022_23(value: Int): Int  {
  var salary:Double = value.toDouble()
  var monthlyTax:Double = 0.0
  val taxSlabs = arrayOf<Double>(50_000.0, 50_000.0, 100_000.0, 100_000.0, 200_000.0, 500_000.0)
  var taxRates = arrayOf<Double>(0.0, 0.025, 0.125, 0.2, 0.25, 0.325, 0.35)
  if (salary > 50_000.0 && salary <= 10_00_000) {
    var index = 0
    do {
      val taxAddition = when(salary >= taxSlabs[index]) {
        true -> taxSlabs[index] * taxRates[index]
        false -> salary * taxRates[index]
      }
      monthlyTax = monthlyTax + taxAddition
      salary = salary-taxSlabs[index]
      index++
    } while (salary > 0)

  } else if(salary > 10_00_000) {
    monthlyTax = 591_000 + (salary-10_00_000)*0.35
  }
  return (monthlyTax * 12).toInt()
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  TaxCalculatorPakistanTheme {
    TaxCalculatorScreen()
  }
}
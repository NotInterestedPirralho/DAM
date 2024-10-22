package com.example.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.CalcButton.CalcButton
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.ui.theme.Orange


fun add(a: Double, b: Double): Double = a + b
fun subtract(a: Double, b: Double): Double = a - b
fun multiply(a: Double, b: Double): Double = a * b
fun divide(a: Double, b: Double): Double = if (b != 0.0) a / b else Double.NaN
fun percentage(a: Double, b: Double): Double = (a * b) / 100
fun power(a: Double, b: Double): Double = Math.pow(a, b)

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<((Double, Double) -> Double)?>(null) }

    // Função para formatar o número no display
    fun formatNumber(number: Double): String {
        return if (number == number.toInt().toDouble()) {
            number.toInt().toString()  // Mostrar como inteiro se não tiver parte decimal
        } else {
            number.toString()  // Mostrar com casas decimais se tiver
        }
    }

    Column(modifier = modifier.padding(16.dp).fillMaxSize()) {
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        val updateDisplay: () -> Unit = {
            val current = display.toDoubleOrNull()
            if (operand1 != null && current != null && operation != null) {
                operand1 = operation!!(operand1!!, current)
                display = formatNumber(operand1!!)  // Formatar o resultado antes de exibir
            }
        }

        val onButtonClick: (String) -> Unit = { label ->
            when (label) {
                "AC" -> {
                    display = "0"
                    operand1 = null
                    operation = null
                }
                "C" -> {
                    display = "0"
                }
                "+", "-", "x", "/", "^", "%" -> {
                    val current = display.toDoubleOrNull()

                    if (operand1 == null && current != null) {
                        operand1 = current
                    } else if (operand1 != null && current != null) {
                        updateDisplay()  // Atualiza o resultado parcial
                    }

                    display = "0"
                    operation = when (label) {
                        "+" -> ::add
                        "-" -> ::subtract
                        "x" -> ::multiply
                        "/" -> ::divide
                        "^" -> ::power
                        "%" -> ::percentage
                        else -> null
                    }
                }
                "=" -> {
                    updateDisplay()  // Quando aperta "=", atualiza o display
                }
                else -> {
                    display = if (display == "0") label else display + label
                    updateDisplay()  // Atualiza em tempo real com a nova entrada
                }
            }
        }

        val buttons = listOf(
            listOf("AC", "^", "%", "/"),
            listOf("7", "8", "9", "x"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("C", "0", ",", "=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.height(60.dp).weight(1F)) {
                row.forEach { label ->
                    CalcButton(
                        label = label,
                        isOperation = label in listOf("AC", "^", "C", "%", "/", "x", "-", "+", "="),
                        onClick = onButtonClick,
                        modifier = Modifier.weight(1F)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorTheme() {
        CalculatorScreen()
    }
}
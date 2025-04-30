package vcmsa.projects.androidcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var display: EditText
    private var currentNumber = ""
    private var previousNumber = ""
    private var operation: Char = ' '
    private var isOperationPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)
        setupButtons()
    }

    @SuppressLint("DiscouragedApi")
    private fun setupButtons() {
        val digits = listOf(
            "btnZero" to '0', "btnOne" to '1', "btnTwo" to '2', "btnThree" to '3',
            "btnFour" to '4', "btnFive" to '5', "btnSix" to '6', "btnSeven" to '7',
            "btnEight" to '8', "btnNine" to '9'
        )

        digits.forEach { (btnId, digit) ->
            findViewById<Button>(resources.getIdentifier(btnId, "id", packageName)).setOnClickListener {
                handleNumberClick(digit)
            }
        }

        // Operation buttons with custom mapping
        mapOf(
            "btnAdd" to '+',
            "btnSubtract" to '-',
            "btnMultiply" to '*',
            "btnDivide" to '/'
        ).forEach { (btnId, op) ->
            findViewById<Button>(resources.getIdentifier(btnId, "id", packageName)).setOnClickListener {
                handleOperationClick(op)
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            handleEqualsClick()
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            resetCalculator()
        }

        findViewById<Button>(R.id.btnDecimal).setOnClickListener {
            handleDecimalClick()
        }
    }

    private fun handleNumberClick(number: Char) {
        if (operation == '=') resetCalculator()

        if (isOperationPressed) {
            currentNumber = number.toString()
            isOperationPressed = false
        } else {
            currentNumber += number
        }

        updateDisplay()
    }

    private fun handleDecimalClick() {
        if (!currentNumber.contains('.')) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0."
            } else {
                currentNumber += "."
            }
            updateDisplay()
        }
    }

    private fun handleOperationClick(newOp: Char) {
        if (currentNumber.isEmpty()) return

        if (operation != ' ' && operation != '=') {
            handleEqualsClick()
        }

        previousNumber = currentNumber
        currentNumber = ""
        operation = newOp
        isOperationPressed = true
    }

    private fun handleEqualsClick() {
        if (previousNumber.isEmpty() || currentNumber.isEmpty()) return

        val num1 = previousNumber.toDoubleOrNull()
        val num2 = currentNumber.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            display.setText("Error")
            return
        }

        try {
            val result = when (operation) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                '*' -> num1 * num2
                '/' -> {
                    if (num2 == 0.0) throw ArithmeticException("Division by zero")
                    num1 / num2
                }
                else -> return
            }

            val roundedResult = (result * 1000000).roundToInt().toDouble() / 1000000
            display.setText(roundedResult.toString())

            currentNumber = roundedResult.toString()
            previousNumber = ""
            operation = '='
        } catch (e: Exception) {
            display.setText("Error")
        }
    }

    private fun resetCalculator() {
        currentNumber = ""
        previousNumber = ""
        operation = ' '
        isOperationPressed = false
        display.setText("")
    }

    private fun updateDisplay() {
        display.setText(currentNumber)
    }
}

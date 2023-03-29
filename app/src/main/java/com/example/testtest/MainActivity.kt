package com.example.testtest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val resultsList = mutableListOf<Int>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberOfDiceEditText = findViewById<EditText>(R.id.numberOfDiceEditText)
        val numberOfSidesEditText = findViewById<EditText>(R.id.numberOfSidesEditText)
        val rollButton = findViewById<Button>(R.id.rollButton)
        val rerollOnesButton = findViewById<Button>(R.id.rerollOnesButton)
        val applyThresholdButton = findViewById<Button>(R.id.applyThresholdButton)
        val rollResultsTextView = findViewById<TextView>(R.id.rollResultsTextView)
        val summaryTextView = findViewById<TextView>(R.id.summaryTextView)
        val thresholdEditText = findViewById<EditText>(R.id.thresholdEditText)

        rollButton.setOnClickListener {
            val numberOfDice = numberOfDiceEditText.text.toString().toIntOrNull() ?: return@setOnClickListener
            val numberOfSides = numberOfSidesEditText.text.toString().toIntOrNull() ?: return@setOnClickListener

            resultsList.clear()
            resultsList.addAll(rollDice(numberOfDice, numberOfSides))
            displayResults(rollResultsTextView, summaryTextView)
        }

        rerollOnesButton.setOnClickListener {
            val numberOfSides = numberOfSidesEditText.text.toString().toIntOrNull() ?: return@setOnClickListener

            resultsList.replaceAll { roll -> if (roll == 1) Random.nextInt(1, numberOfSides + 1) else roll }
            displayResults(rollResultsTextView, summaryTextView)
        }

        applyThresholdButton.setOnClickListener {
            val rerollThreshold = thresholdEditText.text.toString().toIntOrNull() ?: return@setOnClickListener

            val filteredResults = resultsList.filter { roll -> roll > rerollThreshold }
            val summary = summarizeResults(filteredResults)
            val totalValue = filteredResults.sum()

            rollResultsTextView.text = "Roll results: ${filteredResults.joinToString(", ")}"
            summaryTextView.text = "Total value: $totalValue\nSummary:\n" + summary.map { (result, count) -> "$result: $count time(s)" }.joinToString("\n")
        }
    }

    private fun rollDice(numberOfDice: Int, numberOfSides: Int): List<Int> {
        return (1..numberOfDice).map {
            val roll = Random.nextInt(1, numberOfSides + 1)
            roll
        }
    }

    private fun summarizeResults(results: List<Int>): Map<Int, Int> {
        return results.groupingBy { it }.eachCount()
    }

    @SuppressLint("SetTextI18n")
    private fun displayResults(rollResultsTextView: TextView, summaryTextView: TextView) {
        val summary = summarizeResults(resultsList)
        val totalValue = resultsList.sum()

        rollResultsTextView.text = "Roll results: ${resultsList.joinToString(", ")}"
        summaryTextView.text = "Total value: $totalValue\nSummary:\n" + summary.map { (result, count) -> "$result: $count time(s)" }.joinToString("\n")
    }
}
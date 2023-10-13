package com.akalugin.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private var searchText: String? = null
    private var inputMethodManager: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        val clearButton = findViewById<ImageButton>(R.id.clear_button)

        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                updateClearButtonVisibility(clearButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.apply {
                text.clear()

                inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            }
        }

        searchText = searchEditText.text.toString()
        updateClearButtonVisibility(clearButton)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_TEXT, searchText)

        outState.putInt(SEARCH_SELECTION_START, searchEditText.selectionStart)
        outState.putInt(SEARCH_SELECTION_END, searchEditText.selectionEnd)

        outState.putBoolean(
            SEARCH_INPUT_ACTIVE,
            inputMethodManager?.isActive(searchEditText) ?: false
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchEditText.setText(searchText)

        val selectionStart = savedInstanceState.getInt(SEARCH_SELECTION_START, 0)
        val selectionEnd = savedInstanceState.getInt(SEARCH_SELECTION_END, 0)
        searchEditText.setSelection(selectionStart, selectionEnd)

        if (savedInstanceState.getBoolean(SEARCH_INPUT_ACTIVE, false)) {
            inputMethodManager?.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun updateClearButtonVisibility(clearButton: View) {
        clearButton.visibility = if (searchText.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_SELECTION_START = "SEARCH_SELECTION_START"
        private const val SEARCH_SELECTION_END = "SEARCH_SELECTION_END"
        private const val SEARCH_INPUT_ACTIVE = "SEARCH_INPUT_ACTIVE"
    }
}
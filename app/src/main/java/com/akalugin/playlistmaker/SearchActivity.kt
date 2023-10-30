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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        createRecycler()
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

    private fun createRecycler() {
        val recycler: RecyclerView = findViewById(R.id.search_results_recycler)

        val tracks = arrayListOf(
            Track(
                trackName = "Smells Like Teen Spirit",
                artistName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            ),

            Track(
                trackName = "Billie Jean",
                artistName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            ),

            Track(
                trackName = "Stayin' Alive",
                artistName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 =
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            ),

            Track(
                trackName = "Whole Lotta Love",
                artistName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 =
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            ),
            Track(
                trackName = "Sweet Child O'Mine",
                artistName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 =
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg",
            ),
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackAdapter(tracks)
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_SELECTION_START = "SEARCH_SELECTION_START"
        private const val SEARCH_SELECTION_END = "SEARCH_SELECTION_END"
        private const val SEARCH_INPUT_ACTIVE = "SEARCH_INPUT_ACTIVE"
    }
}
package com.akalugin.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var clearButton: View
    private lateinit var searchInputEditText: EditText
    private lateinit var nothingFoundTextView: View
    private lateinit var networkErrorLayout: ViewGroup
    private var searchText: String? = null
    private var inputMethodManager: InputMethodManager? = null

    private val itunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = arrayListOf<Track>()
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.toolbar.setNavigationOnClickListener { finish() }

        clearButton = binding.clearSearchButton
        searchInputEditText = binding.searchInputEditText
        nothingFoundTextView = binding.nothingFoundTextView
        networkErrorLayout = binding.networkErrorLayout

        searchInputEditText.doOnTextChanged { text, _, _, _ ->
            searchText = text.toString()
            updateClearButtonVisibility()
        }

        clearButton.setOnClickListener {
            searchInputEditText.apply {
                text.clear()

                inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            }
            searchTracks()
        }

        searchText = searchInputEditText.text.toString()
        updateClearButtonVisibility()

        val recycler: RecyclerView = binding.searchResultsRecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
                true
            }
            false
        }

        binding.updateSearchResultsButton.setOnClickListener { searchTracks() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_TEXT, searchText)

        outState.putInt(SEARCH_SELECTION_START, searchInputEditText.selectionStart)
        outState.putInt(SEARCH_SELECTION_END, searchInputEditText.selectionEnd)

        outState.putBoolean(
            SEARCH_INPUT_ACTIVE,
            inputMethodManager?.isActive(searchInputEditText) ?: false
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchInputEditText.setText(searchText)

        val selectionStart = savedInstanceState.getInt(SEARCH_SELECTION_START, 0)
        val selectionEnd = savedInstanceState.getInt(SEARCH_SELECTION_END, 0)
        searchInputEditText.setSelection(selectionStart, selectionEnd)

        if (savedInstanceState.getBoolean(SEARCH_INPUT_ACTIVE, false)) {
            inputMethodManager?.showSoftInput(searchInputEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun updateClearButtonVisibility() {
        clearButton.visibility = if (searchText.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun searchTracks() {
        val text = searchText

        if (!text.isNullOrEmpty()) {
            iTunesService.search(text).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    val code = response.code()
                    if (code == 200) {
                        tracks.clear()

                        val results = response.body()?.results
                        if (results.isNullOrEmpty()) {
                            nothingFoundTextView.isVisible = true
                        } else {
                            tracks.addAll(results)
                            nothingFoundTextView.isVisible = false
                        }
                        adapter.notifyDataSetChanged()

                        networkErrorLayout.isVisible = false
                    } else {
                        showNetworkError(code.toString())
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showNetworkError(t.message.toString())
                }
            })
        } else {
            tracks.clear()
            adapter.notifyDataSetChanged()

            networkErrorLayout.isVisible = false
            nothingFoundTextView.isVisible = false
        }
    }

    fun showNetworkError(additionalMessage: String) {
        tracks.clear()
        adapter.notifyDataSetChanged()

        networkErrorLayout.isVisible = true
        nothingFoundTextView.isVisible = false

        if (additionalMessage.isNotEmpty()) {
            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                .show()
        }
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_SELECTION_START = "SEARCH_SELECTION_START"
        const val SEARCH_SELECTION_END = "SEARCH_SELECTION_END"
        const val SEARCH_INPUT_ACTIVE = "SEARCH_INPUT_ACTIVE"
    }
}
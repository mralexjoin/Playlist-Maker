package com.akalugin.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
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

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.toolbar.setNavigationOnClickListener { finish() }

        val clearButton = binding.clearSearchButton

        val searchEditText = binding.searchInputEditText
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
            searchTracks()
        }

        searchText = searchEditText.text.toString()
        updateClearButtonVisibility(clearButton)

        val recycler: RecyclerView = binding.searchResultsRecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
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

        val searchEditText = binding.searchInputEditText
        outState.putInt(SEARCH_SELECTION_START, searchEditText.selectionStart)
        outState.putInt(SEARCH_SELECTION_END, searchEditText.selectionEnd)

        outState.putBoolean(
            SEARCH_INPUT_ACTIVE,
            inputMethodManager?.isActive(searchEditText) ?: false
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val searchEditText = binding.searchInputEditText
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
                            binding.nothingFoundTextView.visibility = View.VISIBLE
                        } else {
                            tracks.addAll(results)
                            binding.nothingFoundTextView.visibility = View.GONE
                        }
                        adapter.notifyDataSetChanged()

                        binding.networkErrorLayout.visibility = View.GONE
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

            binding.networkErrorLayout.visibility = View.GONE
            binding.nothingFoundTextView.visibility = View.GONE
        }
    }

    fun showNetworkError(additionalMessage: String) {
        tracks.clear()
        adapter.notifyDataSetChanged()

        binding.networkErrorLayout.visibility = View.VISIBLE
        binding.nothingFoundTextView.visibility = View.GONE

        if (additionalMessage.isNotEmpty()) {
            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                .show()
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_SELECTION_START = "SEARCH_SELECTION_START"
        private const val SEARCH_SELECTION_END = "SEARCH_SELECTION_END"
        private const val SEARCH_INPUT_ACTIVE = "SEARCH_INPUT_ACTIVE"
    }
}
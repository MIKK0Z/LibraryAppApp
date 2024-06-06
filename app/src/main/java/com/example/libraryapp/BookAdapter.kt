package com.example.libraryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookAdapter(private val booksList: BooksResponse): RecyclerView.Adapter<BookAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        val bookID: TextView = itemView.findViewById(R.id.bookID)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = booksList[position]
        holder.bookTitle.text = currentItem.name
        holder.bookID.text = currentItem.id

        holder.deleteBtn.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            val apiService = retrofit.create(ApiInterface::class.java)

            val body = mapOf(
                "userID" to userID,
                "bookID" to currentItem.id,
            )



            apiService.deleteBook(body).enqueue(object: Callback<BooksResponse> {
                override fun onResponse(
                    call: Call<BooksResponse?>,
                    response: Response<BooksResponse?>
                ) {
//                    books = response.body() ?: BooksResponse()
                    Log.d("delete", books.toString())
//                    binding.booksRecycler.layoutManager = LinearLayoutManager(activity)
//                    binding.booksRecycler.setHasFixedSize(false)
//
//                    binding.booksRecycler.adapter = BookAdapter(books)
                }

                override fun onFailure(call: Call<BooksResponse?>, t: Throwable) {
                    Log.d("fetch", "books fetch failed")
                }
            })
        }
    }

}
package fr.esgi.transpal

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import fr.esgi.transpal.network.dto.CardModel

class CardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private val cardList = mutableListOf<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ajouter des cartes (Exemple)
        cardList.add(CardModel("1234567812345678", "Dark Vador", "12/24"))
        cardList.add(CardModel("9876543210987654", "Jack Sparrow", "06/25"))



        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val backButton = findViewById<LinearLayout>(R.id.back_button_container)
        backButton.setOnClickListener {
            finish()
        }

        // Adapter
        cardAdapter = CardAdapter(cardList)
        recyclerView.adapter = cardAdapter

        recyclerView.post {
            Log.d("CardActivity", "RecyclerView height: ${recyclerView.height}")
        }


    }
}

package com.chiki.dissertpusher

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.chiki.dissertpusher.databinding.ActivityMainBinding

const val KEY_REVENUE = "key_revenue"
const val KEY_DESSERT_SOLD = "key_dessert_sold"

class MainActivity : AppCompatActivity(), LifecycleObserver {

    //Binding
    private lateinit var binding:ActivityMainBinding

    //Logic
    private val allDesserts = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 20),
        Dessert(R.drawable.froyo, 30, 50),
        Dessert(R.drawable.gingerbread, 50, 100),
        Dessert(R.drawable.honeycomb, 100, 200),
        Dessert(R.drawable.icecreamsandwich, 500, 500),
        Dessert(R.drawable.jellybean, 1000, 1000),
        Dessert(R.drawable.kitkat, 2000, 2000),
        Dessert(R.drawable.lollipop, 3000, 4000),
        Dessert(R.drawable.marshmallow, 4000, 8000),
        Dessert(R.drawable.nougat, 5000, 16000),
        Dessert(R.drawable.oreo, 6000, 20000)
    )
    private var revenue = 0
    private var dessertSold = 0
    private var currentDessert = allDesserts[0]

    //Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        if(savedInstanceState!=null){
            revenue = savedInstanceState.getInt(KEY_REVENUE)
            dessertSold = savedInstanceState.getInt(KEY_DESSERT_SOLD)
        }

        binding.dessertButton.setOnClickListener{
            onDessertClicked()
        }
        // Set the TextViews and the ImageView to the right values
        binding.revenue = revenue
        binding.amountSold = dessertSold
        binding.dessertButton.setImageResource(currentDessert.imageId)
        showCurrentDessert()
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.shareMenuButton-> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REVENUE,revenue)
        outState.putInt(KEY_DESSERT_SOLD,dessertSold)
    }

    //Menu methods
    private fun onShare(){
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text,dessertSold,revenue))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        }catch (ex:ActivityNotFoundException){
            Toast.makeText(this,getString(R.string.sharing_not_available),Toast.LENGTH_LONG).show()
        }
    }

    //Actions
    private fun onDessertClicked() {
        // Update the score
        revenue +=currentDessert.price
        dessertSold++

        binding.revenue = revenue
        binding.amountSold = dessertSold

        //Show the next dessert
        showCurrentDessert()
    }
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts){
            if (dessertSold >= dessert.startProductionAmount){
                newDessert = dessert
            }else break
        }
        if (newDessert!=currentDessert){
            currentDessert = newDessert
            binding.dessertButton.setImageResource(currentDessert.imageId)
        }
    }

}
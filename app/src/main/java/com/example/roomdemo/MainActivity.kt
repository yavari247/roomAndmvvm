package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.adapter.MyRecyclerViewAdapter
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.db.local.Subscriber
import com.example.roomdemo.db.local.SubscriberDatabase
import com.example.roomdemo.db.repository.SubscriberRepository
import com.example.roomdemo.viewmodels.SubscriberViewModel
import com.example.roomdemo.viewmodels.SubscriberViewModelFactory

class MainActivity : AppCompatActivity(), SubscriberViewModel.SendSuccesfullText {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        /*initialize "lateinit var listener:SendSuccesfullText" with activity context beacause its null*/
        /*this activity extend SubscriberViewModel. SendSuccesfullText so by context we can fill listener in subscriberViewModel */
        subscriberViewModel.setListener(this@MainActivity)
        binding.myViewModel = subscriberViewModel
        //dataBinding+liveData:bind livedata to xml .use livedata directly by databinding ->text=@{myviewModel.inputname
        //so we should set lifecycleowner
        binding.lifecycleOwner = this
        initRecyclerView()
    }

    private fun initRecyclerView() {

        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager
                .VERTICAL, false
        )
        adapter = MyRecyclerViewAdapter(

            { x: Subscriber ->
                onClick(x)
            },
        )
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscriberList()

    }

    private fun displaySubscriberList() {
        /* @Query("SELECT * FROM subscriber_data_table")
efun getAllSubscribers():LiveData<List<Subscriber>>*
class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel() {
val subscribers=repository.subscribers
so subscribers is a livedata and we can observe it
  */
        subscriberViewModel.subscribers.observe(this@MainActivity, Observer {
            //fill arraylist of recyclerviewadapter
            adapter.setList(it)
            //tell recyclerview there is a new update for arraylist
            adapter.notifyDataSetChanged()
        })

    }

    private fun onClick(x: Subscriber) {

        subscriberViewModel.initUpdateAndDelete(x)
    }

    override fun sendSuccesfull(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}
package com.example.roomdemo.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.local.Subscriber
import com.example.roomdemo.db.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    private lateinit var listener: SendSuccesfullText
    val subscribers = repository.subscribers
    val inputName = MutableLiveData<String?>()
    val inputEmail = MutableLiveData<String?>()
    private var isUpdateOrDelete = false
    lateinit var subscriberToUpdateOrDelete: Subscriber
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    /*this method belong to save button */

    fun saveOrUpdate() {
/*validation*/
        if (inputName.value == null) {
            listener.sendSuccesfull("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            listener.sendSuccesfull("Please enter subscriber's email")

        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            listener.sendSuccesfull("Please enter a correct email address")

        } else {
            /*for update*/
            if (isUpdateOrDelete) {
                /*inputName is a livedata that is bind to edittext_name in xml*/
                /*we have used databiningeh 2 tarafeh for inputname and email*/
                /*<EditText text=@={myviewmodel.inputname}>*/
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
                /*for insert new record*/
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }


    //takes subscriber from onclick method in mainactivity that is taken from adapter
    //and is a object of selected item
    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        //save the input of init method in subscriberToUpdateOrDelete.
        //its an object of selected item
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"

    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {

            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        var newRowId: Long = repository.insert(subscriber)
        if (newRowId > -1)
            listener.sendSuccesfull("successfully inserted new row")
        else
            listener.sendSuccesfull("Error occured")


    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        //save the input of init method in subscriberToUpdateOrDelete.
        //its an object of selected item
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
        listener.sendSuccesfull("successfully updated")
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        //save the input of init method in subscriberToUpdateOrDelete.
        //its an object of selected item
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
        //send string by interface method
        listener.sendSuccesfull("successfully deleted")

    }

    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    /*initialize listener by call this method in mainactivity*/
    /*beacause we should distinct listener belong to which activity to run method of interface there*/
    fun setListener(listener: SendSuccesfullText) {
        this.listener = listener
    }

    public interface SendSuccesfullText {
        fun sendSuccesfull(str: String)
    }

}

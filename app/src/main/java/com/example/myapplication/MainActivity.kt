package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.ResponseHandler
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.io.Reader
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val productAdapter = ProductAdapter(this)
    }

    class ProductAdapter(val context: Context) : BaseAdapter() {
        val baseUrl : String="https://2jt4kq01ij.execute-api.ap-northeast-2.amazonaws.com/prod/"
        val getUrl : String="/products?page="
        var productsList=ArrayList<Product>()
       // var context: Context?=null
        init {
           FuelManager.instance.baseHeaders=mapOf("User-Agent" to "DemoApp")

           for(i in 1..10){
               //print(i)
               search(i) { productPage ->
                    //productsList.
                   //print(productPage.query)
               }
           }
       }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            TODO("not implemented")
        }

        override fun getItem(position: Int): Any {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemId(position: Int): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCount(): Int {
            return productsList.size
        }
        fun getSearchUrl(num :Int): String{
            return baseUrl+getUrl+num.toString()
        }
        fun search(num: Int,responseHandler: (result: ProductPage)->Unit?){
            getSearchUrl(num).httpGet().responseObject(ProductListDataDeserializer()){
                _,_,result->
                when(result){
                    is Result.Failure -> {
                        Log.i("ErrorMsg", result.getException().message)
                        result.getException().stackTrace
                        throw Exception(result.getException())//
                    }

                    is Result.Success->{
                        val(data, _) = result
                        responseHandler.invoke(data as ProductPage)
                        print(data)
                    }
                }
            }
        }

        class ProductListDataDeserializer : ResponseDeserializable<ProductPage>{
            override fun deserialize(reader: Reader)= Gson().fromJson(reader,ProductPage::class.java)
        }

    }
}

package com.example.crawling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.content.Intent
import android.net.Uri
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var btn: Button
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn)
        tv = findViewById(R.id.tv)

        btn.setOnClickListener {
//            request("https://m.comic.naver.com/webtoon/weekday?week=mon")
//            request("https://comic.naver.com/webtoon/list?titleId=764480&week=mon")
            request("http://rukia2003.cafe24.com/ftp/other/voca_insert.php")
        }
    }

    fun request(url: String) {
        val requestQueue = Volley.newRequestQueue(this)

        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                tv.text = response
//                var jsonObj =  JSONObject(response)
//                var result = jsonObj.getString("result")
//                if(result == "OK"){
//                    Toast.makeText(this, "정상",Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this, "저장 실패!",Toast.LENGTH_SHORT).show()
//                } //강사님과 php 코드가 다르기 때문에 주석처리 해야한다
//
//                getEpisode(response)
//                var str = response.substring(response.indexOf("<ul class=\"list_toon\">") + 15)
//                getTitle(str)

            },
            Response.ErrorListener { error ->
                Toast.makeText(MainActivity@ this, error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String,String> = HashMap()
                params["userid"] = "serveace"
                params["password"] = "3608"
                params["phone"] = "010-4814-3608"
                params["name"] = "post보내기"
                params["address"] = "서울"
                params["intro"] = "송파구 백수"
                return params
            }
        }

        requestQueue.add(request)
    }

    fun getEpisode(str: String){
        try{
            var sIdx = str.indexOf("<span class=\"name\"><strong>")+27
            if(sIdx != -1) {
                var nStr = str.substring(sIdx)
                var eIdx = nStr.indexOf("</strong>")
                var episodeName = nStr.substring(0, eIdx)
                Log.d("aabb", "ep: " + episodeName)
                nStr = nStr.substring(eIdx)
                getEpisode(nStr)
            }
        } catch (e : Exception) {

        }
    }

    var idx = 0
    fun getTitle(str: String) {
        var sIdx = str.indexOf("<strong class=\"title\">")
        sIdx = sIdx - 200
        sIdx = str.indexOf("/webtoon/list")
        if (sIdx != -1) {
            var tempStr = str.substring(sIdx)
            sIdx = tempStr.indexOf("\"");
            tempStr = "https://comic.naver.com" + tempStr.substring(0, sIdx);
            Log.d("aabb", "url: " + tempStr)

            sIdx = str.indexOf("<strong class=\"title\">")

            sIdx = str.indexOf("<strong class=\"title\">") + 22
            var nStr = str.substring(sIdx)
            var eIdx = nStr.indexOf("</strong>")
            var title = nStr.substring(0, eIdx)
            nStr = str.substring(sIdx + eIdx)
            idx++
            Log.d("aabb", "$idx title: $title")
            getTitle(nStr)
        }
    }

    fun getUrl(str: String) {
        var sIdx = str.indexOf("/webtoon/detail?titleId")
        if (sIdx != -1) {
            var str = str.substring(sIdx)
            var eIdx = str.indexOf("\"")
            var url = "https://m.comic.naver.com" + str.substring(0, eIdx)
            Log.d("aabb", "url: $url")
            str = str.substring(eIdx)
            getUrl(str)
//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(browserIntent)
        }
    }

}
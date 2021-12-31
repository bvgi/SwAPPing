package com.example.swapping.ui.searching

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad

class ResultsSearchViewModel : ViewModel() {

    fun getResult(userID: Int, text: String, filterS: String, filterR: String, filterC: String, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByStatus(text, userID, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByRate(text, userID, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsCategory(text, userID, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAds(text, userID)
        }
        return result
    }

    fun getSortResult(userID: Int, text: String, filterS: String, filterR: String, filterC: String, sort: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByStatus(text, userID, filterS, sort)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByRate(text, userID, filterR, sort)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsCategory(text, userID, filterC, sort)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAds(text, userID, sort)
        }
        return result
    }


    fun getCategorySortResult(userID: Int, category: String, filterS: String, filterR: String, sort: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null") {
            dbHelper.findAdsByCategoryByStatus(category, userID, sort, filterS)
        } else if(filterS != "null" && filterR != "null"){
            val statusResult = dbHelper.findAdsByCategoryByStatus(category, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByCategoryByRate(category, userID, sort, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null"){
            dbHelper.findAdsByCategoryByRate(category, userID, sort, filterR)
        }  else {
            dbHelper.findAdsByCategory(category, userID, sort)
        }
        return result
    }

    fun getCategoryResult(userID: Int, category: String, filterS: String, filterR: String, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val result = if(filterS != "null" && filterR == "null") {
            dbHelper.findAdsByCategoryByStatus(category, userID, filterS)
        } else if(filterS != "null" && filterR != "null"){
            val statusResult = dbHelper.findAdsByCategoryByStatus(category, userID, filterS)
            val rateResult = dbHelper.findAdsByCategoryByRate(category, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null"){
            dbHelper.findAdsByCategoryByRate(category, userID, filterR)
        }  else {
            dbHelper.findAdsByCategory(category, userID)
        }
        return result
    }

    fun getVoivodeshipSortResult(userID: Int, voivodeship: String, filterS: String, filterR: String, filterC: String, sort: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAdsByVoivodeship(voivodeship, userID, sort)
        }
        return result
    }

    fun getVoivodeshipResult(userID: Int, voivodeship: String, filterS: String, filterR: String, filterC: String, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAdsByVoivodeship(voivodeship, userID)
        }
        return result
    }
}
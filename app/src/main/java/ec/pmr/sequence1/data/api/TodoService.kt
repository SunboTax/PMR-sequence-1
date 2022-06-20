package ec.pmr.sequence1.data.api

import android.util.Log
import retrofit2.http.*

interface TodoService {

    //use username and password to login
    @POST("authenticate")
    suspend fun login(
        @Query("user") username: String,
        @Query("password") password: String
    ): User

    @POST("users")
    suspend fun addUser(
        @Query("pseudo") username: String,
        @Query("pass") password: String
    ):User

    //get all the lists
    @GET("lists")
    suspend fun getLists(
        @Query("hash") token: String
    ): Lists

    // show the items in the list clicked
    @GET("lists/{list_id}/items")
    suspend fun getItems(
        @Path("list_id") listId: Int,
        @Query("hash") token: String
    ): Items

    // add a new list
    @POST("lists")
    suspend fun addList(
        @Query("label") label: String,
        @Query("hash") token: String
    ): ListOperation

    @DELETE("lists/{list_id}")
    suspend fun deleteList(
        @Path("list_id") listId: Int,
        @Query("hash") token:String
    )

    // add a new item(which is not checked by default)
    @POST("lists/{list_id}/items")
    suspend fun addItem(
        @Path("list_id") listId: Int,
        @Query("label") label: String,
        @Query("hash") token: String
    ): ItemOperation

    // change the status of the item
    @PUT("lists/{list_id}/items/{item_id}")
    suspend fun changeItem(
        @Path("list_id") listId: Int,
        @Path("item_id") itemId: Int,
        @Query("check") isChecked: String,
        @Query("hash") token: String
    ): ItemOperation

    @DELETE("lists/{list_id}/items/{item_id}")
    suspend fun deleteItem(
        @Path("list_id") listId: Int,
        @Path("item_id") itemId: Int,
        @Query("hash") token: String
    ):ItemOperation
}
package com.hotix.myhotixhousekeeping.retrofit2;

import com.hotix.myhotixhousekeeping.models.Login;
import com.hotix.myhotixhousekeeping.models.PannesData;
import com.hotix.myhotixhousekeeping.models.PermissionsData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {


    /***
     ** GET *********************************************************************************************
     **/

    //Login service call
    @GET("/HNGAPI/api/MyHotixHouseKeeping/login?")
    Call<Login> loginQuery(@Query("login") String login,
                           @Query("password") String password);

    //Get Autorisation service call
    @GET("/HNGAPI/api/MyHotixHouseKeeping/GetAutorisation?")
    Call<PermissionsData> getAutorisationQuery(@Query("id") String id);

    //Update Autorisation service call
    @GET("/HNGAPI/api/MyHotixHouseKeeping/UpdateAutorisation?")
    Call<ResponseBody> updateAutorisationQuery(@Query("util_id") String util_id,
                                               @Query("has_config") String has_config,
                                               @Query("has_add_panne") String has_add_panne,
                                               @Query("has_add_objet") String has_add_objet,
                                               @Query("has_close_panne") String has_close_panne,
                                               @Query("has_close_objet") String has_close_objet,
                                               @Query("has_mouchard_room") String has_mouchard_room,
                                               @Query("has_change_statut") String has_change_statut,
                                               @Query("has_etat_lieu") String has_etat_lieu,
                                               @Query("has_view_client") String has_view_client,
                                               @Query("has_fm") String has_fm);

    //Get List Pannes Cloture service call
    @GET("/HNGAPI/api/MyHotixHouseKeeping/GetListPannesCloture?")
    Call<PannesData> getListPannesClotureQuery(@Query("etat") String etat,
                                               @Query("image") String image,
                                               @Query("dateDeb") String dateDeb,
                                               @Query("dateFin") String dateFin);

    //Cloture Panne service call
    @GET("/HNGAPI/api/MyHotixHouseKeeping/CloturePanne?")
    Call<ResponseBody> cloturePanneQuery(@Query("panneId") String panneId,
                                       @Query("user_login") String user_login,
                                       @Query("comment") String comment,
                                       @Query("technicienId") String technicienId);


/***
 ** POST ********************************************************************************************
 **/

}
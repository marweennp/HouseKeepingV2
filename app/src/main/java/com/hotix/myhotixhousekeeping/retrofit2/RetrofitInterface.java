package com.hotix.myhotixhousekeeping.retrofit2;

import com.hotix.myhotixhousekeeping.models.AffectedRoomData;
import com.hotix.myhotixhousekeeping.models.ArrivalData;
import com.hotix.myhotixhousekeeping.models.ForecastData;
import com.hotix.myhotixhousekeeping.models.FoundObjData;
import com.hotix.myhotixhousekeeping.models.HotelSettings;
import com.hotix.myhotixhousekeeping.models.InformerData;
import com.hotix.myhotixhousekeeping.models.Login;
import com.hotix.myhotixhousekeeping.models.PannesData;
import com.hotix.myhotixhousekeeping.models.PermissionsData;
import com.hotix.myhotixhousekeeping.models.RoomRackData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {


    /***
     ** GET *********************************************************************************************
     **/

    //Get Infos service call
    @GET("/hotixsupport/api/myhotix/GetInfos?")
    Call<HotelSettings> getInfosQuery(@Query("codehotel") String codehotel,
                                      @Query("applicationId") String applicationId);

    //Login service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/login?")
    Call<Login> loginQuery(@Query("login") String login,
                           @Query("password") String password);

    //Get Autorisation service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetAutorisation?")
    Call<PermissionsData> getAutorisationQuery(@Query("id") String id);

    //Update Autorisation service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/UpdateAutorisation?")
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
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetListPannesCloture?")
    Call<PannesData> getListPannesClotureQuery(@Query("etat") String etat,
                                               @Query("image") String image,
                                               @Query("datedeb") String datedeb,
                                               @Query("datefin") String datefin);

    //Get List Objets Trouves service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetListObjetsTrouves?")
    Call<FoundObjData> getListObjetsTrouvesQuery(@Query("etat") String etat,
                                                 @Query("image") String image,
                                                 @Query("datedeb") String datedeb,
                                                 @Query("datefin") String datefin);

    //Cloture Panne service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/CloturePanne?")
    Call<ResponseBody> cloturePanneQuery(@Query("panneId") String panneId,
                                         @Query("user_login") String user_login,
                                         @Query("comment") String comment,
                                         @Query("technicienId") String technicienId);

    //Cloture Objet Trouve service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/ClotureObjetTrouve?")
    Call<ResponseBody> clotureObjetTrouveQuery(@Query("objTrouveId") String objTrouveId);

    //Reclame Panne service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/ReclamePanne?")
    Call<ResponseBody> reclamePanneQuery(@Query("prodId") String prodId,
                                         @Query("typePanneId") String typePanneId,
                                         @Query("urgent") String urgent,
                                         @Query("duree") String duree,
                                         @Query("lieu") String lieu,
                                         @Query("nom") String nom,
                                         @Query("prenom") String prenom,
                                         @Query("user_login") String user_login,
                                         @Query("comment") String comment,
                                         @Query("ImageByteArray") String ImageByteArray);

    //Declarer Objet Trouves service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/DeclarerObjetTrouves?")
    Call<ResponseBody> declarerObjetTrouvesQuery(@Query("prodNum") String prodNum,
                                                 @Query("objTrouveDesc") String objTrouveDesc,
                                                 @Query("objTrouveNom") String objTrouveNom,
                                                 @Query("objTrouvePrenom") String objTrouvePrenom,
                                                 @Query("objTrouveLieu") String objTrouveLieu,
                                                 @Query("objTrouveRenduNom") String objTrouveRenduNom,
                                                 @Query("objTrouveRenduPrenom") String objTrouveRenduPrenom,
                                                 @Query("login") String login,
                                                 @Query("comment") String comment,
                                                 @Query("ImageByteArray") String ImageByteArray);

    //Get Arrivees Prevues service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetArriveesPrevues?")
    Call<ArrivalData> getArriveesPrevuesQuery(@Query("dateDeb") String dateDeb,
                                              @Query("dateFin") String dateFin);

    //Get List Affection FM service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetListAffectionFM?")
    Call<AffectedRoomData> getListAffectionFMQuery(@Query("EmployeId") String EmployeId);

    //Affectation Femme Menage service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/AffectationFemmeMenage?")
    Call<ResponseBody> affectationFemmeMenageQuery(@Query("EmployeId") String EmployeId,
                                                   @Query("chaine_affectation") String chaine_affectation);

    //Get Etat Rack Room service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetEtatRackRoom?")
    Call<RoomRackData> getEtatRackRoomQuery(@Query("etage") String etage,
                                            @Query("bloc") String bloc);

    //Changer Produit Staut service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/ChangerProduitStaut?")
    Call<ResponseBody> changerProduitStautQuery(@Query("user") String user,
                                                @Query("prodId") String prodId,
                                                @Query("typeHebId") String typeHebId,
                                                @Query("typeProdId") String typeProdId,
                                                @Query("statut") String statut,
                                                @Query("oldStatut") String oldStatut,
                                                @Query("comment") String comment);

    //Update Etat Lieu service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/UpdateEtatLieu?")
    Call<ResponseBody> updateEtatLieuQuery(@Query("prodId") String prodId,
                                           @Query("etatTv") String etatTv,
                                           @Query("etatBar") String etatBar,
                                           @Query("etatSer") String etatSer);

    //Get Mouchard Rack Room service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetMouchardRackRoom?")
    Call<InformerData> getMouchardRackRoomQuery(@Query("etage") String etage,
                                                @Query("bloc") String bloc,
                                                @Query("dateDeb") String dateDeb,
                                                @Query("dateFin") String dateFin);

    //Get Prevision service call
    @GET("/HNGAPI/v0/api/MyHotixHouseKeeping/GetPrevision?")
    Call<ForecastData> getPrevisionQuery(@Query("dateDeb") String dateDeb,
                                         @Query("dateFin") String dateFin);


/***
 ** POST ********************************************************************************************
 **/

}
package br.com.pi.pi4.rest;

import java.util.List;

import br.com.pi.pi4.entity.ParticipanteGrupo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by temp.cas on 31/03/2016.
 */
public interface PIService {
    @GET("grupo/{evento}")
    Call<List<ParticipanteGrupo>> getGrupos(@Path("evento") String evento);
}

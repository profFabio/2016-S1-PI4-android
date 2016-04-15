package br.com.pi.pi4.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.pi.pi4.entity.ParticipanteGrupo;

/**
 * Created by Fabio on 08/04/2016.
 */
public class GrupoUtil {

    public static HashMap<String,ArrayList<ParticipanteGrupo>> getGrupos (List<ParticipanteGrupo> todosParticipantes) {
        HashMap<String, ArrayList<ParticipanteGrupo>> ret = new HashMap<>();
        for (int i=0; i< todosParticipantes.size(); i+=1) {
            ParticipanteGrupo participante = todosParticipantes.get(i);
            ArrayList<ParticipanteGrupo> participantes = ret.get(participante.getNmGrupo());
            if (participantes == null) {
                participantes = new ArrayList<>();
            }
            participantes.add(participante);
            ret.put(participante.getNmGrupo(),participantes);
        }
        return ret;
    }
}

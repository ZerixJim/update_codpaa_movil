package com.codpaa.provider;/*
 * Created by grim on 24/05/2016.
 */

import android.provider.BaseColumns;

public class DbEstructure {

    public static final String NAME_TABLE = "usuarios";


    /**
     * user table
     */
    public static class Usuario implements BaseColumns{
        private Usuario(){
            //sin instancias
        }
        public static final String TABLE = "usuarios";
        public static final String ID_USER = "idCelular";
        public static final String NOMBRE = "nombre";
        public static final String USER = "user";
        public static final String PASS = "pass";

    }


    /**
     * tinedas visitadas, tabla no utilizada
     */

    public static class TiendasVisitadas implements BaseColumns {

        private TiendasVisitadas(){}
        public static final String ID_TIENDA = "tiendasVisitadas";
        public static final String ID_PROMOTOR = "idPromotor";


    }

    /**
     * tabla donde se reguistra la entrada del promotor
     */

    public static class VisitaTienda implements BaseColumns{
        private VisitaTienda(){ }

        public static final String TABLE = "coordenadas";
        public static final String ID_TIENDA = "idTienda";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String FECHA = "fecha";
        public static final String HORA = "hora";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String PRECISION = "precision";
        public static final String TIPO = "tipo";
        public static final String ESTATUS = "status";
        public static final String SEMANA = "semana";

    }





}

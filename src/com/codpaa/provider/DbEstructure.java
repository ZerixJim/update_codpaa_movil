package com.codpaa.provider;/*
 * Created by grim on 24/05/2016.
 */

import android.provider.BaseColumns;

public class DbEstructure {


    /**
     * user table
     */
    public static class Usuario implements BaseColumns{
        private Usuario(){/* sin instancias */ }
        public static final String TABLE_NAME = "usuarios";
        public static final String ID_USER = "idCelular";
        public static final String NOMBRE = "nombre";
        public static final String USER = "user";
        public static final String PASS = "pass";
        public static final String TIPO_PROMOTOR = "tipoPromotor";
        public static final String STATUS = "estatus";
        public static final String FECHA_SYNC = "fecha_sync";
    }

    /**
     * Table photo
     */

    public static class Photo implements BaseColumns{
        private Photo(){}
        public static final String TABLE_NAME = "photo";
        public static final String ID_PHOTO = "idPhoto";
        public static final String ID_TIENDA = "idTienda";
        public static final String ID_CELULAR = "idCelular";
        public static final String ID_MARCA = "idExhibicion";
        public static final String FECHA  = "fecha";
        public static final String FECHA_CAPTURA  = "fecha_captura";
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

    /**
     * Tabla donde se guardan los mensajes
     */
    public static class Mensaje implements BaseColumns{
        private Mensaje(){}
        public static final String TABLE_NAME = "mensaje";
        public static final String ID_MENSAJE = "id_mensaje";
        public static final String MENSAJE = "mensaje";
        public static final String ASUNTO = "asunto";
        public static final String CONTENT = "content";
        public static final String FECHA = "fecha";
        public static final String ESTATUS = "estatus";
        public static final String ENVIADO = "enviado";
        public static final String ID_SERVIDOR = "id_servidor";
        public static final String ID_PROMOTOR = "id_promotor";
        public static final String FECHA_LECTURA = "fecha_lectura";
    }


    public static class Menus implements BaseColumns{
        private Menus(){}
        public static final String TABLE_NAME = "menus";
        public static final String ID_MENU = "id_menu";
        //public static final String
    }

    public static class Tienda implements BaseColumns{
        private Tienda(){}
        public static final String TABLE_NAME = "clientes";
        public static final String ID_TIENDA = "idTienda";
        public static final String GRUPO = "grupo";
        public static final String SUCURSAL = "sucursal";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String ID_TIPO = "idTipo";
        public static final String ID_FORMATO = "idFormato";
    }

    /**
     * table producto by formato
     */
    public static class ProductByFormato implements BaseColumns {
        private ProductByFormato(){}
        public static final String TABLE_NAME = "productoFormato";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_FORMATO = "idFormato";
    }

    /**
     * table producto by tienda
     */

    public static class ProductoByTienda implements BaseColumns {

        private ProductoByTienda(){}

        public static final String TABLE_NAME = "productoTienda";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_TIENDA = "idTienda";

    }


    /**
     * table tienda_producto_catologo
     */
    public static class TiendaProductoCatalogo implements BaseColumns {
        private TiendaProductoCatalogo(){}

        public static final String TABLE_NAME = "tienda_productos_catologo";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_TIENDA = "idTienda";
        public static final String FECHA = "fecha";
        public static final String ID_PROMOTOR = "idPromotor";

    }



}

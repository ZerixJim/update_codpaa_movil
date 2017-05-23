package com.codpaa.provider;/*
 * Created by grim on 24/05/2016.
 */

import android.database.sqlite.SQLiteDatabase;
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

        public static final String TABLE_NAME = "tienda_productos_catalogo";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_TIENDA = "idTienda";
        public static final String FECHA = "fecha";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String ESTATUS = "status";

    }


    /**
     * table photo_producto
     */

    public static class PhotoProducto implements BaseColumns {

        private PhotoProducto(){}

        public static final String TABLE_NAME = "photo_producto";
        public static final String ID_PHOTO = "idFoto";
        public static final String ID_PRODUCTO = "idProducto";

    }

    /**
     * table materiales
     */
    public static class Materiales implements BaseColumns{
        private Materiales(){}

        public static final String TABLE_NAME = "materiales";
        public static final String ID_MATERIAL = "idMaterial";
        public static final String MATERIAL = "material";
        public static final String UNIDAD = "unidad";
        public static final String SOLICITUD_MAXIMA = "solicitudMaxima";
        public static final String TIPO_MATERIAL = "tipo_material";

    }

    /**
     * table materiales_solicitud
     */

    public static class MaterialesSolicitud implements BaseColumns{
        private MaterialesSolicitud(){}

        public static final String TABLE_NAME = "materiales_solicitud";
        public static final String ID_SOLICITUD = "id_solicitud";
        public static final String ID_MATERIAL = "id_material";
        public static final String ID_PROMOTOR = "id_promotor";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String CANTIDAD = "cantidad";
        public static final String ID_TIENDA = "id_tienda";
        public static final String FECHA = "fecha";
        public static final String STATUS = "estatus";

    }


    public static class EncuestaFoto implements BaseColumns {
        private EncuestaFoto(){}

        public static final String TABLE_NAME = "encuesta_foto";
        public static final String PHOTO_PATH = "path";
        public static final String ID_ENCUESTA = "idEncuesta";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String STATUS = "estatus";
    }

    public static class EncustaPreguntas implements BaseColumns{
        private EncustaPreguntas(){}

        public static final String TABLE_NAME = "encuesta_respuestas";
        public static final String ID_PREGUNTA = "idPregunta";
        public static final String RESPUESTA = "respuesta";
        public static final String ID_ENCUESTA = "idEncuesta";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String ID_TIENDA = "idTienda";
        public static final String ESTATUS = "estatus";

    }

    public static class Preguntas implements BaseColumns{
        private Preguntas(){}

        public static final String TABLE_NAME = "preguntas";
        public static final String ID_PREGUNTA = "id_pregunta";
        public static final String DESCRIPCION = "descripcion";
        public static final String ID_ENCUESTA = "id_encuesta";
        public static final String ID_TIPO_PREGUNTA = "id_tipo";
        public static final String ID_TIPO_ENCUESTA = "tipo_encuesta";
        public static final String NOMBRE_ENCUESTA = "nombre_encuesta";
        public static final String ID_MARCA = "id_marca";

    }


    public static class Opciones implements BaseColumns{

        private Opciones(){}

        public static final String TABLE_NAME = "opciones";
        public static final String ID_OPCION = "idOpcion";
        public static final String OPCION = "opcion";
        public static final String ID_PREGUNTA = "idPregunta";
    }

    public static class ProductoCatalogadoTienda implements BaseColumns{

        private ProductoCatalogadoTienda(){}

        public static final String TABLE_NAME = "producto_catalogado_tienda";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String ID_TIENDA = "idTienda";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String FECHA_CAPTURA = "fecha_captura";
        public static final String ESTATUS_PRODUCTO = "estatus_producto";
        public static final String CANTIDAD = "cantidad";
        public static final String ESTATUS_REGISTRO = "estatus_registro";
        public static final String ESTATUS_PROCESO = "estatus_proceso";
        public static final String FIRMA = "firma";

    }

    public static class ProcesoCatalogacionObjeciones{

        private ProcesoCatalogacionObjeciones(){}


        public static final String TABLE_NAME = "proceso_catalogacion_objeciones";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_TIENDA = "idTienda";
        public static final String DESCRIPCION = "descripcion";
        public static final String FECHA = "fecha_captura";


    }

}

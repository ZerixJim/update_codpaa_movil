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
        public static final String COMMENT = "comentario";
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
        public static final String FECHA_CAPTURA = "fecha_captura";
        public static final String HORA = "hora";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String PRECISION = "precision";
        public static final String TIPO = "tipo";
        public static final String ESTATUS = "status";
        public static final String SEMANA = "semana";
        public static final String AUTO_TIME = "auto_time";

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
        public static final String ESTATUS = "estatus";
        public static final String FECHA_UPDATE = "fecha_update";

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


    /**
     *  table Agotados
     */

    public static class SolicitudAgodatos {

        private SolicitudAgodatos(){}

        public static final String TABLE_NAME = "solicitud_agotados";
        public static final String ID_TIENDA = "id_tienda";
        public static final String ID_PROMOTOR = "id_promotor";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String STATUS_PRODUCTO = "estatus_producto";
        public static final String STATUS_REGISTRO = "estatus";
        public static final String FECHA = "fecha";


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
        public static final String FECHA_UPDATE = "fecha_update";
        public static final String ESTATUS_PRODUCTO = "estatus_producto";
        public static final String CANTIDAD = "cantidad";
        public static final String ESTATUS_REGISTRO = "estatus_registro";
        public static final String ESTATUS_PROCESO = "estatus_proceso";
        public static final String CANTIDAD_ENTREGA = "cantidad_entrega";
        public static final String FIRMA = "firma";
        public static final String FOLIO = "folio";

    }

    public static class InventarioProducto implements BaseColumns{
        private InventarioProducto(){}

        public static final String TABLE_NAME = "invProducto";
        public static final String ID_TIENDA = "idTienda";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String FECHA = "fecha";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String CANTIDAD_FISICO = "cantidadFisico";
        public static final String CANTIDAD_SISTEMA = "cantidadSistema";
        public static final String ESTATUS = "status";
        public static final String TIPO = "tipo";
        public static final String FECHA_CADUCIDAD = "fecha_caducidad";
        public static final String LOTE = "lote";


    }

    public static class ProcesoCatalogacionObjeciones{

        private ProcesoCatalogacionObjeciones(){}


        public static final String TABLE_NAME = "proceso_catalogacion_objeciones";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String ID_TIENDA = "idTienda";
        public static final String DESCRIPCION = "descripcion";
        public static final String FECHA = "fecha_captura";


    }

    /**
     * Table tienda_marca
     *
     *  brands asigned by stors
     */


    public static class TiendaMarca implements BaseColumns{
        private TiendaMarca(){}

        public static final String TABLE_NAME = "tienda_marca";
        public static final String ID_TIENDA = Tienda.ID_TIENDA;
        public static final String ID_MARCA = "idMarca";

    }

    /**
     * Table categoria/frentes/tonos
     */

    public static class TonoPallet {

        private TonoPallet(){}

        public static final String TABLE_NAME = "tono_tienda";
        public static final String CATEGORIA = "categoria";
        public static final String FRENTES = "cantidad_frentes";
        public static final String TONOS = "cantidad_tonos";
        public static final String FECHA = "fecha_captura";
        public static final String STATUS = "status";
        public static final String PROMOTOR = "id_promotor";
        public static final String ID_TIENDA = Tienda.ID_TIENDA;

    }


    /**
     * Table frentes tinte
     */

    public static class PrecioMarca {

        private PrecioMarca(){}

        public static final String TABLE_NAME = "precio_marca_tienda";
        public static final String ID_TIENDA = Tienda.ID_TIENDA;
        public static final String ID_MARCA = "id_marca";
        public static final String PRICE = "price";
        public static final String FECHA = "fecha_captura";
        public static final String STATUS = "status";


    }



    /**
     *Table productos disponibles
     */

    public static class productosDisponibles{
        private productosDisponibles(){}

        public static final String TABLE_NAME = "productosDisponibles";
        public static final String ID_TIENDA = "idTienda";
        public static final String ID_MARCA = "idMarca";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String ID_PRODUCTO = "idProducto";
        public static final String FECHA = "fecha";
    }


    /**
     * Tabla de categorias de producto
     * */
     public static class encuestas{
        private  encuestas(){}

        public static final String TABLE_NAME = "encuestas";
        public static final String ID_PREGUNTA = "idPregunta";
        public static final String ID_MARCA = "idMarca";
        public static final String PREGUNTA = "pregunta";
        public static final String STATUS = "status";
    }

    public static class promotor_encuesta{
        private  promotor_encuesta(){}

        public static final String TABLE_NAME = "promotor_encuesta";
        public static final String ID_ENCUESTA = "idEncuesta";
        public static final String SUCURSAL = "Sucursal";
        public static final String ID_ESTADO = "idEstado";
        public static final String ID_PROMOTOR = "idPromotor";
        public static final String ID_MARCA = "idMarca";
        public static final String FECHA_CAPTURA = "fecha_captura";
        public static final String PREGUNTA1 = "pregunta1";
        public static final String PREGUNTA2 = "pregunta2";
        public static final String PREGUNTA3 = "pregunta3";
        public static final String PREGUNTA4 = "pregunta4";
        public static final String PREGUNTA5 = "pregunta5";
        public static final String PREGUNTA6 = "pregunta6";
        public static final String PREGUNTA7 = "pregunta7";
        public static final String PREGUNTA8 = "pregunta8";
        public static final String PREGUNTA9 = "pregunta9";
        public static final String PREGUNTA10 = "pregunta10";
        public static final String PREGUNTA11 = "pregunta11";
        public static final String PREGUNTA12 = "pregunta12";
        public static final String PREGUNTA13 = "pregunta13";
        public static final String PREGUNTA14 = "pregunta14";
        public static final String PREGUNTA15 = "pregunta15";
        public static final String PREGUNTA16 = "pregunta16";
        public static final String PREGUNTA17 = "pregunta17";
        public static final String PREGUNTA18 = "pregunta18";
        public static final String PREGUNTA19 = "pregunta19";
        public static final String PREGUNTA20 = "pregunta20";
        public static final String PREGUNTA21 = "pregunta21";
        public static final String PREGUNTA22 = "pregunta22";
        public static final String PREGUNTA23 = "pregunta23";
        public static final String PREGUNTA24 = "pregunta24";
        public static final String PREGUNTA25 = "pregunta25";
        public static final String PREGUNTA26 = "pregunta26";
        public static final String PREGUNTA27 = "pregunta27";
        public static final String PREGUNTA28 = "pregunta28";
        public static final String PREGUNTA29 = "pregunta29";
        public static final String PREGUNTA30 = "pregunta30";
        public static final String PREGUNTA31 = "pregunta31";

    }
    public static class respuestas_encuesta{
        private  respuestas_encuesta(){}

        public static final String TABLE_NAME = "respuesta_encuesta";
        public static final String ID_RESPUESTA = "idRespuesta";
        public static final String ID_MARCA = "idMarca";
        public static final String RESPUESTA = "respuesta";
        public static final String STATUS = "status";
    }
}

<?php


namespace fotos;


class FotosHelix
{

    static function queryFotos($idMarca, $region, $date)
    {


        $stores = self::getStoreByRegion($region);


        $pdo = \ConexionPDO::getInstance()->getDB();


        $host = "plataformavanguardia.net/";
        $sql = "SELECT pc.idphotoCatalogo,concat(m.grupo,' ',m.sucursal,' (',m.idTienda,')') AS nombreTienda,e.nombre,m.numeroEconomico economico, es.id_nielsen nielsen ,
	            pro.nombre AS nombrePromo,pro.idCelular AS idProm,concat(pc.fecha,' ',TIME(pc.fecha_captura)) AS fecha, pc.imagen, tp.descripcion AS nombreTipo,
	            concat(sup.nombreSupervisor,' ',apellidoSupervisor) AS nombreSuper, m.idTipoTienda as tipoTienda, v.nombre as asesor,
	            es.nombre as estado, pc.id_marca , CONCAT('https://',:host,pc.imagen)  hv, tf.cadena, pc.comentario, ct.comentarios comentarioGeneral 

                FROM photoCatalogo AS pc
                

                RIGHT JOIN maestroTiendas AS m ON pc.id_tienda=m.idTienda
                LEFT JOIN vendedor_tienda as vt on vt.id_tienda=m.idTienda
                LEFT JOIN vendedores as v on v.id_vendedor=vt.id_vendedor

                LEFT JOIN tipoExhibicion AS e ON pc.id_exhibicion=e.idExhibicion

                RIGHT JOIN Promotores AS pro ON pc.id_promotor=pro.idCelular
				
				LEFT JOIN Supervisores AS sup ON sup.idSupervisores=pro.Supervisor

                LEFT JOIN tipoPromotor AS tp ON pro.idtipoPromotor=tp.idtipoPromotor
                
                LEFT JOIN photo_producto as pp on pp.idPhotoCatalogo=pc.idphotoCatalogo 
                
                LEFT JOIN estados as es on es.id = m.idEstado
				
                left join tiendas_formatos as tf on (m.idFormato = tf.idFormato) 

                LEFT JOIN (
			
			        SELECT ct.idTienda, ct.fecha_captura, ct.idMarca, GROUP_CONCAT(DISTINCT ct.comentario SEPARATOR '/') comentarios FROM comentarioTienda AS ct 
			
			        WHERE ct.fecha_captura = :date   
			        GROUP BY ct.idTienda, ct.fecha_captura, ct.idMarca 
			
                ) AS ct ON (ct.idTienda = pc.id_tienda AND ct.fecha_captura = DATE(pc.fecha_captura) AND ct.idMarca = pc.id_marca)


				WHERE DATE(pc.fecha_captura)  = :date and pc.id_marca = :idMarca and m.razonsocial = $stores
				

                GROUP BY pc.idphotoCatalogo

				";


        $sentence = $pdo->prepare($sql);

        $sentence->bindParam(':date', $date, \PDO::PARAM_STR);
        $sentence->bindParam(':host', $host, \PDO::PARAM_STR);
        $sentence->bindParam(':idMarca', $idMarca, \PDO::PARAM_INT);


        $sentence->execute();

        //$sentence->debugDumpParams();

        return $sentence->fetchAll(\PDO::FETCH_OBJ);


    }


    static function getStoreByRegion($idRegion)
    {

        //soriana
        if ($idRegion == 1) {
            return 206;
        //al super OPERADORA FUTURAMA S.A. DE C.V.
        } elseif ($idRegion == 2) {
            return 40;

        // calimax
        } elseif ($idRegion == 3) {
            return 209;

        // heb
        } elseif ($idRegion == 4) {

            return 210;
        //casa ley
        } elseif ($idRegion == 5) {

            return 85;
        //chedraui
        } elseif ($idRegion == 6) {

            return 204;
        }else{
            return 0;
        }

    }


    static function getMails($idgrupo)
    {

        if ($idgrupo == 1) {

            return array('gustavoibarra1989@gmail.com');

            //return array('gustavoibarra1989@gmail.com');

        } elseif ($idgrupo == 2) {

            return array();

        }elseif ($idgrupo == 3) {

            return array();

        }elseif ($idgrupo == 4) {

            return array();

        }elseif ($idgrupo == 5) {

            return array();

        }
        elseif ($idgrupo == 6) {

            return array();

        }

        else {
            return "";
        }
    }


}
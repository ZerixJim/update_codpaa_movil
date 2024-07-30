<?php


namespace fotos;


class Fotos
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


				WHERE DATE(pc.fecha_captura)  = :date and pc.id_marca in (249,335) and pc.id_tienda in $stores 
				

                GROUP BY pc.idphotoCatalogo

				";


        $sentence = $pdo->prepare($sql);

        $sentence->bindParam(':date', $date, \PDO::PARAM_STR);
        $sentence->bindParam(':host', $host, \PDO::PARAM_STR);
         //$sentence->bindParam(':idMarca', $idMarca, \PDO::PARAM_INT);


        $sentence->execute();

        //$sentence->debugDumpParams();

        return $sentence->fetchAll(\PDO::FETCH_OBJ);


    }


    static function getStoreByRegion($idRegion)
    {

        if ($idRegion == 1) {
            return "(
                92,
                93,
                277,
                345,
                570,
                578,
                580,
                595,
                596,
                597,
                598,
                599,
                602,
                605,
                606,
                610,
                612,
                613,
                617,
                618,
                620,
                622,
                1137,
                1144,
                1146,
                1147,
                1156,
                1158,
                6555,
                6558,
                6586,
                6700,
                7800,
                7801,
                11130,
                11132,
                11135,
                11137,
                11140,
                11147,
                11152,
                11456,
                11457,
                11458,
                11628,
                11817,
                11880,
                11882,
                11948,
                11949,
                11950,
                11951,
                12037,
                12101,
                12102,
                12114,
                12375,
                12381,
                12549,
                12556,
                13209,
                13676,
                14052,
                14223,
                14224,
                14241,
                14487,
                14488,
                14490,
                14491,
                14497,
                14502,
                14511,
                14598,
                14648,
                14675,
                15019,
                15020,
                15145,
                15146,
                15147,
                15148,
                15149,
                15150,
                15151,
                15237)";
        } elseif ($idRegion == 2) {
            return "(
                78,
                79,
                114,
                116,
                118,
                119,
                120,
                130,
                137,
                154,
                297,
                298,
                300,
                301,
                302,
                310,
                318,
                320,
                321,
                329,
                334,
                626,
                632,
                633,
                638,
                639,
                645,
                648,
                649,
                655,
                659,
                682,
                685,
                688,
                689,
                695,
                704,
                705,
                706,
                707,
                708,
                721,
                725,
                726,
                731,
                734,
                748,
                749,
                765,
                766,
                801,
                802,
                803,
                1159,
                1167,
                1168,
                1170,
                1171,
                1174,
                1188,
                1195,
                1197,
                1264,
                1265,
                1272,
                1298,
                1303,
                1307,
                1308,
                1309,
                1310,
                1311,
                1313,
                1314,
                1315,
                1343,
                1345,
                1346,
                1348,
                1361,
                1405,
                1438,
                1440,
                1684,
                4712,
                4760,
                4772,
                4773,
                4775,
                6774,
                6779,
                6787,
                10851,
                11183,
                11327,
                11827,
                11986,
                11987,
                12074,
                12215,
                12216,
                12219,
                12221,
                12234,
                12541,
                12542,
                12543,
                12560,
                12564,
                13006,
                13107,
                13119,
                13557,
                13821,
                14057,
                14058,
                14436,
                14461,
                14473,
                14505,
                14508,
                14509,
                14515,
                14523,
                14597,
                14601,
                14602,
                14650,
                14655,
                14656,
                14670,
                14910,
                14911,
                15021,
                15272,
                15273,
                15286)";
        } elseif ($idRegion == 3) {
            return "(
                56,
                59,
                64,
                65,
                72,
                75,
                76,
                224,
                231,
                239,
                259,
                261,
                522,
                531,
                532,
                537,
                549,
                562,
                563,
                565,
                566,
                993,
                1002,
                1013,
                1023,
                1024,
                1042,
                1043,
                1045,
                1102,
                1103,
                1109,
                1110,
                1111,
                1112,
                1113,
                1114,
                1115,
                1116,
                1121,
                1122,
                1126,
                4732,
                5916,
                5917,
                5918,
                5919,
                5920,
                5921,
                5922,
                5923,
                5924,
                5925,
                9714,
                11070,
                14506,
                14669,
                15023)";
        } elseif ($idRegion == 4) {

            return "(595,1144,597,12549,596,11132)";
        } elseif ($idRegion == 10) {

            return "(595,1144,597,12549,596,11132)";

        } else {

            return "(0)";

        }

    }


    static function getMails($idgrupo)
    {

        if ($idgrupo == 1) {


            return array('erikardz82@gmail.com',
                'gris.villalobos@plataformavanguardia.com',
                'ricardo.rodriguez@plataformavanguardia.com'

            );

        } elseif ($idgrupo == 2) {

            return array(
                'gris.villalobos@plataformavanguardia.com',
                'valdes.qro17@yahoo.com'

            );

        } elseif ($idgrupo == 3) {


            return array(
                'gris.villalobos@plataformavanguardia.com',
                'blancomtz1974@gmail.com',
                'seferinoriveraserrano@yahoo.com.mx',
                'rubencortez1982@hotmail.com',
                'frank-clorox@hotmail.com'
            );

        } elseif ($idgrupo == 4) {
            return array('ricardo.rodriguez@plataformavanguardia.com');
        } elseif ($idgrupo == 10) {
            return array('ricardo.rodriguez@plataformavanguardia.com');
        } else {
            return "";
        }
    }


}
<?php
/**
 * Created by PhpStorm.
 * User: Gustavo Ibarra
 * Date: 01/04/2019
 * Time: 12:11
 */
session_start();




if (isset($_SESSION["idUser"])){

    include_once "../../../connexion/DataBase.php";



    $idPromotor = $_REQUEST['idPromotor'];
    $idTienda = $_REQUEST['idTienda'];
    $fecha = $_REQUEST['fecha'];

    $sqlPhotos = "SELECT m.`nombre`, COUNT(pc.idphotoCatalogo) fotos FROM photoCatalogo pc  
          
          
          LEFT JOIN Marca m ON m.`idMarca` = pc.id_marca
          
          
          WHERE pc.`id_promotor` = $idPromotor AND pc.`id_tienda` = $idTienda AND DATE(pc.`fecha_captura`) = '$fecha'
          
          
          GROUP BY pc.`id_tienda`, pc.id_marca";


    $manager = DataBase::getInstance();


    $array = array();

    $response = $manager->ejecutarConsulta($sqlPhotos);


    while($row = mysqli_fetch_array($response)){

        array_push($array, $row);

    }



    $sqlFrentes = "SELECT DISTINCT m.`nombre`, COUNT(fc.`idFrentesCharola`) frentes  FROM frentesCharola fc

	LEFT JOIN Marca m ON fc.`idMarca` = m.`idMarca`
	
	WHERE fc.`idCelular` = $idPromotor AND fc.`idTienda` = $idTienda AND fc.`fecha_captura` = '$fecha'
	
	GROUP BY fc.`idMarca`";




    $response = $manager->ejecutarConsulta($sqlFrentes);


    while($row = mysqli_fetch_array($response)){



        $position = $manager->search_item_array($row["nombre"],$array, "nombre");

        if ($position >=0){

            $array[$position]["frentes"] = $row["frentes"];
        }else{

            array_push($array, $row);

        }

    }



    $sqlInventarios = "SELECT m.`nombre`, COUNT(iv.`idInventario`) inventario 
	
	FROM inventarioBodega iv
	
	LEFT JOIN Producto p ON p.`idProducto` = iv.`idProducto`

	LEFT JOIN Marca m ON m.`idMarca` = p.`idMarca`
	
	WHERE iv.`idPromotor` =". $idPromotor ."  AND iv.`idTienda` = ".  $idTienda ." AND iv.`fecha_captura` = '". $fecha ."'
	
	GROUP BY m.`idMarca`";



    $response3 = $manager->ejecutarConsulta($sqlInventarios);


    while ($row3 = mysqli_fetch_array($response3)){

        $position = $manager->search_item_array($row3["nombre"], $array, "nombre");

        if ($position >= 0){

            $array[$position]["inventario"] = $row3["inventario"];

        }else{

            array_push($array, $row3);

        }


    }



    $sqlInteligencia = "select m.nombre,count(im.idInteligencia) inteligencia from inteligenciaMercado im  
                        left join Producto p on p.idProducto = im.idProducto

                        left join Marca m on m.idMarca = p.idMarca

                        where im.idPromotor = $idPromotor and im.idTienda = $idTienda and im.fecha_captura = '$fecha'
                        group by m.idMarca
                        ";


    $response4 = $manager->ejecutarConsulta($sqlInteligencia);


    while ($row4 = mysqli_fetch_array($response4)){

        $position = $manager->search_item_array($row4["nombre"], $array, "nombre");

        if ($position >= 0){

            $array[$position]["inteligencia"] = $row4["inteligencia"];

        }else{

            array_push($array, $row4);

        }

    }



    echo json_encode(array("data"=>$array), JSON_PRETTY_PRINT);


}else{


    http_response_code(422);


    json_encode(array("code"=>422, "mensaje"=>"Acceso denegado"));


}
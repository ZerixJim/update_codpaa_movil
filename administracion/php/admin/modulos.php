<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 23/01/2018
 * Time: 04:55 PM
 */

session_start();




if (isset($_SESSION['idUser'])){



    include_once '../../connexion/DataBase.php';


    if ($_SESSION['id_perfil'] == 1){


        $sql = "select m.id_menu, m.menu, mc.categoria 
                  
                from menus m
                LEFT JOIN menus_cat mc on mc.id_menu_cat=m.id_menu_cat
                ORDER BY mc.categoria asc, m.menu ASC 
                ";

        $manager = DataBase::getInstance();


        $request = $manager->ejecutarConsulta($sql);

        $json = array();

        while ($row = mysqli_fetch_object($request)){

            array_push($json, $row);

        }

        echo json_encode($json, JSON_PRETTY_PRINT);

    }


}else{
    echo 'acceso denegado';
}
<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 21/04/2017
 * Time: 10:46 AM
 */

ob_start();
session_start();

if(isset($_SESSION['idUser'])){




}else{
    echo "acceso denegado";
}
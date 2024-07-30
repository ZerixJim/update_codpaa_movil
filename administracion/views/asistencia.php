<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 3/09/14

 * Time: 10:13

 */

ob_start();

session_start();
include_once('../php/seguridad.php');

if(isset($_SESSION['idUser']) && isset($_SESSION['permiso'])){



    if($_SESSION['permiso'] >= 1){ ?>

    <div title="Asistencia" id="asistencia" closable="true">

<iframe 
src="https://docs.google.com/spreadsheets/d/1T2Kw6d6QA4t8Zi1_jkSP2aWW-70m4yZw7cclZJ2wQzA/pubhtml?widget=true&amp;headers=false" width="100%" height="600px"></iframe> 


    </div>

<? }else{}



}else{

    echo "valor= ".$_SESSION['idUser'];

}



?>








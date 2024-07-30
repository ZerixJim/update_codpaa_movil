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

    <div title="Horarios Autoservicios" id="auto_horarios" closable="true">

<iframe 
src="https://docs.google.com/spreadsheets/d/10-OWNqBF1vFgMHqSXMcaG7f0wt-4BqHjY6OalzV8Ufc/pubhtml?widget=true&amp;headers=false" width="100%" height="600px"></iframe> 
 


    </div>

<? }else{}



}else{

    echo "valor= ".$_SESSION['idUser'];

}



?>







